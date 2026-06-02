import React from 'react';

/**
 * [F08] Affichage ou saisie de note mentor (étoiles).
 * readOnly=false → clic pour noter (prêt pour POST /api/mentors/{id}/rate de M2).
 */
export default function StarRating({
  value = 0,
  onChange,
  readOnly = true,
  size = 'md',
}) {
  const display = readOnly ? Math.round(value) : value;

  const handleClick = (star) => {
    if (!readOnly && onChange) onChange(star);
  };

  return (
    <div
      className={`star-rating star-rating--${size}`}
      role={readOnly ? 'img' : 'group'}
      aria-label={readOnly ? `Note : ${value} sur 5` : 'Choisir une note'}
    >
      {[1, 2, 3, 4, 5].map((star) => (
        <button
          key={star}
          type="button"
          className={`star ${star <= display ? 'star--on' : ''}`}
          disabled={readOnly}
          onClick={() => handleClick(star)}
          aria-label={`${star} étoile${star > 1 ? 's' : ''}`}
        >
          ★
        </button>
      ))}
      {readOnly && <span className="star-val">{value ? Number(value).toFixed(1) : '—'}</span>}
    </div>
  );
}
