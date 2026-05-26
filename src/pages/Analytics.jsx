import { AlertTriangle } from 'lucide-react';
import { useEffect, useState } from 'react';
import { Bar, BarChart, CartesianGrid, Cell, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts';
import { getApiErrorMessage } from '../api/axios';
import { getHighReturnProducts, getLocationReturns, getSizeIssueProducts, getSuspiciousCustomers } from '../api/analytics';
import EmptyState from '../components/EmptyState';
import LoadingSpinner from '../components/LoadingSpinner';
import PageHeader from '../components/PageHeader';
import { formatCurrency, formatNumber } from '../utils/format';
import { useToast } from '../components/Toast';

export default function Analytics() {
  const toast = useToast();
  const [state, setState] = useState({
    high: { loading: true, data: [] },
    size: { loading: true, data: [] },
    locations: { loading: true, data: [] },
    suspicious: { loading: true, data: [] }
  });

  useEffect(() => {
    loadSection('high', getHighReturnProducts);
    loadSection('size', getSizeIssueProducts);
    loadSection('locations', getLocationReturns);
    loadSection('suspicious', getSuspiciousCustomers);
  }, []);

  const loadSection = (key, fn) => {
    fn()
      .then((data) => setState((current) => ({ ...current, [key]: { loading: false, data } })))
      .catch((error) => {
        toast.error(getApiErrorMessage(error));
        setState((current) => ({ ...current, [key]: { loading: false, data: [] } }));
      });
  };

  return (
    <>
      <PageHeader title="Analytics Overview" description="Comprehensive breakdown of return metrics and flagged activity." />
      <div className="grid grid-cols-1 gap-6 xl:grid-cols-2">
        <ChartPanel title="High Return Products" subtitle="Return rate exposure" loading={state.high.loading} data={state.high.data} color="#EF4444" />
        <ChartPanel title="Size Issue Products" subtitle="Size/Fit return concentration" loading={state.size.loading} data={state.size.data} color="#F59E0B" />
        <LocationPanel loading={state.locations.loading} data={state.locations.data} />
        <SuspiciousPanel loading={state.suspicious.loading} data={state.suspicious.data} />
      </div>
    </>
  );
}

function ChartPanel({ title, subtitle, loading, data, color }) {
  const chartData = data.map((item) => ({ ...item, productName: item.name }));
  return (
    <section className="glass-panel rounded-xl p-card-padding">
      <div className="mb-6 border-b border-outline-variant pb-4">
        <h3 className="font-title-md text-title-md text-on-surface">{title}</h3>
        <p className="mt-1 text-sm text-on-surface-variant">{subtitle}</p>
      </div>
      {loading ? <LoadingSpinner /> : chartData.length ? (
        <div className="h-[320px]">
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={chartData} layout="vertical" margin={{ left: 24, right: 24 }}>
              <CartesianGrid stroke="#33324a" strokeDasharray="3 3" horizontal={false} />
              <XAxis type="number" stroke="#908fa3" fontSize={12} unit="%" />
              <YAxis type="category" dataKey="productName" width={140} stroke="#908fa3" fontSize={12} />
              <Tooltip contentStyle={{ background: '#1A192E', border: '1px solid #464557', borderRadius: 8 }} formatter={(value) => [`${Number(value).toFixed(1)}%`, 'Return Rate']} />
              <Bar dataKey="returnRate" radius={[0, 6, 6, 0]}>
                {chartData.map((item) => <Cell key={item.productId} fill={color} />)}
              </Bar>
            </BarChart>
          </ResponsiveContainer>
        </div>
      ) : <EmptyState icon={AlertTriangle} message="No products found" />}
    </section>
  );
}

function LocationPanel({ loading, data }) {
  return (
    <section className="glass-panel rounded-xl p-card-padding">
      <div className="mb-6 border-b border-outline-variant pb-4">
        <h3 className="font-title-md text-title-md text-on-surface">Location Wise Returns</h3>
        <p className="mt-1 text-sm text-on-surface-variant">Refund exposure by city</p>
      </div>
      {loading ? <LoadingSpinner /> : data.length ? (
        <div className="h-[320px]">
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={data} layout="vertical" margin={{ left: 20, right: 34 }}>
              <CartesianGrid stroke="#33324a" strokeDasharray="3 3" horizontal={false} />
              <XAxis type="number" stroke="#908fa3" fontSize={12} />
              <YAxis type="category" dataKey="city" width={110} stroke="#908fa3" fontSize={12} />
              <Tooltip contentStyle={{ background: '#1A192E', border: '1px solid #464557', borderRadius: 8 }} formatter={(value, name, props) => [name === 'totalReturns' ? value : formatCurrency(props.payload.totalRefundAmount), name]} />
              <Bar dataKey="totalReturns" fill="#3B82F6" radius={[0, 6, 6, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>
      ) : <EmptyState message="No city data found" />}
    </section>
  );
}

function SuspiciousPanel({ loading, data }) {
  return (
    <section className="glass-panel rounded-xl p-card-padding">
      <div className="mb-6 border-b border-outline-variant pb-4">
        <h3 className="font-title-md text-title-md text-on-surface">Suspicious Customers</h3>
        <p className="mt-1 text-sm text-on-surface-variant">Customers flagged by return behavior</p>
      </div>
      {loading ? <LoadingSpinner /> : data.length ? (
        <div className="table-shell border-0 bg-transparent">
          <table className="data-table">
            <thead>
              <tr><th>Name</th><th>Email</th><th>City</th><th>Total Returns</th><th>Total Refund</th><th>Status</th></tr>
            </thead>
            <tbody>
              {data.map((customer) => (
                <tr key={customer.customerId}>
                  <td className="font-semibold text-status-error">⚠ {customer.name}</td>
                  <td>{customer.email}</td>
                  <td>{customer.city || '-'}</td>
                  <td>{formatNumber(customer.totalReturns)}</td>
                  <td className="font-mono">{formatCurrency(customer.totalRefundAmount)}</td>
                  <td><span className="rounded-full bg-status-error/10 px-2.5 py-1 text-xs font-semibold text-status-error">Flagged</span></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : <EmptyState message="No suspicious customers found" />}
    </section>
  );
}
