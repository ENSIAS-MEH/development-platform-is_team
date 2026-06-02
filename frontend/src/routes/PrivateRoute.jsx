import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export function PrivateRoute() {
  const { user, loading } = useAuth();
  if (loading) return <div className="page-loading">Chargement…</div>;
  return user ? <Outlet /> : <Navigate to="/login" replace />;
}

export function RoleRoute({ roles }) {
  const { user, loading } = useAuth();
  if (loading) return <div className="page-loading">Chargement…</div>;
  if (!user) return <Navigate to="/login" replace />;
  if (!roles.includes(user.role)) return <Navigate to="/unauthorized" replace />;
  return <Outlet />;
}

export function getHomePath(role) {
  if (role === 'ADMIN') return '/admin/dashboard';
  if (role === 'MENTOR') return '/mentor/sessions';
  return '/student/roadmaps';
}
