import React, { useEffect, useState } from 'react';
import { roadmapsAPI } from '../../api/roadmaps';
import { mentorsAPI } from '../../api/mentors';
import './Admin.css';

export default function AdminAnalyticsPage() {
  const [roadmaps, setRoadmaps] = useState([]);
  const [mentors, setMentors] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([roadmapsAPI.getAll(), mentorsAPI.search()])
      .then(([rm, mt]) => {
        setRoadmaps(rm ?? []);
        setMentors(mt ?? []);
      })
      .finally(() => setLoading(false));
  }, []);

  const byFiliere = FILIERES.map((f) => ({
    filiere: f,
    count: roadmaps.filter((r) => r.filiere === f).length,
  })).filter((x) => x.count > 0);

  const avgRating =
    mentors.length > 0
      ? (mentors.reduce((acc, m) => acc + Number(m.rating || 0), 0) / mentors.length).toFixed(1)
      : '—';

  const totalEnrollments = roadmaps.reduce((acc, r) => acc + (r.enrollmentCount || 0), 0);

  return (
    <div className="admin-page">
      <div className="page-header">
        <h1 className="page-title">Analytique</h1>
        <p className="page-subtitle">Statistiques plateforme [F10]</p>
      </div>

      <div className="stat-cards">
        <div className="stat-card stat-card--blue">
          <div className="stat-card-value">{loading ? '…' : roadmaps.length}</div>
          <div className="stat-card-label">Roadmaps</div>
        </div>
        <div className="stat-card stat-card--teal">
          <div className="stat-card-value">{loading ? '…' : totalEnrollments}</div>
          <div className="stat-card-label">Inscriptions totales</div>
        </div>
        <div className="stat-card stat-card--amber">
          <div className="stat-card-value">{loading ? '…' : mentors.length}</div>
          <div className="stat-card-label">Mentors</div>
        </div>
        <div className="stat-card stat-card--red">
          <div className="stat-card-value">{loading ? '…' : avgRating}</div>
          <div className="stat-card-label">Note moyenne mentors</div>
        </div>
      </div>

      <div className="chart-grid">
        <div className="chart-card">
          <h3>Roadmaps par filière</h3>
          {byFiliere.length === 0 ? (
            <p className="empty-inline">Aucune donnée.</p>
          ) : (
            <div className="bar-chart" aria-hidden>
              {byFiliere.map((item) => (
                <div
                  key={item.filiere}
                  className="bar"
                  style={{ height: `${Math.min(100, item.count * 25 + 10)}%` }}
                  title={`${item.filiere}: ${item.count}`}
                />
              ))}
            </div>
          )}
          <ul className="legend-list">
            {byFiliere.map((item) => (
              <li key={item.filiere}>{item.filiere} — {item.count}</li>
            ))}
          </ul>
        </div>
        <div className="chart-card">
          <h3>Top mentors (note)</h3>
          <ul className="legend-list">
            {[...mentors]
              .sort((a, b) => Number(b.rating || 0) - Number(a.rating || 0))
              .slice(0, 5)
              .map((m) => (
                <li key={m.id}>
                  {m.firstName} {m.lastName} — {m.rating != null ? Number(m.rating).toFixed(1) : '—'} ★
                </li>
              ))}
          </ul>
        </div>
      </div>
    </div>
  );
}

const FILIERES = ['2IA', 'BI', 'GL', 'IDF', 'IDSIT', 'SSE', 'SSI'];
