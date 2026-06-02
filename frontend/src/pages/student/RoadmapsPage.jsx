import React from 'react';
import ComingSoon from '../../components/common/ComingSoon';
import './Student.css';

export default function RoadmapsPage() {
  return (
    <div className="student-page">
      <div className="page-header">
        <h1 className="page-title">Mes Roadmaps</h1>
        <p className="page-subtitle">Suivi de progression visuel — branché sur les API M2</p>
      </div>
      <ComingSoon
        icon="🗺️"
        title="Progression roadmaps"
        description="Cette page affichera barres de progression et étapes une fois les endpoints M2 disponibles."
        endpoints={[
          { method: 'GET', path: '/api/roadmaps' },
          { method: 'GET', path: '/api/roadmaps/my-progress' },
          { method: 'POST', path: '/api/roadmaps/{id}/enroll' },
        ]}
      />
    </div>
  );
}
