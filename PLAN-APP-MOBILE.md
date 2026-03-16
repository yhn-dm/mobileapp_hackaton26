## Plan multiphase – Application mobile (React Native / Expo)

Ce plan ne concerne **que l’application mobile** (front + intégration API).  
Objectif : obtenir une app qui, **à elle seule**, respecte complètement le cahier des charges mobile du `HACKATHON-2026-Cahier-des-charges-Projet-Dev_PO.txt` :

- Authentification **JWT** robuste et extensible.
- Saisie **rapide** et fiable des données terrain (parkings, énergie, matériaux optionnels).
- Consultation **d’indicateurs simples** (CO₂ total, par m², par employé) pour un site.
- UX/UI **moderne**, cohérente avec le dashboard web, et **accessibilité maximale** (inspirée des recommandations officielles React Native / Expo / WCAG).

---

### Phase 0 – Alignement sur le cahier des charges & périmètre mobile

- **Rappel des attentes spécifiques mobile (interprétation du cahier des charges)**
  - “Application mobile (React Native) permettant une saisie rapide sur le terrain” :  
    ⇒ l’app doit être **optimisée pour la saisie**, pas pour l’analyse lourde.
  - “Consultation d’indicateurs simples” :  
    ⇒ l’app doit afficher des **KPIs synthétiques** par site (CO₂ total, CO₂/m², CO₂/employé, ratio construction/exploitation).
  - “Auth. basée sur JWT, architecture modulaire” :  
    ⇒ la mobile app consomme l’auth JWT existante, et est prête à gérer des évolutions (refresh tokens, nouveaux rôles, API externes).

- **Frontières mobile vs web (clarifiées)**
  - **Mobile** :
    - Création de compte (facultatif selon stratégie produit).
    - Connexion / déconnexion, gestion de session.
    - Saisie / édition : parkings, consommations énergétiques, éventuellement matériaux.
    - Visualisation de **quelques indicateurs clés** par site.  
  - **Web Angular** :
    - Dashboard complet, graphiques avancés, comparaison multi-sites, export PDF, heatmaps.

- **Public cible mobile**
  - Personnes sur le **terrain** (facility manager, référent RSE, technicien).
  - Contexte d’usage : déplacement, réseau parfois limité, besoin de **friction minimale** et d’une **lisibilité maximale**.

---

### Phase 1 – Cartographie de l’authentification backend & exigences mobiles

- **Analyser l’existant côté API (fait)**
  - `SecurityConfig` : 
    - JWT stateless, `/api/auth/**` public, le reste protégé.
    - CORS ouvert (`*`) → OK pour mobile/web.
  - `AuthService` :
    - `register` : création utilisateur (`email`, `password`, `firstName`, `lastName`, `role`), hashage BCrypt, génération d’un **access token JWT**.
    - `login` : vérification via `AuthenticationManager`, génération d’un **access token JWT**.
  - **Manques actuels** (fonctionnel) :
    - Pas de **refresh token** (expiration ⇒ déconnexion brutale).
    - Pas de **changement de mot de passe**, **mot de passe oublié / reset**, **verrouillage après X tentatives**.
    - Pas de gestion fine des rôles dans les endpoints (mais `UserRole` existe).

- **Côté mobile, définir les besoins auth “complets”**
  - Minimum pour hackathon :
    - Login avec conservation du token.
    - Logout propre (clear token + retour écran login).
    - Gestion de l’expiration (401 ⇒ déconnexion).
  - Compléments souhaitables :
    - Écran de **création de compte** (appel `/api/auth/register`).
    - Écran **“Mon profil”** (afficher `email`, `prénom`, `nom`, `role`).
    - Indicateur de rôle dans l’UI (badge ADMIN / USER).

- **Décision**  
  Pour le hackathon, on considère que **l’API fournit la base JWT suffisante** (login/register).  
  L’authentification sera rendue **complète côté mobile** (gestion session, erreurs, profil, UX) et **prête à accueillir** des extensions backend (refresh token, reset, change password).

---

### Phase 2 – Architecture mobile dédiée à l’auth “complète”

- **Contexte et stockage**
  - `AuthContext` : état global (`token`, `user`, `isLoading`), méthodes `login`, `logout`, `register`.
  - Stockage :
    - Mobile natif : `expo-secure-store`.
    - Web : fallback `localStorage`.
  - Intercepteur axios (`apiClient`) :
    - Ajout du header `Authorization: Bearer <token>`.
    - Gestion globale des **401** ⇒ appel automatique à `logout` (token expiré ou invalide).

