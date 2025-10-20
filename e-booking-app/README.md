# 🌍 E-Booking - Application de Réservation de Rendez-vous

Application fullstack de réservation de rendez-vous avec Spring Boot et Angular.

## 📋 Table des matières

- [Architecture](#architecture)
- [Technologies](#technologies)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Démarrage](#démarrage)
- [Fonctionnalités](#fonctionnalités)
- [Structure du projet](#structure-du-projet)
- [API Endpoints](#api-endpoints)

## 🏗️ Architecture

L'application suit une architecture en trois couches :

- **Frontend** : Angular 15 avec Angular Material
- **Backend** : Spring Boot 3.4.10 avec Spring Security et JWT
- **Base de données** : PostgreSQL 15

## 🚀 Technologies

### Backend
- Java 17
- Spring Boot 3.4.10
- Spring Data JPA
- Spring Security
- JWT (JSON Web Tokens)
- MapStruct
- PostgreSQL
- Docker

### Frontend
- Angular 15.2.0
- Angular Material
- TypeScript
- RxJS
- SCSS

## 📦 Prérequis

- Docker & Docker Compose
- Java 17 (pour développement local)
- Node.js 18+ & npm (pour développement local)
- Maven 3.9+ (pour développement local)

## 💻 Installation

### Avec Docker (Recommandé)

1. Cloner le repository
```bash
git clone <repository-url>
cd e-booking-app
```

2. Lancer tous les services avec Docker Compose
```bash
cd ops
docker-compose up --build
```

Les services seront disponibles à :
- Frontend : http://localhost:4200
- Backend : http://localhost:8080
- PgAdmin : http://localhost:8081

### Développement local

#### Backend

```bash
cd backend/ebooking
mvn clean install
mvn spring-boot:run
```

#### Frontend

```bash
cd frontend
npm install
ng serve
```

## 🎯 Démarrage rapide

1. **Lancer l'application avec Docker**
```bash
cd ops
docker-compose up
```

2. **Accéder à l'application**
- Ouvrir http://localhost:4200 dans votre navigateur
- Créer un compte ou se connecter

3. **Comptes par défaut**
- Admin : admin@ebooking.com / admin123
- Prestataire : pro@ebooking.com / pro123
- Client : client@ebooking.com / client123

## ✨ Fonctionnalités

### Pour les Clients
- ✅ Inscription et connexion
- ✅ Recherche de services disponibles
- ✅ Consultation des prestataires
- ✅ Réservation de rendez-vous
- ✅ Gestion de ses rendez-vous
- ✅ Annulation de rendez-vous

### Pour les Prestataires (PRO)
- ✅ Gestion de ses services
- ✅ Gestion de ses disponibilités
- ✅ Consultation des demandes de rendez-vous
- ✅ Confirmation/Annulation de rendez-vous
- ✅ Tableau de bord avec statistiques

### Pour les Administrateurs
- ✅ Gestion des utilisateurs
- ✅ Activation/Blocage de comptes
- ✅ Consultation des statistiques globales
- ✅ Vue d'ensemble de la plateforme

## 📁 Structure du projet

```
e-booking-app/
├── backend/
│   └── ebooking/
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/com/ebooking/
│       │   │   │   ├── controller/
│       │   │   │   ├── dto/
│       │   │   │   ├── entity/
│       │   │   │   ├── mapper/
│       │   │   │   ├── repository/
│       │   │   │   ├── security/
│       │   │   │   └── services/
│       │   │   └── resources/
│       │   └── test/
│       ├── Dockerfile
│       └── pom.xml
├── frontend/
│   └── src/
│       ├── app/
│       │   ├── components/
│       │   │   ├── auth/
│       │   │   │   ├── login/
│       │   │   │   └── register/
│       │   │   ├── client/
│       │   │   │   └── dashboard/
│       │   │   ├── prestataire/
│       │   │   │   └── dashboard/
│       │   │   └── admin/
│       │   │       └── dashboard/
│       │   ├── guards/
│       │   ├── models/
│       │   └── services/
│       ├── environments/
│       ├── Dockerfile
│       └── nginx.conf
└── ops/
    └── docker-compose.yml
```

## 🔌 API Endpoints

### Authentification
- `POST /api/auth/register` - Inscription
- `POST /api/auth/login` - Connexion

### Users
- `GET /api/users` - Liste des utilisateurs (ADMIN)
- `GET /api/users/{id}` - Détails d'un utilisateur
- `PUT /api/users/{id}` - Modifier un utilisateur
- `DELETE /api/users/{id}` - Supprimer un utilisateur

### Services
- `GET /api/services` - Liste des services
- `GET /api/services/{id}` - Détails d'un service
- `GET /api/services/prestataire/{id}` - Services d'un prestataire
- `POST /api/services` - Créer un service (PRO)
- `PUT /api/services/{id}` - Modifier un service (PRO)
- `DELETE /api/services/{id}` - Supprimer un service (PRO)

### Prestataires
- `GET /api/prestataires` - Liste des prestataires
- `GET /api/prestataires/{id}` - Détails d'un prestataire
- `GET /api/prestataires/{id}/disponibilites` - Disponibilités d'un prestataire
- `POST /api/prestataires` - Créer un prestataire
- `PUT /api/prestataires/{id}` - Modifier un prestataire

### Rendez-vous
- `GET /api/rendez-vous` - Liste des rendez-vous (ADMIN)
- `GET /api/rendez-vous/client/{id}` - Rendez-vous d'un client
- `GET /api/rendez-vous/prestataire/{id}` - Rendez-vous d'un prestataire
- `POST /api/rendez-vous` - Créer un rendez-vous
- `PUT /api/rendez-vous/{id}` - Modifier un rendez-vous
- `DELETE /api/rendez-vous/{id}` - Annuler un rendez-vous

### Admin
- `GET /api/admin/statistiques` - Statistiques globales
- `PUT /api/admin/users/{id}/activer` - Activer un compte
- `PUT /api/admin/users/{id}/bloquer` - Bloquer un compte

## 🎨 Design

L'application utilise Angular Material avec un thème personnalisé indigo-pink et des dégradés modernes pour une interface utilisateur attractive :

- Gradient principal : `#667eea` → `#764ba2`
- Design responsive et adaptatif
- Animations fluides
- Cartes avec ombres et effets de survol
- Icônes Material Design

## 🔐 Sécurité

- Authentification JWT
- Guards de routes basés sur les rôles
- Intercepteur HTTP pour les tokens
- Validation des données côté frontend et backend
- Protection CORS
- Headers de sécurité nginx

## 🧪 Tests

### Backend
```bash
cd backend/ebooking
mvn test
```

Tests disponibles:
- AuthServiceTest - Tests d'authentification
- UserServiceTest - Tests de gestion des utilisateurs
- RendezVousServiceTest - Tests de gestion des rendez-vous

### Frontend
```bash
cd frontend
ng test
```

## 📝 Configuration

### Backend (application.properties)
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/e_booking
spring.datasource.username=booking_user
spring.datasource.password=booking_pass
```

### Frontend (environment.ts)
```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

## 🐳 Docker

### Build des images
```bash
# Backend
cd backend/ebooking
docker build -t ebooking-backend .

# Frontend
cd frontend
docker build -t ebooking-frontend .
```

### Lancer avec Docker Compose
```bash
cd ops
docker-compose up -d
```

### Arrêter les services
```bash
docker-compose down
```

### Voir les logs
```bash
docker-compose logs -f
```

## 👥 Rôles et permissions

### CLIENT
- Réserver des services
- Gérer ses rendez-vous
- Consulter les prestataires

### PRO (Prestataire)
- Créer et gérer ses services
- Définir ses disponibilités
- Accepter/Refuser les rendez-vous

### ADMIN
- Gérer tous les utilisateurs
- Activer/Bloquer des comptes
- Consulter les statistiques

## 🛠️ Développement

### Commandes utiles

**Frontend:**
```bash
ng generate component components/nom
ng serve
ng build --configuration production
```

**Backend:**
```bash
mvn clean install
mvn spring-boot:run
mvn test
```

## 📄 Licence

Ce projet est développé dans le cadre d'un mini-projet académique.

## 👨‍💻 Auteur

Projet E-Booking - 2025
