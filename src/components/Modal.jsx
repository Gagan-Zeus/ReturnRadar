import { X } from 'lucide-react';
import { useEffect } from 'react';

export default function Modal({ open, title, children, onClose, footer }) {
  useEffect(() => {
    if (!open) return undefined;
    const handleKeyDown = (event) => {
      if (event.key === 'Escape') onClose();
    };
    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [open, onClose]);

  if (!open) return null;

  return (
    <div className="fixed inset-0 z-[100] flex items-center justify-center bg-black/60 p-4 backdrop-blur-sm" onMouseDown={onClose}>
      <div className="glass-panel max-h-[90vh] w-full max-w-2xl overflow-hidden rounded-xl" onMouseDown={(event) => event.stopPropagation()}>
        <div className="flex items-center justify-between border-b border-outline-variant px-6 py-4">
          <h3 className="font-title-md text-title-md text-on-surface">{title}</h3>
          <button className="rounded-lg p-2 text-on-surface-variant transition hover:bg-surface-variant hover:text-on-surface" onClick={onClose} type="button">
            <X size={18} />
          </button>
        </div>
        <div className="max-h-[calc(90vh-140px)] overflow-y-auto px-6 py-5">{children}</div>
        {footer ? <div className="flex justify-end gap-3 border-t border-outline-variant px-6 py-4">{footer}</div> : null}
      </div>
    </div>
  );
}
