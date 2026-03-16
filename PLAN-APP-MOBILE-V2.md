## Plan multiphase v2 – Améliorations possibles de l’application mobile

Objectif : lister **toutes** les pistes d’amélioration de l’app mobile, sans modifier le schéma de BDD actuel, puis proposer un plan multiphase exhaustif.

---

## 1. Réflexion analytique – Axes d’amélioration

### 1.1. Fonctionnalités métier

- **Matériaux (construction) côté mobile – saisie simplifiée**
  - Aujourd’hui, la saisie des matériaux se fait plutôt via le back / web.  
  - Amélioration : permettre, pour un site, l’ajout rapide de quantités de matériaux clés (béton, acier, bois…) directement sur mobile :
    - Sélecteur de matériau (liste venant de `material_types`).
    - Champ quantité avec unité rappelée (tonnes / m³).
  - Intérêt :
    - Compléter le périmètre “construction” même depuis le terrain.
    - Donner plus de contexte à l’utilisateur sur l’impact des choix de matériaux.

- **Saisie multi-annuelle des consommations et duplications intelligentes**
  - Actuellement, l’écran Énergie vise plutôt une année à la fois.  
  - Amélioration :
    - Tableau par années (2023, 2024, 2025…) avec possibilité de dupliquer la conso de l’année N sur N+1.
    - Pré-remplissage : proposer par défaut l’année courante et 1–2 années autour.

- **Notes terrain & pièces jointes**
  - Ajouter un champ “Note” libre lié au site, à l’année ou à la saisie (énergie / parking).
  - Possibilité de prendre une **photo** (ex. compteur, plan de parking) et de l’associer à une saisie (stockage côté API à voir plus tard, sans changer le schéma principal).
  - Intérêt : enrichir les données chiffrées par du contexte terrain.

- **Sélection / filtrage avancé de sites**
  - Recherche par nom, ville, filtrage par seuil d’émissions (si données disponibles).
  - Tri : par surface, par nombre d’employés, par ordre alphabétique.

---

### 1.2. UX / UI avancée

- **Refonte visuelle complète en design moderne**
  - Palette cohérente avec le branding (Capgemini / école) :
    - Couleurs primaires / secondaires, états (hover, focus, disabled).
  - Mise en place systématique :
    - Grille / spacing homogènes (4 ou 8 px).
    - Typographie hiérarchisée (titres, sous-titres, texte, labels, légendes).
  - Icônes :
    - Icônes pour les types d’énergie, parkings, indicateurs (contrôle visuel rapide).

- **Mode sombre**
  - Suivre la préférence système (`Appearance`).
  - Travailler les contrastes pour conserver lisibilité (fond sombre, texte clair, accent color maîtrisée).

- **Feedback visuel riche**
  - États de chargement par écran :
    - Skeletons (cartes grises) pour liste de sites et fiches.
    - Spinners discrets couplés à des messages (“Chargement des sites…”).
  - États vides soigneusement designés :
    - Illustrations / icônes + texte court (“Aucun site encore disponible”, “Aucune consommation pour 2025”).

- **Guidage utilisateur**
  - Petites “tours” de bienvenue :
    - Sur la première ouverture, surligner les zones clés (ex. “Clique ici pour saisir les parkings”).
  - Tooltips / info-bulles sur les KPIs :
    - “CO₂e/m² : Emissions totales divisées par la surface utile”.

---

### 1.3. Accessibilité approfondie

- **Compatibilité stricte avec WCAG / Apple / Google a11y**
  - Vérification des contrastes colorimétriques (outils automatiques).
  - Support des préférences système :
    - Taille de police (Dynamic Type-like).
    - Réduction des animations.

- **Optimisation lecteur d’écran**
  - Définition systématique des `accessibilityRole`, `accessibilityLabel` et, si pertinent, `accessibilityHint` sur :
    - Boutons, liens, cartes cliquables (sites, actions).
  - Groupement logique :
    - Regrouper dans des `View` accessibles les blocs d’info (ex. carte de site) pour lecture fluide.

