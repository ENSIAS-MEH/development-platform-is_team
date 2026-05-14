-- ============================================================
-- V2 : Données initiales de test
-- Mot de passe pour tous : Pasword123
-- Hash BCrypt généré avec force 10
-- ============================================================

INSERT INTO users (email, password, role) VALUES
  ('admin@ensias.ma',   '$2a$12$zjsKgSN0tYV4pPvWT.NbH.DxxcW2BBjwXHQV8rrkbPPa0OfGTs0iW', 'ADMIN');

INSERT INTO users (email, password, role) VALUES
 ('mentor1@ensias.ma', '$2a$12$zjsKgSN0tYV4pPvWT.NbH.DxxcW2BBjwXHQV8rrkbPPa0OfGTs0iW', 'MENTOR');

INSERT INTO users (email, password, role) VALUES
  ('mentor2@ensias.ma', '$2a$12$zjsKgSN0tYV4pPvWT.NbH.DxxcW2BBjwXHQV8rrkbPPa0OfGTs0iW', 'MENTOR');

INSERT INTO users (email, password, role) VALUES
  ('./mvnw test','$2a$12$zjsKgSN0tYV4pPvWT.NbH.DxxcW2BBjwXHQV8rrkbPPa0OfGTs0iW', 'STUDENT');

INSERT INTO users (email, password, role) VALUES
  ('student2@ensias.ma','$2a$12$zjsKgSN0tYV4pPvWT.NbH.DxxcW2BBjwXHQV8rrkbPPa0OfGTs0iW', 'STUDENT');

INSERT INTO mentor_profiles (user_id, first_name, last_name, filiere, promo, bio, expertise) VALUES
  (2, 'Youssef', 'Alami',   'Génie Logiciel',  2022, 'Alumni passionné de Spring Boot.', 'Java, Spring, DevOps');

INSERT INTO mentor_profiles (user_id, first_name, last_name, filiere, promo, bio, expertise) VALUES
  (3, 'Imane',   'Benali',  'Génie des Données',2023, 'Spécialisée en Data Engineering.',   'Python, Spark, SQL');

INSERT INTO student_profiles (user_id, first_name, last_name, filiere, annee_etude) VALUES
  (4, 'Fatima', 'Zahra',   'Génie Logiciel',  2);

INSERT INTO student_profiles (user_id, first_name, last_name, filiere, annee_etude) VALUES
  (5, 'Amine',  'Rachidi', 'Génie des Données',1);
