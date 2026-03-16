import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { ActivityIndicator, StyleSheet, Text, View } from 'react-native';
import type { NativeStackScreenProps } from '@react-navigation/native-stack';

import { fetchSiteById } from '../api/sitesApi';
import type { Site } from '../types/api';
import type { AppStackParamList } from '../navigation/AppStack';
import { t } from '../i18n/strings';
import { colors } from '../theme/colors';
import { ScreenContainer } from '../components/ui/ScreenContainer';
import { SectionCard } from '../components/ui/SectionCard';
import { PrimaryButton } from '../components/ui/PrimaryButton';
import { track } from '../utils/analytics';

type Props = NativeStackScreenProps<AppStackParamList, 'SiteDetail'>;

export const SiteDetailScreen: React.FC<Props> = ({ route, navigation }) => {
  const { siteId } = route.params;

  const { data, isLoading, isError } = useQuery<Site>({
    queryKey: ['site', siteId],
    queryFn: async () => {
      const site = await fetchSiteById(siteId);
      track('site_opened', { siteId: site.id, name: site.name });
      return site;
    },
  });

  if (isLoading) {
    return (
      <ScreenContainer style={styles.center}>
        <ActivityIndicator />
      </ScreenContainer>
    );
  }

  if (isError || !data) {
    return (
      <ScreenContainer style={styles.center}>
        <Text>{t('siteDetail.loadError')}</Text>
      </ScreenContainer>
    );
  }

  return (
    <ScreenContainer style={styles.container}>
      <SectionCard>
        <Text style={styles.label}>{t('siteDetail.name')}</Text>
        <Text style={styles.value}>{data.name}</Text>
      </SectionCard>

      {data.city && (
        <SectionCard>
          <Text style={styles.label}>{t('siteDetail.city')}</Text>
          <Text style={styles.value}>{data.city}</Text>
        </SectionCard>
      )}

      {data.address && (
        <SectionCard>
          <Text style={styles.label}>{t('siteDetail.address')}</Text>
          <Text style={styles.value}>{data.address}</Text>
        </SectionCard>
      )}

      <SectionCard>
        <Text style={styles.label}>{t('siteDetail.surface')}</Text>
        <Text style={styles.value}>{data.totalSurfaceM2} m²</Text>
      </SectionCard>

      {data.employeeCount != null && (
        <SectionCard>
          <Text style={styles.label}>{t('siteDetail.employeeCount')}</Text>
          <Text style={styles.value}>{data.employeeCount}</Text>
        </SectionCard>
      )}

      {data.workstationCount != null && (
        <SectionCard>
          <Text style={styles.label}>{t('siteDetail.workstationCount')}</Text>
          <Text style={styles.value}>{data.workstationCount}</Text>
        </SectionCard>
      )}

      <View style={styles.actions}>
        <PrimaryButton
          label={t('siteDetail.parkingEntry')}
          onPress={() => navigation.navigate('SiteParking', { siteId: data.id, siteName: data.name })}
        />
        <PrimaryButton
          label={t('siteDetail.energyEntry')}
          onPress={() => navigation.navigate('SiteEnergy', { siteId: data.id, siteName: data.name })}
        />
        <PrimaryButton
          label={t('siteDetail.kpis')}
          onPress={() => navigation.navigate('SiteKpi', { siteId: data.id, siteName: data.name })}
        />
        <PrimaryButton
          label="Matériaux"
          onPress={() => navigation.navigate('SiteMaterials', { siteId: data.id, siteName: data.name })}
        />
        <PrimaryButton label="Mon profil" onPress={() => navigation.navigate('Profile')} />
      </View>
    </ScreenContainer>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  center: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  label: {
    fontSize: 12,
    color: colors.mutedText,
    marginBottom: 4,
  },
  value: {
    fontSize: 16,
    fontWeight: '500',
    color: colors.text,
  },
  actions: {
    marginTop: 24,
    gap: 12,
  },
});

