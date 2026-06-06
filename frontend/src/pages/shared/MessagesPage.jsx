import React, { useEffect, useState, useCallback } from 'react';
import { messagesAPI } from '../../api/messages';
import toast from 'react-hot-toast';
import { formatDateTime } from '../../api/utils';
import '../student/Student.css';

export default function MessagesPage() {
  const [conversations, setConversations] = useState([]);
  const [activeId, setActiveId] = useState(null);
  const [messages, setMessages] = useState([]);
  const [draft, setDraft] = useState('');
  const [loading, setLoading] = useState(true);
  const [sending, setSending] = useState(false);

  const loadConversations = useCallback(async () => {
    try {
      const data = await messagesAPI.getConversations();
      setConversations(data ?? []);
      if (data?.length && !activeId) setActiveId(data[0].id);
    } catch {
      toast.error('Impossible de charger les conversations.');
    } finally {
      setLoading(false);
    }
  }, [activeId]);

  useEffect(() => {
    loadConversations();
  }, [loadConversations]);

  useEffect(() => {
    if (!activeId) {
      setMessages([]);
      return;
    }
    messagesAPI
      .getMessages(activeId)
      .then((data) => setMessages(data ?? []))
      .catch(() => toast.error('Impossible de charger les messages.'));
  }, [activeId]);

  const handleSend = async (e) => {
    e.preventDefault();
    if (!draft.trim() || !activeId) return;
    setSending(true);
    try {
      const msg = await messagesAPI.send(activeId, draft.trim());
      setMessages((prev) => [...prev, msg]);
      setDraft('');
    } catch {
      /* intercepteur */
    } finally {
      setSending(false);
    }
  };

  const activeConv = conversations.find((c) => c.id === activeId);

  return (
    <div className="student-page">
      <div className="page-header">
        <h1 className="page-title">Messagerie</h1>
        <p className="page-subtitle">Échanges avec vos mentors ou étudiants</p>
      </div>

      {loading ? (
        <div className="page-loading">Chargement…</div>
      ) : (
        <div className="chat-layout">
          <aside className="chat-sidebar">
            <h3>Conversations</h3>
            {conversations.length === 0 ? (
              <p className="chat-empty-text">Aucune conversation. Contactez un mentor depuis la page mentors.</p>
            ) : (
              <ul className="conv-list">
                {conversations.map((c) => (
                  <li key={c.id}>
                    <button
                      type="button"
                      className={`conv-item ${activeId === c.id ? 'active' : ''}`}
                      onClick={() => setActiveId(c.id)}
                    >
                      <strong>{c.studentEmail}</strong>
                      <span>↔ {c.mentorEmail}</span>
                    </button>
                  </li>
                ))}
              </ul>
            )}
          </aside>

          <section className="chat-panel">
            {!activeConv ? (
              <div className="chat-empty">Sélectionnez une conversation</div>
            ) : (
              <>
                <div className="chat-panel-header">
                  {activeConv.studentEmail} · {activeConv.mentorEmail}
                </div>
                <ul className="msg-list">
                  {messages.map((m) => (
                    <li key={m.id} className="msg-item">
                      <div className="msg-meta">{m.senderEmail} · {formatDateTime(m.sentAt)}</div>
                      <div className="msg-content">{m.content}</div>
                    </li>
                  ))}
                </ul>
                <form className="msg-form" onSubmit={handleSend}>
                  <input
                    type="text"
                    placeholder="Votre message…"
                    value={draft}
                    onChange={(e) => setDraft(e.target.value)}
                  />
                  <button type="submit" className="btn-primary" disabled={sending || !draft.trim()}>
                    Envoyer
                  </button>
                </form>
              </>
            )}
          </section>
        </div>
      )}
    </div>
  );
}
