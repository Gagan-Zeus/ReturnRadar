import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json'
  }
});

export const unwrap = (response) => response.data?.data ?? response.data;

export const getApiErrorMessage = (error) => {
  const status = error?.response?.status;
  const message = error?.response?.data?.message;

  if (status === 404) return 'Resource not found';
  if (status === 409) return 'Duplicate entry detected';
  if (status === 400) return message || 'Validation error';
  if (status === 500) return 'Server error, please try again';

  return message || error?.message || 'Something went wrong';
};

export default api;
