import React, { useMemo } from 'react';
import { useQuery } from '@tanstack/react-query';
import { ActivityIndicator, StyleSheet, Text, View } from 'react-native';
import type { NativeStackScreenProps } from '@react-navigation/native-stack';

import { fetchReportsForSite } from '../api/reportsApi';
import type { CarbonReport } from '../types/api';
import type { AppStackParamList } from '../navigation/AppStack';
import { ScreenContainer } from '../components/ui/ScreenContainer';
import { track } from '../utils/analytics';

type Props = NativeStackScreenProps<AppStackParamList, 'SiteKpi'>;

export const SiteKpiScreen: React.FC<Props> = ({ route }) => {
  const { siteId } = route.params;

  const { data, isLoading, isError } = useQuery<CarbonReport[]>({
    queryKey: ['siteReports', siteId],
    queryFn: async () => {
      const reports = await fetchReportsForSite(siteId);
      if (reports.length > 0) {
        track('kpi_viewed', { siteId, reports: reports.length });
      }
      return reports;
    },
  });

  const latest = useMemo(() => {
    if (!data || data.length === 0) return undefined;
    return [...data].sort(
      (a, b) => new Date(b.calculatedAt).getTime() - new Date(a.calculatedAt).getTime(),
    )[0];
  }, [data]);

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
        <Text>Impossible de charger les indicateurs.</Text>
      </ScreenContainer>
    );
  }

  if (!latest) {
    return (
      <ScreenContainer style={styles.center}>
        <Text>Aucun rapport carbone disponible pour ce site.</Text>
        <Text style={styles.small}>
          Lance un calcul depuis le back-office ou l’API, puis reviens sur cet écran.
        </Text>
      </ScreenContainer>
    );
  }

  const construction = latest.constructionCo2Kg;
  const exploitation = latest.exploitationCo2Kg;
  const total = latest.totalCo2Kg;

  const constructionPct = total > 0 ? Math.round((construction / total) * 100) : 0;
  const exploitationPct = total > 0 ? Math.round((exploitation / total) * 100) : 0;

  return (
    <ScreenContainer style={styles.container}>
      <View
        style={styles.card}
        accessible
        accessibilityRole="summary"
        accessibilityLabel={`Année de référence ${latest.referenceYear}. Calculé le ${new Date(latest.calculatedAt).toLocaleDateString(
          'fr-FR',
        )}.`}
      >
        <Text style={styles.label}>Année de référence</Text>
        <Text style={styles.value}>{latest.referenceYear}</Text>
        <Text style={styles.sub}>Calculé le {new Date(latest.calculatedAt).toLocaleDateString()}</Text>
      </View>

      <View
        style={styles.card}
        accessible
        accessibilityLabel={`Bilan CO2. Construction ${Math.round(construction)} kilogrammes. Exploitation ${Math.round(
          exploitation,
        )} kilogrammes. Total ${Math.round(total)} kilogrammes.`}
      >
        <Text style={styles.sectionTitle}>Bilan CO₂ (kgCO₂e)</Text>
        <View style={styles.row}>
          <View style={styles.col}>
            <Text style={styles.label}>Construction</Text>
            <Text style={styles.value}>{Math.round(construction).toLocaleString('fr-FR')}</Text>
            <Text style={styles.sub}>{constructionPct}% du total</Text>
          </View>
          <View style={styles.col}>
            <Text style={styles.label}>Exploitation</Text>
            <Text style={styles.value}>{Math.round(exploitation).toLocaleString('fr-FR')}</Text>
            <Text style={styles.sub}>{exploitationPct}% du total</Text>
          </View>
        </View>
        <View style={styles.totalRow}>
          <Text style={styles.totalLabel}>Total</Text>
          <Text style={styles.totalValue}>{Math.round(total).toLocaleString('fr-FR')} kgCO₂e</Text>
        </View>
      </View>

      <View
        style={styles.card}
        accessible
        accessibilityLabel={`Intensité carbone. Par mètre carré ${
          latest.co2PerM2 != null ? latest.co2PerM2.toFixed(2) : 'non disponible'
        }, par employé ${
          latest.co2PerEmployee != null ? latest.co2PerEmployee.toFixed(2) : 'non disponible'
        }.`}
      >
        <Text style={styles.sectionTitle}>Intensité carbone</Text>
        <View style={styles.row}>
          <View style={styles.col}>
            <Text style={styles.label}>Par m²</Text>
            <Text style={styles.value}>
              {latest.co2PerM2 != null ? latest.co2PerM2.toFixed(2) : '-'} kgCO₂e/m²
            </Text>
          </View>
          <View style={styles.col}>
            <Text style={styles.label}>Par employé</Text>
            <Text style={styles.value}>
              {latest.co2PerEmployee != null ? latest.co2PerEmployee.toFixed(2) : '-'} kgCO₂e/empl.
            </Text>
          </View>
        </View>
      </View>
    </ScreenContainer>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
    backgroundColor: '#f5f5f5',
    gap: 12,
  },
  center: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 16,
  },
  small: {
    fontSize: 12,
    marginTop: 8,
    color: '#666',
    textAlign: 'center',
  },
  card: {
    backgroundColor: '#fff',
    borderRadius: 12,
    padding: 16,
    shadowColor: '#000',
    shadowOpacity: 0.05,
    shadowRadius: 6,
    shadowOffset: { width: 0, height: 2 },
    elevation: 2,
  },
  label: {
    fontSize: 12,
    color: '#777',
  },
  value: {
    fontSize: 18,
    fontWeight: '600',
    marginTop: 4,
  },
  sub: {
    fontSize: 12,
    color: '#777',
    marginTop: 4,
  },
  sectionTitle: {
    fontSize: 14,
    fontWeight: '600',
    marginBottom: 8,
  },
  row: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    gap: 16,
  },
  col: {
    flex: 1,
  },
  totalRow: {
    marginTop: 12,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  totalLabel: {
    fontSize: 14,
    fontWeight: '600',
  },
  totalValue: {
    fontSize: 16,
    fontWeight: '700',
  },
});

