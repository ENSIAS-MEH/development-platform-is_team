import React from 'react';
import './Student.css';

export default function RoadmapsPage() {
  return (
    <div className="student-page">
      <div className="page-header">
        <h1 className="page-title">Mes Roadmaps</h1>
        <p className="page-subtitle">Suivez votre progression sur chaque roadmap</p>
      </div>

      <div className="coming-soon-card">
        <div className="coming-soon-icon">🗺️</div>
        <h2>En attente des endpoints M2</h2>
        <p>
          Cette page consommera <code>GET /api/roadmaps</code> et <code>GET /api/roadmaps/my-progress</code>
          une fois que M2 aura poussé ses endpoints.
        </p>
        <div className="endpoint-list">
          <div className="endpoint-item">
            <span className="method get">GET</span>
            <span>/api/roadmaps</span>
            <span className="status pending">⏳ En attente M2</span>
          </div>
          <div className="endpoint-item">
            <span className="method get">GET</span>
            <span>/api/roadmaps/my-progress</span>
            <span className="status pending">⏳ En attente M2</span>
          </div>
          <div className="endpoint-item">
            <span className="method post">POST</span>
            <span>/api/roadmaps/{'{id}'}/enroll</span>
            <span className="status pending">⏳ En attente M2</span>
          </div>
        </div>
      </div>
    </div>
  );
}
