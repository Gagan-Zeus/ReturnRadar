import { Plus } from 'lucide-react';
import { useEffect, useState } from 'react';
import { getApiErrorMessage } from '../api/axios';
import { createInspection, getAllInspections } from '../api/inspections';
import EmptyState from '../components/EmptyState';
import LoadingSpinner from '../components/LoadingSpinner';
import Modal from '../components/Modal';
import PageHeader from '../components/PageHeader';
import { BooleanBadge, ConditionBadge } from '../components/StatusBadge';
import { useToast } from '../components/Toast';
import { formatDateTime } from '../utils/format';

const conditions = ['NEW', 'GOOD', 'FAIR', 'DAMAGED', 'UNUSABLE'];
const initialForm = { returnId: '', warehouseId: '', productCondition: 'GOOD', approved: true, rejectionReason: '', inspectorName: '' };

export default function Inspections() {
  const toast = useToast();
  const [inspections, setInspections] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [form, setForm] = useState(initialForm);
  const [submitting, setSubmitting] = useState(false);

  const loadInspections = async () => {
    setLoading(true);
    getAllInspections()
      .then(setInspections)
      .catch((error) => toast.error(getApiErrorMessage(error)))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    loadInspections();
  }, []);

  const closeModal = () => {
    setModalOpen(false);
    setForm(initialForm);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setSubmitting(true);
    try {
      await createInspection({ ...form, returnId: Number(form.returnId) });
      toast.success('Inspection created successfully');
      closeModal();
      loadInspections();
    } catch (error) {
      toast.error(getApiErrorMessage(error));
    } finally {
      setSubmitting(false);
    }
  };

  const updateForm = (field, value) => setForm((current) => ({ ...current, [field]: value }));

  return (
    <>
      <PageHeader
        title="Inspections"
        description="Warehouse quality checks and approval decisions."
        action={<button className="primary-button" onClick={() => setModalOpen(true)} type="button"><Plus size={16} /> Add Inspection</button>}
      />

      {loading ? <LoadingSpinner /> : inspections.length ? (
        <div className="table-shell">
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Return ID</th>
                <th>Warehouse</th>
                <th>Inspector</th>
                <th>Condition</th>
                <th>Approved</th>
                <th>Rejection Reason</th>
                <th>Date</th>
              </tr>
            </thead>
            <tbody>
              {inspections.map((inspection) => (
                <tr key={inspection.inspectionId}>
                  <td className="font-mono">#{inspection.inspectionId}</td>
                  <td>{inspection.returnId}</td>
                  <td>{inspection.warehouseId || '-'}</td>
                  <td>{inspection.inspectorName || '-'}</td>
                  <td><ConditionBadge value={inspection.productCondition} /></td>
                  <td><BooleanBadge value={inspection.approved} /></td>
                  <td>{inspection.rejectionReason || '-'}</td>
                  <td>{formatDateTime(inspection.inspectedAt)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : <EmptyState message="No inspections found" />}

      <Modal open={modalOpen} title="Add Inspection" onClose={closeModal} footer={<><button className="secondary-button" onClick={closeModal} type="button">Cancel</button><button className="primary-button" disabled={submitting} form="inspection-form" type="submit">{submitting ? 'Saving...' : 'Add Inspection'}</button></>}>
        <form id="inspection-form" className="grid grid-cols-1 gap-4 md:grid-cols-2" onSubmit={handleSubmit}>
          <Field label="Return ID" type="number" value={form.returnId} onChange={(value) => updateForm('returnId', value)} required />
          <Field label="Warehouse ID" value={form.warehouseId} onChange={(value) => updateForm('warehouseId', value)} required />
          <label>
            <span className="field-label">Product Condition</span>
            <select className="field-input" value={form.productCondition} onChange={(event) => updateForm('productCondition', event.target.value)}>
              {conditions.map((condition) => <option key={condition} value={condition}>{condition}</option>)}
            </select>
          </label>
          <Field label="Inspector Name" value={form.inspectorName} onChange={(value) => updateForm('inspectorName', value)} required />
          <label className="flex items-center gap-3 rounded-lg border border-outline-variant bg-surface-container-high px-3 py-2">
            <input checked={form.approved} className="h-4 w-4 rounded border-outline-variant bg-surface text-primary focus:ring-primary" type="checkbox" onChange={(event) => updateForm('approved', event.target.checked)} />
            <span className="text-sm font-semibold text-on-surface">Approved</span>
          </label>
          <label className="md:col-span-2">
            <span className="field-label">Rejection Reason</span>
            <textarea className="field-input min-h-[88px]" value={form.rejectionReason} onChange={(event) => updateForm('rejectionReason', event.target.value)} />
          </label>
        </form>
      </Modal>
    </>
  );
}

function Field({ label, value, onChange, ...props }) {
  return <label><span className="field-label">{label}</span><input className="field-input" value={value} onChange={(event) => onChange(event.target.value)} {...props} /></label>;
}
