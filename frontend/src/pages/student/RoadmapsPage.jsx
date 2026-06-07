import React, { useEffect, useState, useCallback } from 'react';
import { roadmapsAPI } from '../../api/roadmaps';
import ProgressBar from '../../components/common/ProgressBar';
import toast from 'react-hot-toast';
import { FILIERES } from '../../api/utils';
import './Student.css';

function RoadmapCard({ roadmap, actionLabel, onAction, showProgress }) {
  return (
    <article className="roadmap-card">
      <div className="roadmap-card-head">
        <h3>{roadmap.title}</h3>
        <span className="roadmap-badge">{roadmap.filiere || '—'}</span>
      </div>
      <p className="roadmap-desc">{roadmap.description || 'Sans description'}</p>
      <p className="roadmap-meta">Mentor : {roadmap.mentorName || '—'} · {roadmap.enrollmentCount ?? 0} inscrit(s)</p>
      {showProgress && (
        <ProgressBar value={roadmap.progressPercent ?? 0} label="Progression" />
      )}
      {roadmap.steps?.length > 0 && (
        <ul className="step-list">
          {roadmap.steps.map((s) => (
            <li key={s.id}>{s.stepOrder}. {s.title}</li>
          ))}
        </ul>
      )}
      {actionLabel && onAction && (
        <button type="button" className="btn-primary btn-sm" onClick={() => onAction(roadmap)}>
          {actionLabel}
        </button>
      )}
    </article>
  );
}

export default function RoadmapsPage() {
  const [tab, setTab] = useState('progress');
  const [myRoadmaps, setMyRoadmaps] = useState([]);
  const [catalog, setCatalog] = useState([]);
  const [filiere, setFiliere] = useState('');
  const [loading, setLoading] = useState(true);

  const loadCatalog = useCallback(() =>
    roadmapsAPI.getAll(filiere ? { filiere } : {}).then((data) => setCatalog(data ?? [])), [filiere]);

  const loadProgress = useCallback(() =>
    roadmapsAPI.getMyProgress().then((data) => setMyRoadmaps(data ?? [])), []);

  useEffect(() => {
    setLoading(true);
    Promise.all([loadProgress(), loadCatalog()]).finally(() => setLoading(false));
  }, [loadProgress, loadCatalog]);

  const handleEnroll = async (roadmap) => {
    try {
      await roadmapsAPI.enroll(roadmap.id);
      toast.success('Inscription à la roadmap réussie !');
      await Promise.all([loadProgress(), loadCatalog()]);
      setTab('progress');
    } catch {
      /* intercepteur */
    }
  };

  return (
    <div className="student-page">
      <div className="page-header">
        <h1 className="page-title">Mes Roadmaps</h1>
        <p className="page-subtitle">Suivez votre progression et découvrez de nouveaux parcours</p>
      </div>

      <div className="tabs">
        <button type="button" className={`tab ${tab === 'progress' ? 'active' : ''}`} onClick={() => setTab('progress')}>
          Ma progression
        </button>
        <button type="button" className={`tab ${tab === 'catalog' ? 'active' : ''}`} onClick={() => setTab('catalog')}>
          Parcourir
        </button>
      </div>

      {tab === 'catalog' && (
        <div className="mentor-filters" style={{ marginBottom: '1rem' }}>
          <select value={filiere} onChange={(e) => setFiliere(e.target.value)} className="search-input">
            <option value="">Toutes filières</option>
            {FILIERES.map((f) => (
              <option key={f} value={f}>{f}</option>
            ))}
          </select>
        </div>
      )}

      {loading ? (
        <div className="page-loading">Chargement…</div>
      ) : tab === 'progress' ? (
        myRoadmaps.length === 0 ? (
          <div className="empty-state"><p>Aucune roadmap suivie. Parcourez le catalogue pour vous inscrire.</p></div>
        ) : (
          <div className="roadmaps-grid">
            {myRoadmaps.map((r) => (
              <RoadmapCard key={r.id} roadmap={r} showProgress />
            ))}
          </div>
        )
      ) : catalog.length === 0 ? (
        <div className="empty-state"><p>Aucune roadmap disponible.</p></div>
      ) : (
        <div className="roadmaps-grid">
          {catalog.map((r) => (
            <RoadmapCard key={r.id} roadmap={r} actionLabel="S'inscrire" onAction={handleEnroll} />
          ))}
        </div>
      )}
    </div>
  );
}
