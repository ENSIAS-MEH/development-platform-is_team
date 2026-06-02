import React from 'react';
import './Admin.css';

/**
 * [F10] Dashboard analytique — données mock jusqu'à agrégats backend M2.
 */
const MOCK_STATS = [
  { label: 'Sessions ce mois', value: 42, trend: '+12%' },
  { label: 'Roadmaps actives', value: 18, trend: '+3' },
  { label: 'Note moyenne mentors', value: '4.6', trend: '+0.2' },
  { label: 'Taux complétion roadmaps', value: '67%', trend: '+5%' },
];

export default function AdminAnalyticsPage() {
  return (
    <div className="admin-page">
      <div className="page-header">
        <h1 className="page-title">Analytique</h1>
        <p className="page-subtitle">Statistiques plateforme [F10] — brancher sur API stats M2</p>
      </div>

      <div className="admin-banner">
        Graphiques illustratifs. Remplacer par <code>GET /api/admin/analytics</code> quand disponible.
      </div>

      <div className="stat-cards">
        {MOCK_STATS.map((s) => (
          <div key={s.label} className="stat-card stat-card--blue">
            <div className="stat-card-value">{s.value}</div>
            <div className="stat-card-label">{s.label}</div>
            <div className="stat-trend">{s.trend}</div>
          </div>
        ))}
      </div>

      <div className="chart-grid">
        <div className="chart-card">
          <h3>Sessions par semaine</h3>
          <div className="bar-chart" aria-hidden>
            {[40, 65, 45, 80, 55, 90, 70].map((h, i) => (
              <div key={i} className="bar" style={{ height: `${h}%` }} />
            ))}
          </div>
        </div>
        <div className="chart-card">
          <h3>Répartition filières (mentors)</h3>
          <ul className="legend-list">
            {['GL', '2IA', 'BI', 'SSE'].map((f, i) => (
              <li key={f}>
                <span className="legend-dot" style={{ background: `hsl(${220 + i * 30}, 70%, 50%)` }} />
                {f} — {25 - i * 4}%
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
}
