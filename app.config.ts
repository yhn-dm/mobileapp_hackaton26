import { ExpoConfig } from 'expo/config';

const config: ExpoConfig = {
  name: 'Hackathon Carbone Mobile',
  slug: 'hackathon-carbone-mobile',
  version: '1.0.0',
  extra: {
    apiBaseUrl: process.env.API_BASE_URL ?? 'http://10.0.2.2:8080',
  },
};

export default config;

