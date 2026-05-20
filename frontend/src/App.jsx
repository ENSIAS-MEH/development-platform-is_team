import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { AuthProvider } from './context/AuthContext';
import { PrivateRoute, RoleRoute } from './routes/PrivateRoute';
import AppLayout from './components/common/AppLayout';

// Auth pages
import LoginPage from './pages/auth/LoginPage';
import RegisterPage from './pages/auth/RegisterPage';
import UnauthorizedPage from './pages/auth/UnauthorizedPage';

// Student pages
import RoadmapsPage from './pages/student/RoadmapsPage';
import SessionsPage from './pages/student/SessionsPage';
import MentorsPage from './pages/student/MentorsPage';
import ProfilePage from './pages/student/ProfilePage';

// Mentor pages
import MentorSessionsPage from './pages/mentor/MentorSessionsPage';
import MentorRoadmapsPage from './pages/mentor/MentorRoadmapsPage';
import MentorMessagesPage from './pages/mentor/MentorMessagesPage';

// Admin pages
import AdminDashboard from './pages/admin/AdminDashboard';

import './styles/global.css';
import './pages/auth/Auth.css';

function WithLayout({ children }) {
  return <AppLayout>{children}</AppLayout>;
}

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Toaster
          position="top-right"
          toastOptions={{
            duration: 3500,
            style: {
              fontFamily: 'DM Sans, sans-serif',
              fontSize: '0.875rem',
              borderRadius: '10px',
              boxShadow: '0 4px 12px rgba(0,0,0,0.1)',
            },
          }}
        />
        <Routes>
          {/* Public routes */}
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/unauthorized" element={<UnauthorizedPage />} />

          {/* Redirect root */}
          <Route path="/" element={<Navigate to="/login" replace />} />

          {/* Protected routes — any authenticated user */}
          <Route element={<PrivateRoute />}>

            {/* STUDENT routes */}
            <Route element={<RoleRoute roles={['STUDENT']} />}>
              <Route path="/student/roadmaps" element={<WithLayout><RoadmapsPage /></WithLayout>} />
              <Route path="/student/sessions" element={<WithLayout><SessionsPage /></WithLayout>} />
              <Route path="/student/mentors" element={<WithLayout><MentorsPage /></WithLayout>} />
              <Route path="/student/profile" element={<WithLayout><ProfilePage /></WithLayout>} />
            </Route>

            {/* MENTOR routes */}
            <Route element={<RoleRoute roles={['MENTOR']} />}>
              <Route path="/mentor/sessions" element={<WithLayout><MentorSessionsPage /></WithLayout>} />
              <Route path="/mentor/roadmaps" element={<WithLayout><MentorRoadmapsPage /></WithLayout>} />
              <Route path="/mentor/messages" element={<WithLayout><MentorMessagesPage /></WithLayout>} />
              <Route path="/mentor/profile" element={<WithLayout><ProfilePage /></WithLayout>} />
            </Route>

            {/* ADMIN routes */}
            <Route element={<RoleRoute roles={['ADMIN']} />}>
              <Route path="/admin/dashboard" element={<WithLayout><AdminDashboard /></WithLayout>} />
            </Route>

          </Route>

          {/* 404 fallback */}
          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}
