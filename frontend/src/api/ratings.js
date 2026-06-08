import api from './client';
import { unwrap } from './utils';

export const ratingsAPI = {
  rate: (data) => api.post('/ratings', data).then(unwrap),
  getByMentor: (mentorId) => api.get(`/ratings/mentor/${mentorId}`).then(unwrap),
};
