import React, { useEffect, useState } from 'react';
import api from '../../api/client';
import toast from 'react-hot-toast';
import './Student.css';

const FILIERES = ['Toutes', '2IA', 'BI', 'GL', 'IDF', 'IDSIT', 'SSE', 'SSI'];

function StarRating({ value = 0 }) {
  return (
    <div className="star-row" aria-label={`Note : ${value} sur 5`}>
      {[1, 2, 3, 4, 5].map((s) => (
        <span key={s} className={`star ${s <= Math.round(value) ? 'star--on' : ''}`}>★</span>
      ))}
      <span className="star-val">{value ? value.toFixed(1) : '—'}</span>
    </div>
  );
}

function MentorCard({ mentor }) {
  const initials = `${mentor.firstName?.[0] ?? ''}${mentor.lastName?.[0] ?? ''}`.toUpperCase();
  return (
    <div className="mentor-card">
      <div className="mentor-card-header">
        <div className="mentor-avatar">{initials}</div>
        <div className="mentor-info">
          <div className="mentor-name">{mentor.firstName} {mentor.lastName}</div>
          <div className="mentor-filiere">{mentor.filiere} · Promo {mentor.promo}</div>
        </div>
      </div>
      {mentor.bio && <p className="mentor-bio">{mentor.bio}</p>}
      <div className="mentor-card-footer">
        <StarRating value={mentor.averageRating} />
        <span className="sessions-count">{mentor.sessionCount ?? 0} sessions</span>
      </div>
    </div>
  );
}

export default function MentorsPage() {
  const [mentors, setMentors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [filiere, setFiliere] = useState('Toutes');

  useEffect(() => {
    // Uses M1 users endpoint filtered by role=MENTOR
    api.get('/users?role=MENTOR')
      .then((res) => setMentors(res.data.data ?? []))
      .catch(() => toast.error('Impossible de charger les mentors.'))
      .finally(() => setLoading(false));
  }, []);

  const filtered = mentors.filter((m) => {
    const matchFiliere = filiere === 'Toutes' || m.filiere === filiere;
    const matchSearch = search === '' ||
      `${m.firstName} ${m.lastName} ${m.bio ?? ''}`.toLowerCase().includes(search.toLowerCase());
    return matchFiliere && matchSearch;
  });

  return (
    <div className="student-page">
      <div className="page-header">
        <h1 className="page-title">Trouver un mentor</h1>
        <p className="page-subtitle">Parcourez les mentors disponibles et contactez-les</p>
      </div>

      <div className="mentor-filters">
        <input
          className="search-input"
          type="text"
          placeholder="🔍  Rechercher par nom, bio…"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <div className="filiere-tabs">
          {FILIERES.map((f) => (
            <button
              key={f}
              className={`filiere-tab ${filiere === f ? 'active' : ''}`}
              onClick={() => setFiliere(f)}
            >{f}</button>
          ))}
        </div>
      </div>

      {loading ? (
        <div className="page-loading">Chargement des mentors…</div>
      ) : filtered.length === 0 ? (
        <div className="empty-state">
          <div style={{ fontSize: '2.5rem' }}>🔍</div>
          <p>Aucun mentor trouvé pour ces critères.</p>
        </div>
      ) : (
        <div className="mentors-grid">
          {filtered.map((m) => <MentorCard key={m.id} mentor={m} />)}
        </div>
      )}
    </div>
  );
}
