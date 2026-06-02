import React, { useEffect, useState } from 'react';
import { fetchAllUsers, deleteUser } from '../../api/users';
import toast from 'react-hot-toast';
import './Admin.css';

export default function AdminDashboard() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [endpointReady, setEndpointReady] = useState(true);
  const [search, setSearch] = useState('');
  const [filterRole, setFilterRole] = useState('ALL');
  const [stats, setStats] = useState({ total: 0, students: 0, mentors: 0, admins: 0 });

  useEffect(() => {
    fetchAllUsers().then((data) => {
      if (data === null) {
        setEndpointReady(false);
        setUsers([]);
        return;
      }
      setUsers(data);
      setStats({
        total: data.length,
        students: data.filter((u) => u.role === 'STUDENT').length,
        mentors: data.filter((u) => u.role === 'MENTOR').length,
        admins: data.filter((u) => u.role === 'ADMIN').length,
      });
    }).finally(() => setLoading(false));
  }, []);

  const handleDelete = async (userId) => {
    if (!window.confirm('Supprimer cet utilisateur ?')) return;
    try {
      await deleteUser(userId);
      setUsers((prev) => prev.filter((u) => u.id !== userId));
      toast.success('Utilisateur supprimé.');
    } catch {
      toast.error('Suppression impossible — vérifiez que GET/DELETE /api/admin/users existe.');
    }
  };

  const filtered = users.filter((u) => {
    const matchRole = filterRole === 'ALL' || u.role === filterRole;
    const q = search.toLowerCase();
    return (
      matchRole &&
      (!q || `${u.firstName} ${u.lastName} ${u.email}`.toLowerCase().includes(q))
    );
  });

  return (
    <div className="admin-page">
      <div className="page-header">
        <h1 className="page-title">Dashboard Admin</h1>
        <p className="page-subtitle">Modération et gestion des utilisateurs [F09]</p>
      </div>

      {!endpointReady && (
        <div className="admin-banner">
          L&apos;endpoint <code>GET /api/admin/users</code> n&apos;est pas encore exposé par le backend.
          La modération s&apos;activera dès que M1/M2 l&apos;ajoutera.
        </div>
      )}

      <div className="stat-cards">
        {[
          { label: 'Total', value: stats.total, icon: '👥', color: 'blue' },
          { label: 'Étudiants', value: stats.students, icon: '🎓', color: 'teal' },
          { label: 'Mentors', value: stats.mentors, icon: '🧑‍🏫', color: 'amber' },
          { label: 'Admins', value: stats.admins, icon: '🛡️', color: 'red' },
        ].map((s) => (
          <div key={s.label} className={`stat-card stat-card--${s.color}`}>
            <div className="stat-card-icon">{s.icon}</div>
            <div className="stat-card-value">{loading ? '…' : s.value}</div>
            <div className="stat-card-label">{s.label}</div>
          </div>
        ))}
      </div>

      <div className="table-controls">
        <input
          className="search-input"
          type="search"
          placeholder="Rechercher…"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <div className="role-tabs">
          {['ALL', 'STUDENT', 'MENTOR', 'ADMIN'].map((r) => (
            <button
              key={r}
              type="button"
              className={`role-tab ${filterRole === r ? 'active' : ''}`}
              onClick={() => setFilterRole(r)}
            >
              {r === 'ALL' ? 'Tous' : r}
            </button>
          ))}
        </div>
      </div>

      <div className="table-card">
        {loading ? (
          <div className="table-empty">Chargement…</div>
        ) : filtered.length === 0 ? (
          <div className="table-empty">Aucun utilisateur à afficher.</div>
        ) : (
          <table className="admin-table">
            <thead>
              <tr>
                <th>Utilisateur</th>
                <th>Email</th>
                <th>Rôle</th>
                <th>Filière</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((u) => (
                <tr key={u.id}>
                  <td>
                    <div className="user-cell">
                      <div className="table-avatar">
                        {`${u.firstName?.[0] ?? ''}${u.lastName?.[0] ?? ''}`.toUpperCase()}
                      </div>
                      <span>{u.firstName} {u.lastName}</span>
                    </div>
                  </td>
                  <td className="text-muted">{u.email}</td>
                  <td>
                    <span className={`role-badge role-badge--${u.role?.toLowerCase()}`}>{u.role}</span>
                  </td>
                  <td className="text-muted">{u.filiere || '—'}</td>
                  <td>
                    <button type="button" className="btn-danger-sm" onClick={() => handleDelete(u.id)}>
                      Supprimer
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