- **Flux Auth complet côté mobile**
  - `LoginScreen` :
    - Formulaire (email, mot de passe), validation, affichage erreurs lisibles.
    - Gestion des états : `idle`, `submitting`, `error`.
  - `RegisterScreen` :
    - Formulaire : email, mot de passe, confirmation mot de passe, prénom, nom.
    - Appel `POST /api/auth/register`.
    - Stratégie après inscription :
      - Soit connexion auto (utiliser le token renvoyé).
      - Soit redirection vers Login avec message (“Compte créé, vous pouvez vous connecter”).
  - `ProfileScreen` :
    - Affiche les infos `user` (email, prénom, nom, rôle).
    - Bouton **Déconnexion**.
    - Placeholder “Changer mon mot de passe” (désactivé tant que l’API ne l’implémente pas).
  - Navigation :
    - Stack Auth : `Login`, `Register`.
    - Stack App : `Sites`, `SiteDetail`, `SiteParking`, `SiteEnergy`, `SiteKpi`, `Profile`.

- **Évolutions possibles (si backend évolue)**
  - Support futur :
    - `POST /api/auth/refresh` : gestion transparente des refresh tokens dans `apiClient` (renouveler le token avant expiration).
    - `POST /api/auth/change-password` : formulaire dédié dans `ProfileScreen`.
    - `POST /api/auth/forgot-password` : écran de demande de reset (email).

---

### Phase 3 – Design system mobile & lignes directrices UX/UI (graphisme et cohérence)

- **Palette & composants de base**
  - Couleurs (`theme/colors.ts`) :
    - `brand` : couleur principale (boutons, liens).
    - `background` : fond d’écran.
    - `card` : panneaux / cartes.
    - `text`, `mutedText`, `border`, `error`.
  - Composants communs :
    - `PrimaryButton` (taille, contrastes, `Pressable`).
    - `TextInputField` (label + input + helper/error text).
    - `ScreenContainer` (gestion du padding, `SafeAreaView`).
    - `SectionCard` (cartes pour les sections de détail).

- **Lignes UX spécifiques mobile**
  - **Clarté des parcours** :
    - Toujours partir de “Mes sites” après login.
    - Pas plus de 2–3 actions majeures par écran.
  - **Feedback immédiat** :
    - Spinners pour chargements.
    - Toasts / Alertes pour succès & erreurs (ex : “Parkings mis à jour”).
  - **Formulaires courts** :
    - Saisie parkings : 3 champs max (sous-dalle, sous-sol, aériens).
    - Saisie énergie : année + source + valeur.

- **Détails graphiques supplémentaires**
  - Bordures / ombres très légères sur les cartes (`shadowOpacity` faible, `elevation` modérée) pour garder une UI “clean”.
  - Hiérarchie typographique simple :
    - Titre d’écran : 22–24, semi-bold.
    - Sous-titres / labels de section : 16–18, semi-bold.
    - Texte standard : 14–16, regular.
  - Espacement vertical cohérent (multiples de 4 ou 8) pour la lisibilité.

- **Comportement plateforme**
  - Gérer le clavier (`KeyboardAvoidingView`, scroll) pour que les champs bas de page restent visibles.
  - Bouton “Retour” natif Android correctement géré via la navigation (éviter les fermetures brutales).
  - Gérer les **états de chargement** par écran (skeletons ou placeholders, pas d’écran vide).

- **Thèmes & variantes**
  - Prévoir une **compatibilité thème sombre** (couleurs adaptées) en s’appuyant sur le mode du système.
  - Garder des espacements et typographies identiques entre clair/sombre pour ne pas dégrader la lisibilité.

---

### Phase 4 – Accessibilité (a11y) et bonnes pratiques WCAG mobile

- **Textes & contrastes**
  - Vérifier le ratio de contraste couleurs texte / fond (au moins 4.5:1 pour le texte normal).
  - Police :
    - Taille minimale 14–16 pour texte courant.
    - Titres ≥ 18–20.

