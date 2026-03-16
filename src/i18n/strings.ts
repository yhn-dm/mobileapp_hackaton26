export type SupportedLocale = 'fr';

type Dictionary = Record<string, string>;

const fr: Dictionary = {
  // Auth
  'auth.title': 'Connexion',
  'auth.email': 'Email',
  'auth.password': 'Mot de passe',
  'auth.login': 'Se connecter',
  'auth.loggingIn': 'Connexion...',
  'auth.loginErrorTitle': 'Erreur',
  'auth.loginErrorMsg': 'Impossible de se connecter. Vérifie l’email et le mot de passe.',

  // Sites
  'sites.title': 'Mes sites',
  'sites.loadError': 'Impossible de charger les sites.',
  'sites.empty': 'Aucun site pour le moment.',
  'sites.surface': 'Surface',
  'sites.employees': 'Employés',

  // Site detail
  'siteDetail.name': 'Nom',
  'siteDetail.city': 'Ville',
  'siteDetail.address': 'Adresse',
  'siteDetail.surface': 'Surface totale',
  'siteDetail.employeeCount': 'Nombre d’employés',
  'siteDetail.workstationCount': 'Postes de travail',
  'siteDetail.parkingEntry': 'Saisie parkings',
  'siteDetail.energyEntry': 'Saisie énergie',
  'siteDetail.kpis': 'Voir les indicateurs',
  'siteDetail.loadError': 'Impossible de charger le site.',

  // Parking
  'parking.help': 'Saisis le nombre de places de parking par type pour ce site.',
  'parking.total': 'Total places',
  'parking.save': 'Enregistrer',
  'parking.saving': 'Enregistrement...',
  'parking.successTitle': 'Succès',
  'parking.successMsg': 'Parkings mis à jour.',
  'parking.errorTitle': 'Erreur',
  'parking.errorMsg': 'Impossible de sauvegarder les parkings.',
  'parking.loadError': 'Impossible de charger les parkings.',
  'parking.type.sousDalle': 'Sous-dalle',
  'parking.type.sousSol': 'Sous-sol',
  'parking.type.aerien': 'Aériens',

  // Energy
  'energy.help':
    "Saisis une consommation énergétique annuelle (en MWh) pour ce site. Utilise l’année 2025 pour coller à l’annexe du cahier des charges.",
  'energy.year': 'Année',
  'energy.source': 'Source d’énergie',
  'energy.consumption': 'Consommation (MWh/an)',
  'energy.save': 'Enregistrer',
  'energy.saving': 'Enregistrement...',
  'energy.successTitle': 'Succès',
  'energy.successMsg': 'Consommation enregistrée.',
  'energy.errorTitle': 'Erreur',
  'energy.errorMsg': 'Impossible de sauvegarder la consommation.',
  'energy.existing': 'Consommations existantes',
  'energy.noneForYear': 'Aucune consommation enregistrée pour {year}.',
  'energy.loadError': 'Impossible de charger les consommations.',
  'energy.src.electricite': 'Électricité',
  'energy.src.gaz': 'Gaz naturel',
  'energy.src.fioul': 'Fioul',
  'energy.src.bois': 'Bois / biomasse',
  'energy.src.autre': 'Autre',

  // KPIs
  'kpis.loadError': 'Impossible de charger les indicateurs.',
  'kpis.empty': 'Aucun rapport carbone disponible pour ce site.',
  'kpis.emptyHint': 'Lance un calcul depuis le back-office ou l’API, puis reviens sur cet écran.',
  'kpis.refYear': 'Année de référence',
  'kpis.calculatedAt': 'Calculé le {date}',
  'kpis.section.total': 'Bilan CO₂ (kgCO₂e)',
  'kpis.section.intensity': 'Intensité carbone',
  'kpis.construction': 'Construction',
  'kpis.exploitation': 'Exploitation',
  'kpis.total': 'Total',
  'kpis.perM2': 'Par m²',
  'kpis.perEmployee': 'Par employé',
};

const dictByLocale: Record<SupportedLocale, Dictionary> = { fr };

let currentLocale: SupportedLocale = 'fr';

export const setLocale = (locale: SupportedLocale) => {
  currentLocale = locale;
};

export const t = (key: string, params?: Record<string, string | number>): string => {
  const dict = dictByLocale[currentLocale];
  const template = dict[key] ?? key;
  if (!params) return template;
  return Object.entries(params).reduce((acc, [k, v]) => acc.replaceAll(`{${k}}`, String(v)), template);
};

