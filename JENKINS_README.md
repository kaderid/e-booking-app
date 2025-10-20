# Guide de Configuration Jenkins pour E-Booking

Ce document explique comment configurer et utiliser le pipeline Jenkins CI/CD pour le projet E-Booking.

## 📋 Prérequis

### Sur le serveur Jenkins

1. **Jenkins** (version 2.400+)
2. **Plugins Jenkins requis** :
   - Pipeline
   - Git
   - Docker Pipeline
   - Maven Integration
   - NodeJS
   - Email Extension
   - JUnit
   - SonarQube Scanner (optionnel)

3. **Outils à installer** :
   - Maven 3.9+
   - JDK 17
   - Node.js 18+
   - Docker
   - Docker Compose

## 🔧 Configuration Jenkins

### 1. Configuration des outils globaux

Aller dans **Manage Jenkins > Global Tool Configuration** :

#### Maven
- Nom : `Maven 3.9`
- Version : Maven 3.9.x
- Installer automatiquement : ✅

#### JDK
- Nom : `JDK 17`
- JAVA_HOME : Chemin vers JDK 17
- Installer automatiquement : ✅

#### NodeJS
- Nom : `NodeJS 18`
- Version : NodeJS 18.x
- Installer automatiquement : ✅
- Packages npm globaux : `@angular/cli`

### 2. Configuration des credentials

Aller dans **Manage Jenkins > Manage Credentials** :

#### Docker Hub (pour push des images)
- Type : Username with password
- ID : `docker-hub-credentials`
- Username : Votre username Docker Hub
- Password : Votre token Docker Hub

#### Email (optionnel)
- Configurer dans **Manage Jenkins > Configure System > Extended E-mail Notification**
- SMTP Server : smtp.gmail.com
- Port : 587
- Credentials : Votre email et mot de passe d'application

### 3. Création du Job Jenkins

1. **Nouveau Job** :
   - Cliquer sur "New Item"
   - Nom : `e-booking-pipeline`
   - Type : Pipeline
   - Cliquer sur "OK"

2. **Configuration du Job** :

   #### General
   - Description : `Pipeline CI/CD pour E-Booking`
   - ✅ GitHub project (optionnel)

   #### Build Triggers
   - ✅ Poll SCM : `H/5 * * * *` (vérifier toutes les 5 minutes)
   - ✅ GitHub hook trigger (si configuré)

   #### Pipeline
   - Definition : `Pipeline script from SCM`
   - SCM : `Git`
   - Repository URL : URL de votre dépôt Git
   - Credentials : Vos credentials Git
   - Branch : `*/main` (ou `*/develop` pour staging)
   - Script Path : `Jenkinsfile`

3. **Sauvegarder**

## 🚀 Utilisation du Pipeline

### Structure du Pipeline

Le pipeline contient les étapes suivantes :

1. **Checkout** : Récupération du code source
2. **Build Backend** : Compilation du backend Spring Boot
3. **Test Backend** : Exécution des tests backend
4. **Package Backend** : Création du JAR
5. **Build Frontend** : Installation des dépendances Angular
6. **Test Frontend** : Exécution des tests Angular
7. **Build Frontend Production** : Build de production
8. **Code Quality Analysis** : Analyse SonarQube (optionnel)
9. **Build Docker Images** : Création des images Docker (branche main uniquement)
10. **Push Docker Images** : Push vers Docker Hub (branche main uniquement)
11. **Deploy to Staging** : Déploiement staging (branche develop)
12. **Deploy to Production** : Déploiement production (branche main, avec validation manuelle)
13. **Health Check** : Vérification de santé des services

### Branches et Déploiement

- **`develop`** : Déploiement automatique sur l'environnement de staging
- **`main`** :
  - Build et push des images Docker
  - Déploiement en production (nécessite validation manuelle)
- **Autres branches** : Build et tests uniquement

### Lancer un Build

1. **Automatique** :
   - Le pipeline se déclenche automatiquement à chaque push
   - Ou toutes les 5 minutes si des changements sont détectés (Poll SCM)

2. **Manuel** :
   - Aller sur le job Jenkins
   - Cliquer sur "Build Now"

## 📊 Rapports et Résultats

### Consulter les Résultats

- **Console Output** : Logs détaillés de chaque étape
- **Test Results** : Résultats des tests unitaires (JUnit)
- **Build Status** : Succès ✅, Échec ❌, ou Instable ⚠️

### Notifications Email

Les notifications sont envoyées dans les cas suivants :
- ✅ Build réussi
- ❌ Build échoué
- ⚠️ Build instable

## 🐳 Docker Images

Les images Docker sont créées et poussées vers Docker Hub :

- **Backend** : `ebooking/backend:latest` et `ebooking/backend:{BUILD_NUMBER}`
- **Frontend** : `ebooking/frontend:latest` et `ebooking/frontend:{BUILD_NUMBER}`

## 🔍 Dépannage

### Erreur : Maven not found
- Vérifier la configuration de Maven dans Global Tool Configuration
- S'assurer que le nom correspond : `Maven 3.9`

### Erreur : NodeJS not found
- Vérifier la configuration de Node.js dans Global Tool Configuration
- S'assurer que le nom correspond : `NodeJS 18`

### Erreur : Docker command not found
- Installer Docker sur le serveur Jenkins
- Ajouter l'utilisateur Jenkins au groupe docker :
  ```bash
  sudo usermod -aG docker jenkins
  sudo systemctl restart jenkins
  ```

### Tests Frontend échouent
- Installer Chrome/Chromium sur le serveur Jenkins :
  ```bash
  sudo apt-get install chromium-browser
  ```

### Problème de connexion à la base de données
- Vérifier que PostgreSQL est démarré
- Vérifier les credentials dans les variables d'environnement

## 📝 Variables d'Environnement

Les variables suivantes peuvent être configurées :

```groovy
// Base de données
DB_HOST = 'localhost'
DB_PORT = '5432'
DB_NAME = 'e_booking_test'
DB_USER = 'booking_user'
DB_PASSWORD = 'booking_pass'

// Docker
DOCKER_REGISTRY = 'docker.io'
DOCKER_IMAGE_BACKEND = 'ebooking/backend'
DOCKER_IMAGE_FRONTEND = 'ebooking/frontend'
```

## 🎯 Bonnes Pratiques

1. **Branches** :
   - `main` : Code de production
   - `develop` : Code de développement/staging
   - `feature/*` : Nouvelles fonctionnalités

2. **Commits** :
   - Messages clairs et descriptifs
   - Tests passants avant de push

3. **Pull Requests** :
   - Code review obligatoire
   - Tests passants
   - Build Jenkins réussi

4. **Déploiement** :
   - Staging d'abord (branche develop)
   - Production après validation (branche main)

## 📞 Support

En cas de problème :
1. Consulter les logs Jenkins
2. Vérifier la configuration des outils
3. Contacter l'équipe DevOps

## Arreter Jenkins: 
ps aux | grep jenkins

## Redemarer Jenkins:
/Users/username/jenkins-start.sh

---

**Projet E-Booking** - Pipeline CI/CD avec Jenkins
