import api, { unwrap } from './axios';

export const getAllProducts = () => api.get('/api/products').then(unwrap);
export const createProduct = (data) => api.post('/api/products', data).then(unwrap);
