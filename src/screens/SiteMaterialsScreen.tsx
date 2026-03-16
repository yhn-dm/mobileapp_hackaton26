import React, { useMemo, useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { ActivityIndicator, Alert, FlatList, StyleSheet, Text, TextInput, View } from 'react-native';
import type { NativeStackScreenProps } from '@react-navigation/native-stack';

import type { AppStackParamList } from '../navigation/AppStack';
import type { MaterialType, SiteMaterial } from '../types/api';
import { fetchMaterialTypes, fetchSiteMaterials, upsertSiteMaterial } from '../api/materialsApi';
import { colors } from '../theme/colors';
import { ScreenContainer } from '../components/ui/ScreenContainer';
import { SectionCard } from '../components/ui/SectionCard';
import { PrimaryButton } from '../components/ui/PrimaryButton';
import { track } from '../utils/analytics';

type Props = NativeStackScreenProps<AppStackParamList, 'SiteMaterials'>;

export const SiteMaterialsScreen: React.FC<Props> = ({ route }) => {
  const { siteId } = route.params;
  const queryClient = useQueryClient();
  const [selectedMaterialId, setSelectedMaterialId] = useState<string | null>(null);
  const [quantity, setQuantity] = useState('');

  const {
    data: materialTypes,
    isLoading: loadingTypes,
    isError: errorTypes,
  } = useQuery<MaterialType[]>({
    queryKey: ['materialTypes'],
    queryFn: fetchMaterialTypes,
  });

  const {
    data: siteMaterials,
    isLoading: loadingSiteMaterials,
    isError: errorSiteMaterials,
  } = useQuery<SiteMaterial[]>({
    queryKey: ['siteMaterials', siteId],
    queryFn: () => fetchSiteMaterials(siteId),
  });

  const materialsById = useMemo(() => {
    const map = new Map<string, MaterialType>();
    materialTypes?.forEach((m) => map.set(m.id, m));
    return map;
  }, [materialTypes]);

  const mutation = useMutation({
    mutationFn: () =>
      upsertSiteMaterial(siteId, {
        materialTypeId: selectedMaterialId as string,
        quantity: Number(quantity || 0),
      }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['siteMaterials', siteId] });
      Alert.alert('Succès', 'Matériau enregistré pour ce site.');
      track('materials_update_success', { siteId, materialTypeId: selectedMaterialId, quantity: Number(quantity || 0) });
    },
    onError: () => {
      Alert.alert('Erreur', 'Impossible de sauvegarder le matériau.');
      track('materials_update_failure', { siteId, materialTypeId: selectedMaterialId });
    },
  });

  const handleSave = () => {
    if (!selectedMaterialId) {
      Alert.alert('Information', 'Sélectionne un type de matériau.');
      return;
    }
    mutation.mutate();
  };

  if (loadingTypes || loadingSiteMaterials) {
    return (
      <ScreenContainer style={styles.center}>
        <ActivityIndicator />
      </ScreenContainer>
    );
  }

  if (errorTypes || errorSiteMaterials) {
    return (
      <ScreenContainer style={styles.center}>
        <Text>Impossible de charger les matériaux.</Text>
      </ScreenContainer>
    );
  }

  return (
    <ScreenContainer>
      <SectionCard>
        <Text style={styles.title}>Matériaux utilisés</Text>
        <Text style={styles.help}>
          Sélectionne un matériau et saisis la quantité correspondante pour ce site (unité selon le type).
        </Text>

        <FlatList
          data={materialTypes}
          keyExtractor={(item) => item.id}
          horizontal
          showsHorizontalScrollIndicator={false}
          contentContainerStyle={styles.chipsRow}
          renderItem={({ item }) => (
            <Text
              style={[
                styles.chip,
                selectedMaterialId === item.id && styles.chipSelected,
              ]}
              onPress={() => setSelectedMaterialId(item.id)}
            >
              {item.name} ({item.unit})
            </Text>
          )}
        />

        <View style={styles.field}>
          <Text style={styles.label}>Quantité</Text>
          <TextInput
            style={styles.input}
            keyboardType="numeric"
            value={quantity}
            onChangeText={(v) => setQuantity(v.replace(/[^0-9.]/g, ''))}
          />
        </View>

        <PrimaryButton
          label={mutation.isPending ? 'Enregistrement...' : 'Enregistrer'}
          onPress={handleSave}
          loading={mutation.isPending}
          style={styles.saveButton}
        />
      </SectionCard>

      <SectionCard>
        <Text style={styles.subtitle}>Matériaux déjà saisis</Text>
        {siteMaterials && siteMaterials.length > 0 ? (
          siteMaterials.map((sm) => {
            const mt = materialsById.get(sm.materialTypeId);
            if (!mt) return null;
            return (
              <Text key={sm.id} style={styles.item}>
                {mt.name} : {sm.quantity} {mt.unit}
              </Text>
            );
          })
        ) : (
          <Text style={styles.empty}>Aucun matériau saisi pour ce site.</Text>
        )}
      </SectionCard>
    </ScreenContainer>
  );
};

const styles = StyleSheet.create({
  center: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  title: {
    fontSize: 18,
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
  help: {
    fontSize: 14,
    marginBottom: 12,
  },
  chipsRow: {
    marginVertical: 8,
  },
  chip: {
    paddingHorizontal: 10,
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
  field: {
    marginTop: 12,
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
  saveButton: {
    marginTop: 16,
  },
  item: {
    fontSize: 14,
    marginBottom: 4,
  },
  empty: {
    fontStyle: 'italic',
  },
});

