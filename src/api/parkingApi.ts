import { apiClient } from './apiClient';
import type { ParkingType, SiteParking } from '../types/api';

export const fetchSiteParking = async (siteId: string): Promise<SiteParking[]> => {
  const { data } = await apiClient.get<SiteParking[]>(`/api/sites/${siteId}/parking`);
  return data;
};

interface UpsertParkingPayload {
  type: ParkingType;
  count: number;
}

export const upsertSiteParking = async (siteId: string, payloads: UpsertParkingPayload[]): Promise<void> => {
  // On envoie un tableau, en supposant que l’API accepte plusieurs entrées.
  // Si ce n’est pas le cas, on pourra adapter en faisant une requête par type.
  await apiClient.post(`/api/sites/${siteId}/parking`, payloads);
};

