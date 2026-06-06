import api from './client';
import { unwrap } from './utils';

export const mentorsAPI = {
  search: (params) => api.get('/mentors/search', { params }).then(unwrap),
};
