import api from './client';
import { unwrap } from './utils';

export const messagesAPI = {
  getConversations: () => api.get('/messages/conversations').then(unwrap),
  createConversation: (otherUserId) =>
    api.post('/messages/conversations', null, { params: { otherUserId } }).then(unwrap),
  getMessages: (conversationId) => api.get(`/messages/${conversationId}`).then(unwrap),
  send: (conversationId, content) =>
    api.post(`/messages/${conversationId}`, { content }).then(unwrap),
};
