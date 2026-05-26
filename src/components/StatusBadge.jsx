import { CheckCircle2, XCircle } from 'lucide-react';
import { labelize } from '../utils/format';

const statusStyles = {
  REQUESTED: 'bg-outline-variant/30 text-on-surface-variant before:bg-outline',
  APPROVED: 'bg-status-info/10 text-status-info before:bg-status-info',
  INSPECTED: 'bg-primary-container/15 text-primary before:bg-primary',
  REFUNDED: 'bg-status-success/10 text-status-success before:bg-status-success',
  REJECTED: 'bg-status-error/10 text-status-error before:bg-status-error',
  PICKED_UP: 'bg-status-warning/10 text-status-warning before:bg-status-warning',
  CLOSED: 'bg-surface-variant text-on-surface-variant before:bg-surface-container-highest'
};

const reasonStyles = {
  SIZE_TOO_SMALL: 'bg-status-warning/10 text-status-warning',
  SIZE_TOO_LARGE: 'bg-status-warning/10 text-status-warning',
  DAMAGED_ITEM: 'bg-status-error/10 text-status-error',
  WRONG_PRODUCT: 'bg-status-info/10 text-status-info',
  POOR_QUALITY: 'bg-yellow-400/10 text-yellow-300'
};

const conditionStyles = {
  NEW: 'bg-status-success/10 text-status-success',
  GOOD: 'bg-teal-400/10 text-teal-300',
  FAIR: 'bg-status-warning/10 text-status-warning',
  DAMAGED: 'bg-orange-500/10 text-orange-300',
  UNUSABLE: 'bg-status-error/10 text-status-error'
};

const issueStyles = {
  MISSING_INSPECTION: 'bg-status-warning/10 text-status-warning',
  DUPLICATE_RETURN: 'bg-status-error/10 text-status-error',
  INVALID_REFUND_AMOUNT: 'bg-yellow-400/10 text-yellow-300',
  INCONSISTENT_WAREHOUSE_RECORD: 'bg-status-error/10 text-status-error',
  SUSPICIOUS_RETURN_PATTERN: 'bg-primary-container/15 text-primary'
};

export function StatusBadge({ value }) {
  return (
    <span className={`inline-flex items-center rounded-full px-2.5 py-1 text-xs font-semibold before:mr-1.5 before:h-1.5 before:w-1.5 before:rounded-full ${statusStyles[value] || statusStyles.REQUESTED}`}>
      {labelize(value)}
    </span>
  );
}

export function ReasonBadge({ value }) {
  return <span className={`inline-flex rounded-full px-2.5 py-1 text-xs font-semibold ${reasonStyles[value] || 'bg-surface-variant text-on-surface-variant'}`}>{labelize(value)}</span>;
}

export function ConditionBadge({ value }) {
  return <span className={`inline-flex rounded-full px-2.5 py-1 text-xs font-semibold ${conditionStyles[value] || 'bg-surface-variant text-on-surface-variant'}`}>{labelize(value)}</span>;
}

export function IssueTypeBadge({ value }) {
  return <span className={`inline-flex rounded-full px-2.5 py-1 text-xs font-semibold ${issueStyles[value] || 'bg-surface-variant text-on-surface-variant'}`}>{labelize(value)}</span>;
}

export function BooleanBadge({ value }) {
  return value ? (
    <span className="inline-flex items-center gap-1 rounded-full bg-status-success/10 px-2.5 py-1 text-xs font-semibold text-status-success">
      <CheckCircle2 size={14} /> Yes
    </span>
  ) : (
    <span className="inline-flex items-center gap-1 rounded-full bg-status-error/10 px-2.5 py-1 text-xs font-semibold text-status-error">
      <XCircle size={14} /> No
    </span>
  );
}
