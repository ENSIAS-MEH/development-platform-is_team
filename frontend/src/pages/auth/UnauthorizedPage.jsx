import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { getHomePath } from '../../routes/PrivateRoute';
import './Auth.css';

export default function UnauthorizedPage() {
  const { user } = useAuth();
  const navigate = useNavigate();

  return (
    <div className="auth-page" style={{ justifyContent: 'center', alignItems: 'center' }}>
      <div className="auth-card" style={{ maxWidth: 420, textAlign: 'center' }}>
        <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>🚫</div>
        <h2 className="auth-title">Accès refusé</h2>
        <p className="auth-hint" style={{ marginBottom: '1.5rem' }}>
          Vous n&apos;avez pas les droits pour accéder à cette page.
        </p>
        {user ? (
          <button type="button" className="btn-primary btn-full" onClick={() => navigate(getHomePath(user.role))}>
            Retour à mon espace
          </button>
        ) : (
          <Link to="/login" className="btn-primary btn-full" style={{ display: 'inline-block' }}>
            Se connecter
          </Link>
        )}
      </div>
    </div>
  );
}
