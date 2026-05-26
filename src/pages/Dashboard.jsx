import { AlertTriangle, Package, RotateCcw, ShieldAlert, Users } from 'lucide-react';
import { useEffect, useState } from 'react';
import { Bar, BarChart, Cell, Pie, PieChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts';
import { getDataQualityIssues, getHighReturnProducts, getLocationReturns, getReturnReasons, getSuspiciousCustomers } from '../api/analytics';
import { getAllReturns } from '../api/returns';
import EmptyState from '../components/EmptyState';
import LoadingSpinner from '../components/LoadingSpinner';
import PageHeader from '../components/PageHeader';
import StatCard from '../components/StatCard';
import { ReasonBadge, StatusBadge } from '../components/StatusBadge';
import { useToast } from '../components/Toast';
import { formatCurrency, formatDateTime, formatNumber, labelize } from '../utils/format';
import { getApiErrorMessage } from '../api/axios';

const pieColors = ['#310df4', '#6366f1', '#8b5cf6', '#3B82F6', '#10B981'];

export default function Dashboard() {
  const toast = useToast();
  const [loading, setLoading] = useState(true);
  const [data, setData] = useState({
    returns: [],
    suspicious: [],
    highReturnProducts: [],
    issues: [],
    reasons: [],
    locations: []
  });

  useEffect(() => {
    Promise.all([
      getAllReturns(),
      getSuspiciousCustomers(),
      getHighReturnProducts(),
      getDataQualityIssues(),
      getReturnReasons(),
      getLocationReturns()
    ])
      .then(([returns, suspicious, highReturnProducts, issues, reasons, locations]) => {
        setData({ returns, suspicious, highReturnProducts, issues, reasons, locations });
      })
      .catch((error) => toast.error(getApiErrorMessage(error)))
      .finally(() => setLoading(false));
  }, [toast]);

  const recentReturns = [...data.returns].sort((a, b) => new Date(b.requestedAt || 0) - new Date(a.requestedAt || 0)).slice(0, 8);
  const locationTopFive = data.locations.slice(0, 5);

  return (
    <>
      <PageHeader title="Overview" description="Here's what's happening with your returns today." />
      <div className="mb-section-gap grid grid-cols-1 gap-gutter md:grid-cols-2 xl:grid-cols-4">
        <StatCard label="Total Returns" value={formatNumber(data.returns.length)} icon={RotateCcw} loading={loading} tone="primary" />
        <StatCard label="Suspicious Customers" value={formatNumber(data.suspicious.length)} icon={Users} loading={loading} tone="error" />
        <StatCard label="High Return Products" value={formatNumber(data.highReturnProducts.length)} icon={Package} loading={loading} tone="warning" />
        <StatCard label="Data Quality Issues" value={formatNumber(data.issues.length)} icon={ShieldAlert} loading={loading} tone="info" />
      </div>

      <div className="mb-section-gap grid grid-cols-1 gap-gutter xl:grid-cols-5">
        <section className="glass-panel rounded-xl p-card-padding xl:col-span-3">
          <h3 className="mb-6 font-title-md text-title-md text-on-surface">Return Reasons</h3>
          {loading ? <LoadingSpinner /> : data.reasons.length ? (
            <div className="h-[320px]">
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={data.reasons}>
                  <XAxis dataKey="reason" tickFormatter={labelize} stroke="#908fa3" fontSize={12} />
                  <YAxis stroke="#908fa3" fontSize={12} />
                  <Tooltip cursor={{ fill: 'rgba(255,255,255,0.04)' }} contentStyle={{ background: '#1A192E', border: '1px solid #464557', borderRadius: 8 }} />
                  <Bar dataKey="count" fill="#310df4" radius={[6, 6, 0, 0]} />
                </BarChart>
              </ResponsiveContainer>
            </div>
          ) : <EmptyState icon={AlertTriangle} message="No return reason data found" />}
        </section>

        <section className="glass-panel rounded-xl p-card-padding xl:col-span-2">
          <h3 className="mb-6 font-title-md text-title-md text-on-surface">Returns by City</h3>
          {loading ? <LoadingSpinner /> : locationTopFive.length ? (
            <div className="grid gap-4 lg:grid-cols-[1fr_160px]">
              <div className="h-[280px]">
                <ResponsiveContainer width="100%" height="100%">
                  <PieChart>
                    <Pie data={locationTopFive} dataKey="totalReturns" nameKey="city" innerRadius={60} outerRadius={100} paddingAngle={4}>
                      {locationTopFive.map((entry, index) => <Cell key={entry.city} fill={pieColors[index % pieColors.length]} />)}
                    </Pie>
                    <Tooltip contentStyle={{ background: '#1A192E', border: '1px solid #464557', borderRadius: 8 }} />
                  </PieChart>
                </ResponsiveContainer>
              </div>
              <div className="flex flex-col justify-center gap-3">
                {locationTopFive.map((item, index) => (
                  <div key={item.city} className="flex items-center justify-between gap-3 text-sm">
                    <span className="flex items-center gap-2 text-on-surface-variant"><span className="h-2.5 w-2.5 rounded-full" style={{ backgroundColor: pieColors[index % pieColors.length] }} />{item.city}</span>
                    <span className="font-mono text-on-surface">{item.totalReturns}</span>
                  </div>
                ))}
              </div>
            </div>
          ) : <EmptyState message="No location data found" />}
        </section>
      </div>

      <section>
        <div className="mb-4 flex items-center justify-between">
          <h3 className="font-title-md text-title-md text-on-surface">Recent Returns</h3>
        </div>
        {loading ? <LoadingSpinner /> : recentReturns.length ? (
          <div className="table-shell">
            <table className="data-table">
              <thead>
                <tr>
                  <th>Return ID</th>
                  <th>Product ID</th>
                  <th>Customer ID</th>
                  <th>Reason</th>
                  <th>Status</th>
                  <th>City</th>
                  <th>Amount</th>
                  <th>Date</th>
                </tr>
              </thead>
              <tbody>
                {recentReturns.map((item) => (
                  <tr key={item.returnId}>
                    <td className="font-mono">#{item.returnId}</td>
                    <td>{item.productId}</td>
                    <td>{item.customerId}</td>
                    <td><ReasonBadge value={item.reason} /></td>
                    <td><StatusBadge value={item.returnStatus} /></td>
                    <td>{item.city || '-'}</td>
                    <td className="font-mono">{formatCurrency(item.refundAmount)}</td>
                    <td>{formatDateTime(item.requestedAt)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : <EmptyState message="No recent returns found" />}
      </section>
    </>
  );
}
