import api from './client';

export async function fetchMentors(params = {}) {
  try {
    const res = await api.get('/users', {
      params: { role: 'MENTOR', ...params },
      silent: true,
    });
    return res.data?.data ?? [];
  } catch {
    return [];
  }
}

export async function fetchAllUsers() {
  try {
    const res = await api.get('/admin/users', { silent: true });
    return res.data?.data ?? [];
  } catch {
    return null;
  }
}

export async function deleteUser(userId) {
  return api.delete(`/admin/users/${userId}`);
}
