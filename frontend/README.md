# MentorPath — Frontend (M3)

**Rôle :** Frontend & UX · React 18 · React Router v6 · Axios · react-hot-toast  
**Prof.** EL HAMLAOUI Mahmoud · ENSIAS · 2025/2026

---

## Stack & architecture

| Couche | Choix | Justification |
|--------|--------|---------------|
| UI | React 18 (CRA) | Écosystème mature, équipe M3 |
| Routing | React Router v6 | Guards par rôle, nested routes |
| HTTP | Axios | Intercepteurs JWT + toasts centralisés [§6] |
| État auth | Context API | Suffisant pour F01/F02 sans Redux |
| Feedback | react-hot-toast | Messages d’erreur user-friendly [§6] |

```
frontend/src/
├── api/           client.js, auth.js, users.js
├── context/       AuthContext.jsx
├── routes/        PrivateRoute, RoleRoute, getHomePath
├── hooks/         useNotifications (polling F12)
├── components/    AppLayout, StarRating, ComingSoon, NotificationBell
└── pages/         auth, student, mentor, admin
```

Le frontend consomme le backend Spring Boot M1 (`REACT_APP_API_URL`, défaut `http://localhost:8080/api`). Format de réponse attendu :

```json
{ "success": true, "message": "...", "data": { ... } }
```

JWT : `localStorage.token` + header `Authorization: Bearer <token>`.

---

## Démarrage

```bash
cd frontend
cp .env.example .env
npm install
npm start          # http://localhost:3000
npm test           # tests Jest
npm run build      # build production (Docker / Nginx)
```

**Comptes seed (backend M1)** : voir `V2__seed.sql` — mot de passe `Pasword123`  
**Swagger backend** : http://localhost:8080/swagger-ui.html

---

## Checklist M3 — fonctionnalités UI

| ID | Page / composant | Route | Backend |
|----|------------------|-------|---------|
| F01 | Inscription / Connexion | `/register`, `/login` | ✅ M1 |
| F02 | Profil (avatar initiales, édition) | `/*/profile` | ✅ M1 |
| F03 | Roadmaps mentor (placeholder) | `/mentor/roadmaps` | ⏳ M2 |
| F04 | Suivi roadmaps étudiant | `/student/roadmaps` | ⏳ M2 |
| F05 | Sessions étudiant | `/student/sessions` | ⏳ M2 |
| F06 | Agenda mentor | `/mentor/sessions` | ⏳ M2 |
| F07 | Messagerie | `/mentor/messages` | ⏳ M2 |
| F08 | Notation mentor (étoiles + modal) | `/student/mentors` | ⏳ M2 |
| F09 | Dashboard admin modération | `/admin/dashboard` | selon M1/M2 |
| F10 | Dashboard analytique | `/admin/analytics` | mock → API stats |
| F11 | Recherche mentors (filtres) | `/student/mentors` | `GET /users?role=MENTOR` |
| F12 | Notifications (polling 30s) | cloche topbar | ⏳ `/notifications` |

**§6** — Guards `PrivateRoute` + `RoleRoute` (STUDENT / MENTOR / ADMIN), intercepteur 401 → `/login`, toasts sur erreurs API.

---

## Branche Git (équipe)

```bash
git checkout develop
git pull origin develop
git checkout -b feature/m3-frontend
# … travail …
git add frontend/
git commit -m "feat(frontend): app React MentorPath M3 closes #N"
git push origin feature/m3-frontend
```

Ouvrir une **Pull Request vers `develop`**.

---

## Défis & solutions

1. **Backend partiel (M1 seul)** — Pages M2 en `ComingSoon` + appels `silent` pour éviter des toasts inutiles sur 404.
2. **JWT expiré** — Intercepteur Axios redirige vers `/login` sans boucle sur les pages publiques.
3. **Rôles multiples** — `getHomePath(role)` centralise la redirection après login/register.
4. **Notifications sans WebSocket** — Hook `useNotifications` en polling jusqu’à livraison M2.
5. **Docker** — Le Dockerfile du repo attend le dossier `frontend/` et `npm run build` → servir `/build` via Nginx.

---

## Captures UI

À ajouter dans `docs/screenshots/` après validation visuelle :

- Login / Register
- Profil étudiant
- Recherche mentors
- Dashboard admin

---

## Tests

```bash
npm test -- --watchAll=false
```

Test unitaire : formulaire de connexion (`App.test.js`).

**Bonus** : Cypress / Playwright — non inclus ; à ajouter sur branche dédiée si demandé.