- **Structure sémantique & navigation par lecteur d’écran**
  - Ajouter `accessibilityLabel` sur :
    - Boutons (“Se connecter”, “Enregistrer la consommation 2025 électricité”, etc.).
    - Icônes (s’il y en a).
  - Utiliser `accessibilityRole` (`button`, `header`, `text`, etc.) sur les éléments interactifs.
  - Ordre logique de navigation :
    - Champs de formulaires dans l’ordre visuel.
    - Boutons d’action en dernier.

- **Erreurs & messages**
  - Chaque erreur de champ **liée au champ** :
    - `accessible` + description textuelle claire (“Email invalide”, “Champ requis”).
  - Erreurs globales (ex : 401) :
    - Message en haut de l’écran, lisible par lecteur d’écran (et retour au login).

- **Interactions**
  - Zone de touch minimale : 44x44 dp.
  - Éviter de compter sur la couleur seule pour signaler une erreur (texte d’erreur + icône ou motif).

- **Internationalisation et langue**
  - Textes regroupés dans un fichier (`i18n/strings.ts`) avec clés stables :
    - Permet de supporter **FR** puis **EN** si nécessaire.
  - Éviter de concaténer des chaînes avec ordre de mots figé ; préférer des templates (`{year}`, `{value}`).
  - Bascule de langue :
    - soit via la langue du système (mobile),
    - soit via une préférence utilisateur renvoyée par l’API.

- **Préférences d’accessibilité système**
  - Respecter les options système :
    - **taille de police** (Dynamic Type) : ne pas fixer de tailles absolues rigides, utiliser des styles qui supportent le scaling.
    - **réduction des animations** : si l’option “Réduire les animations” est active, limiter les transitions complexes.
  - Éviter les animations flashing / rapides qui pourraient gênér certains utilisateurs.

---

### Phase 5 – Raffinement de l’auth côté mobile (état complet)

- **Étendre `AuthContext`**
  - Ajouter l’objet `user` (id, email, prénom, nom, rôle).
  - Persister éventuellement `user` avec le token (pour éviter un appel supplémentaire au démarrage).

- **Écran Register**
  - Validation :
    - Email valide, mot de passe ≥ N caractères.
    - Confirmation mot de passe identique.
  - UX :
    - Lien “Créer un compte” depuis l’écran de login.
    - Après registration : connexion automatique ou redirection vers login avec message.

- **Écran Profil**
  - Affiche :
    - Email (non éditable).
    - Prénom, nom, rôle (lecture seule pour l’instant).
  - Actions :
    - Bouton “Déconnexion”.
    - Placeholder “Changer mon mot de passe” (désactivé tant que l’API ne le supporte pas encore).

- **Gestion des erreurs réseau / API**
  - 401 : géré globalement (logout automatique).
  - 4xx / 5xx autres :
    - Message utilisateur générique (“Impossible de joindre le serveur”).
    - Logging console pour le debug.

- **Sécurité supplémentaire côté mobile**
  - Ne jamais logguer le JWT dans la console ou des messages visibles.
  - Forcer un `logout` si le token ou l’utilisateur semblent corrompus (ex : parsing impossible).

---

### Phase 6 – Améliorations UI / accessibilité par écran (parcours métier)

- **Login & Register**
  - Labels explicites + placeholders.
  - Messages d’erreur sous les champs, en rouge, lisibles.
  - Bouton principal large, en bas de l’écran, plein largeur.

- **Liste des sites**
  - Cartes lisibles :
    - Nom (titre), ville, surface, employés.
  - Indication claire si la liste est vide.
  - Pull-to-refresh pour recharger.
   - Option de **recherche / filtrage** (par nom de site, ville) pour les parcs de sites importants.

- **Fiche site**
  - Sections en cartes :
    - Infos générales.
    - Stats (surface, employés, postes).
  - Boutons d’action regroupés (Saisie parkings / Saisie énergie / Indicateurs / Profil).

- **Parkings & Énergie**
  - Explication courte en haut.
  - Résumé calculé (ex. total de places, années affectées).
  - Boutons avec textes clairs (éviter juste une icône).
  - Optionnel : dans l’écran Énergie, proposer un **raccourci vers la dernière année utilisée** (2025) pour accélérer la saisie.

- **Indicateurs (KPIs)**
  - Présenter les valeurs de façon très simple :
    - Bloc “Année de référence”.
    - Bloc “Construction vs exploitation”.
    - Bloc “Intensité carbone (m² / employé)”.
  - Utiliser les couleurs pour la hiérarchie visuelle, mais garder le texte explicite.

