import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

export default function UnauthorizedPage() {
  const navigate = useNavigate();
  const { user } = useAuth();

  const goHome = () => {
    if (user?.role === 'ADMIN') navigate('/admin/dashboard');
    else if (user?.role === 'MENTOR') navigate('/mentor/sessions');
    else navigate('/student/roadmaps');
  };

  return (
    <div style={{ height: '100vh', display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', gap: 16 }}>
      <div style={{ fontSize: '4rem' }}>🚫</div>
      <h1 style={{ fontSize: '1.5rem', color: 'var(--gray-900)' }}>Accès refusé</h1>
      <p style={{ color: 'var(--gray-500)', fontSize: '0.9rem' }}>Vous n'avez pas les droits pour accéder à cette page.</p>
      <button className="btn-primary" onClick={goHome} style={{ marginTop: 8 }}>
        Retour à l'accueil
      </button>
    </div>
  );
}
