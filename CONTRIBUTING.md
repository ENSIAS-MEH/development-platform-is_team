# Convention de Commits — MentorPath

> Ce fichier explique aux membres de l'équipe les règles à suivre pour contribuer au projet.
> Avec ce fichier, tout le monde suit la même convention.

## Format général
`<type>(<scope>): <description> closes #<numéro issue>`

## Branches
- `main` → code stable, production
- `develop` → intégration continue
- `feature/m1-*` → Membre 1
- `feature/m2-*` → Membre 2
- `feature/m3-*` → Membre 3
- `feature/m4-*` → Membre 4

## Types
- `feat` : nouvelle fonctionnalité
- `fix` : correction de bug
- `ci` : pipeline CI/CD
- `chore` : configuration, setup
- `docs` : documentation
- `test` : ajout de tests

## Exemples
feat(auth): add JWT token generation closes #5
ci(docker): add multi-stage Dockerfile closes #2
chore(git): setup branch protection rules closes #1
fix(api): handle null pointer in UserService closes #8