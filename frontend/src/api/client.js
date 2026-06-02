import axios from 'axios';
import toast from 'react-hot-toast';

const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8080/api',
  headers: { 'Content-Type': 'application/json' },
});

const PUBLIC_PATHS = ['/login', '/register', '/unauthorized'];

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error?.response?.status;
    const message = error?.response?.data?.message;
    const isPublicPage = PUBLIC_PATHS.some((p) => window.location.pathname.startsWith(p));

    if (status === 401 && !isPublicPage) {
      localStorage.removeItem('token');
      window.location.href = '/login';
      toast.error('Session expirée, veuillez vous reconnecter.');
    } else if (status === 403) {
      toast.error(message || 'Accès refusé.');
    } else if (status === 404 && !error.config?.silent && !isPublicPage) {
      toast.error(message || 'Ressource introuvable.');
    } else if (status >= 500) {
      toast.error('Erreur serveur, réessayez plus tard.');
    } else if (message && !error.config?.silent) {
      toast.error(message);
    }

    return Promise.reject(error);
  }
);

export default api;
