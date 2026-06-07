import React, { useEffect, useState, useCallback } from 'react';
import { useAuth } from '../../context/AuthContext';
import { roadmapsAPI } from '../../api/roadmaps';
import toast from 'react-hot-toast';
import { FILIERES } from '../../api/utils';
import '../student/Student.css';

const emptyForm = {
  title: '',
  description: '',
  filiere: '',
  steps: [{ title: '', description: '', stepOrder: 1 }],
};

export default function MentorRoadmapsPage() {
  const { user } = useAuth();
  const [roadmaps, setRoadmaps] = useState([]);
  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);
  const [saving, setSaving] = useState(false);

  const load = useCallback(() =>
    roadmapsAPI.getAll().then((data) => {
      const mine = (data ?? []).filter((r) => r.mentorName === user?.email);
      setRoadmaps(mine);
    }), [user?.email]);

  useEffect(() => {
    load().finally(() => setLoading(false));
  }, [load]);

  const updateStep = (index, field, value) => {
    setForm((prev) => {
      const steps = [...prev.steps];
      steps[index] = { ...steps[index], [field]: value };
      return { ...prev, steps };
    });
  };

  const addStep = () => {
    setForm((prev) => ({
      ...prev,
      steps: [...prev.steps, { title: '', description: '', stepOrder: prev.steps.length + 1 }],
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.title.trim()) {
      toast.error('Titre requis.');
      return;
    }
    setSaving(true);
    const payload = {
      title: form.title.trim(),
      description: form.description,
      filiere: form.filiere,
      steps: form.steps.filter((s) => s.title?.trim()).map((s, i) => ({
        title: s.title,
        description: s.description,
        stepOrder: i + 1,
      })),
    };
    try {
      if (editingId) {
        await roadmapsAPI.update(editingId, payload);
        toast.success('Roadmap mise à jour.');
      } else {
        await roadmapsAPI.create(payload);
        toast.success('Roadmap créée.');
      }
      setForm(emptyForm);
      setEditingId(null);
      await load();
    } catch {
      /* intercepteur */
    } finally {
      setSaving(false);
    }
  };

  const startEdit = (r) => {
    setEditingId(r.id);
    setForm({
      title: r.title,
      description: r.description || '',
      filiere: r.filiere || '',
      steps: r.steps?.length
        ? r.steps.map((s) => ({ title: s.title, description: s.description, stepOrder: s.stepOrder }))
        : [{ title: '', description: '', stepOrder: 1 }],
    });
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Supprimer cette roadmap ?')) return;
    try {
      await roadmapsAPI.remove(id);
      toast.success('Roadmap supprimée.');
      await load();
    } catch {
      /* intercepteur */
    }
  };

  return (
    <div className="student-page">
      <div className="page-header">
        <h1 className="page-title">Mes Roadmaps</h1>
        <p className="page-subtitle">Créer et gérer vos parcours de mentorat</p>
      </div>

      <div className="form-card">
        <h2>{editingId ? 'Modifier la roadmap' : 'Nouvelle roadmap'}</h2>
        <form onSubmit={handleSubmit} className="session-form">
          <div className="field">
            <label htmlFor="title">Titre</label>
            <input id="title" value={form.title} onChange={(e) => setForm((p) => ({ ...p, title: e.target.value }))} required />
          </div>
          <div className="field">
            <label htmlFor="description">Description</label>
            <textarea id="description" rows={3} value={form.description} onChange={(e) => setForm((p) => ({ ...p, description: e.target.value }))} />
          </div>
          <div className="field">
            <label htmlFor="filiere">Filière</label>
            <select id="filiere" value={form.filiere} onChange={(e) => setForm((p) => ({ ...p, filiere: e.target.value }))}>
              <option value="">Choisir…</option>
              {FILIERES.map((f) => <option key={f} value={f}>{f}</option>)}
            </select>
          </div>
          <div className="steps-editor">
            <h3>Étapes</h3>
            {form.steps.map((step, i) => (
              <div key={i} className="step-row">
                <input
                  placeholder={`Étape ${i + 1} — titre`}
                  value={step.title}
                  onChange={(e) => updateStep(i, 'title', e.target.value)}
                />
                <input
                  placeholder="Description"
                  value={step.description}
                  onChange={(e) => updateStep(i, 'description', e.target.value)}
                />
              </div>
            ))}
            <button type="button" className="btn-ghost-sm" onClick={addStep}>+ Ajouter une étape</button>
          </div>
          <div className="modal-actions">
            {editingId && (
              <button type="button" className="btn-ghost" onClick={() => { setEditingId(null); setForm(emptyForm); }}>
                Annuler
              </button>
            )}
            <button type="submit" className="btn-primary" disabled={saving}>
              {saving ? 'Enregistrement…' : editingId ? 'Mettre à jour' : 'Publier'}
            </button>
          </div>
        </form>
      </div>

      <h2 className="section-title">Roadmaps publiées</h2>
      {loading ? (
        <div className="page-loading">Chargement…</div>
      ) : roadmaps.length === 0 ? (
        <div className="empty-state"><p>Aucune roadmap publiée.</p></div>
      ) : (
        <div className="roadmaps-grid">
          {roadmaps.map((r) => (
            <article key={r.id} className="roadmap-card">
              <h3>{r.title}</h3>
              <p>{r.description}</p>
              <p className="roadmap-meta">{r.filiere} · {r.enrollmentCount ?? 0} inscrit(s)</p>
              <div className="mentor-actions">
                <button type="button" className="btn-ghost-sm" onClick={() => startEdit(r)}>Modifier</button>
                <button type="button" className="btn-ghost-sm danger" onClick={() => handleDelete(r.id)}>Supprimer</button>
              </div>
            </article>
          ))}
        </div>
      )}
    </div>
  );
}
