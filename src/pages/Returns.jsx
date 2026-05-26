import { Plus } from 'lucide-react';
import { useEffect, useMemo, useState } from 'react';
import { createReturn, getAllReturns, updateStatus } from '../api/returns';
import { getApiErrorMessage } from '../api/axios';
import EmptyState from '../components/EmptyState';
import LoadingSpinner from '../components/LoadingSpinner';
import Modal from '../components/Modal';
import PageHeader from '../components/PageHeader';
import { ReasonBadge, StatusBadge } from '../components/StatusBadge';
import { useToast } from '../components/Toast';
import { formatCurrency, formatDateTime } from '../utils/format';

const reasonValues = ['SIZE_TOO_SMALL', 'SIZE_TOO_LARGE', 'WRONG_PRODUCT', 'DAMAGED_ITEM', 'POOR_QUALITY', 'LATE_DELIVERY', 'CHANGED_MIND', 'DEFECTIVE', 'NOT_AS_DESCRIBED', 'OTHER'];
const statusValues = ['REQUESTED', 'APPROVED', 'REJECTED', 'PICKED_UP', 'INSPECTED', 'REFUNDED', 'CLOSED'];
const initialForm = { orderId: '', productId: '', customerId: '', reason: 'SIZE_TOO_SMALL', description: '', quantity: 1, city: '', refundAmount: 0, pickupDate: '' };

export default function Returns() {
  const toast = useToast();
  const [returns, setReturns] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [form, setForm] = useState(initialForm);
  const [submitting, setSubmitting] = useState(false);

  const sortedReturns = useMemo(() => [...returns].sort((a, b) => new Date(b.requestedAt || 0) - new Date(a.requestedAt || 0)), [returns]);

  const loadReturns = () => {
    setLoading(true);
    getAllReturns()
      .then(setReturns)
      .catch((error) => toast.error(getApiErrorMessage(error)))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    loadReturns();
  }, []);

  const closeModal = () => {
    setModalOpen(false);
    setForm(initialForm);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setSubmitting(true);
    try {
      await createReturn({
        ...form,
        orderId: Number(form.orderId),
        productId: Number(form.productId),
        customerId: Number(form.customerId),
        quantity: Number(form.quantity),
        refundAmount: Number(form.refundAmount),
        pickupDate: form.pickupDate || null
      });
      toast.success('Return request created successfully');
      closeModal();
      loadReturns();
    } catch (error) {
      toast.error(getApiErrorMessage(error));
    } finally {
      setSubmitting(false);
    }
  };

  const handleStatusChange = async (id, status) => {
    try {
      const updated = await updateStatus(id, status);
      setReturns((items) => items.map((item) => (item.returnId === id ? updated : item)));
      toast.success('Return status updated');
    } catch (error) {
      toast.error(getApiErrorMessage(error));
    }
  };

  const updateForm = (field, value) => setForm((current) => ({ ...current, [field]: value }));

  return (
    <>
      <PageHeader
        title="Return Requests"
        description="Track reverse logistics requests, refund exposure, and status movement."
        action={<button className="primary-button" onClick={() => setModalOpen(true)} type="button"><Plus size={16} /> New Return</button>}
      />

      {loading ? <LoadingSpinner /> : sortedReturns.length ? (
        <div className="table-shell">
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Order</th>
                <th>Product</th>
                <th>Customer</th>
                <th>Reason</th>
                <th>Status</th>
                <th>City</th>
                <th>Refund</th>
                <th>Requested At</th>
                <th>Update</th>
              </tr>
            </thead>
            <tbody>
              {sortedReturns.map((item) => (
                <tr key={item.returnId}>
                  <td className="font-mono">#{item.returnId}</td>
                  <td>{item.orderId}</td>
                  <td>{item.productId}</td>
                  <td>{item.customerId}</td>
                  <td><ReasonBadge value={item.reason} /></td>
                  <td><StatusBadge value={item.returnStatus} /></td>
                  <td>{item.city || '-'}</td>
                  <td className="font-mono">{formatCurrency(item.refundAmount)}</td>
                  <td>{formatDateTime(item.requestedAt)}</td>
                  <td>
                    <select className="field-input min-w-[150px] py-1.5" value={item.returnStatus || 'REQUESTED'} onChange={(event) => handleStatusChange(item.returnId, event.target.value)}>
                      {statusValues.map((status) => <option key={status} value={status}>{status.replaceAll('_', ' ')}</option>)}
                    </select>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : <EmptyState message="No return requests found" />}

      <Modal
        open={modalOpen}
        title="New Return"
        onClose={closeModal}
        footer={(
          <>
            <button className="secondary-button" onClick={closeModal} type="button">Cancel</button>
            <button className="primary-button" disabled={submitting} form="return-form" type="submit">{submitting ? 'Saving...' : 'Create Return'}</button>
          </>
        )}
      >
        <form id="return-form" className="grid grid-cols-1 gap-4 md:grid-cols-3" onSubmit={handleSubmit}>
          <Field label="Order ID" type="number" value={form.orderId} onChange={(value) => updateForm('orderId', value)} required />
          <Field label="Product ID" type="number" value={form.productId} onChange={(value) => updateForm('productId', value)} required />
          <Field label="Customer ID" type="number" value={form.customerId} onChange={(value) => updateForm('customerId', value)} required />
          <label>
            <span className="field-label">Reason</span>
            <select className="field-input" value={form.reason} onChange={(event) => updateForm('reason', event.target.value)}>
              {reasonValues.map((reason) => <option key={reason} value={reason}>{reason.replaceAll('_', ' ')}</option>)}
            </select>
          </label>
          <Field label="Quantity" type="number" min="1" value={form.quantity} onChange={(value) => updateForm('quantity', value)} required />
          <Field label="Refund Amount" type="number" min="0" value={form.refundAmount} onChange={(value) => updateForm('refundAmount', value)} required />
          <Field label="City" value={form.city} onChange={(value) => updateForm('city', value)} />
          <Field label="Pickup Date" type="datetime-local" value={form.pickupDate} onChange={(value) => updateForm('pickupDate', value)} />
          <label className="md:col-span-3">
            <span className="field-label">Description</span>
            <textarea className="field-input min-h-[96px]" value={form.description} onChange={(event) => updateForm('description', event.target.value)} />
          </label>
        </form>
      </Modal>
    </>
  );
}

function Field({ label, value, onChange, ...props }) {
  return (
    <label>
      <span className="field-label">{label}</span>
      <input className="field-input" value={value} onChange={(event) => onChange(event.target.value)} {...props} />
    </label>
  );
}
