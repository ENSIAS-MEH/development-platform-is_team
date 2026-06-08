import React, { useState } from 'react';
import { Bell, X } from 'lucide-react';
import { useNotifications } from '../../hooks/useNotifications';

export default function NotificationBell() {
  const { items, unreadCount, loading } = useNotifications(true);
  const [open, setOpen] = useState(false);

  return (
    <div className="notif-wrap">
      <button
        type="button"
        className={`notif-bell ${open ? 'notif-bell--active' : ''}`}
        title="Notifications"
        onClick={() => setOpen((o) => !o)}
        aria-expanded={open}
        aria-haspopup="dialog"
      >
        <Bell size={20} strokeWidth={1.75} />
        {unreadCount > 0 && <span className="notif-badge">{unreadCount}</span>}
      </button>
      {open && (
        <>
          <div className="notif-backdrop" onClick={() => setOpen(false)} aria-hidden />
          <div className="notif-panel" role="dialog" aria-label="Notifications">
            <div className="notif-panel-header">
              <strong>Notifications</strong>
              <button type="button" className="notif-close" onClick={() => setOpen(false)} aria-label="Fermer">
                <X size={18} strokeWidth={1.75} />
              </button>
            </div>
            {loading && <p className="notif-empty">Chargement…</p>}
            {!loading && items.length === 0 && (
              <p className="notif-empty">Aucune notification pour le moment.</p>
            )}
            <ul className="notif-list">
              {items.map((n) => (
                <li key={n.id} className={n.read ? '' : 'notif-unread'}>
                  <div className="notif-title">{n.title}</div>
                  <div className="notif-body">{n.message}</div>
                </li>
              ))}
            </ul>
          </div>
        </>
      )}
    </div>
  );
}
