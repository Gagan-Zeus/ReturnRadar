import api, { unwrap } from './axios';

export const getAllCustomers = () => api.get('/api/customers').then(unwrap);
export const createCustomer = (data) => api.post('/api/customers', data).then(unwrap);
