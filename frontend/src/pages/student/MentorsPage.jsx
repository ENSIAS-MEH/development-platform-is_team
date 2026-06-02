import React, { useEffect, useState } from 'react';
import { fetchMentors } from '../../api/users';
import StarRating from '../../components/common/StarRating';
import toast from 'react-hot-toast';
import './Student.css';

const FILIERES = ['Toutes', '2IA', 'BI', 'GL', 'IDF', 'IDSIT', 'SSE', 'SSI'];

function MentorCard({ mentor, onRate }) {
  const initials = `${mentor.firstName?.[0] ?? ''}${mentor.lastName?.[0] ?? ''}`.toUpperCase();
  return (
    <article className="mentor-card">
      <div className="mentor-card-header">
        <div className="mentor-avatar">{initials}</div>
        <div className="mentor-info">
          <div className="mentor-name">{mentor.firstName} {mentor.lastName}</div>
          <div className="mentor-filiere">
            {mentor.filiere || '—'} · Promo {mentor.promo || '—'}
          </div>
        </div>
      </div>
      {mentor.bio && <p className="mentor-bio">{mentor.bio}</p>}
      <div className="mentor-card-footer">
        <StarRating value={mentor.averageRating ?? 0} />
        <button type="button" className="btn-ghost-sm" onClick={() => onRate(mentor)}>
          Noter
        </button>
      </div>
    </article>
  );
}

export default function MentorsPage() {
  const [mentors, setMentors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [apiHint, setApiHint] = useState(false);
  const [search, setSearch] = useState('');
  const [filiere, setFiliere] = useState('Toutes');
  const [ratingModal, setRatingModal] = useState(null);
  const [draftRating, setDraftRating] = useState(0);
  const [comment, setComment] = useState('');

  useEffect(() => {
    fetchMentors()
      .then((data) => {
        setMentors(data);
        if (data.length === 0) setApiHint(true);
      })
      .finally(() => setLoading(false));
  }, []);

  const filtered = mentors.filter((m) => {
    const matchFiliere = filiere === 'Toutes' || m.filiere === filiere;
    const q = search.toLowerCase();
    const matchSearch =
      !q || `${m.firstName} ${m.lastName} ${m.bio ?? ''}`.toLowerCase().includes(q);
    return matchFiliere && matchSearch;
  });

  const submitRating = () => {
    toast.success(
      `Note enregistrée localement (${draftRating}/5). Brancher POST /api/mentors/{id}/reviews quand M2 sera prêt.`
    );
    setRatingModal(null);
    setDraftRating(0);
    setComment('');
  };

  return (
    <div className="student-page">
      <div className="page-header">
        <h1 className="page-title">Trouver un mentor</h1>
        <p className="page-subtitle">Filtres par filière et recherche textuelle</p>
      </div>

      <div className="mentor-filters">
        <input
          className="search-input"
          type="search"
          placeholder="Rechercher par nom, bio…"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <div className="filiere-tabs">
          {FILIERES.map((f) => (
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

      {apiHint && !loading && (
        <p className="api-hint">
          Endpoint <code>GET /api/users?role=MENTOR</code> non disponible — affichage vide en attendant M2 (F11).
        </p>
      )}

      {loading ? (
        <div className="page-loading">Chargement des mentors…</div>
      ) : filtered.length === 0 ? (
        <div className="empty-state">
          <p>Aucun mentor trouvé pour ces critères.</p>
        </div>
      ) : (
        <div className="mentors-grid">
          {filtered.map((m) => (
            <MentorCard key={m.id ?? m.email} mentor={m} onRate={setRatingModal} />
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
              <button type="button" className="btn-ghost" onClick={() => setRatingModal(null)}>
                Annuler
              </button>
              <button type="button" className="btn-primary" onClick={submitRating} disabled={!draftRating}>
                Envoyer
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
