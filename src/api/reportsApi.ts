import { apiClient } from './apiClient';
import type { CarbonReport } from '../types/api';

// Endpoint simplifié : récupère les rapports d’un site, on prendra le plus récent côté mobile.
export const fetchReportsForSite = async (siteId: string): Promise<CarbonReport[]> => {
  const { data } = await apiClient.get<CarbonReport[]>('/api/carbon-reports', {
    params: { siteId },
  });
  return data;
};