- **Navigation clavier / focus (web)**
  - Ordre logique de focus défini par la structure.
  - États focus visuels explicites (contours, surbrillance).

---

### 1.4. Performances et expérience terrain

- **Optimisation listes & requêtes**
  - Utilisation de `React Query` avec stratégies de cache + refetch intelligent.
  - Pagination / lazy loading pour longues listes de sites.

- **Gestion des erreurs réseau avancée**
  - Compléter les messages d’erreur :
    - Distinction claire “Pas de réseau” vs “Erreur serveur”.
  - Affichage d’un bandeau persistant “Hors connexion” quand l’API est injoignable.

- **Préparation au mode offline (sans changer la BDD)**
  - Mise en place de caches locaux structurant déjà les données comme :
    - “brouillons” de saisie (parkings / énergie) stockés en local, à rejouer plus tard.
  - Logique de “retry” automatique dès que le réseau revient.

---

### 1.5. Sécurité & gouvernance côté mobile

- **Durée de session et lock**
  - Inactivité prolongée ⇒ demander à l’utilisateur de se reconnecter (sans changer la logique token côté backend).
  - Option de verrouillage applicatif via un écran PIN / biométrie (à configurer en option).

- **Meilleure gestion des rôles**
  - Utiliser `user.role` pour :
    - masquer certaines actions (ex. édition réservée aux MANAGER/ADMIN),
    - afficher un badge de rôle dans le profil et éventuellement sur la liste des sites (pour rappeler la vision gouvernance).

---

### 1.6. Observabilité, analytics & logs

- **Instrumentation analytics (sans modifier la BDD)**
  - Intégration optionnelle d’un SDK (Amplitude, Segment, etc.).
  - Événements clés :
    - `login_success`, `login_failure`,
    - `site_opened`, `parking_update_success`, `energy_update_success`,
    - `kpi_viewed`.

- **Logs structurés côté mobile**
  - Structurer les logs de debug pour faciliter la corrélation avec les logs backend (siteId, userId, action…).

---

## 2. Plan multiphase exhaustif (v2)

### Phase A – Consolidation fonctionnelle (sans toucher à la BDD)

1. **Renforcer la saisie énergie**
   - Vue “multi-années” :
     - tableau des consommations par année (lignes) et par source (colonnes).
     - actions : “Dupliquer depuis l’année précédente”, “Réinitialiser”.
   - Validations :
     - valeur ≥ 0, message clair si saisie absurde (ex. 1 000 000 MWh).

2. **Saisie matériaux simplifiée côté mobile**
   - Écran `SiteMaterialsScreen` :
     - Liste des matériaux existants (`GET /api/material-types`).
     - Pour le site : `GET /api/sites/{id}/materials`.
     - Formulaire pour ajouter / modifier des quantités.
   - UI :
     - Sélecteur de matériau (dropdown).
     - Champ quantité avec unité affichée.

3. **Notes terrain**
   - Ajouter un champ “Note terrain” sur les écrans **Énergie** et **Parkings** :
     - conservé en mémoire côté mobile (ou envoyé avec les requêtes si le backend expose un champ `notes`).
   - Affichage de la note existante en dessous des champs.

---

### Phase B – UX/UI avancée & polish graphique

1. **Refonte globale des écrans clés**
   - Appliquer `ScreenContainer`, `SectionCard`, `PrimaryButton`, `TextInputField` :
     - déjà fait pour Auth & Fiche Site,
     - à étendre à Parkings, Énergie, KPIs, Profil.
   - Ajouter des icônes :
     - énergie (éclair pour électricité, flamme pour gaz, etc.),
     - parkings (P), employés, surface.

2. **Mode sombre**
   - Ajouter un switch ou adopter la configuration système via `useColorScheme`.
   - Définir un `colorsDark` cohérent et un thème global capable de basculer clair/sombre.

