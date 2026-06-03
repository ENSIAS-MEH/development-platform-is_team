import axios from 'axios';
import toast from 'react-hot-toast';

const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || '/api',
  headers: { 'Content-Type': 'application/json' },
});

const PUBLIC_PATHS = ['/login', '/register', '/unauthorized'];

function extractErrorMessage(error) {
  const data = error?.response?.data;
  const message = data?.message;

  if (data?.data && typeof data.data === 'object' && !Array.isArray(data.data)) {
    const fieldMsg = Object.values(data.data).find(Boolean);
    if (fieldMsg) return String(fieldMsg);
  }

  if (message) return message;
  if (!error?.response) {
    return 'Impossible de joindre le serveur. Vérifiez que le backend tourne sur le port 8080 (PostgreSQL + Spring Boot).';
  }
  return null;
}

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error?.response?.status;
    const isPublicPage = PUBLIC_PATHS.some((p) => window.location.pathname.startsWith(p));
    const msg = extractErrorMessage(error);

    if (status === 401 && !isPublicPage) {
      localStorage.removeItem('token');
      window.location.href = '/login';
      toast.error('Session expirée, veuillez vous reconnecter.');
    } else if (msg && !error.config?.silent) {
      toast.error(msg);
    }

    return Promise.reject(error);
  }
);

export default api;
