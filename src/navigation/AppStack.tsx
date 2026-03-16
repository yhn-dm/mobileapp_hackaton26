import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';

import { SiteListScreen } from '../screens/SiteListScreen';
import { SiteDetailScreen } from '../screens/SiteDetailScreen';
import { SiteParkingScreen } from '../screens/SiteParkingScreen';
import { SiteEnergyScreen } from '../screens/SiteEnergyScreen';
import { SiteKpiScreen } from '../screens/SiteKpiScreen';
import { ProfileScreen } from '../screens/ProfileScreen';
import { SiteMaterialsScreen } from '../screens/SiteMaterialsScreen';
import { AboutScreen } from '../screens/AboutScreen';

export type AppStackParamList = {
  Sites: undefined;
  SiteDetail: { siteId: string; siteName: string };
  SiteParking: { siteId: string; siteName: string };
  SiteEnergy: { siteId: string; siteName: string };
  SiteKpi: { siteId: string; siteName: string };
  Profile: undefined;
  SiteMaterials: { siteId: string; siteName: string };
  About: undefined;
};

const Stack = createNativeStackNavigator<AppStackParamList>();

export const AppStack: React.FC = () => {
  return (
    <Stack.Navigator>
      <Stack.Screen name="Sites" component={SiteListScreen} options={{ title: 'Mes sites' }} />
      <Stack.Screen
        name="SiteDetail"
        component={SiteDetailScreen}
        options={({ route }) => ({ title: route.params.siteName })}
      />
      <Stack.Screen
        name="SiteParking"
        component={SiteParkingScreen}
        options={({ route }) => ({ title: `${route.params.siteName} - Parkings` })}
      />
      <Stack.Screen
        name="SiteEnergy"
        component={SiteEnergyScreen}
        options={({ route }) => ({ title: `${route.params.siteName} - Énergie` })}
      />
      <Stack.Screen
        name="SiteKpi"
        component={SiteKpiScreen}
        options={({ route }) => ({ title: `${route.params.siteName} - Indicateurs` })}
      />
      <Stack.Screen
        name="SiteMaterials"
        component={SiteMaterialsScreen}
        options={({ route }) => ({ title: `${route.params.siteName} - Matériaux` })}
      />
      <Stack.Screen name="About" component={AboutScreen} options={{ title: 'À propos' }} />
      <Stack.Screen name="Profile" component={ProfileScreen} options={{ title: 'Mon profil' }} />
    </Stack.Navigator>
  );
};


