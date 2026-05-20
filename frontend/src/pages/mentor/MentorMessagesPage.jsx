import React from 'react';
import '../student/Student.css';

export default function MentorMessagesPage() {
  return (
    <div className="student-page">
      <div className="page-header">
        <h1 className="page-title">Messages</h1>
        <p className="page-subtitle">Échangez avec vos étudiants</p>
      </div>

      <div className="coming-soon-card">
        <div className="coming-soon-icon">💬</div>
        <h2>En attente des endpoints M2</h2>
        <p>L'interface de messagerie sera disponible dès que M2 aura poussé les entités Message et Conversation.</p>
        <div className="endpoint-list">
          <div className="endpoint-item">
            <span className="method get">GET</span>
            <span>/api/messages/conversations</span>
            <span className="status pending">⏳ En attente M2</span>
          </div>
          <div className="endpoint-item">
            <span className="method get">GET</span>
            <span>/api/messages/conversations/{'{id}'}/messages</span>
            <span className="status pending">⏳ En attente M2</span>
          </div>
          <div className="endpoint-item">
            <span className="method post">POST</span>
            <span>/api/messages/send</span>
            <span className="status pending">⏳ En attente M2</span>
          </div>
        </div>
      </div>
    </div>
  );
}