- **Écrans complémentaires (optionnels) pour coller au cahier des charges**
  - **“Historique rapide”** :
    - liste des années de consommation par site (2023, 2024, 2025…),
    - affichage total CO₂ exploitation par année (sans graphiques lourds).
  - **“Aide / À propos”** :
    - rappels sur les indicateurs fournis,
    - mentions sur la source des facteurs d’émission (ADEME / OpenData).

- **Compléments fonctionnels orientés terrain**
  - Possibilité d’ajouter **des notes terrain** lors de la saisie (champ texte libre lié à l’année ou au site).
  - Possibilité d’associer **une photo** (ex. compteur, plan de parking) à une saisie, si la contrainte temps / stockage le permet.

---

### Phase 7 – Tests manuels structurés & check-list de validation

- **Scénarios Auth**
  - Créer un utilisateur via l’app (si Register activé).
  - Se connecter, fermer l’app / onglet, rouvrir :
    - vérifier que la session est restaurée.
  - Forcer un 401 (changer manuellement la clé JWT côté backend ou attendre expiration) :
    - vérifier que l’utilisateur est renvoyé à l’écran de login.

- **Saisie terrain**
  - Saisir des parkings pour un site, recharger la liste, vérifier persistance.
  - Saisir une conso énergie 2025, recharger, vérifier l’affichage.
  - Vérifier le comportement quand le réseau est perdu pendant une saisie (message d’erreur clair, pas de crash).

- **Accessibilité**
  - Tester avec un zoom texte fort (dans les réglages du device ou du navigateur).
  - Vérifier navigation clavier sur Web (tabulation).
  - Activer un lecteur d’écran (NVDA/VoiceOver) et tester au moins login + un formulaire.

- **Performance & robustesse**
  - Mesurer le temps d’ouverture de l’app jusqu’à l’affichage de “Mes sites”.
  - Vérifier que la navigation entre écrans est fluide même sur device modeste (pas de freezes).

---

### Phase 8 – Préparation du livrable mobile pour le pitch

- **Documentation**
  - Mettre à jour `mobile/README.md` :
    - Prérequis.
    - Commandes de lancement (web / Android).
    - Compte de démo (`user@hackathon.fr` / `motdepasse`).
    - Parcours de démo simplifié.

- **Scénario de démo**
  - Reprendre `DEMO-PITCH.md` en insistant sur :
    - Sécurité : JWT, déconnexion automatique si 401.
    - Simplicité d’usage terrain : quelques champs, gros boutons, feedback immédiat.
    - Accessibilité : textes lisibles, contrastes, structure claire.

- **Optionnel**
  - Générer quelques captures d’écran (login, liste sites, saisie énergie, KPIs).
  - Les intégrer dans les slides du pitch.

---

### Phase 9 – Extensions futures pour coller à la vision “plateforme”

- **Évolutions auth côté backend & intégration mobile**
  - Ajout d’un **refresh token** et d’un endpoint `/api/auth/refresh` :
    - côté mobile : stockage du refresh token, rafraîchissement automatique en cas de 401/expiration soft.
  - Implémentation d’un vrai **mot de passe oublié** :
    - écran mobile “Mot de passe oublié” ⇒ envoi email avec lien / code.
  - Rôles plus riches (`ADMIN`, `MANAGER`, `VIEWER`) :
    - adapter l’UI mobile pour masquer certaines actions aux rôles “view only”.

- **Synchronisation / mode offline (terrain)**
  - Côté mobile : file d’attente locale des saisies (parkings, énergie) quand le réseau est indisponible.
  - Synchronisation à la reconnexion avec gestion de conflits simple (last-write-wins).

- **Observabilité et analytics mobile**
  - Traquer :
    - taux d’échec des formulaires (erreurs par champ),
    - temps moyen entre ouverture app et saisie validée.
  - Exploiter ces données pour optimiser encore la saisie terrain, en accord avec la vision “Day 2” du cahier des charges.

Avec ces phases, l’application mobile est **exhaustive fonctionnellement** pour son périmètre, **cohérente graphiquement** et **soignée en accessibilité**, tout en ouvrant une trajectoire claire vers une vraie plateforme multi‑campus. 

