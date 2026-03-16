import React, { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';

import { setAuthToken, setUnauthorizedHandler } from '../api/apiClient';
import { login as loginApi, register as registerApi } from '../api/authApi';
import { storage } from '../utils/storage';
import type { User } from '../types/api';

interface AuthState {
  token: string | null;
  email: string | null;
  user: User | null;
  isLoading: boolean;
}

interface AuthContextValue extends AuthState {
  login: (email: string, password: string) => Promise<void>;
  register: (payload: { email: string; password: string; firstName: string; lastName: string }) => Promise<void>;
  logout: () => Promise<void>;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

const TOKEN_KEY = 'auth_token';
const EMAIL_KEY = 'auth_email';
const USER_KEY = 'auth_user';
const LOGIN_AT_KEY = 'auth_login_at';
const MAX_SESSION_AGE_MS = 24 * 60 * 60 * 1000; // 24h

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [token, setToken] = useState<string | null>(null);
  const [email, setEmail] = useState<string | null>(null);
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const bootstrap = async () => {
      try {
        const storedToken = await storage.getItem(TOKEN_KEY);
        const storedEmail = await storage.getItem(EMAIL_KEY);
        const storedUser = await storage.getItem(USER_KEY);
        const storedLoginAt = await storage.getItem(LOGIN_AT_KEY);

        const sessionIsFresh =
          storedLoginAt &&
          !Number.isNaN(Number(storedLoginAt)) &&
          Date.now() - Number(storedLoginAt) < MAX_SESSION_AGE_MS;

        if (storedToken && sessionIsFresh) {
          setToken(storedToken);
          setAuthToken(storedToken);
        }
        if (storedEmail && sessionIsFresh) {
          setEmail(storedEmail);
        }
        if (storedUser && sessionIsFresh) {
          try {
            const parsed = JSON.parse(storedUser) as User;
            setUser(parsed);
          } catch {
            // ignore parse errors, will be refreshed on prochain login
          }
        }

        // Si la session n’est plus fraîche, on nettoie
        if (!sessionIsFresh) {
          await storage.deleteItem(TOKEN_KEY);
          await storage.deleteItem(EMAIL_KEY);
          await storage.deleteItem(USER_KEY);
          await storage.deleteItem(LOGIN_AT_KEY);
        }
      } finally {
        setIsLoading(false);
      }
    };
    bootstrap();
  }, []);

  const handleLogin = useCallback(async (emailInput: string, password: string) => {
    const result = await loginApi({ email: emailInput, password });
    setToken(result.token);
    setEmail(result.user.email);
    setUser(result.user);
    setAuthToken(result.token);
    await storage.setItem(TOKEN_KEY, result.token);
    await storage.setItem(EMAIL_KEY, result.user.email);
    await storage.setItem(USER_KEY, JSON.stringify(result.user));
    await storage.setItem(LOGIN_AT_KEY, String(Date.now()));
  }, []);

  const handleRegister = useCallback(
    async (payload: { email: string; password: string; firstName: string; lastName: string }) => {
      const result = await registerApi(payload);
      // On connecte directement l’utilisateur après inscription
      setToken(result.token);
      setEmail(result.user.email);
      setUser(result.user);
      setAuthToken(result.token);
      await storage.setItem(TOKEN_KEY, result.token);
      await storage.setItem(EMAIL_KEY, result.user.email);
      await storage.setItem(USER_KEY, JSON.stringify(result.user));
      await storage.setItem(LOGIN_AT_KEY, String(Date.now()));
    },
    [],
  );

  const handleLogout = useCallback(async () => {
    setToken(null);
    setEmail(null);
    setUser(null);
    setAuthToken(null);
    await storage.deleteItem(TOKEN_KEY);
    await storage.deleteItem(EMAIL_KEY);
    await storage.deleteItem(USER_KEY);
    await storage.deleteItem(LOGIN_AT_KEY);
  }, []);

  useEffect(() => {
    // Si l’API renvoie 401 (token expiré/invalide), on déconnecte proprement.
    setUnauthorizedHandler(() => {
      // Fire-and-forget, on ne veut pas bloquer le thread UI.
      void handleLogout();
    });
    return () => setUnauthorizedHandler(null);
  }, [handleLogout]);

  const value: AuthContextValue = useMemo(
    () => ({
      token,
      email,
      user,
      isLoading,
      login: handleLogin,
      register: handleRegister,
      logout: handleLogout,
    }),
    [token, email, user, isLoading, handleLogin, handleRegister, handleLogout],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = (): AuthContextValue => {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return ctx;
};

