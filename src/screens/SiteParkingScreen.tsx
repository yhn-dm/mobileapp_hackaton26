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

import { fetchSiteParking, upsertSiteParking } from '../api/parkingApi';
import type { AppStackParamList } from '../navigation/AppStack';
import type { ParkingType, SiteParking } from '../types/api';
import { colors } from '../theme/colors';
import { ScreenContainer } from '../components/ui/ScreenContainer';
import { SectionCard } from '../components/ui/SectionCard';
import { PrimaryButton } from '../components/ui/PrimaryButton';
import { track } from '../utils/analytics';

type Props = NativeStackScreenProps<AppStackParamList, 'SiteParking'>;

const parkingTypes: { type: ParkingType; label: string }[] = [
  { type: 'SOUS_DALLE', label: 'Sous-dalle' },
  { type: 'SOUS_SOL', label: 'Sous-sol' },
  { type: 'AERIEN', label: 'Aériens' },
];

export const SiteParkingScreen: React.FC<Props> = ({ route }) => {
  const { siteId } = route.params;
  const queryClient = useQueryClient();

  const { data, isLoading, isError } = useQuery<SiteParking[]>({
    queryKey: ['siteParking', siteId],
    queryFn: () => fetchSiteParking(siteId),
  });

  const initialCounts = useMemo(() => {
    const byType: Record<ParkingType, number> = {
      SOUS_DALLE: 0,
      SOUS_SOL: 0,
      AERIEN: 0,
    };
    if (data) {
      data.forEach((p) => {
        byType[p.type] = p.count;
      });
    }
    return byType;
  }, [data]);

  const [counts, setCounts] = useState<Record<ParkingType, string>>({
    SOUS_DALLE: initialCounts.SOUS_DALLE.toString(),
    SOUS_SOL: initialCounts.SOUS_SOL.toString(),
    AERIEN: initialCounts.AERIEN.toString(),
  });

  const mutation = useMutation({
    mutationFn: () =>
      upsertSiteParking(
        siteId,
        parkingTypes.map(({ type }) => ({
          type,
          count: Number(counts[type] || 0),
        })),
      ),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['siteParking', siteId] });
      Alert.alert('Succès', 'Parkings mis à jour.');
      track('parking_update_success', { siteId });
    },
    onError: () => {
      Alert.alert('Erreur', 'Impossible de sauvegarder les parkings.');
      track('parking_update_failure', { siteId });
    },
  });

  const handleChange = (type: ParkingType, value: string) => {
    setCounts((prev) => ({ ...prev, [type]: value.replace(/[^0-9]/g, '') }));
  };

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
        <Text>Impossible de charger les parkings.</Text>
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
            <Text style={styles.help}>Saisis le nombre de places de parking par type pour ce site.</Text>

            {parkingTypes.map(({ type, label }) => (
              <View key={type} style={styles.field}>
                <Text style={styles.label}>{label}</Text>
                <TextInput
                  style={styles.input}
                  keyboardType="numeric"
                  value={counts[type]}
                  onChangeText={(v) => handleChange(type, v)}
                />
              </View>
            ))}

            <View style={styles.summary}>
              <Text style={styles.summaryText}>
                Total places :{' '}
                {Object.values(counts)
                  .map((v) => Number(v || 0))
                  .reduce((a, b) => a + b, 0)}
              </Text>
            </View>

            <PrimaryButton
              label={mutation.isPending ? 'Enregistrement...' : 'Enregistrer'}
              onPress={() => mutation.mutate()}
              loading={mutation.isPending}
              style={styles.saveButton}
            />
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
  summary: {
    marginTop: 8,
    marginBottom: 24,
  },
  summaryText: {
    fontWeight: '500',
  },
  saveButton: {
    marginTop: 8,
  },
});

