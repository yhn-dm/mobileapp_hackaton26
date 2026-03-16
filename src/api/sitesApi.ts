import { apiClient } from './apiClient';
import type { Site } from '../types/api';

export const fetchSites = async (): Promise<Site[]> => {
  const { data } = await apiClient.get<Site[]>('/api/sites');
  return data;
};

export const fetchSiteById = async (id: string): Promise<Site> => {
  const { data } = await apiClient.get<Site>(`/api/sites/${id}`);
  return data;
};

