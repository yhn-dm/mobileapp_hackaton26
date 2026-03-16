## Mobile – Hackathon Carbone (React Native / Expo)

Cette app mobile est la “version terrain” : authentification JWT, saisie rapide (parkings + énergie) et consultation d’indicateurs simples.

---

### Prérequis

- Node.js + npm
- Expo Go sur un téléphone (recommandé) **ou** Android Studio (émulateur)
- L’API Spring Boot en marche (Docker Compose du projet `SupDeVinci_hackathon-26-main`)

---

### 1) Démarrer l’API (backend)

Dans `SupDeVinci_hackathon-26-main/SupDeVinci_hackathon-26-main` :

```bash
docker compose up --build
```

L’API est sur `http://localhost:8080`.

---

### 2) Configurer l’URL de l’API pour le mobile

Expo lit `API_BASE_URL` via `app.config.ts`.

- Émulateur Android : `http://10.0.2.2:8080`
- Téléphone sur le même réseau : `http://<IP_DE_TA_MACHINE>:8080`

Copie `mobile/.env.example` en `mobile/.env` et adapte :

```bash
API_BASE_URL=http://10.0.2.2:8080
```

> Si tu utilises un vrai téléphone, remplace `10.0.2.2` par l’IP locale du PC (ex : `192.168.x.x`).

---

### 3) Lancer l’app mobile

Dans `mobile/` :

```bash
npm install
npm run android
```

Ou pour Expo Go (scan QR code) :

```bash
npm run start
```

---

### 4) Compte de démo

Selon le `schema.sql` importé au démarrage, un utilisateur de démo existe :

- Email : `demo@hackathon.fr`
- Mot de passe : dépend du backend (si le hash est un placeholder, crée un compte via `/api/auth/register`)

---

### 5) Parcours de démo

- Se connecter (JWT)
- Ouvrir un site
- Renseigner parkings
- Renseigner une consommation énergie (ex. 2025 – Électricité – 1840 MWh)
- Ouvrir “Indicateurs” (si un rapport carbone existe côté API)

---

### 6) Accessibilité (a11y)

L’application mobile applique plusieurs bonnes pratiques d’accessibilité :

- Labels explicites pour tous les champs et boutons.
- Support des lecteurs d’écran via `accessibilityLabel`, `accessibilityRole` et `accessibilityHint` sur les éléments interactifs.
- Contrastes suffisants entre texte et fond via le thème `colors.ts`.
- Zones tactiles suffisamment grandes (boutons principaux larges, cartes cliquables).

Pour tester :

- Sur le Web : augmenter la taille des polices dans le navigateur et naviguer au clavier (Tab / Shift+Tab).
- Sur mobile : activer le lecteur d’écran (VoiceOver / TalkBack) et vérifier les parcours Login + saisie terrain.

