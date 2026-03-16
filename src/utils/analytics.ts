type AnalyticsEvent =
  | 'login_success'
  | 'login_failure'
  | 'site_opened'
  | 'parking_update_success'
  | 'parking_update_failure'
  | 'energy_update_success'
  | 'energy_update_failure'
  | 'materials_update_success'
  | 'materials_update_failure'
  | 'kpi_viewed';

interface AnalyticsPayload {
  [key: string]: string | number | boolean | undefined;
}

// Couche analytics minimale : aujourd’hui, log console structurée.
// Plus tard, pourra être branchée sur un SDK (Segment, Amplitude…)
export const track = (event: AnalyticsEvent, payload: AnalyticsPayload = {}) => {
  // eslint-disable-next-line no-console
  console.log('[analytics]', event, payload);
};

