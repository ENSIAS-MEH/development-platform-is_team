import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { authAPI } from '../../api/auth';
import toast from 'react-hot-toast';
import './Profile.css';

const FILIERES = ['2IA', 'BI', 'GL', 'IDF', 'IDSIT', 'SSE', 'SSI'];

export default function ProfilePage() {
  const { user, updateUser } = useAuth();
  const [form, setForm] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [errors, setErrors] = useState({});

  useEffect(() => {
    authAPI.getMe()
      .then((res) => setForm(res.data.data))
      .catch(() => toast.error('Impossible de charger le profil.'))
      .finally(() => setLoading(false));
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
    if (errors[name]) setErrors((prev) => ({ ...prev, [name]: '' }));
  };

  const validate = () => {
    const errs = {};
    if (!form.firstName?.trim()) errs.firstName = 'Prénom requis';
    if (!form.lastName?.trim()) errs.lastName = 'Nom requis';
    if (!form.filiere) errs.filiere = 'Filière requise';
    return errs;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const errs = validate();
    if (Object.keys(errs).length > 0) { setErrors(errs); return; }
    setSaving(true);
    try {
      const res = await authAPI.updateMe(form);
      updateUser(res.data.data);
      toast.success('Profil mis à jour !');
    } catch {
      // handled by interceptor
    } finally {
      setSaving(false);
    }
  };

  if (loading) return <div className="page-loading">Chargement du profil…</div>;
  if (!form) return null;

  const initials = `${form.firstName?.[0] ?? ''}${form.lastName?.[0] ?? ''}`.toUpperCase();

  return (
    <div className="profile-page">
      <div className="page-header">
        <h1 className="page-title">Mon Profil</h1>
        <p className="page-subtitle">Gérez vos informations personnelles</p>
      </div>

      <div className="profile-grid">
        {/* Avatar card */}
        <div className="profile-avatar-card">
          <div className="big-avatar">{initials}</div>
          <div className="avatar-name">{form.firstName} {form.lastName}</div>
          <div className="avatar-role-badge">{form.role}</div>
          <div className="avatar-email">{form.email}</div>

          {form.role === 'STUDENT' && (
            <div className="profile-stats">
              <div className="stat">
                <div className="stat-value">{form.filiere || '—'}</div>
                <div className="stat-label">Filière</div>
              </div>
              <div className="stat">
                <div className="stat-value">{form.anneeEtude ? `${form.anneeEtude}ème` : '—'}</div>
                <div className="stat-label">Année</div>
              </div>
            </div>
          )}

          {form.role === 'MENTOR' && (
            <div className="profile-stats">
              <div className="stat">
                <div className="stat-value">{form.filiere || '—'}</div>
                <div className="stat-label">Filière</div>
              </div>
              <div className="stat">
                <div className="stat-value">{form.promo || '—'}</div>
                <div className="stat-label">Promo</div>
              </div>
            </div>
          )}
        </div>

        {/* Edit form */}
        <div className="profile-form-card">
          <h2 className="card-title">Modifier les informations</h2>
          <form onSubmit={handleSubmit} className="profile-form" noValidate>
            <div className="fields-row">
              <div className="field">
                <label htmlFor="firstName">Prénom</label>
                <input id="firstName" name="firstName" value={form.firstName || ''} onChange={handleChange}
                  className={errors.firstName ? 'input-error' : ''} />
                {errors.firstName && <span className="field-error">{errors.firstName}</span>}
              </div>
              <div className="field">
                <label htmlFor="lastName">Nom</label>
                <input id="lastName" name="lastName" value={form.lastName || ''} onChange={handleChange}
                  className={errors.lastName ? 'input-error' : ''} />
                {errors.lastName && <span className="field-error">{errors.lastName}</span>}
              </div>
            </div>

            <div className="field">
              <label htmlFor="email">Email (non modifiable)</label>
              <input id="email" name="email" value={form.email || ''} disabled className="input-disabled" />
            </div>

            <div className="field">
              <label htmlFor="filiere">Filière</label>
              <select id="filiere" name="filiere" value={form.filiere || ''} onChange={handleChange}
                className={errors.filiere ? 'input-error' : ''}>
                <option value="">Choisir…</option>
                {FILIERES.map((f) => <option key={f} value={f}>{f}</option>)}
              </select>
              {errors.filiere && <span className="field-error">{errors.filiere}</span>}
            </div>

            {form.role === 'STUDENT' && (
              <div className="field">
                <label htmlFor="anneeEtude">Année d'étude</label>
                <select id="anneeEtude" name="anneeEtude" value={form.anneeEtude || ''} onChange={handleChange}>
                  <option value="">Choisir…</option>
                  {['1','2','3','4','5'].map((a) => <option key={a} value={a}>{a}ème année</option>)}
                </select>
              </div>
            )}

            {form.role === 'MENTOR' && (
              <>
                <div className="field">
                  <label htmlFor="promo">Promotion</label>
                  <input id="promo" name="promo" value={form.promo || ''} onChange={handleChange} placeholder="ex: 2022" />
                </div>
                <div className="field">
                  <label htmlFor="bio">Bio (courte présentation)</label>
                  <textarea id="bio" name="bio" value={form.bio || ''} onChange={handleChange}
                    rows={4} placeholder="Parlez de votre parcours, de vos compétences…" />
                </div>
                <div className="field">
                  <label htmlFor="linkedin">LinkedIn (optionnel)</label>
                  <input id="linkedin" name="linkedin" value={form.linkedin || ''} onChange={handleChange}
                    placeholder="https://linkedin.com/in/…" />
                </div>
              </>
            )}

            <button type="submit" className="btn-primary" disabled={saving}
              style={{ marginTop: '0.5rem', alignSelf: 'flex-start', minWidth: 140 }}>
              {saving ? <span className="btn-spinner" /> : '💾 Enregistrer'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
