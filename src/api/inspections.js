import api, { unwrap } from './axios';

export const getAllInspections = () => api.get('/api/inspections').then(unwrap);
export const createInspection = (data) => api.post('/api/inspections', data).then(unwrap);
export const getInspectionByReturn = (returnId) => api.get(`/api/inspections/return/${returnId}`).then(unwrap);
