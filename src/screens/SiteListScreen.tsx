import React from 'react';
import { useQuery } from '@tanstack/react-query';
import {
  ActivityIndicator,
  FlatList,
  RefreshControl,
  SafeAreaView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import type { NativeStackScreenProps } from '@react-navigation/native-stack';

import { fetchSites } from '../api/sitesApi';
import type { Site } from '../types/api';
import type { AppStackParamList } from '../navigation/AppStack';
import { t } from '../i18n/strings';
import { colors } from '../theme/colors';

type Props = NativeStackScreenProps<AppStackParamList, 'Sites'>;

export const SiteListScreen: React.FC<Props> = ({ navigation }) => {
  const { data, isLoading, isError, isRefetching, refetch } = useQuery<Site[]>({
    queryKey: ['sites'],
    queryFn: fetchSites,
  });

  const renderItem = ({ item }: { item: Site }) => (
    <TouchableOpacity
      style={styles.card}
      accessibilityRole="button"
      accessibilityLabel={`${item.name}${item.city ? `, ${item.city}` : ''}`}
      onPress={() => navigation.navigate('SiteDetail', { siteId: item.id, siteName: item.name })}
    >
      <Text style={styles.name}>{item.name}</Text>
      {item.city && <Text style={styles.city}>{item.city}</Text>}
      <Text style={styles.meta}>
        {t('sites.surface')} : {item.totalSurfaceM2} m²
      </Text>
      {item.employeeCount != null && (
        <Text style={styles.meta}>
          {t('sites.employees')} : {item.employeeCount}
        </Text>
      )}
    </TouchableOpacity>
  );

  return (
    <SafeAreaView style={styles.container}>
      {isLoading && (
        <View style={styles.center}>
          <ActivityIndicator />
        </View>
      )}
      {isError && (
        <View style={styles.center}>
          <Text>{t('sites.loadError')}</Text>
        </View>
      )}
      {data && (
        <FlatList
          data={data}
          keyExtractor={(item) => item.id}
          renderItem={renderItem}
          refreshControl={
            <RefreshControl refreshing={isRefetching} onRefresh={() => void refetch()} />
          }
          contentContainerStyle={data.length === 0 ? styles.center : undefined}
          ListEmptyComponent={<Text>{t('sites.empty')}</Text>}
        />
      )}
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: colors.background,
  },
  center: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 16,
  },
  card: {
    backgroundColor: colors.card,
    marginHorizontal: 16,
    marginVertical: 8,
    padding: 16,
    borderRadius: 12,
    shadowColor: '#000',
    shadowOpacity: 0.05,
    shadowRadius: 6,
    shadowOffset: { width: 0, height: 2 },
    elevation: 2,
  },
  name: {
    fontSize: 16,
    fontWeight: '600',
    marginBottom: 4,
    color: colors.text,
  },
  city: {
    fontSize: 14,
    color: '#555',
    marginBottom: 4,
  },
  meta: {
    fontSize: 12,
    color: colors.mutedText,
  },
});

