import React from 'react';
import '../student/Student.css';

export default function MentorRoadmapsPage() {
  return (
    <div className="student-page">
      <div className="page-header">
        <h1 className="page-title">Mes Roadmaps</h1>
        <p className="page-subtitle">Créez et gérez vos roadmaps pédagogiques</p>
      </div>

      <div className="coming-soon-card">
        <div className="coming-soon-icon">🗺️</div>
        <h2>En attente des endpoints M2</h2>
        <p>Le formulaire de création et la liste de vos roadmaps seront connectés ici dès que M2 aura poussé ses endpoints.</p>
        <div className="endpoint-list">
          <div className="endpoint-item">
            <span className="method get">GET</span>
            <span>/api/roadmaps/my-roadmaps</span>
            <span className="status pending">⏳ En attente M2</span>
          </div>
          <div className="endpoint-item">
            <span className="method post">POST</span>
            <span>/api/roadmaps</span>
            <span className="status pending">⏳ En attente M2</span>
          </div>
          <div className="endpoint-item">
            <span className="method put">PUT</span>
            <span>/api/roadmaps/{'{id}'}</span>
            <span className="status pending">⏳ En attente M2</span>
          </div>
          <div className="endpoint-item">
            <span className="method delete">DELETE</span>
            <span>/api/roadmaps/{'{id}'}</span>
            <span className="status pending">⏳ En attente M2</span>
          </div>
        </div>
      </div>
    </div>
  );
}
