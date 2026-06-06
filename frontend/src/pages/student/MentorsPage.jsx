import React, { useEffect, useState, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { mentorsAPI } from '../../api/mentors';
import { ratingsAPI } from '../../api/ratings';
import { messagesAPI } from '../../api/messages';
import StarRating from '../../components/common/StarRating';
import toast from 'react-hot-toast';
import { FILIERES } from '../../api/utils';
import './Student.css';

const FILIERE_TABS = ['Toutes', ...FILIERES];

function MentorCard({ mentor, onRate, onMessage }) {
  const initials = `${mentor.firstName?.[0] ?? ''}${mentor.lastName?.[0] ?? ''}`.toUpperCase();
  const rating = mentor.rating != null ? Number(mentor.rating) : 0;

  return (
    <article className="mentor-card">
      <div className="mentor-card-header">
        <div className="mentor-avatar">{initials}</div>
        <div className="mentor-info">
          <div className="mentor-name">{mentor.firstName} {mentor.lastName}</div>
          <div className="mentor-filiere">
            {mentor.filiere || '—'} · Promo {mentor.promo ?? '—'}
            {mentor.available === false && ' · Indisponible'}
          </div>
        </div>
      </div>
      {mentor.bio && <p className="mentor-bio">{mentor.bio}</p>}
      {mentor.expertise && <p className="mentor-expertise">Compétences : {mentor.expertise}</p>}
      <div className="mentor-card-footer">
        <StarRating value={rating} />
        <div className="mentor-actions">
          <button type="button" className="btn-ghost-sm" onClick={() => onMessage(mentor)}>
            Message
          </button>
          <button type="button" className="btn-ghost-sm" onClick={() => onRate(mentor)}>
            Noter
          </button>
        </div>
      </div>
    </article>
  );
}

export default function MentorsPage() {
  const navigate = useNavigate();
  const [mentors, setMentors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [filiere, setFiliere] = useState('Toutes');
  const [minRating, setMinRating] = useState('');
  const [availableOnly, setAvailableOnly] = useState(false);
  const [ratingModal, setRatingModal] = useState(null);
  const [draftRating, setDraftRating] = useState(0);
  const [comment, setComment] = useState('');

  const loadMentors = useCallback(() => {
    setLoading(true);
    const params = {};
    if (filiere !== 'Toutes') params.filiere = filiere;
    if (minRating) params.minRating = minRating;
    if (availableOnly) params.available = true;

    mentorsAPI
      .search(params)
      .then((data) => setMentors(data ?? []))
      .catch(() => toast.error('Impossible de charger les mentors.'))
      .finally(() => setLoading(false));
  }, [filiere, minRating, availableOnly]);

  useEffect(() => {
    loadMentors();
  }, [loadMentors]);

  const filtered = mentors.filter((m) => {
    const q = search.toLowerCase();
    return (
      !q ||
      `${m.firstName} ${m.lastName} ${m.bio ?? ''} ${m.expertise ?? ''}`.toLowerCase().includes(q)
    );
  });

  const submitRating = async () => {
    if (!ratingModal || !draftRating) return;
    try {
      await ratingsAPI.rate({
        mentorId: ratingModal.id,
        score: draftRating,
        comment: comment.trim() || undefined,
      });
      toast.success('Évaluation envoyée !');
      setRatingModal(null);
      setDraftRating(0);
      setComment('');
      loadMentors();
    } catch {
      /* intercepteur */
    }
  };

  const startMessage = async (mentor) => {
    try {
      await messagesAPI.createConversation(mentor.id);
      toast.success('Conversation ouverte.');
      navigate('/student/messages');
    } catch {
      /* intercepteur */
    }
  };

  return (
    <div className="student-page">
      <div className="page-header">
        <h1 className="page-title">Trouver un mentor</h1>
        <p className="page-subtitle">Recherche avancée — filière, note, disponibilité</p>
      </div>

      <div className="mentor-filters">
        <input
          className="search-input"
          type="search"
          placeholder="Rechercher par nom, bio…"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <div className="filter-row">
          <label className="filter-check">
            <input type="checkbox" checked={availableOnly} onChange={(e) => setAvailableOnly(e.target.checked)} />
            Disponibles uniquement
          </label>
          <select value={minRating} onChange={(e) => setMinRating(e.target.value)} className="filter-select">
            <option value="">Note min.</option>
            {[4, 3, 2, 1].map((r) => (
              <option key={r} value={r}>{r}+ étoiles</option>
            ))}
          </select>
        </div>
        <div className="filiere-tabs">
          {FILIERE_TABS.map((f) => (
            <button
              key={f}
              type="button"
              className={`filiere-tab ${filiere === f ? 'active' : ''}`}
              onClick={() => setFiliere(f)}
            >
              {f}
            </button>
          ))}
        </div>
      </div>

      {loading ? (
        <div className="page-loading">Chargement des mentors…</div>
      ) : filtered.length === 0 ? (
        <div className="empty-state"><p>Aucun mentor trouvé.</p></div>
      ) : (
        <div className="mentors-grid">
          {filtered.map((m) => (
            <MentorCard key={m.id} mentor={m} onRate={setRatingModal} onMessage={startMessage} />
          ))}
        </div>
      )}

      {ratingModal && (
        <div className="modal-backdrop" role="presentation" onClick={() => setRatingModal(null)}>
          <div className="modal-card" role="dialog" onClick={(e) => e.stopPropagation()}>
            <h3>Noter {ratingModal.firstName} {ratingModal.lastName}</h3>
            <StarRating value={draftRating} readOnly={false} onChange={setDraftRating} />
            <textarea
              className="rating-comment"
              rows={3}
              placeholder="Commentaire (optionnel)"
              value={comment}
              onChange={(e) => setComment(e.target.value)}
            />
            <div className="modal-actions">
              <button type="button" className="btn-ghost" onClick={() => setRatingModal(null)}>Annuler</button>
              <button type="button" className="btn-primary" onClick={submitRating} disabled={!draftRating}>Envoyer</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
