-- ============================================================
-- HACKATHON 2026 - Calculateur d'empreinte carbone d'un site
-- Schema PostgreSQL
-- ============================================================

-- ============================================================
-- EXTENSIONS
-- ============================================================
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================================================
-- ENUMERATIONS
-- ============================================================

CREATE TYPE user_role AS ENUM ('ADMIN', 'USER');

CREATE TYPE parking_type AS ENUM (
    'SOUS_DALLE',       -- Sous-dalle
    'SOUS_SOL',         -- Sous-sol
    'AERIEN'            -- Aérien / extérieur
);

CREATE TYPE energy_source AS ENUM (
    'ELECTRICITE',
    'GAZ_NATUREL',
    'FIOUL',
    'BOIS_BIOMASSE',
    'AUTRE'
);

-- ============================================================
-- TABLE : users
-- Utilisateurs avec authentification JWT
-- ============================================================
CREATE TABLE users (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email       VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name  VARCHAR(100),
    last_name   VARCHAR(100),
    role        user_role NOT NULL DEFAULT 'USER',
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at  TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- ============================================================
-- TABLE : sites
-- Sites physiques saisis par les utilisateurs
-- ============================================================
CREATE TABLE sites (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id             UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name                VARCHAR(255) NOT NULL,
    address             TEXT,
    city                VARCHAR(100),
    -- Caractéristiques du site
    total_surface_m2    NUMERIC(12, 2) NOT NULL CHECK (total_surface_m2 > 0),
    employee_count      INTEGER CHECK (employee_count >= 0),
    workstation_count   INTEGER CHECK (workstation_count >= 0),
    construction_year   INTEGER,
    description         TEXT,
    created_at          TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at          TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- ============================================================
-- TABLE : site_parking
-- Places de parking par type pour un site
-- ============================================================
CREATE TABLE site_parking (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    site_id     UUID NOT NULL REFERENCES sites(id) ON DELETE CASCADE,
    type        parking_type NOT NULL,
    count       INTEGER NOT NULL CHECK (count >= 0),
    UNIQUE (site_id, type)
);

-- ============================================================
-- TABLE : site_energy_consumptions
-- Consommation énergétique annuelle d'un site
-- ============================================================
CREATE TABLE site_energy_consumptions (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    site_id             UUID NOT NULL REFERENCES sites(id) ON DELETE CASCADE,
    year                INTEGER NOT NULL CHECK (year > 1900),
    source              energy_source NOT NULL,
    consumption_mwh     NUMERIC(12, 3) NOT NULL CHECK (consumption_mwh >= 0),
    UNIQUE (site_id, year, source)
);

-- ============================================================
-- TABLE : material_types  (table de référence)
-- Matériaux de construction avec leurs facteurs d'émission
-- Source : ADEME / bases publiques
-- ============================================================
CREATE TABLE material_types (
    id                          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    code                        VARCHAR(50) NOT NULL UNIQUE,  -- ex: BETON, ACIER, VERRE, BOIS
    name                        VARCHAR(150) NOT NULL,
    unit                        VARCHAR(20) NOT NULL,           -- ex: kg, m3, m2, tonne
    co2_factor_kg_per_unit      NUMERIC(12, 6) NOT NULL,       -- kgCO2e par unité
    source                      VARCHAR(100) DEFAULT 'ADEME',
    description                 TEXT,
    updated_at                  TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- ============================================================
-- TABLE : site_materials
-- Quantités de matériaux utilisés pour la construction d'un site
-- ============================================================
CREATE TABLE site_materials (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    site_id             UUID NOT NULL REFERENCES sites(id) ON DELETE CASCADE,
    material_type_id    UUID NOT NULL REFERENCES material_types(id),
    quantity            NUMERIC(14, 3) NOT NULL CHECK (quantity >= 0),
    UNIQUE (site_id, material_type_id)
);

-- ============================================================
-- TABLE : energy_emission_factors  (table de référence)
-- Facteurs d'émission pour chaque source d'énergie
-- Source : ADEME
-- ============================================================
CREATE TABLE energy_emission_factors (
    id                          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    source                      energy_source NOT NULL,
    country_code                VARCHAR(5) NOT NULL DEFAULT 'FR',
    year                        INTEGER NOT NULL,
    factor_kg_co2_per_kwh       NUMERIC(10, 6) NOT NULL,   -- kgCO2e / kWh
    source_name                 VARCHAR(100) DEFAULT 'ADEME',
    UNIQUE (source, country_code, year)
);

-- ============================================================
-- TABLE : carbon_reports
-- Résultats calculés de l'empreinte carbone d'un site
-- Historisation des calculs dans le temps
-- ============================================================
CREATE TABLE carbon_reports (
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    site_id                 UUID NOT NULL REFERENCES sites(id) ON DELETE CASCADE,
    calculated_at           TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    reference_year          INTEGER NOT NULL,   -- Année de référence du calcul
    -- Empreinte construction (kgCO2e)
    construction_co2_kg     NUMERIC(16, 3) NOT NULL DEFAULT 0,
    -- Empreinte exploitation annuelle (kgCO2e)
    exploitation_co2_kg     NUMERIC(16, 3) NOT NULL DEFAULT 0,
    -- Total
    total_co2_kg            NUMERIC(16, 3) GENERATED ALWAYS AS (construction_co2_kg + exploitation_co2_kg) STORED,
    -- KPIs
    co2_per_m2              NUMERIC(12, 4),    -- kgCO2e / m²
    co2_per_employee        NUMERIC(12, 4),    -- kgCO2e / employé
    notes                   TEXT
);

-- ============================================================
-- TABLE : carbon_report_details
-- Détail de l'empreinte carbone par catégorie
-- ============================================================
CREATE TYPE report_category AS ENUM (
    'MATERIAU_BETON',
    'MATERIAU_ACIER',
    'MATERIAU_VERRE',
    'MATERIAU_BOIS',
    'MATERIAU_AUTRE',
    'ENERGIE_ELECTRICITE',
    'ENERGIE_GAZ',
    'ENERGIE_FIOUL',
    'ENERGIE_AUTRE',
    'PARKING'
);

CREATE TABLE carbon_report_details (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    report_id   UUID NOT NULL REFERENCES carbon_reports(id) ON DELETE CASCADE,
    category    report_category NOT NULL,
    co2_kg      NUMERIC(16, 3) NOT NULL DEFAULT 0,
    percentage  NUMERIC(5, 2),  -- % du total (0-100)
    UNIQUE (report_id, category)
);

-- ============================================================
-- INDEX
-- ============================================================
CREATE INDEX idx_sites_user_id         ON sites(user_id);
CREATE INDEX idx_site_parking_site_id  ON site_parking(site_id);
CREATE INDEX idx_site_energy_site_id   ON site_energy_consumptions(site_id);
CREATE INDEX idx_site_materials_site_id ON site_materials(site_id);
CREATE INDEX idx_carbon_reports_site_id ON carbon_reports(site_id);
CREATE INDEX idx_carbon_reports_date   ON carbon_reports(calculated_at DESC);
CREATE INDEX idx_report_details_report ON carbon_report_details(report_id);

-- ============================================================
-- TRIGGER : mise à jour automatique du champ updated_at
-- ============================================================
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_sites_updated_at
    BEFORE UPDATE ON sites
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- ============================================================
-- DONNEES DE REFERENCE : facteurs d'émission matériaux (ADEME)
-- ============================================================
INSERT INTO material_types (code, name, unit, co2_factor_kg_per_unit, source, description) VALUES
    ('BETON',       'Béton armé',               'tonne',  250.0,   'ADEME',    'Béton standard avec armatures acier'),
    ('BETON_M3',    'Béton armé',               'm3',     600.0,   'ADEME',    'Béton armé par m³ (~2,4 t/m³)'),
    ('ACIER',       'Acier de structure',        'tonne',  1850.0,  'ADEME',    'Acier laminé à chaud, production primaire'),
    ('VERRE',       'Verre plat',               'tonne',  870.0,   'ADEME',    'Verre float standard'),
    ('BOIS_MASSIF', 'Bois massif / charpente',  'tonne',  -1500.0, 'ADEME',    'Bois massif (stockage carbone inclus)'),
    ('BOIS_CLT',    'Bois lamellé-croisé (CLT)','tonne',  -900.0,  'ADEME',    'CLT, stockage carbone inclus'),
    ('ALUMINIUM',   'Aluminium',                'tonne',  11000.0, 'ADEME',    'Aluminium primaire'),
    ('BRIQUE',      'Brique de terre cuite',    'tonne',  200.0,   'ADEME',    'Brique pleine standard'),
    ('LAINE_ROCHE', 'Laine de roche',           'tonne',  1000.0,  'ADEME',    'Isolant minéral'),
    ('PVC',         'PVC rigide',               'tonne',  3700.0,  'ADEME',    'PVC (fenêtres, canalisations)');

-- ============================================================
-- DONNEES DE REFERENCE : facteurs d'émission énergie (ADEME)
-- ============================================================
INSERT INTO energy_emission_factors (source, country_code, year, factor_kg_co2_per_kwh, source_name) VALUES
    ('ELECTRICITE',  'FR', 2024, 0.0512,  'ADEME'),   -- Mix électrique français 2024
    ('GAZ_NATUREL',  'FR', 2024, 0.2271,  'ADEME'),   -- Gaz naturel
    ('FIOUL',        'FR', 2024, 0.3240,  'ADEME'),   -- Fioul domestique
    ('BOIS_BIOMASSE','FR', 2024, 0.0300,  'ADEME'),   -- Bois / biomasse
    ('AUTRE',        'FR', 2024, 0.2000,  'ADEME');   -- Autre (valeur par défaut)

-- ============================================================
-- EXEMPLE DE DONNEES : Site Capgemini Rennes (Annexe PDF)
-- ============================================================

-- Utilisateur de démonstration
INSERT INTO users (email, password_hash, first_name, last_name, role)
VALUES ('demo@hackathon.fr', '$2b$12$PLACEHOLDER_HASH', 'Demo', 'User', 'ADMIN');

-- Site de démonstration
WITH inserted_user AS (SELECT id FROM users WHERE email = 'demo@hackathon.fr')
INSERT INTO sites (user_id, name, address, city, total_surface_m2, employee_count, workstation_count, construction_year)
SELECT
    id,
    'Capgemini Rennes - Site Principal',
    'Rennes, France',
    'Rennes',
    11771,
    1800,
    1037,
    NULL
FROM inserted_user;

-- Parking (données de l'annexe)
WITH s AS (SELECT id FROM sites WHERE name = 'Capgemini Rennes - Site Principal')
INSERT INTO site_parking (site_id, type, count) VALUES
    ((SELECT id FROM s), 'SOUS_DALLE', 41),
    ((SELECT id FROM s), 'SOUS_SOL',   184),
    ((SELECT id FROM s), 'AERIEN',     83);

-- Consommation énergétique 2025 (données de l'annexe : 1 840 MWh)
WITH s AS (SELECT id FROM sites WHERE name = 'Capgemini Rennes - Site Principal')
INSERT INTO site_energy_consumptions (site_id, year, source, consumption_mwh)
VALUES ((SELECT id FROM s), 2025, 'ELECTRICITE', 1840);
