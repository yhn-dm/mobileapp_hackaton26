import { apiClient } from './apiClient';
import type { AuthLoginResponse, AuthRegisterResponse, User } from '../types/api';

interface LoginPayload {
  email: string;
  password: string;
}

export const login = async (payload: LoginPayload): Promise<AuthLoginResponse> => {
  const { data } = await apiClient.post<AuthLoginResponse>('/api/auth/login', payload);
  return data;
};

interface RegisterPayload {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
}

export const register = async (payload: RegisterPayload): Promise<AuthRegisterResponse> => {
  const { data } = await apiClient.post<AuthRegisterResponse>('/api/auth/register', payload);
  return data;
};


