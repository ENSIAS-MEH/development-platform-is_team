import api from './client';
import { unwrap } from './utils';

export const adminAPI = {
  getStats: () => api.get('/admin/stats').then(unwrap),
};
