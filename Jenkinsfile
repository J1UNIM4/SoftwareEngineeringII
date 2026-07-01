// Pipeline CI/CD - Proyecto Final IS2 (agente Windows)
// Disparado por commit via webhook de GitHub (githubPush).
// Sin webhook publico, usar como alternativa: pollSCM('H/2 * * * *')
// Etapas: Build -> Unit Tests -> Analisis Estatico -> Despliegue Docker

pipeline {
    agent any

    triggers {
        githubPush()
    }

    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timeout(time: 30, unit: 'MINUTES')
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            // Compilacion + gestion de dependencias + empaquetado del jar (rubrica item 3)
            steps {
                bat 'mvnw.cmd -B clean package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('Pruebas Unitarias') {
            // JUnit 5 + Mockito (rubrica item 5)
            steps {
                bat 'mvnw.cmd -B test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Analisis Estatico (SonarQube)') {
            // Rubrica item 4. Requiere servidor SonarQube configurado en Jenkins
            // (Manage Jenkins -> System -> SonarQube servers, nombre 'SonarQube').
            // No rompe el build si Sonar esta caido.
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                    withSonarQubeEnv('SonarQube') {
                        bat 'mvnw.cmd -B sonar:sonar'
                    }
                }
            }
        }

        stage('Despliegue (Docker)') {
            // Gestion de entrega via contenedores (rubrica item 10).
            // Solo en ramas estables: main (produccion) y desarrollo (staging).
            when {
                anyOf {
                    branch 'main'
                    branch 'desarrollo'
                }
            }
            steps {
                bat 'docker compose build'
                bat 'docker compose up -d'
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
