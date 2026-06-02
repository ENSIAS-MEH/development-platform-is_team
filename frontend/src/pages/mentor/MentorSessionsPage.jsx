import React from 'react';
import ComingSoon from '../../components/common/ComingSoon';
import '../student/Student.css';

export default function MentorSessionsPage() {
  return (
    <div className="student-page">
      <div className="page-header">
        <h1 className="page-title">Agenda des sessions</h1>
        <p className="page-subtitle">Calendrier mentor — planification M2</p>
      </div>
      <ComingSoon
        icon="📅"
        title="Calendrier mentor"
        description="Vue agenda : accepter / refuser les demandes de session des étudiants."
        endpoints={[
          { method: 'GET', path: '/api/sessions/mentor-schedule' },
          { method: 'PUT', path: '/api/sessions/{id}/accept' },
          { method: 'PUT', path: '/api/sessions/{id}/decline' },
        ]}
      />
    </div>
  );
}
