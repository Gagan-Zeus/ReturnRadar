import { Inbox } from 'lucide-react';

export default function EmptyState({ icon: Icon = Inbox, message = 'No data found' }) {
  return (
    <div className="flex min-h-[180px] flex-col items-center justify-center gap-3 rounded-xl border border-dashed border-outline-variant bg-surface-raised/50 text-on-surface-variant">
      <Icon size={28} className="text-primary" />
      <p className="text-sm font-medium">{message}</p>
    </div>
  );
}
