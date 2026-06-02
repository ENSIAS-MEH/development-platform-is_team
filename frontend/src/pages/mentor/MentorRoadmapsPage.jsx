import React from 'react';
import ComingSoon from '../../components/common/ComingSoon';
import '../student/Student.css';

export default function MentorRoadmapsPage() {
  return (
    <div className="student-page">
      <div className="page-header">
        <h1 className="page-title">Mes Roadmaps</h1>
        <p className="page-subtitle">Création et édition de parcours</p>
      </div>
      <ComingSoon
        icon="🗺️"
        title="Éditeur de roadmap"
        description="CRUD roadmaps mentor : titres, étapes, publication."
        endpoints={[
          { method: 'POST', path: '/api/roadmaps' },
          { method: 'PUT', path: '/api/roadmaps/{id}' },
          { method: 'DELETE', path: '/api/roadmaps/{id}' },
        ]}
      />
    </div>
  );
}
