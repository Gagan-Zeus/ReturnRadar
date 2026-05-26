export default function LoadingSpinner({ label = 'Loading data' }) {
  return (
    <div className="flex min-h-[220px] flex-col items-center justify-center gap-3 text-on-surface-variant">
      <div className="h-8 w-8 animate-spin rounded-full border-2 border-primary/30 border-t-primary" />
      <p className="text-sm">{label}</p>
    </div>
  );
}
