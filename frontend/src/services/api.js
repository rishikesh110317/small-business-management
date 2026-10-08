import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('sbm_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

let isRedirecting = false;

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const isAuthEndpoint = error.config && error.config.url && error.config.url.includes('/auth/');
    if (error.response && error.response.status === 401 && !isRedirecting && !isAuthEndpoint) {
      isRedirecting = true;
      localStorage.removeItem('sbm_token');
      localStorage.removeItem('sbm_user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;
