# Hackathon 2026 — API Carbon Footprint

API Spring Boot pour le calcul de l'empreinte carbone de sites tertiaires.

## Prérequis

| Outil | Version minimale |
|-------|-----------------|
| Java | 25 |
| Maven | 3.9+ |
| Docker & Docker Compose | 27+ |
| PostgreSQL *(si lancement local sans Docker)* | 16+ |

---

## Lancement avec Docker Compose (recommandé)

Lance l'ensemble de la stack : base de données, API et pgAdmin.

```bash
docker compose up --build
```

Le schéma SQL est automatiquement appliqué au démarrage de la base de données.

| Service | URL |
|---------|-----|
| API | http://localhost:8080 |
| pgAdmin | http://localhost:5050 |

**Credentials pgAdmin :**
- Email : `hackathon26@sdv.com`
- Mot de passe : `hackathon26`

Pour arrêter :

```bash
docker compose down
```

Pour tout supprimer (y compris le volume PostgreSQL) :

```bash
docker compose down -v
```

---

## Lancement en local (sans Docker)

### 1. Créer la base de données PostgreSQL

```sql
CREATE DATABASE hackathon_db;
```

### 2. Appliquer le schéma

```bash
psql -U postgres -d hackathon_db -f SQL/schema.sql
```

### 3. Configurer `application.properties`

Le fichier `src/main/resources/application.properties` est préconfiguré avec :

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hackathon_db
spring.datasource.username=postgres
spring.datasource.password=postgres
```

Adapter si nécessaire.

### 4. Lancer l'application

```bash
./mvnw spring-boot:run
```

L'API est disponible sur **http://localhost:8080**.

---

## Authentification

L'API utilise **JWT**. Les endpoints `/api/auth/**` sont publics, tous les autres nécessitent un token.

### Créer un compte

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "motdepasse",
    "firstName": "Prénom",
    "lastName": "Nom"
  }'
```

### Se connecter

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "motdepasse"
  }'
```

Réponse :

```json
{
  "token": "<jwt>",
  "email": "user@example.com"
}
```

### Utiliser le token

```bash
curl http://localhost:8080/api/sites \
  -H "Authorization: Bearer <jwt>"
```

---

## Endpoints principaux

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/auth/register` | Créer un compte |
| POST | `/api/auth/login` | Se connecter |
| GET / POST | `/api/users` | Lister / créer des utilisateurs |
| GET / PUT / DELETE | `/api/users/{id}` | Lire / modifier / supprimer un utilisateur |
| GET / POST | `/api/sites` | Lister / créer des sites |
| GET / PUT / DELETE | `/api/sites/{id}` | Lire / modifier / supprimer un site |
| GET / POST | `/api/sites/{siteId}/parking` | Gérer les parkings d'un site |
| GET / POST | `/api/sites/{siteId}/energy-consumptions` | Gérer les consommations énergétiques |
| GET / POST | `/api/sites/{siteId}/materials` | Gérer les matériaux d'un site |
| GET / POST | `/api/material-types` | Lister / créer des types de matériaux |
| GET / POST | `/api/energy-emission-factors` | Lister / créer des facteurs d'émission |
| GET / POST | `/api/carbon-reports` | Lister / créer des rapports carbone |
| GET / POST | `/api/carbon-reports/{reportId}/details` | Gérer le détail d'un rapport |

---

## Structure du projet

```
src/main/java/com/example/api/
├── controller/       Contrôleurs REST
├── service/          Logique métier
├── repository/       Accès base de données (Spring Data JPA)
├── entity/           Entités JPA
├── dto/
│   ├── request/      Corps des requêtes (Create / Update)
│   └── response/     Corps des réponses
├── enums/            Enums partagés
├── security/         JWT (filtre, service, config Spring Security)
└── exception/        Gestion globale des erreurs
```

---

## Variables d'environnement (Docker)

| Variable | Valeur par défaut | Description |
|----------|------------------|-------------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://db:5432/hackathon_db` | URL JDBC |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | Utilisateur PostgreSQL |
| `SPRING_DATASOURCE_PASSWORD` | `postgres` | Mot de passe PostgreSQL |
| `APP_JWT_SECRET` | `hackathon2026secretkey256bitslong!!` | Clé de signature JWT |
| `APP_JWT_EXPIRATION` | `86400000` | Durée de vie du token (ms) — 24h |
