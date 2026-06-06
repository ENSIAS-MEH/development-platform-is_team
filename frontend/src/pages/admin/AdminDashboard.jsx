import React, { useEffect, useState } from 'react';
import { roadmapsAPI } from '../../api/roadmaps';
import { mentorsAPI } from '../../api/mentors';
import './Admin.css';

export default function AdminDashboard() {
  const [stats, setStats] = useState({ roadmaps: 0, mentors: 0 });
  const [roadmaps, setRoadmaps] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([roadmapsAPI.getAll(), mentorsAPI.search()])
      .then(([rm, mt]) => {
        setRoadmaps(rm ?? []);
        setStats({ roadmaps: (rm ?? []).length, mentors: (mt ?? []).length });
      })
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="admin-page">
      <div className="page-header">
        <h1 className="page-title">Dashboard Admin</h1>
        <p className="page-subtitle">Vue d&apos;ensemble de la plateforme [F09]</p>
      </div>

      <div className="admin-banner">
        La modération utilisateurs (<code>/api/admin/users</code>) n&apos;est pas encore exposée.
        Voici les statistiques disponibles via les API M2.
      </div>

      <div className="stat-cards">
        <div className="stat-card stat-card--blue">
          <div className="stat-card-value">{loading ? '…' : stats.roadmaps}</div>
          <div className="stat-card-label">Roadmaps publiées</div>
        </div>
        <div className="stat-card stat-card--amber">
          <div className="stat-card-value">{loading ? '…' : stats.mentors}</div>
          <div className="stat-card-label">Mentors actifs</div>
        </div>
      </div>

      <div className="table-card">
        <h3 className="section-title" style={{ padding: '1rem' }}>Dernières roadmaps</h3>
        {loading ? (
          <div className="table-empty">Chargement…</div>
        ) : roadmaps.length === 0 ? (
          <div className="table-empty">Aucune roadmap.</div>
        ) : (
          <table className="admin-table">
            <thead>
              <tr>
                <th>Titre</th>
                <th>Filière</th>
                <th>Mentor</th>
                <th>Inscrits</th>
              </tr>
            </thead>
            <tbody>
              {roadmaps.slice(0, 10).map((r) => (
                <tr key={r.id}>
                  <td>{r.title}</td>
                  <td>{r.filiere || '—'}</td>
                  <td>{r.mentorName || '—'}</td>
                  <td>{r.enrollmentCount ?? 0}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
