import api from './client';
import { unwrap } from './utils';

export const roadmapsAPI = {
  getAll: (params) => api.get('/roadmaps', { params }).then(unwrap),
  getById: (id) => api.get(`/roadmaps/${id}`).then(unwrap),
  getMyProgress: () => api.get('/roadmaps/my-progress').then(unwrap),
  enroll: (id) => api.post(`/roadmaps/${id}/enroll`).then(unwrap),
  create: (data) => api.post('/roadmaps', data).then(unwrap),
  update: (id, data) => api.put(`/roadmaps/${id}`, data).then(unwrap),
  remove: (id) => api.delete(`/roadmaps/${id}`).then(unwrap),
};
