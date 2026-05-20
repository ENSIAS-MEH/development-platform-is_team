import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import toast from 'react-hot-toast';
import './Auth.css';

export default function LoginPage() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: '', password: '' });
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState({});

  const validate = () => {
    const errs = {};
    if (!form.email) errs.email = 'Email requis';
    else if (!/\S+@\S+\.\S+/.test(form.email)) errs.email = 'Email invalide';
    if (!form.password) errs.password = 'Mot de passe requis';
    return errs;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
    if (errors[name]) setErrors((prev) => ({ ...prev, [name]: '' }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const errs = validate();
    if (Object.keys(errs).length > 0) { setErrors(errs); return; }
    setLoading(true);
    try {
      const user = await login(form);
      toast.success(`Bienvenue, ${user.firstName} !`);
      if (user.role === 'ADMIN') navigate('/admin/dashboard');
      else if (user.role === 'MENTOR') navigate('/mentor/sessions');
      else navigate('/student/roadmaps');
    } catch {
      // errors handled by interceptor
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-left">
        <div className="auth-brand">
          <span className="auth-logo">M</span>
          <span className="auth-brand-name">MentorPath</span>
        </div>
        <h1 className="auth-tagline">Trouvez votre mentor,<br />tracez votre parcours.</h1>
        <p className="auth-sub">La plateforme qui connecte étudiants et mentors ENSIAS pour un accompagnement sur mesure.</p>
      </div>

      <div className="auth-right">
        <div className="auth-card">
          <h2 className="auth-title">Connexion</h2>
          <p className="auth-hint">Pas encore de compte ? <Link to="/register" className="auth-link">S'inscrire</Link></p>

          <form onSubmit={handleSubmit} className="auth-form" noValidate>
            <div className="field">
              <label htmlFor="email">Email</label>
              <input
                id="email" name="email" type="email"
                value={form.email} onChange={handleChange}
                placeholder="prenom.nom@ensias.um5.ac.ma"
                className={errors.email ? 'input-error' : ''}
                autoComplete="email"
              />
              {errors.email && <span className="field-error">{errors.email}</span>}
            </div>

            <div className="field">
              <label htmlFor="password">Mot de passe</label>
              <input
                id="password" name="password" type="password"
                value={form.password} onChange={handleChange}
                placeholder="••••••••"
                className={errors.password ? 'input-error' : ''}
                autoComplete="current-password"
              />
              {errors.password && <span className="field-error">{errors.password}</span>}
            </div>

            <button type="submit" className="btn-primary btn-full" disabled={loading}>
              {loading ? <span className="btn-spinner" /> : 'Se connecter'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
