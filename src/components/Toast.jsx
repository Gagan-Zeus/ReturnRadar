import { CheckCircle2, XCircle } from 'lucide-react';
import { createContext, useCallback, useContext, useMemo, useState } from 'react';

const ToastContext = createContext(null);

export function ToastProvider({ children }) {
  const [toasts, setToasts] = useState([]);

  const removeToast = useCallback((id) => {
    setToasts((items) => items.filter((toast) => toast.id !== id));
  }, []);

  const showToast = useCallback((type, message) => {
    const id = crypto.randomUUID();
    setToasts((items) => [...items, { id, type, message }]);
    window.setTimeout(() => removeToast(id), 3000);
  }, [removeToast]);

  const value = useMemo(() => ({
    success: (message) => showToast('success', message),
    error: (message) => showToast('error', message)
  }), [showToast]);

  return (
    <ToastContext.Provider value={value}>
      {children}
      <div className="fixed right-5 top-5 z-[120] flex w-[360px] max-w-[calc(100vw-40px)] flex-col gap-3">
        {toasts.map((toast) => (
          <div
            key={toast.id}
            className={`glass-panel flex items-start gap-3 rounded-xl px-4 py-3 shadow-xl ${toast.type === 'success' ? 'text-status-success' : 'text-status-error'}`}
          >
            {toast.type === 'success' ? <CheckCircle2 size={20} /> : <XCircle size={20} />}
            <p className="text-sm font-semibold text-on-surface">{toast.message}</p>
          </div>
        ))}
      </div>
    </ToastContext.Provider>
  );
}

export function useToast() {
  const context = useContext(ToastContext);
  if (!context) {
    throw new Error('useToast must be used inside ToastProvider');
  }
  return context;
}
