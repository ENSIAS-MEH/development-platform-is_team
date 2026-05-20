# MentorPath CI Pipeline Schema

## Flow

Push to develop / Pull Request to main or develop
                        ↓
        ┌───────────────────────────────┐
        │                               │
        ▼                               ▼
┌──────────────────┐         ┌──────────────────────┐
│  backend-ci      │         │  frontend-ci          │
│  ubuntu-22.04    │         │  ubuntu-22.04         │
│                  │         │                       │
│ Checkout code    │         │ Checkout code         │
│ Setup Java 17    │         │ Setup Node.js 20      │
│ Cache Maven      │         │ Cache npm             │
│ clean verify     │         │ npm install           │
│  ├ JUnit tests   │         │ npm test (CI=true)    │
│  └ JaCoCo ≥80%  │         │ npm run build         │
│ Checkstyle       │         │ Trivy fs scan         │
│ Trivy fs scan    │         │ Build Docker image    │
│ Build JAR        │         │ Trivy image scan      │
│ Build Docker img │         │ Push to Docker Hub    │
│ Trivy image scan │         └──────────────────────┘
│ Push to DockerHub│
└──────────────────┘
        │                               │
        └───────────────┬───────────────┘
                        ▼
                 ┌─────────────┐
                 │   deploy    │
                 │ ubuntu-22.04│
                 │             │
                 │ Deploy to   │
                 │  staging    │
                 │             │
                 │ Send email  │
                 │notification │
                 └─────────────┘
                        │
            ┌───────────┴───────────┐
            ▼                       ▼
      ✅ Pipeline passed      ❌ Pipeline failed
      Email sent              Job blocked