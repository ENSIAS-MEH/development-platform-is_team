import axios from 'axios';
import toast from 'react-hot-toast';

const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8080/api',
  headers: { 'Content-Type': 'application/json' },
});

// Attach JWT token to every request
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

// Handle responses globally
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error?.response?.status;
    const message = error?.response?.data?.message;

    if (status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
      toast.error('Session expirée, veuillez vous reconnecter.');
    } else if (status === 403) {
      toast.error('Accès refusé.');
    } else if (status === 404) {
      toast.error(message || 'Ressource introuvable.');
    } else if (status >= 500) {
      toast.error('Erreur serveur, réessayez plus tard.');
    } else if (message) {
      toast.error(message);
    }

    return Promise.reject(error);
  }
);

export default api;
