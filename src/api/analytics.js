import api, { unwrap } from './axios';

export const getHighReturnProducts = () => api.get('/api/analytics/high-return-products').then(unwrap);
export const getReturnReasons = () => api.get('/api/analytics/return-reasons').then(unwrap);
export const getSizeIssueProducts = () => api.get('/api/analytics/size-issue-products').then(unwrap);
export const getLocationReturns = () => api.get('/api/analytics/location-wise-returns').then(unwrap);
export const getSuspiciousCustomers = () => api.get('/api/analytics/suspicious-customers').then(unwrap);
export const getDataQualityIssues = () => api.get('/api/analytics/data-quality-issues').then(unwrap);
