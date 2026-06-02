import React from 'react';
import ComingSoon from '../../components/common/ComingSoon';
import '../student/Student.css';

export default function MentorMessagesPage() {
  return (
    <div className="student-page">
      <div className="page-header">
        <h1 className="page-title">Messagerie</h1>
        <p className="page-subtitle">Échanges mentor ↔ étudiant</p>
      </div>
      <ComingSoon
        icon="💬"
        title="Messagerie interne"
        description="Liste des conversations et envoi de messages."
        endpoints={[
          { method: 'GET', path: '/api/messages/conversations' },
          { method: 'POST', path: '/api/messages/send' },
        ]}
      />
    </div>
  );
}