3. **Animations douces**
   - Ajout d’animations discrètes :
     - transitions de cartes, entrée des écrans (fade, slide léger).
     - respecter la préférence “Réduire les animations”.

---

### Phase C – Accessibilité approfondie

1. **Passage systématique sur les écrans**
   - Audit :
     - Login, Register, ListeSites, FicheSite, Parkings, Énergie, KPIs, Profil.
   - Pour chaque écran :
     - vérifier les `accessibilityLabel`, `accessibilityRole` et `accessibilityHint`.
     - ajuster l’ordre visuel / ordre de focus.

2. **Test lecteurs d’écran**
   - Scénarios :
     - Se connecter uniquement via lecteur d’écran.
     - Saisir une conso énergie avec la voix / navigation par focus.

3. **Documentation a11y**
   - Ajouter une section “Accessibilité” dans `mobile/README.md` :
     - ce qui a été fait,
     - bonnes pratiques pour futures contributions.

---

### Phase D – Performances & réseau

1. **Optimisation React Query**
   - Configurer des `staleTime` raisonnables :
     - ex. sites : quelques minutes,
     - consumptions/parkings : rafraîchissement sur focus écran.
   - Éviter de recharger toute la liste inutilement (utiliser le cache).

2. **Gestion réseau améliorée**
   - Détecter l’état réseau (via `NetInfo` ou équivalent) :
     - afficher un bandeau “Hors ligne” si pas d’accès.
   - Sur erreur d’écriture :
     - message précis (“Serveur injoignable”, “Votre connexion semble coupée”).

3. **Préfiguration “offline”**
   - Définir des structures de stockage local :
     - `pendingEnergyUpdates`, `pendingParkingUpdates` avec siteId, year/source ou type, date de création.
   - Mettre en place la logique de queue sans forcément activer la sync automatique (décrite dans la doc).

---

### Phase E – Sécurité & gouvernance

1. **Rôles**
   - Utiliser `user.role` côté UI :
     - affichage d’un badge rôle (ex. ADMIN, USER).
     - désactivation de certains boutons si le rôle n’est pas suffisant (ex. édition réservée à ADMIN).

2. **Durée de session**
   - Côté mobile :
     - stocker aussi l’horodatage de login.
     - après X heures/jours d’inactivité, forcer un logout doux (message + retour login).

3. **Protection locale**
   - Option : verrouillage applicatif (PIN / biométrie) en plus du token, paramétrable dans Profil.

---

### Phase F – Observabilité & analytics

1. **Instrumentation**
   - Ajouter une couche analytics (ou au moins des hooks internes) pour émettre les événements clés :
     - login_success / failure,
     - site_opened,
     - parking_update_success,
     - energy_update_success,
     - kpi_viewed.

2. **Tableau de bord interne (doc)**
   - Décrire comment ces événements pourront être exploités :
     - identification des écrans problématiques,
     - compréhension des parcours les plus utilisés.

---

### Phase G – Fignolage produit & pitch

1. **Micro-copies & pédagogie**
   - Relire tous les textes visibles (labels, messages, erreurs) :
     - ton homogène, pédagogique, non anxiogène.
   - Ajouter une page “À propos” :
     - rappel de la finalité de l’appli,
     - mention ADEME / OpenData pour les facteurs d’émission.

2. **Captures d’écran & scénarios de démonstration**
   - Captures :
     - Login, Liste des sites, Fiche site, Saisie parkings, Saisie énergie, KPIs, Profil.
   - Scénarios :
     - story utilisateur (personna “responsable site”) montrant l’usage typique en 1–2 minutes.

3. **Checklist finale (mobile only)**
   - Reprendre les phases A→F en checklist.
   - Cocher ce qui est implémenté, ce qui est en cours, ce qui est pour la roadmap.

---

Ce plan v2 permet de pousser l’app mobile **bien au-delà** du minimum demandé, sans toucher au schéma de BDD actuel, tout en restant aligné avec la vision “plateforme” et en offrant un terrain solide pour un futur produit industriel. 

