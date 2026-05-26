import { ShieldAlert } from 'lucide-react';
import { useEffect, useMemo, useState } from 'react';
import { getApiErrorMessage } from '../api/axios';
import { getDataQualityIssues } from '../api/analytics';
import EmptyState from '../components/EmptyState';
import LoadingSpinner from '../components/LoadingSpinner';
import PageHeader from '../components/PageHeader';
import StatCard from '../components/StatCard';
import { IssueTypeBadge } from '../components/StatusBadge';
import { useToast } from '../components/Toast';
import { formatDateTime, formatNumber } from '../utils/format';

export default function DataQuality() {
  const toast = useToast();
  const [issues, setIssues] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getDataQualityIssues()
      .then(setIssues)
      .catch((error) => toast.error(getApiErrorMessage(error)))
      .finally(() => setLoading(false));
  }, [toast]);

  const unresolved = useMemo(() => issues.filter((issue) => !issue.resolved).length, [issues]);

  return (
    <>
      <PageHeader title="Data Quality Issues" description="Operational anomalies, duplicate activity, and inconsistent records." />
      <div className="mb-6 grid grid-cols-1 gap-gutter md:grid-cols-2">
        <StatCard label="Total Issues" value={formatNumber(issues.length)} icon={ShieldAlert} loading={loading} tone="warning" />
        <StatCard label="Unresolved" value={formatNumber(unresolved)} icon={ShieldAlert} loading={loading} tone="error" />
      </div>
      {loading ? <LoadingSpinner /> : issues.length ? (
        <div className="table-shell">
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Issue Type</th>
                <th>Reference ID</th>
                <th>Reference Type</th>
                <th>Description</th>
                <th>Detected At</th>
                <th>Resolved</th>
              </tr>
            </thead>
            <tbody>
              {issues.map((issue) => (
                <tr key={issue.issueId}>
                  <td className="font-mono">#{issue.issueId}</td>
                  <td><IssueTypeBadge value={issue.issueType} /></td>
                  <td>{issue.referenceId || '-'}</td>
                  <td>{issue.referenceType || '-'}</td>
                  <td className="min-w-[280px] whitespace-normal">{issue.description || '-'}</td>
                  <td>{formatDateTime(issue.detectedAt)}</td>
                  <td>{issue.resolved ? <span className="rounded-full bg-status-success/10 px-2.5 py-1 text-xs font-semibold text-status-success">Resolved</span> : <span className="rounded-full bg-status-error/10 px-2.5 py-1 text-xs font-semibold text-status-error">Open</span>}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : <EmptyState icon={ShieldAlert} message="No data quality issues found" />}
    </>
  );
}
