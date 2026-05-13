# MentorPath CI Pipeline Schema

## Flow
Push code
    ↓
GitHub crée une VM Linux (ubuntu-22.04)
    ↓
Télécharge le code
    ↓
Installe Java 21
    ↓
Cache des dépendances Maven (~/.m2)
    ↓
Compile le projet (mvn clean verify)
    ↓
Lance les tests JUnit
    ↓
Vérifie coverage >= 80% (JaCoCo)
    ↓
✅ Merge autorisé    ❌ Merge refusé