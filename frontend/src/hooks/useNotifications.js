import { useState, useEffect, useCallback } from 'react';
import { useAuth } from '../context/AuthContext';
import { sessionsAPI } from '../api/sessions';
import { messagesAPI } from '../api/messages';

const POLL_MS = 30000;

export function useNotifications(enabled = true) {
  const { user } = useAuth();
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchNotifications = useCallback(async () => {
    if (!enabled || !user) return;
    setLoading(true);
    const notifs = [];

    try {
      if (user.role === 'STUDENT') {
        const sessions = await sessionsAPI.getMySessions();
        (sessions ?? []).forEach((s) => {
          if (s.status === 'ACCEPTED' || s.status === 'DECLINED') {
            notifs.push({
              id: `session-${s.id}-${s.status}`,
              title: s.status === 'ACCEPTED' ? 'Session acceptée' : 'Session refusée',
              message: `${s.mentorName || s.mentorEmail} — ${s.status}`,
              read: false,
            });
          }
        });
      }
      if (user.role === 'MENTOR') {
        const schedule = await sessionsAPI.getMentorSchedule();
        const pending = (schedule ?? []).filter((s) => s.status === 'PENDING');
        if (pending.length > 0) {
          notifs.push({
            id: 'pending-sessions',
            title: 'Nouvelles demandes',
            message: `${pending.length} session(s) en attente de réponse`,
            read: false,
          });
        }
      }
      const convs = await messagesAPI.getConversations();
      if ((convs ?? []).length > 0) {
        notifs.push({
          id: 'messages',
          title: 'Messagerie',
          message: `${convs.length} conversation(s) active(s)`,
          read: false,
        });
      }
    } catch {
      /* silencieux */
    }

    setItems(notifs);
    setLoading(false);
  }, [enabled, user]);

  useEffect(() => {
    fetchNotifications();
    if (!enabled) return undefined;
    const id = setInterval(fetchNotifications, POLL_MS);
    return () => clearInterval(id);
  }, [enabled, fetchNotifications]);

  const unreadCount = items.filter((n) => !n.read).length;

  return { items, unreadCount, loading, refresh: fetchNotifications };
}
