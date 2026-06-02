import React from 'react';
import ComingSoon from '../../components/common/ComingSoon';
import './Student.css';

export default function SessionsPage() {
  return (
    <div className="student-page">
      <div className="page-header">
        <h1 className="page-title">Mes Sessions</h1>
        <p className="page-subtitle">Demandes et historique — API M2</p>
      </div>
      <ComingSoon
        icon="📅"
        title="Sessions étudiant"
        description="Formulaire de demande de session et liste des sessions passées / à venir."
        endpoints={[
          { method: 'POST', path: '/api/sessions/request' },
          { method: 'GET', path: '/api/sessions/my-sessions' },
        ]}
      />
    </div>
  );
}
