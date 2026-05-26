export default function PageHeader({ title, description, action }) {
  return (
    <div className="mb-6 flex flex-col items-start justify-between gap-4 sm:flex-row sm:items-center">
      <div>
        <h2 className="font-headline-lg text-headline-lg text-on-surface">{title}</h2>
        {description ? <p className="mt-1 text-base text-on-surface-variant">{description}</p> : null}
      </div>
      {action ? <div className="shrink-0">{action}</div> : null}
    </div>
  );
}
