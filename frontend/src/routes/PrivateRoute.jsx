import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

// Redirects to /login if not authenticated
export function PrivateRoute() {
  const { user, loading } = useAuth();
  if (loading) return <div className="page-loading">Chargement…</div>;
  return user ? <Outlet /> : <Navigate to="/login" replace />;
}

// Redirects to /unauthorized if role doesn't match
export function RoleRoute({ roles }) {
  const { user, loading } = useAuth();
  if (loading) return <div className="page-loading">Chargement…</div>;
  if (!user) return <Navigate to="/login" replace />;
  if (!roles.includes(user.role)) return <Navigate to="/unauthorized" replace />;
  return <Outlet />;
}
