# 🎓 MentorPath  
### Plateforme d'Orientation & Parcours Étudiant Guidé

---

## 📌 1. Introduction

### 🔹 1.1 Contexte & Motivation
MentorPath est une application sociale dédiée aux étudiants, conçue pour faciliter l'orientation académique et professionnelle à travers un système de mentorat structuré.

Les étudiants avancés (alumni ou étudiants de dernières années) peuvent :
- partager leurs **roadmaps de parcours**
- créer des **guides de compétences**
- accompagner les nouveaux étudiants via des **sessions planifiées**

👉 Ce projet répond à un besoin réel à l'ENSIAS :  
la transmission du savoir entre promotions est souvent **informelle et non structurée**.  
**MentorPath digitalise et structure ce processus.**

---

### 🎯 1.2 Objectifs
- Créer un réseau social de mentorat entre étudiants et alumni  
- Permettre la publication et le suivi de roadmaps personnalisées  
- Faciliter la planification de sessions de mentorat  
- Mettre en place un système de réputation (évaluations)  
- Garantir la sécurité et la confidentialité des données  

---

## ⚙️ 2. Spécifications du Projet

### 🔹 2.1 Exigences Fonctionnelles

| ID  | Fonctionnalité | Acteur | Priorité |
|-----|--------------|--------|----------|
| F01 | Inscription / Connexion sécurisée | Tous | Haute |
| F02 | Gestion du profil | STUDENT / MENTOR | Haute |
| F03 | Publier une roadmap | MENTOR | Haute |
| F04 | Suivre une roadmap | STUDENT | Haute |
| F05 | Demander une session | STUDENT | Haute |
| F06 | Planifier une session | MENTOR | Haute |
| F07 | Messagerie interne | STUDENT / MENTOR | Moyenne |
| F08 | Évaluer un mentor | STUDENT | Moyenne |
| F09 | Modération | ADMIN | Haute |
| F10 | Dashboard analytique | ADMIN | Moyenne |
| F11 | Recherche de mentors | STUDENT | Haute |
| F12 | Notifications temps réel | Tous | Moyenne |

---

### 🔹 2.2 Exigences Non-Fonctionnelles

- ⚡ **Performance** : temps de réponse API < 300ms  
- 🔐 **Sécurité** : JWT, BCrypt, gestion des rôles  
- 📈 **Scalabilité** : architecture REST stateless  
- 🟢 **Disponibilité** : 99% uptime (dev/test)  
- 🧪 **Maintenabilité** :
  - couverture de tests ≥ 80%  
  - documentation (Javadoc, Swagger/OpenAPI)

---

### 👥 2.3 Acteurs & Rôles

| Rôle    | Description | Permissions |
|--------|------------|------------|
| STUDENT | Étudiant cherchant de l'orientation | Consulter roadmaps, demander sessions, évaluer |
| MENTOR  | Étudiant avancé / alumni | Créer roadmaps, gérer sessions, répondre |
| ADMIN   | Administrateur | Gérer utilisateurs, modérer, analytics |

---

### 📖 2.4 User Stories

- 👨‍🎓 En tant que **STUDENT**, je veux chercher un mentor par filière afin de trouver un guide adapté  
- 🧑‍🏫 En tant que **MENTOR**, je veux publier ma roadmap ENSIAS afin d’aider les nouveaux étudiants  
- 📅 En tant que **STUDENT**, je veux planifier une session pour obtenir des conseils personnalisés  
- 📊 En tant que **ADMIN**, je veux consulter les statistiques pour mesurer l’impact  

---

## 🛠️ Technologies 



## 🚀 Installation 

