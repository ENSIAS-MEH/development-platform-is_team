export function unwrap(res) {
  return res.data?.data;
}

export function formatDateTime(value) {
  if (!value) return '—';
  return new Date(value).toLocaleString('fr-FR', {
    dateStyle: 'medium',
    timeStyle: 'short',
  });
}

export function toApiDateTime(localValue) {
  if (!localValue) return null;
  return localValue.length === 16 ? `${localValue}:00` : localValue;
}

export const FILIERES = ['2IA', 'BI', 'GD', 'GL', 'IDF', 'IDSIT', 'SSE', 'SSI'];

export const SESSION_STATUS_LABEL = {
  PENDING: { label: 'En attente', className: 'status-pending' },
  ACCEPTED: { label: 'Acceptée', className: 'status-accepted' },
  DECLINED: { label: 'Refusée', className: 'status-declined' },
};
