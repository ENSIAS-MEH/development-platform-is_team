import React, { useState } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import './Layout.css';

const NAV_STUDENT = [
  { to: '/student/roadmaps', icon: '🗺️', label: 'Mes Roadmaps' },
  { to: '/student/sessions', icon: '📅', label: 'Mes Sessions' },
  { to: '/student/mentors', icon: '🔍', label: 'Chercher un mentor' },
  { to: '/student/profile', icon: '👤', label: 'Mon Profil' },
];

const NAV_MENTOR = [
  { to: '/mentor/sessions', icon: '📅', label: 'Mes Sessions' },
  { to: '/mentor/roadmaps', icon: '🗺️', label: 'Mes Roadmaps' },
  { to: '/mentor/messages', icon: '💬', label: 'Messages' },
  { to: '/mentor/profile', icon: '👤', label: 'Mon Profil' },
];

const NAV_ADMIN = [
  { to: '/admin/dashboard', icon: '📊', label: 'Dashboard' },
  { to: '/admin/users', icon: '👥', label: 'Utilisateurs' },
];

function getNav(role) {
  if (role === 'MENTOR') return NAV_MENTOR;
  if (role === 'ADMIN') return NAV_ADMIN;
  return NAV_STUDENT;
}

export default function AppLayout({ children }) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [mobileOpen, setMobileOpen] = useState(false);

  const navItems = getNav(user?.role);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const initials = user
    ? `${user.firstName?.[0] ?? ''}${user.lastName?.[0] ?? ''}`.toUpperCase()
    : '?';

  return (
    <div className="app-shell">
      {/* Mobile overlay */}
      {mobileOpen && (
        <div className="sidebar-overlay" onClick={() => setMobileOpen(false)} />
      )}

      {/* Sidebar */}
      <aside className={`sidebar ${mobileOpen ? 'sidebar--open' : ''}`}>
        <div className="sidebar-header">
          <span className="sidebar-logo">M</span>
          <span className="sidebar-brand">MentorPath</span>
        </div>

        <nav className="sidebar-nav">
          {navItems.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              className={({ isActive }) => `nav-item ${isActive ? 'nav-item--active' : ''}`}
              onClick={() => setMobileOpen(false)}
            >
              <span className="nav-icon">{item.icon}</span>
              <span className="nav-label">{item.label}</span>
            </NavLink>
          ))}
        </nav>

        <div className="sidebar-footer">
          <div className="user-chip">
            <div className="user-avatar">{initials}</div>
            <div className="user-info">
              <div className="user-name">{user?.firstName} {user?.lastName}</div>
              <div className="user-role">{user?.role}</div>
            </div>
          </div>
          <button className="logout-btn" onClick={handleLogout} title="Se déconnecter">
            <span>↩</span>
          </button>
        </div>
      </aside>

      {/* Main content */}
      <div className="main-wrapper">
        <header className="top-bar">
          <button className="hamburger" onClick={() => setMobileOpen(true)}>☰</button>
          <div className="top-bar-right">
            <NotificationBell />
          </div>
        </header>
        <main className="page-content">{children}</main>
      </div>
    </div>
  );
}

function NotificationBell() {
  return (
    <button className="notif-bell" title="Notifications">
      🔔
    </button>
  );
}
