import { BarChart2, ClipboardCheck, LayoutDashboard, Package, Radar, RotateCcw, ShieldAlert, Users } from 'lucide-react';
import { NavLink } from 'react-router-dom';

const navItems = [
  { to: '/', label: 'Dashboard', icon: LayoutDashboard },
  { to: '/returns', label: 'Returns', icon: RotateCcw },
  { to: '/products', label: 'Products', icon: Package },
  { to: '/customers', label: 'Customers', icon: Users },
  { to: '/inspections', label: 'Inspections', icon: ClipboardCheck },
  { to: '/analytics', label: 'Analytics', icon: BarChart2 },
  { to: '/data-quality', label: 'Data Quality', icon: ShieldAlert }
];

export default function Sidebar() {
  return (
    <aside className="hidden h-screen w-[280px] shrink-0 flex-col border-r border-outline-variant bg-surface-container py-6 md:fixed md:left-0 md:top-0 md:z-50 md:flex">
      <div className="mb-8 flex items-center gap-3 px-6">
        <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-primary-container text-on-primary-container">
          <Radar size={19} />
        </div>
        <div>
          <h1 className="font-title-md text-title-md font-bold tracking-tight text-on-surface">ReturnRadar</h1>
          <p className="font-mono text-[10px] font-medium uppercase tracking-wider text-on-surface-variant">Analytics Suite</p>
        </div>
      </div>

      <nav className="flex-1 space-y-1 px-3">
        {navItems.map(({ to, label, icon: Icon }) => (
          <NavLink
            key={to}
            to={to}
            end={to === '/'}
            className={({ isActive }) =>
              [
                'group flex items-center gap-3 rounded-lg px-4 py-3 text-sm font-medium transition-colors active:scale-[0.98]',
                isActive
                  ? 'rounded-r-lg border-l-4 border-primary bg-primary-container/10 text-primary'
                  : 'ml-1 text-on-surface-variant hover:bg-surface-variant/50 hover:text-on-surface'
              ].join(' ')
            }
          >
            {({ isActive }) => (
              <>
                <Icon size={20} className={isActive ? 'text-primary' : 'transition-colors group-hover:text-primary'} />
                <span>{label}</span>
              </>
            )}
          </NavLink>
        ))}
      </nav>

      <div className="mx-3 border-t border-outline-variant/70 pt-4">
        <div className="rounded-xl border border-border-subtle bg-surface-container-high px-4 py-3">
          <p className="font-mono text-[11px] font-medium uppercase tracking-wider text-on-surface-variant">Version</p>
          <p className="mt-1 text-sm font-semibold text-on-surface">v1.0.0</p>
        </div>
      </div>
    </aside>
  );
}
