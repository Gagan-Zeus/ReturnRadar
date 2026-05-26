import { Plus } from 'lucide-react';
import { useEffect, useState } from 'react';
import { getApiErrorMessage } from '../api/axios';
import { createCustomer, getAllCustomers } from '../api/customers';
import EmptyState from '../components/EmptyState';
import LoadingSpinner from '../components/LoadingSpinner';
import Modal from '../components/Modal';
import PageHeader from '../components/PageHeader';
import { useToast } from '../components/Toast';
import { formatCurrency, formatNumber } from '../utils/format';

const initialForm = { name: '', email: '', phone: '', city: '' };

export default function Customers() {
  const toast = useToast();
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [form, setForm] = useState(initialForm);
  const [submitting, setSubmitting] = useState(false);

  const loadCustomers = () => {
    setLoading(true);
    getAllCustomers().then(setCustomers).catch((error) => toast.error(getApiErrorMessage(error))).finally(() => setLoading(false));
  };

  useEffect(() => {
    loadCustomers();
  }, []);

  const closeModal = () => {
    setModalOpen(false);
    setForm(initialForm);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setSubmitting(true);
    try {
      await createCustomer(form);
      toast.success('Customer added successfully');
      closeModal();
      loadCustomers();
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
        title="Customers"
        description="Monitor customer return behavior and refund exposure."
        action={<button className="primary-button" onClick={() => setModalOpen(true)} type="button"><Plus size={16} /> Add Customer</button>}
      />

      {loading ? <LoadingSpinner /> : customers.length ? (
        <div className="table-shell">
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>City</th>
                <th>Total Orders</th>
                <th>Total Returns</th>
                <th>Refund Amount</th>
                <th>Suspicious</th>
              </tr>
            </thead>
            <tbody>
              {customers.map((customer) => (
                <tr key={customer.customerId}>
                  <td className="font-mono">#{customer.customerId}</td>
                  <td className="font-semibold">{customer.name}</td>
                  <td>{customer.email}</td>
                  <td>{customer.city || '-'}</td>
                  <td>{formatNumber(customer.totalOrders)}</td>
                  <td>{formatNumber(customer.totalReturns)}</td>
                  <td className="font-mono">{formatCurrency(customer.totalRefundAmount)}</td>
                  <td>{customer.flaggedAsSuspicious ? <span className="rounded-full bg-status-error/10 px-2.5 py-1 text-xs font-semibold text-status-error">Flagged</span> : <span className="rounded-full bg-status-success/10 px-2.5 py-1 text-xs font-semibold text-status-success">Clean</span>}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : <EmptyState message="No customers found" />}

      <Modal open={modalOpen} title="Add Customer" onClose={closeModal} footer={<><button className="secondary-button" onClick={closeModal} type="button">Cancel</button><button className="primary-button" disabled={submitting} form="customer-form" type="submit">{submitting ? 'Saving...' : 'Add Customer'}</button></>}>
        <form id="customer-form" className="grid grid-cols-1 gap-4 md:grid-cols-2" onSubmit={handleSubmit}>
          <Field label="Name" value={form.name} onChange={(value) => updateForm('name', value)} required />
          <Field label="Email" type="email" value={form.email} onChange={(value) => updateForm('email', value)} required />
          <Field label="Phone" value={form.phone} onChange={(value) => updateForm('phone', value)} />
          <Field label="City" value={form.city} onChange={(value) => updateForm('city', value)} />
        </form>
      </Modal>
    </>
  );
}

function Field({ label, value, onChange, ...props }) {
  return <label><span className="field-label">{label}</span><input className="field-input" value={value} onChange={(event) => onChange(event.target.value)} {...props} /></label>;
}
