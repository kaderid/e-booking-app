pipeline {
    agent any

    tools {
        maven 'Maven 3.9'
        nodejs 'NodeJS 22'
        jdk 'JDK 17'
    }

    environment {
        // Chemins des projets
        BACKEND_DIR = 'e-booking-app/backend/ebooking'
        FRONTEND_DIR = 'e-booking-app/frontend'

        // Variables Docker
        DOCKER_REGISTRY = 'docker.io'
        DOCKER_IMAGE_BACKEND = 'ebooking/backend'
        DOCKER_IMAGE_FRONTEND = 'ebooking/frontend'

        // Base de données
        DB_HOST = 'localhost'
        DB_PORT = '5432'
        DB_NAME = 'e_booking_test'
        DB_USER = 'booking_user'
        DB_PASSWORD = 'booking_pass'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Récupération du code source...'
                checkout scm
            }
        }

        stage('Build Backend') {
            steps {
                echo 'Construction du backend Spring Boot...'
                dir("${BACKEND_DIR}") {
                    sh 'mvn clean compile -DskipTests'
                }
            }
        }

        stage('Test Backend') {
            steps {
                echo 'Exécution des tests backend...'
                dir("${BACKEND_DIR}") {
                    sh 'mvn test'
                }
            }
            post {
                always {
                    junit "${BACKEND_DIR}/target/surefire-reports/*.xml"
                }
            }
        }

        stage('Package Backend') {
            steps {
                echo 'Packaging du backend...'
                dir("${BACKEND_DIR}") {
                    sh 'mvn package -DskipTests'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                echo 'Installation des dépendances frontend...'
                dir("${FRONTEND_DIR}") {
                    sh 'npm ci'
                }
            }
        }

        stage('Test Frontend') {
            steps {
                echo 'Exécution des tests frontend...'
                dir("${FRONTEND_DIR}") {
                    sh 'npm run test:ci'
                }
            }
            post {
                always {
                    // Publier les rapports de tests Karma (optionnel)
                    junit allowEmptyResults: true, testResults: "${FRONTEND_DIR}/coverage/frontend/junit/*.xml"
                    // Publier le rapport de couverture
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: "${FRONTEND_DIR}/coverage/frontend",
                        reportFiles: 'index.html',
                        reportName: 'Frontend Code Coverage'
                    ])
                }
            }
        }

        stage('Build Frontend Production') {
            steps {
                echo 'Build de production du frontend...'
                dir("${FRONTEND_DIR}") {
                    sh 'npm run build -- --configuration production'
                }
            }
        }

        stage('Code Quality Analysis') {
            parallel {
                stage('Backend Code Quality') {
                    steps {
                        echo 'Analyse de la qualité du code backend...'
                        dir("${BACKEND_DIR}") {
                            sh 'mvn sonar:sonar -Dsonar.host.url=${SONAR_HOST_URL} -Dsonar.login=${SONAR_AUTH_TOKEN} || true'
                        }
                    }
                }
                stage('Frontend Code Quality') {
                    steps {
                        echo 'Analyse de la qualité du code frontend...'
                        dir("${FRONTEND_DIR}") {
                            sh 'npm run lint || true'
                        }
                    }
                }
            }
        }

        stage('Build Docker Images') {
            when {
                branch 'main'
            }
            steps {
                script {
                    echo 'Construction des images Docker...'

                    // Build Backend Docker Image
                    dir("${BACKEND_DIR}") {
                        sh """
                            docker build -t ${DOCKER_IMAGE_BACKEND}:${BUILD_NUMBER} .
                            docker tag ${DOCKER_IMAGE_BACKEND}:${BUILD_NUMBER} ${DOCKER_IMAGE_BACKEND}:latest
                        """
                    }

                    // Build Frontend Docker Image
                    dir("${FRONTEND_DIR}") {
                        sh """
                            docker build -t ${DOCKER_IMAGE_FRONTEND}:${BUILD_NUMBER} .
                            docker tag ${DOCKER_IMAGE_FRONTEND}:${BUILD_NUMBER} ${DOCKER_IMAGE_FRONTEND}:latest
                        """
                    }
                }
            }
        }

        stage('Push Docker Images') {
            when {
                branch 'main'
            }
            steps {
                script {
                    echo 'Push des images Docker vers le registre...'
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh """
                            echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin
                            docker push ${DOCKER_IMAGE_BACKEND}:${BUILD_NUMBER}
                            docker push ${DOCKER_IMAGE_BACKEND}:latest
                            docker push ${DOCKER_IMAGE_FRONTEND}:${BUILD_NUMBER}
                            docker push ${DOCKER_IMAGE_FRONTEND}:latest
                            docker logout
                        """
                    }
                }
            }
        }

        stage('Deploy to Staging') {
            when {
                branch 'develop'
            }
            steps {
                echo 'Déploiement sur l\'environnement de staging...'
                sh '''
                    # Arrêter les anciens conteneurs
                    docker-compose -f docker-compose.staging.yml down || true

                    # Démarrer les nouveaux conteneurs
                    docker-compose -f docker-compose.staging.yml up -d

                    # Attendre que les services soient prêts
                    sleep 30

                    # Vérifier la santé des services
                    docker-compose -f docker-compose.staging.yml ps
                '''
            }
        }

        stage('Deploy to Production') {
            when {
                branch 'main'
            }
            steps {
                input message: 'Déployer en production ?', ok: 'Déployer'

                echo 'Déploiement en production...'
                sh '''
                    # Arrêter les anciens conteneurs
                    docker-compose -f docker-compose.prod.yml down || true

                    # Démarrer les nouveaux conteneurs
                    docker-compose -f docker-compose.prod.yml up -d

                    # Attendre que les services soient prêts
                    sleep 30

                    # Vérifier la santé des services
                    docker-compose -f docker-compose.prod.yml ps
                '''
            }
        }

        stage('Health Check') {
            when {
                anyOf {
                    branch 'main'
                    branch 'develop'
                }
            }
            steps {
                echo 'Vérification de la santé de l\'application...'
                script {
                    sh '''
                        # Vérifier le backend
                        curl -f http://localhost:8080/actuator/health || exit 1

                        # Vérifier le frontend
                        curl -f http://localhost:4200 || exit 1
                    '''
                }
            }
        }
    }

    post {
        always {
            echo 'Nettoyage...'
            cleanWs()
        }
        success {
            echo 'Pipeline exécuté avec succès ! ✅'
            emailext(
                subject: "✅ Build Success: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                body: """
                    Le build ${env.BUILD_NUMBER} du projet ${env.JOB_NAME} a réussi.

                    Détails:
                    - Branche: ${env.GIT_BRANCH}
                    - Commit: ${env.GIT_COMMIT}
                    - URL: ${env.BUILD_URL}
                """,
                to: '${DEFAULT_RECIPIENTS}'
            )
        }
        failure {
            echo 'Le pipeline a échoué ! ❌'
            emailext(
                subject: "❌ Build Failed: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                body: """
                    Le build ${env.BUILD_NUMBER} du projet ${env.JOB_NAME} a échoué.

                    Détails:
                    - Branche: ${env.GIT_BRANCH}
                    - Commit: ${env.GIT_COMMIT}
                    - URL: ${env.BUILD_URL}

                    Veuillez consulter les logs pour plus de détails.
                """,
                to: '${DEFAULT_RECIPIENTS}'
            )
        }
        unstable {
            echo 'Le build est instable ⚠️'
        }
    }
}
