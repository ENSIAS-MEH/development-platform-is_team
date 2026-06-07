import React from 'react';

export default function ComingSoon({ icon, title, description, endpoints = [] }) {
  return (
    <div className="coming-soon-card">
      <div className="coming-soon-icon">{icon}</div>
      <h2>{title}</h2>
      <p>{description}</p>
      {endpoints.length > 0 && (
        <div className="endpoint-list">
          {endpoints.map((ep) => (
            <div key={ep.path} className="endpoint-item">
              <span className={`method ${ep.method.toLowerCase()}`}>{ep.method}</span>
              <span>{ep.path}</span>
              <span className="status pending">{ep.status || '⏳ En attente M2'}</span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
