import React, { useMemo, useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import {
  ActivityIndicator,
  Alert,
  KeyboardAvoidingView,
  Platform,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';
import type { NativeStackScreenProps } from '@react-navigation/native-stack';

import { fetchSiteEnergyConsumptions, upsertEnergyConsumption } from '../api/energyApi';
import type { AppStackParamList } from '../navigation/AppStack';
import type { EnergySource, SiteEnergyConsumption } from '../types/api';
import { t } from '../i18n/strings';
import { colors } from '../theme/colors';
import { ScreenContainer } from '../components/ui/ScreenContainer';
import { SectionCard } from '../components/ui/SectionCard';
import { PrimaryButton } from '../components/ui/PrimaryButton';
import { track } from '../utils/analytics';

type Props = NativeStackScreenProps<AppStackParamList, 'SiteEnergy'>;

const energyOptions: { source: EnergySource; label: string }[] = [
  { source: 'ELECTRICITE', label: t('energy.src.electricite') },
  { source: 'GAZ_NATUREL', label: t('energy.src.gaz') },
  { source: 'FIOUL', label: t('energy.src.fioul') },
  { source: 'BOIS_BIOMASSE', label: t('energy.src.bois') },
  { source: 'AUTRE', label: t('energy.src.autre') },
];

export const SiteEnergyScreen: React.FC<Props> = ({ route }) => {
  const { siteId } = route.params;
  const queryClient = useQueryClient();

  const [year, setYear] = useState('2025');
  const [source, setSource] = useState<EnergySource>('ELECTRICITE');
  const [consumption, setConsumption] = useState('');

  const { data, isLoading, isError } = useQuery<SiteEnergyConsumption[]>({
    queryKey: ['siteEnergy', siteId],
    queryFn: () => fetchSiteEnergyConsumptions(siteId),
  });

  const mutation = useMutation({
    mutationFn: () =>
      upsertEnergyConsumption(siteId, {
        year: Number(year),
        source,
        consumptionMwh: Number(consumption || 0),
      }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['siteEnergy', siteId] });
      Alert.alert(t('energy.successTitle'), t('energy.successMsg'));
      track('energy_update_success', { siteId, year: Number(year), source });
    },
    onError: () => {
      Alert.alert(t('energy.errorTitle'), t('energy.errorMsg'));
      track('energy_update_failure', { siteId, year: Number(year), source });
    },
  });

  const years = useMemo(
    () => Array.from(new Set((data ?? []).map((c) => c.year))).sort(),
    [data],
  );

  const existingForYear = data?.filter((c) => c.year === Number(year)) ?? [];

  if (isLoading) {
    return (
      <ScreenContainer style={styles.center}>
        <ActivityIndicator />
      </ScreenContainer>
    );
  }

  if (isError) {
    return (
      <ScreenContainer style={styles.center}>
        <Text>{t('energy.loadError')}</Text>
      </ScreenContainer>
    );
  }

  return (
    <KeyboardAvoidingView
      style={{ flex: 1 }}
      behavior={Platform.OS === 'ios' ? 'padding' : undefined}
      keyboardVerticalOffset={80}
    >
      <ScreenContainer>
        <ScrollView contentContainerStyle={styles.scroll}>
          <SectionCard>
            <Text style={styles.help}>{t('energy.help')}</Text>

            <View style={styles.field}>
              <Text style={styles.label}>{t('energy.year')}</Text>
              <View style={styles.chipsRow}>
                {years.map((y) => (
                  <Text
                    key={y}
                    style={[styles.chip, Number(year) === y && styles.chipSelected]}
                    onPress={() => setYear(String(y))}
                  >
                    {y}
                  </Text>
                ))}
              </View>
              <TextInput
                style={styles.input}
                keyboardType="numeric"
                value={year}
                onChangeText={(v) => setYear(v.replace(/[^0-9]/g, ''))}
              />
            </View>

            <View style={styles.field}>
              <Text style={styles.label}>{t('energy.source')}</Text>
              <View style={styles.chipsRow}>
                {energyOptions.map((option) => (
                  <Text
                    key={option.source}
                    style={[styles.chip, source === option.source && styles.chipSelected]}
                    onPress={() => setSource(option.source)}
                  >
                    {option.label}
                  </Text>
                ))}
              </View>
            </View>

            <View style={styles.field}>
              <Text style={styles.label}>{t('energy.consumption')}</Text>
              <TextInput
                style={styles.input}
                keyboardType="numeric"
                value={consumption}
                onChangeText={(v) => setConsumption(v.replace(/[^0-9.]/g, ''))}
              />
            </View>

            <PrimaryButton
              label={mutation.isPending ? t('energy.saving') : t('energy.save')}
              onPress={() => mutation.mutate()}
              loading={mutation.isPending}
              style={styles.saveButton}
            />
          </SectionCard>

          <SectionCard>
            <Text style={styles.sectionTitle}>{t('energy.existing')}</Text>
            {years.length === 0 && <Text style={styles.empty}>Aucune consommation enregistrée.</Text>}
            {years.length > 0 &&
              years.map((y) => {
                const forYear = data?.filter((c) => c.year === y) ?? [];
                if (forYear.length === 0) return null;
                return (
                  <View key={y} style={{ marginBottom: 8 }}>
                    <Text style={styles.yearTitle}>{y}</Text>
                    {forYear.map((c) => (
                      <Text key={c.id} style={styles.item}>
                        {c.source} : {c.consumptionMwh} MWh
                      </Text>
                    ))}
                  </View>
                );
              })}
          </SectionCard>
        </ScrollView>
      </ScreenContainer>
    </KeyboardAvoidingView>
  );
};

const styles = StyleSheet.create({
  center: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  scroll: {
    padding: 16,
  },
  help: {
    fontSize: 14,
    marginBottom: 16,
  },
  field: {
    marginBottom: 16,
  },
  label: {
    fontSize: 14,
    marginBottom: 4,
  },
  input: {
    backgroundColor: colors.card,
    borderRadius: 8,
    borderWidth: 1,
    borderColor: colors.border,
    paddingHorizontal: 12,
    paddingVertical: 8,
  },
  chipsRow: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
  },
  chip: {
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 16,
    backgroundColor: '#e0e0e0',
    marginRight: 8,
    marginBottom: 8,
  },
  chipSelected: {
    backgroundColor: colors.brand,
    color: '#fff',
  },
  saveButton: {
    backgroundColor: colors.brand,
    borderRadius: 8,
    paddingVertical: 12,
    alignItems: 'center',
    marginBottom: 24,
  },
  save: {
    color: '#fff',
    fontWeight: '600',
  },
  section: {
    padding: 12,
    backgroundColor: colors.card,
    borderRadius: 8,
  },
  sectionTitle: {
    fontWeight: '600',
    marginBottom: 8,
  },
  yearTitle: {
    fontWeight: '600',
  },
  empty: {
    fontStyle: 'italic',
  },
  item: {
    fontSize: 14,
    marginBottom: 4,
  },
});

