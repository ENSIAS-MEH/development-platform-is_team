import React, { useState } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import {
  Map,
  Calendar,
  Search,
  MessageSquare,
  User,
  LayoutDashboard,
  BarChart3,
  LogOut,
  Menu,
} from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import NotificationBell from './NotificationBell';
import './Layout.css';

const ICON_PROPS = { size: 20, strokeWidth: 1.75, 'aria-hidden': true };

const NAV_STUDENT = [
  { to: '/student/roadmaps', Icon: Map, label: 'Mes Roadmaps' },
  { to: '/student/sessions', Icon: Calendar, label: 'Mes Sessions' },
  { to: '/student/mentors', Icon: Search, label: 'Chercher un mentor' },
  { to: '/student/messages', Icon: MessageSquare, label: 'Messages' },
  { to: '/student/profile', Icon: User, label: 'Mon Profil' },
];

const NAV_MENTOR = [
  { to: '/mentor/sessions', Icon: Calendar, label: 'Mes Sessions' },
  { to: '/mentor/roadmaps', Icon: Map, label: 'Mes Roadmaps' },
  { to: '/mentor/messages', Icon: MessageSquare, label: 'Messages' },
  { to: '/mentor/profile', Icon: User, label: 'Mon Profil' },
];

const NAV_ADMIN = [
  { to: '/admin/dashboard', Icon: LayoutDashboard, label: 'Modération' },
  { to: '/admin/analytics', Icon: BarChart3, label: 'Analytique' },
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
      {mobileOpen && (
        <div className="sidebar-overlay" onClick={() => setMobileOpen(false)} role="presentation" />
      )}

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
              <span className="nav-icon">
                <item.Icon {...ICON_PROPS} />
              </span>
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
          <button type="button" className="logout-btn" onClick={handleLogout} title="Se déconnecter" aria-label="Se déconnecter">
            <LogOut size={18} strokeWidth={1.75} />
          </button>
        </div>
      </aside>

      <div className="main-wrapper">
        <header className="top-bar">
          <button type="button" className="hamburger" onClick={() => setMobileOpen(true)} aria-label="Ouvrir le menu">
            <Menu size={22} strokeWidth={1.75} />
          </button>
          <div className="top-bar-right">
            <NotificationBell />
          </div>
        </header>
        <main className="page-content">{children}</main>
      </div>
    </div>
  );
}
