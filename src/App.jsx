import { Bell, HelpCircle, Menu, Plus, Search } from 'lucide-react';
import { Route, Routes } from 'react-router-dom';
import Sidebar from './components/Sidebar';
import { ToastProvider } from './components/Toast';
import Analytics from './pages/Analytics';
import Customers from './pages/Customers';
import Dashboard from './pages/Dashboard';
import DataQuality from './pages/DataQuality';
import Inspections from './pages/Inspections';
import Products from './pages/Products';
import Returns from './pages/Returns';

export default function App() {
  return (
    <ToastProvider>
      <div className="flex min-h-screen bg-background text-on-surface">
        <Sidebar />
        <main className="flex min-h-screen flex-1 flex-col md:ml-[280px]">
          <header className="sticky top-0 z-40 flex h-16 items-center justify-between border-b border-outline-variant bg-surface/80 px-gutter backdrop-blur-md">
            <button className="rounded-lg p-2 text-on-surface md:hidden" type="button">
              <Menu size={20} />
            </button>
            <div className="hidden w-full max-w-xl sm:block">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-on-surface-variant" size={18} />
                <input
                  className="w-full rounded-lg border border-outline-variant bg-surface-container-high py-2 pl-10 pr-3 text-sm text-on-surface outline-none transition placeholder:text-on-surface-variant focus:border-primary focus:ring-2 focus:ring-primary/40"
                  placeholder="Search returns, customers, or products..."
                  type="text"
                />
              </div>
            </div>
            <div className="ml-auto flex items-center gap-3">
              <button className="relative rounded-full p-2 text-on-surface-variant transition hover:bg-surface-variant hover:text-primary" type="button">
                <Bell size={19} />
                <span className="absolute right-2 top-2 h-2 w-2 rounded-full bg-status-error ring-2 ring-surface" />
              </button>
              <button className="hidden rounded-full p-2 text-on-surface-variant transition hover:bg-surface-variant hover:text-primary sm:flex" type="button">
                <HelpCircle size={19} />
              </button>
              <button className="primary-button hidden sm:inline-flex" type="button">
                <Plus size={16} /> New Report
              </button>
            </div>
          </header>
          <div className="mx-auto flex w-full max-w-[1600px] flex-1 flex-col p-gutter">
            <Routes>
              <Route path="/" element={<Dashboard />} />
              <Route path="/returns" element={<Returns />} />
              <Route path="/products" element={<Products />} />
              <Route path="/customers" element={<Customers />} />
              <Route path="/inspections" element={<Inspections />} />
              <Route path="/analytics" element={<Analytics />} />
              <Route path="/data-quality" element={<DataQuality />} />
            </Routes>
          </div>
        </main>
      </div>
    </ToastProvider>
  );
}
