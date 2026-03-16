import React from 'react';
import { StyleSheet, Text, View } from 'react-native';

import { ScreenContainer } from '../components/ui/ScreenContainer';
import { SectionCard } from '../components/ui/SectionCard';
import { colors } from '../theme/colors';

export const AboutScreen: React.FC = () => {
  return (
    <ScreenContainer>
      <SectionCard>
        <Text style={styles.title}>À propos de l’application</Text>
        <Text style={styles.text}>
          Cette application mobile permet de saisir rapidement les données terrain d’un site (parkings, consommations
          d’énergie, matériaux) et de consulter quelques indicateurs clés d’empreinte carbone.
        </Text>
        <Text style={styles.text}>
          Elle s’appuie sur une API Spring Boot et une base PostgreSQL communes avec le dashboard web, garantissant un
          stockage et un calcul homogènes entre les différents canaux.
        </Text>
      </SectionCard>

      <SectionCard>
        <Text style={styles.subtitle}>Indicateurs affichés</Text>
        <Text style={styles.text}>
          Les indicateurs mis en avant sont volontairement simples pour un usage terrain :
        </Text>
        <Text style={styles.bullet}>• CO₂e total (construction + exploitation)</Text>
        <Text style={styles.bullet}>• CO₂e par m²</Text>
        <Text style={styles.bullet}>• CO₂e par employé</Text>
        <Text style={styles.text}>
          Le calcul complet et la comparaison multi-sites sont disponibles dans le dashboard web associé.
        </Text>
      </SectionCard>

      <SectionCard>
        <Text style={styles.subtitle}>Facteurs d’émission</Text>
        <Text style={styles.text}>
          Les facteurs d’émission utilisés pour les matériaux et les sources d’énergie proviennent de référentiels
          officiels (ADEME et bases publiques), stockés dans la base de données sous forme de tables de référence.
        </Text>
        <Text style={styles.text}>
          Cela permet de recalculer ou d’expliquer a posteriori les résultats, et d’aligner les calculs avec les bonnes
          pratiques de reporting carbone.
        </Text>
      </SectionCard>
    </ScreenContainer>
  );
};

const styles = StyleSheet.create({
  title: {
    fontSize: 20,
    fontWeight: '600',
    marginBottom: 8,
    color: colors.text,
  },
  subtitle: {
    fontSize: 16,
    fontWeight: '600',
    marginBottom: 8,
    color: colors.text,
  },
  text: {
    fontSize: 14,
    marginBottom: 8,
  },
  bullet: {
    fontSize: 14,
    marginBottom: 4,
  },
});

