import React, { useEffect, useState } from 'react';
import { sessionsAPI } from '../../api/sessions';
import toast from 'react-hot-toast';
import { formatDateTime, SESSION_STATUS_LABEL } from '../../api/utils';
import '../student/Student.css';

export default function MentorSessionsPage() {
  const [sessions, setSessions] = useState([]);
  const [loading, setLoading] = useState(true);

  const load = () =>
    sessionsAPI.getMentorSchedule().then((data) => setSessions(data ?? []));

  useEffect(() => {
    load().finally(() => setLoading(false));
  }, []);

  const handleAccept = async (id) => {
    try {
      await sessionsAPI.accept(id);
      toast.success('Session acceptée.');
      await load();
    } catch {
      /* intercepteur */
    }
  };

  const handleDecline = async (id) => {
    try {
      await sessionsAPI.decline(id);
      toast.success('Session refusée.');
      await load();
    } catch {
      /* intercepteur */
    }
  };

  const pending = sessions.filter((s) => s.status === 'PENDING');
  const others = sessions.filter((s) => s.status !== 'PENDING');

  return (
    <div className="student-page">
      <div className="page-header">
        <h1 className="page-title">Agenda des sessions</h1>
        <p className="page-subtitle">Gérez les demandes de vos étudiants</p>
      </div>

      {loading ? (
        <div className="page-loading">Chargement…</div>
      ) : (
        <>
          <h2 className="section-title">En attente ({pending.length})</h2>
          {pending.length === 0 ? (
            <p className="empty-inline">Aucune demande en attente.</p>
          ) : (
            <div className="sessions-list">
              {pending.map((s) => (
                <div key={s.id} className="session-card session-card--pending">
                  <div className="session-card-top">
                    <strong>{s.studentName || s.studentEmail}</strong>
                    <span className="status-badge status-pending">En attente</span>
                  </div>
                  <p>{formatDateTime(s.sessionDate)}</p>
                  {s.message && <p className="session-msg">{s.message}</p>}
                  <div className="mentor-actions">
                    <button type="button" className="btn-primary btn-sm" onClick={() => handleAccept(s.id)}>Accepter</button>
                    <button type="button" className="btn-ghost-sm danger" onClick={() => handleDecline(s.id)}>Refuser</button>
                  </div>
                </div>
              ))}
            </div>
          )}

          <h2 className="section-title">Planning</h2>
          {others.length === 0 ? (
            <p className="empty-inline">Aucune session planifiée.</p>
          ) : (
            <div className="sessions-list">
              {others.map((s) => {
                const st = SESSION_STATUS_LABEL[s.status] || { label: s.status, className: '' };
                return (
                  <div key={s.id} className="session-card">
                    <div className="session-card-top">
                      <strong>{s.studentName || s.studentEmail}</strong>
                      <span className={`status-badge ${st.className}`}>{st.label}</span>
                    </div>
                    <p>{formatDateTime(s.sessionDate)}</p>
                  </div>
                );
              })}
            </div>
          )}
        </>
      )}
    </div>
  );
}
