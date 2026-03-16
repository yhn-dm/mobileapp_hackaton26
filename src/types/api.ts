export type UserRole = 'ADMIN' | 'USER';

export interface User {
  id: string;
  email: string;
  firstName?: string;
  lastName?: string;
  role: UserRole;
}

export interface AuthLoginResponse {
  token: string;
  user: User;
}

export interface AuthRegisterResponse {
  token: string;
  user: User;
}

export interface Site {
  id: string;
  name: string;
  address?: string;
  city?: string;
  totalSurfaceM2: number;
  employeeCount?: number;
  workstationCount?: number;
  constructionYear?: number;
}

export type ParkingType = 'SOUS_DALLE' | 'SOUS_SOL' | 'AERIEN';

export interface SiteParking {
  id: string;
  siteId: string;
  type: ParkingType;
  count: number;
}

export type EnergySource =
  | 'ELECTRICITE'
  | 'GAZ_NATUREL'
  | 'FIOUL'
  | 'BOIS_BIOMASSE'
  | 'AUTRE';

export interface SiteEnergyConsumption {
  id: string;
  siteId: string;
  year: number;
  source: EnergySource;
  consumptionMwh: number;
}

export interface CarbonReport {
  id: string;
  siteId: string;
  calculatedAt: string;
  referenceYear: number;
  constructionCo2Kg: number;
  exploitationCo2Kg: number;
  totalCo2Kg: number;
  co2PerM2?: number;
  co2PerEmployee?: number;
  notes?: string;
}

export interface MaterialType {
  id: string;
  code: string;
  name: string;
  unit: string;
  co2FactorKgPerUnit: number;
}

export interface SiteMaterial {
  id: string;
  siteId: string;
  materialTypeId: string;
  quantity: number;
}

