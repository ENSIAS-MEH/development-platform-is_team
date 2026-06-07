-- ============================================================
-- V4 : Réinitialisation des données de démonstration
-- Comptes : 1 admin, 2 mentors, 3 students
-- Mot de passe pour tous : Pasword123
-- ============================================================

TRUNCATE TABLE
    messages,
    conversations,
    ratings,
    sessions,
    roadmap_enrollments,
    roadmap_steps,
    roadmaps,
    student_profiles,
    mentor_profiles,
    users
RESTART IDENTITY CASCADE;

-- ── Utilisateurs ──────────────────────────────────────────────
INSERT INTO users (email, password, role) VALUES
  ('admin@ensias.ma',    '$2a$12$zjsKgSN0tYV4pPvWT.NbH.DxxcW2BBjwXHQV8rrkbPPa0OfGTs0iW', 'ADMIN'),
  ('mentor1@ensias.ma',  '$2a$12$zjsKgSN0tYV4pPvWT.NbH.DxxcW2BBjwXHQV8rrkbPPa0OfGTs0iW', 'MENTOR'),
  ('mentor2@ensias.ma',  '$2a$12$zjsKgSN0tYV4pPvWT.NbH.DxxcW2BBjwXHQV8rrkbPPa0OfGTs0iW', 'MENTOR'),
  ('student1@ensias.ma', '$2a$12$zjsKgSN0tYV4pPvWT.NbH.DxxcW2BBjwXHQV8rrkbPPa0OfGTs0iW', 'STUDENT'),
  ('student2@ensias.ma', '$2a$12$zjsKgSN0tYV4pPvWT.NbH.DxxcW2BBjwXHQV8rrkbPPa0OfGTs0iW', 'STUDENT'),
  ('student3@ensias.ma', '$2a$12$zjsKgSN0tYV4pPvWT.NbH.DxxcW2BBjwXHQV8rrkbPPa0OfGTs0iW', 'STUDENT');

-- ── Profils mentors ───────────────────────────────────────────
INSERT INTO mentor_profiles (user_id, first_name, last_name, filiere, promo, bio, expertise, rating, available) VALUES
  (2, 'Youssef', 'Alami',  'GL', 2022, 'Alumni passionné de Spring Boot et des architectures cloud.', 'Java, Spring, DevOps', 4.50, TRUE),
  (3, 'Imane',   'Benali', 'GD', 2023, 'Spécialisée en data engineering et pipelines Spark.',         'Python, Spark, SQL',   4.20, TRUE);

-- ── Profils étudiants ───────────────────────────────────────
INSERT INTO student_profiles (user_id, first_name, last_name, filiere, annee_etude) VALUES
  (4, 'Sara',   'El Fassi',  'GL', 2),
  (5, 'Amine',  'Rachidi',   'GD', 1),
  (6, 'Fatima', 'Zahra',     'GL', 3);

-- ── Roadmaps ──────────────────────────────────────────────────
INSERT INTO roadmaps (id, title, description, filiere, mentor_id) VALUES
  (1, 'Parcours Spring Boot', 'De zéro à une API REST sécurisée avec Spring Boot.', 'GL', 2),
  (2, 'Data Engineering avec Spark', 'Construire un pipeline de données batch avec Spark et SQL.', 'GD', 3);

INSERT INTO roadmap_steps (roadmap_id, title, description, step_order) VALUES
  (1, 'Initialiser le projet',       'Créer un projet Maven / Spring Initializr.',              1),
  (1, 'Exposer une API REST',        'Créer controllers, DTOs et validation.',                2),
  (1, 'Sécuriser avec JWT',          'Ajouter Spring Security et authentification.',            3),
  (2, 'Modéliser les données',       'Définir le schéma et les sources.',                       1),
  (2, 'Pipeline Spark',              'Lire, transformer et agréger les données.',               2),
  (2, 'Déployer le job',             'Planifier et monitorer le traitement.',                     3);

-- ── Inscriptions roadmaps ─────────────────────────────────────
INSERT INTO roadmap_enrollments (student_id, roadmap_id, progress_percent) VALUES
  (4, 1, 25),
  (5, 2, 10),
  (6, 1, 50);

-- ── Sessions de mentorat ──────────────────────────────────────
INSERT INTO sessions (student_id, mentor_id, session_date, message, status) VALUES
  (4, 2, CURRENT_TIMESTAMP + INTERVAL '3 days',  'Besoin d''aide sur mon projet Spring Boot.',     'PENDING'),
  (5, 3, CURRENT_TIMESTAMP + INTERVAL '5 days',  'Questions sur un pipeline Spark pour mon TP.',   'ACCEPTED'),
  (6, 2, CURRENT_TIMESTAMP - INTERVAL '2 days',  'Demande de revue de code avant la soutenance.',  'DECLINED');

SELECT setval('roadmaps_id_seq', (SELECT MAX(id) FROM roadmaps));
SELECT setval('roadmap_steps_id_seq', (SELECT MAX(id) FROM roadmap_steps));
