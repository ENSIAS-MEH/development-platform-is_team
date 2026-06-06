import React, { useEffect, useState } from 'react';
import { sessionsAPI } from '../../api/sessions';
import { mentorsAPI } from '../../api/mentors';
import toast from 'react-hot-toast';
import { formatDateTime, SESSION_STATUS_LABEL, toApiDateTime } from '../../api/utils';
import './Student.css';

export default function SessionsPage() {
  const [sessions, setSessions] = useState([]);
  const [mentors, setMentors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [form, setForm] = useState({ mentorId: '', sessionDate: '', message: '' });

  const load = () =>
    sessionsAPI.getMySessions().then((data) => setSessions(data ?? []));

  useEffect(() => {
    Promise.all([
      load(),
      mentorsAPI.search({ available: true }).then((data) => setMentors(data ?? [])),
    ]).finally(() => setLoading(false));
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.mentorId || !form.sessionDate) {
      toast.error('Mentor et date requis.');
      return;
    }
    setSubmitting(true);
    try {
      await sessionsAPI.request({
        mentorId: Number(form.mentorId),
        sessionDate: toApiDateTime(form.sessionDate),
        message: form.message,
      });
      toast.success('Demande de session envoyée !');
      setForm({ mentorId: '', sessionDate: '', message: '' });
      await load();
    } catch {
      /* intercepteur */
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="student-page">
      <div className="page-header">
        <h1 className="page-title">Mes Sessions</h1>
        <p className="page-subtitle">Demandez et suivez vos sessions de mentorat</p>
      </div>

      <div className="form-card">
        <h2>Nouvelle demande</h2>
        <form onSubmit={handleSubmit} className="session-form">
          <div className="field">
            <label htmlFor="mentorId">Mentor</label>
            <select
              id="mentorId"
              value={form.mentorId}
              onChange={(e) => setForm((p) => ({ ...p, mentorId: e.target.value }))}
              required
            >
              <option value="">Choisir un mentor…</option>
              {mentors.map((m) => (
                <option key={m.id} value={m.id}>
                  {m.firstName} {m.lastName} — {m.filiere}
                </option>
              ))}
            </select>
          </div>
          <div className="field">
            <label htmlFor="sessionDate">Date et heure</label>
            <input
              id="sessionDate"
              type="datetime-local"
              value={form.sessionDate}
              onChange={(e) => setForm((p) => ({ ...p, sessionDate: e.target.value }))}
              required
            />
          </div>
          <div className="field">
            <label htmlFor="message">Message (optionnel)</label>
            <textarea
              id="message"
              rows={3}
              value={form.message}
              onChange={(e) => setForm((p) => ({ ...p, message: e.target.value }))}
              placeholder="Sujet de la session…"
            />
          </div>
          <button type="submit" className="btn-primary" disabled={submitting}>
            {submitting ? 'Envoi…' : 'Demander une session'}
          </button>
        </form>
      </div>

      <h2 className="section-title">Historique</h2>
      {loading ? (
        <div className="page-loading">Chargement…</div>
      ) : sessions.length === 0 ? (
        <div className="empty-state"><p>Aucune session pour le moment.</p></div>
      ) : (
        <div className="sessions-list">
          {sessions.map((s) => {
            const st = SESSION_STATUS_LABEL[s.status] || { label: s.status, className: '' };
            return (
              <div key={s.id} className="session-card">
                <div className="session-card-top">
                  <strong>{s.mentorName || s.mentorEmail}</strong>
                  <span className={`status-badge ${st.className}`}>{st.label}</span>
                </div>
                <p>{formatDateTime(s.sessionDate)}</p>
                {s.message && <p className="session-msg">{s.message}</p>}
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}
