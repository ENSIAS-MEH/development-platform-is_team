import { useState, useEffect, useCallback } from 'react';
import api from '../api/client';

const POLL_MS = 30000;

/**
 * [F12] Notifications — polling tant que WebSocket M2 n'est pas disponible.
 */
export function useNotifications(enabled = true) {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchNotifications = useCallback(async () => {
    if (!enabled) return;
    setLoading(true);
    try {
      const res = await api.get('/notifications', { silent: true });
      setItems(res.data?.data ?? []);
    } catch (err) {
      if (err?.response?.status !== 404) {
        /* endpoint pas encore livré par M2 */
      }
      setItems([]);
    } finally {
      setLoading(false);
    }
  }, [enabled]);

  useEffect(() => {
    fetchNotifications();
    if (!enabled) return undefined;
    const id = setInterval(fetchNotifications, POLL_MS);
    return () => clearInterval(id);
  }, [enabled, fetchNotifications]);

  const unreadCount = items.filter((n) => !n.read).length;

  return { items, unreadCount, loading, refresh: fetchNotifications };
}
