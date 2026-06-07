import React, { useEffect, useState } from 'react';
import { adminAPI } from '../../api/admin';
import { roadmapsAPI } from '../../api/roadmaps';
import './Admin.css';

export default function AdminDashboard() {
  const [stats, setStats] = useState({ students: 0, mentors: 0, roadmaps: 0 });
  const [roadmaps, setRoadmaps] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([adminAPI.getStats(), roadmapsAPI.getAll()])
      .then(([platformStats, rm]) => {
        setStats(platformStats ?? { students: 0, mentors: 0, roadmaps: 0 });
        setRoadmaps(rm ?? []);
      })
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="admin-page">
      <div className="page-header">
        <h1 className="page-title">Dashboard Admin</h1>
        <p className="page-subtitle">Vue d&apos;ensemble de la plateforme [F09]</p>
      </div>

      <div className="stat-cards">
        <div className="stat-card stat-card--teal">
          <div className="stat-card-value">{loading ? '…' : stats.students}</div>
          <div className="stat-card-label">Étudiants inscrits</div>
        </div>
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
