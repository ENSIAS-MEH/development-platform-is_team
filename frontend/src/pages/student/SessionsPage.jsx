import React from 'react';
import './Student.css';

export default function SessionsPage() {
  return (
    <div className="student-page">
      <div className="page-header">
        <h1 className="page-title">Mes Sessions</h1>
        <p className="page-subtitle">Demandez et gérez vos sessions avec un mentor</p>
      </div>

      <div className="coming-soon-card">
        <div className="coming-soon-icon">📅</div>
        <h2>En attente des endpoints M2</h2>
        <p>
          Cette page consommera les endpoints de sessions une fois que M2 aura poussé ses fonctionnalités.
        </p>
        <div className="endpoint-list">
          <div className="endpoint-item">
            <span className="method post">POST</span>
            <span>/api/sessions/request</span>
            <span className="status pending">⏳ En attente M2</span>
          </div>
          <div className="endpoint-item">
            <span className="method get">GET</span>
            <span>/api/sessions/my-sessions</span>
            <span className="status pending">⏳ En attente M2</span>
          </div>
        </div>
      </div>
    </div>
  );
}
