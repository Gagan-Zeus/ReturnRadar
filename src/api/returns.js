import api, { unwrap } from './axios';

export const getAllReturns = () => api.get('/api/returns').then(unwrap);
export const getReturnById = (id) => api.get(`/api/returns/${id}`).then(unwrap);
export const createReturn = (data) => api.post('/api/returns', data).then(unwrap);
export const updateStatus = (id, status) => api.patch(`/api/returns/${id}/status`, null, { params: { status } }).then(unwrap);
