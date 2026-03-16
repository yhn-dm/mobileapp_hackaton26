import { Platform } from 'react-native';

export const getApiBaseUrl = (): string => {
  // Pour le hackathon, on lit l’URL depuis une variable d’environnement Expo
  // et on fournit une valeur par défaut si rien n’est configuré.
  // Exemple d’override : APP_ENV_API_BASE_URL dans app.config.ts.
  // @ts-expect-error Expo ajoute globalThis.expoConfig au runtime
  const fromConfig = globalThis.expoConfig?.extra?.apiBaseUrl as string | undefined;

  if (fromConfig) return fromConfig;

  // Defaults selon la plateforme :
  // - web (navigateur) : localhost
  // - android emulator : 10.0.2.2
  // - iOS / device réel : à configurer via API_BASE_URL
  return Platform.OS === 'web' ? 'http://localhost:8080' : 'http://10.0.2.2:8080';
};

