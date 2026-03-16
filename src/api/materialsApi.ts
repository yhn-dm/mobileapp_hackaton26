import { apiClient } from './apiClient';
import type { SiteMaterial, MaterialType } from '../types/api';

export const fetchMaterialTypes = async (): Promise<MaterialType[]> => {
  const { data } = await apiClient.get<MaterialType[]>('/api/material-types');
  return data;
};

export const fetchSiteMaterials = async (siteId: string): Promise<SiteMaterial[]> => {
  const { data } = await apiClient.get<SiteMaterial[]>(`/api/sites/${siteId}/materials`);
  return data;
};

interface UpsertSiteMaterialPayload {
  materialTypeId: string;
  quantity: number;
}

export const upsertSiteMaterial = async (siteId: string, payload: UpsertSiteMaterialPayload): Promise<void> => {
  await apiClient.post(`/api/sites/${siteId}/materials`, payload);
};

