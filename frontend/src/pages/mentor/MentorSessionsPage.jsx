import React from 'react';
import '../student/Student.css';

export default function MentorSessionsPage() {
  return (
    <div className="student-page">
      <div className="page-header">
        <h1 className="page-title">Mes Sessions</h1>
        <p className="page-subtitle">Gérez vos sessions avec les étudiants</p>
      </div>

      <div className="coming-soon-card">
        <div className="coming-soon-icon">📅</div>
        <h2>En attente des endpoints M2</h2>
        <p>Le calendrier et la gestion des demandes de sessions seront disponibles dès que M2 aura poussé ses fonctionnalités.</p>
        <div className="endpoint-list">
          <div className="endpoint-item">
            <span className="method get">GET</span>
            <span>/api/sessions/mentor-schedule</span>
            <span className="status pending">⏳ En attente M2</span>
          </div>
          <div className="endpoint-item">
            <span className="method put">PUT</span>
            <span>/api/sessions/{'{id}'}/accept</span>
            <span className="status pending">⏳ En attente M2</span>
          </div>
          <div className="endpoint-item">
            <span className="method put">PUT</span>
            <span>/api/sessions/{'{id}'}/decline</span>
            <span className="status pending">⏳ En attente M2</span>
          </div>
        </div>
      </div>
    </div>
  );
}
