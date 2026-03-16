## Script de démo (mobile) – 2 à 3 minutes

### Préparation

- Backend API lancé via Docker Compose (`/api` accessible).
- App mobile ouverte sur l’écran de connexion.

---

### Démo (fil rouge “terrain”)

1) **Connexion (JWT)**
- “Je suis sur site, je m’authentifie.”
- Saisir email + mot de passe → connexion réussie → arrivée sur “Mes sites”.

2) **Sélection du site**
- Ouvrir “Capgemini Rennes – Site Principal”.
- Montrer les infos clés : surface (11 771 m²), employés (~1 800), postes (~1 037).

3) **Saisie rapide – Parkings**
- Aller sur “Saisie parkings”.
- Saisir : sous‑dalle 41, sous‑sol 184, aériens 83.
- “Enregistrer” → toast/alerte de succès.

4) **Saisie rapide – Énergie**
- Aller sur “Saisie énergie”.
- Année : 2025
- Source : Électricité
- Consommation : 1840 MWh
- “Enregistrer” → succès + la ligne apparaît dans “Consommations existantes”.

5) **Consultation d’indicateurs simples**
- Retour à la fiche du site → “Voir les indicateurs”.
- Montrer :
  - construction vs exploitation,
  - total,
  - kgCO₂e/m², kgCO₂e/employé (si le rapport est présent).

### Phrase de clôture

“Le mobile sert à collecter vite et bien les données terrain. Le dashboard web Angular sert ensuite à l’analyse avancée, la comparaison multi‑sites et les exports PDF.”

