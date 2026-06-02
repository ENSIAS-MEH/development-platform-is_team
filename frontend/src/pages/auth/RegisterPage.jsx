import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { getHomePath } from '../../routes/PrivateRoute';
import toast from 'react-hot-toast';
import './Auth.css';

const ROLES = [
  { value: 'STUDENT', label: 'Étudiant' },
  { value: 'MENTOR', label: 'Mentor' },
];

const FILIERES = ['2IA', 'BI', 'GL', 'IDF', 'IDSIT', 'SSE', 'SSI'];

export default function RegisterPage() {
  const { register } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    confirmPassword: '',
    role: 'STUDENT',
    filiere: '',
    anneeEtude: '',
    promo: '',
  });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);

  const validate = () => {
    const errs = {};
    if (!form.firstName.trim()) errs.firstName = 'Prénom requis';
    if (!form.lastName.trim()) errs.lastName = 'Nom requis';
    if (!form.email) errs.email = 'Email requis';
    else if (!/\S+@\S+\.\S+/.test(form.email)) errs.email = 'Email invalide';
    if (!form.password) errs.password = 'Mot de passe requis';
    else if (form.password.length < 8) errs.password = 'Minimum 8 caractères';
    if (form.password !== form.confirmPassword) errs.confirmPassword = 'Les mots de passe ne correspondent pas';
    if (!form.filiere) errs.filiere = 'Filière requise';
    if (form.role === 'STUDENT' && !form.anneeEtude) errs.anneeEtude = "Année d'étude requise";
    if (form.role === 'MENTOR' && !form.promo) errs.promo = 'Promotion requise';
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
    if (Object.keys(errs).length > 0) {
      setErrors(errs);
      return;
    }
    setLoading(true);
    const payload = {
      firstName: form.firstName.trim(),
      lastName: form.lastName.trim(),
      email: form.email.trim(),
      password: form.password,
      role: form.role,
      filiere: form.filiere,
    };
    if (form.role === 'STUDENT') payload.anneeEtude = form.anneeEtude;
    if (form.role === 'MENTOR') payload.promo = form.promo;

    try {
      const user = await register(payload);
      toast.success('Compte créé avec succès !');
      navigate(getHomePath(user.role));
    } catch {
      /* intercepteur */
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
        <h1 className="auth-tagline">Rejoignez la communauté ENSIAS.</h1>
        <p className="auth-sub">Étudiants et mentors — une seule plateforme.</p>
      </div>

      <div className="auth-right">
        <div className="auth-card auth-card--wide">
          <h2 className="auth-title">Créer un compte</h2>
          <p className="auth-hint">
            Déjà membre ? <Link to="/login" className="auth-link">Se connecter</Link>
          </p>

          <form onSubmit={handleSubmit} className="auth-form" noValidate>
            <div className="role-selector">
              {ROLES.map((r) => (
                <button
                  key={r.value}
                  type="button"
                  className={`role-btn ${form.role === r.value ? 'active' : ''}`}
                  onClick={() => setForm((prev) => ({ ...prev, role: r.value }))}
                >
                  {r.label}
                </button>
              ))}
            </div>

            <div className="fields-row">
              <div className="field">
                <label htmlFor="firstName">Prénom</label>
                <input
                  id="firstName"
                  name="firstName"
                  value={form.firstName}
                  onChange={handleChange}
                  className={errors.firstName ? 'input-error' : ''}
                />
                {errors.firstName && <span className="field-error">{errors.firstName}</span>}
              </div>
              <div className="field">
                <label htmlFor="lastName">Nom</label>
                <input
                  id="lastName"
                  name="lastName"
                  value={form.lastName}
                  onChange={handleChange}
                  className={errors.lastName ? 'input-error' : ''}
                />
                {errors.lastName && <span className="field-error">{errors.lastName}</span>}
              </div>
            </div>

            <div className="field">
              <label htmlFor="email">Email</label>
              <input
                id="email"
                name="email"
                type="email"
                value={form.email}
                onChange={handleChange}
                className={errors.email ? 'input-error' : ''}
              />
              {errors.email && <span className="field-error">{errors.email}</span>}
            </div>

            <div className="fields-row">
              <div className="field">
                <label htmlFor="password">Mot de passe</label>
                <input
                  id="password"
                  name="password"
                  type="password"
                  value={form.password}
                  onChange={handleChange}
                  className={errors.password ? 'input-error' : ''}
                />
                {errors.password && <span className="field-error">{errors.password}</span>}
              </div>
              <div className="field">
                <label htmlFor="confirmPassword">Confirmer</label>
                <input
                  id="confirmPassword"
                  name="confirmPassword"
                  type="password"
                  value={form.confirmPassword}
                  onChange={handleChange}
                  className={errors.confirmPassword ? 'input-error' : ''}
                />
                {errors.confirmPassword && <span className="field-error">{errors.confirmPassword}</span>}
              </div>
            </div>

            <div className="fields-row">
              <div className="field">
                <label htmlFor="filiere">Filière</label>
                <select
                  id="filiere"
                  name="filiere"
                  value={form.filiere}
                  onChange={handleChange}
                  className={errors.filiere ? 'input-error' : ''}
                >
                  <option value="">Choisir…</option>
                  {FILIERES.map((f) => (
                    <option key={f} value={f}>{f}</option>
                  ))}
                </select>
                {errors.filiere && <span className="field-error">{errors.filiere}</span>}
              </div>

              {form.role === 'STUDENT' && (
                <div className="field">
                  <label htmlFor="anneeEtude">Année d&apos;étude</label>
                  <select
                    id="anneeEtude"
                    name="anneeEtude"
                    value={form.anneeEtude}
                    onChange={handleChange}
                    className={errors.anneeEtude ? 'input-error' : ''}
                  >
                    <option value="">Choisir…</option>
                    {['1', '2', '3', '4', '5'].map((a) => (
                      <option key={a} value={a}>{a}ère année</option>
                    ))}
                  </select>
                  {errors.anneeEtude && <span className="field-error">{errors.anneeEtude}</span>}
                </div>
              )}

              {form.role === 'MENTOR' && (
                <div className="field">
                  <label htmlFor="promo">Promotion</label>
                  <input
                    id="promo"
                    name="promo"
                    value={form.promo}
                    onChange={handleChange}
                    placeholder="2022"
                    className={errors.promo ? 'input-error' : ''}
                  />
                  {errors.promo && <span className="field-error">{errors.promo}</span>}
                </div>
              )}
            </div>

            <button type="submit" className="btn-primary btn-full" disabled={loading}>
              {loading ? <span className="btn-spinner" /> : 'Créer mon compte'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
