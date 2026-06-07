import api from './client';
import { unwrap } from './utils';

export const sessionsAPI = {
  request: (data) => api.post('/sessions/request', data).then(unwrap),
  getMySessions: () => api.get('/sessions/my-sessions').then(unwrap),
  getMentorSchedule: () => api.get('/sessions/mentor-schedule').then(unwrap),
  accept: (id) => api.put(`/sessions/${id}/accept`).then(unwrap),
  decline: (id) => api.put(`/sessions/${id}/decline`).then(unwrap),
};
