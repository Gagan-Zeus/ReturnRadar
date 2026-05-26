import { Plus } from 'lucide-react';
import { useEffect, useState } from 'react';
import { getApiErrorMessage } from '../api/axios';
import { createProduct, getAllProducts } from '../api/products';
import EmptyState from '../components/EmptyState';
import LoadingSpinner from '../components/LoadingSpinner';
import Modal from '../components/Modal';
import PageHeader from '../components/PageHeader';
import { useToast } from '../components/Toast';
import { formatCurrency, formatNumber } from '../utils/format';

const categories = ['SHOES', 'SHIRTS', 'JEANS', 'DRESSES', 'JACKETS', 'ACCESSORIES', 'ACTIVEWEAR', 'OTHER'];
const initialForm = { name: '', brand: '', category: 'SHOES', size: '', color: '', price: '', stockQuantity: '' };

export default function Products() {
  const toast = useToast();
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [form, setForm] = useState(initialForm);
  const [submitting, setSubmitting] = useState(false);

  const loadProducts = () => {
    setLoading(true);
    getAllProducts().then(setProducts).catch((error) => toast.error(getApiErrorMessage(error))).finally(() => setLoading(false));
  };

  useEffect(() => {
    loadProducts();
  }, []);

  const closeModal = () => {
    setModalOpen(false);
    setForm(initialForm);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setSubmitting(true);
    try {
      await createProduct({ ...form, price: Number(form.price), stockQuantity: Number(form.stockQuantity) });
      toast.success('Product added successfully');
      closeModal();
      loadProducts();
    } catch (error) {
      toast.error(getApiErrorMessage(error));
    } finally {
      setSubmitting(false);
    }
  };

  const rateClass = (rate) => {
    if (rate > 25) return 'text-status-error';
    if (rate >= 10) return 'text-status-warning';
    return 'text-status-success';
  };

  const updateForm = (field, value) => setForm((current) => ({ ...current, [field]: value }));

  return (
    <>
      <PageHeader
        title="Product Inventory"
        description="Manage products and monitor individual return rates."
        action={<button className="primary-button" onClick={() => setModalOpen(true)} type="button"><Plus size={16} /> Add Product</button>}
      />
      {loading ? <LoadingSpinner /> : products.length ? (
        <div className="table-shell">
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Brand</th>
                <th>Category</th>
                <th>Size</th>
                <th>Price</th>
                <th>Total Sold</th>
                <th>Total Returned</th>
                <th>Return Rate</th>
              </tr>
            </thead>
            <tbody>
              {products.map((product) => (
                <tr key={product.productId}>
                  <td className="font-mono">#{product.productId}</td>
                  <td className="font-semibold">{product.name}</td>
                  <td>{product.brand || '-'}</td>
                  <td>{product.category}</td>
                  <td>{product.size || '-'}</td>
                  <td className="font-mono">{formatCurrency(product.price)}</td>
                  <td>{formatNumber(product.totalSold)}</td>
                  <td>{formatNumber(product.totalReturned)}</td>
                  <td className={`font-mono font-semibold ${rateClass(product.returnRate || 0)}`}>{Number(product.returnRate || 0).toFixed(1)}%</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : <EmptyState message="No products found" />}

      <Modal open={modalOpen} title="Add Product" onClose={closeModal} footer={<><button className="secondary-button" onClick={closeModal} type="button">Cancel</button><button className="primary-button" disabled={submitting} form="product-form" type="submit">{submitting ? 'Saving...' : 'Add Product'}</button></>}>
        <form id="product-form" className="grid grid-cols-1 gap-4 md:grid-cols-2" onSubmit={handleSubmit}>
          <Field label="Name" value={form.name} onChange={(value) => updateForm('name', value)} required />
          <Field label="Brand" value={form.brand} onChange={(value) => updateForm('brand', value)} />
          <label>
            <span className="field-label">Category</span>
            <select className="field-input" value={form.category} onChange={(event) => updateForm('category', event.target.value)}>
              {categories.map((category) => <option key={category} value={category}>{category}</option>)}
            </select>
          </label>
          <Field label="Size" value={form.size} onChange={(value) => updateForm('size', value)} />
          <Field label="Color" value={form.color} onChange={(value) => updateForm('color', value)} />
          <Field label="Price" type="number" min="0.1" step="0.01" value={form.price} onChange={(value) => updateForm('price', value)} required />
          <Field label="Stock Quantity" type="number" min="0" value={form.stockQuantity} onChange={(value) => updateForm('stockQuantity', value)} required />
        </form>
      </Modal>
    </>
  );
}

function Field({ label, value, onChange, ...props }) {
  return <label><span className="field-label">{label}</span><input className="field-input" value={value} onChange={(event) => onChange(event.target.value)} {...props} /></label>;
}
