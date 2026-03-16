import { apiClient } from './apiClient';
import type { EnergySource, SiteEnergyConsumption } from '../types/api';

export const fetchSiteEnergyConsumptions = async (siteId: string): Promise<SiteEnergyConsumption[]> => {
  const { data } = await apiClient.get<SiteEnergyConsumption[]>(`/api/sites/${siteId}/energy-consumptions`);
  return data;
};

interface UpsertEnergyPayload {
  year: number;
  source: EnergySource;
  consumptionMwh: number;
}

export const upsertEnergyConsumption = async (siteId: string, payload: UpsertEnergyPayload): Promise<void> => {
  await apiClient.post(`/api/sites/${siteId}/energy-consumptions`, payload);
};

