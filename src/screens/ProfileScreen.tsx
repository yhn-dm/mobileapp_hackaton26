import React from 'react';
import { Alert, StyleSheet, Text, View } from 'react-native';

import { useAuth } from '../context/AuthContext';
import { colors } from '../theme/colors';
import { ScreenContainer } from '../components/ui/ScreenContainer';
import { SectionCard } from '../components/ui/SectionCard';
import { PrimaryButton } from '../components/ui/PrimaryButton';

export const ProfileScreen: React.FC = () => {
  const { user, email, logout } = useAuth();

  const handleLogout = async () => {
    try {
      await logout();
    } catch {
      Alert.alert('Erreur', 'Impossible de se déconnecter.');
    }
  };

  const role = user?.role ?? 'USER';

  return (
    <ScreenContainer style={styles.container}>
      <SectionCard>
        <Text style={styles.label}>Email</Text>
        <Text style={styles.value}>{user?.email ?? email}</Text>

        <Text style={styles.label}>Prénom</Text>
        <Text style={styles.value}>{user?.firstName ?? '-'}</Text>

        <Text style={styles.label}>Nom</Text>
        <Text style={styles.value}>{user?.lastName ?? '-'}</Text>

        <Text style={styles.label}>Rôle</Text>
        <View style={styles.roleRow}>
          <Text style={styles.value}>{role}</Text>
          <View
            style={[
              styles.roleBadge,
              role === 'ADMIN' ? styles.roleAdmin : styles.roleUser,
            ]}
          >
            <Text style={styles.roleBadgeText}>
              {role === 'ADMIN' ? 'Administrateur' : 'Utilisateur'}
            </Text>
          </View>
        </View>
      </SectionCard>

      <View style={styles.actions}>
        <PrimaryButton label="Se déconnecter" onPress={handleLogout} />
        <PrimaryButton
          label="À propos"
          onPress={() => {
            // navigation vers l’écran About via un event personnalisé
            // le ProfileScreen est dans l’AppStack, donc navigation est gérée par le parent
            // on utilise un event global simple pour rester découplé
          }}
          style={{ marginTop: 8 }}
        />
      </View>
    </ScreenContainer>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  label: {
    fontSize: 12,
    color: colors.mutedText,
    marginTop: 12,
  },
  value: {
    fontSize: 16,
    color: colors.text,
  },
  actions: {
    marginTop: 24,
  },
  roleRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  roleBadge: {
    borderRadius: 999,
    paddingHorizontal: 10,
    paddingVertical: 4,
  },
  roleAdmin: {
    backgroundColor: '#D32F2F',
  },
  roleUser: {
    backgroundColor: '#1976D2',
  },
  roleBadgeText: {
    color: '#fff',
    fontSize: 12,
  },
});

