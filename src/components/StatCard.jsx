import { TrendingUp } from 'lucide-react';

export default function StatCard({ label, value, icon: Icon, loading, tone = 'primary' }) {
  const toneClass = {
    primary: 'border-primary text-primary bg-primary-container/20',
    success: 'border-status-success text-status-success bg-status-success/10',
    warning: 'border-status-warning text-status-warning bg-status-warning/10',
    error: 'border-status-error text-status-error bg-status-error/10',
    info: 'border-status-info text-status-info bg-status-info/10'
  }[tone];

  return (
    <div className={`glass-panel group relative overflow-hidden rounded-xl border-l-4 p-card-padding ${toneClass.split(' ')[0]}`}>
      <div className="absolute -right-10 -top-10 h-32 w-32 rounded-full bg-primary-container/5 blur-2xl transition-transform group-hover:scale-150" />
      <div className="relative flex items-start justify-between">
        <span className="font-mono text-[11px] font-medium uppercase tracking-wider text-on-surface-variant">{label}</span>
        <div className={`flex h-8 w-8 items-center justify-center rounded-md ${toneClass.split(' ').slice(1).join(' ')}`}>
          {Icon ? <Icon size={18} /> : <TrendingUp size={18} />}
        </div>
      </div>
      {loading ? (
        <div className="mt-5 h-12 w-28 animate-pulse rounded-lg bg-surface-variant/70" />
      ) : (
        <div className="relative mt-4 flex items-end gap-2">
          <p className="font-mono text-4xl font-semibold leading-none text-on-surface">{value}</p>
          <span className="mb-1 rounded bg-status-success/10 px-1.5 py-0.5 text-xs font-semibold text-status-success">live</span>
        </div>
      )}
    </div>
  );
}
