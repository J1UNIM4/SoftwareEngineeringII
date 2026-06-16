// Pipeline CI/CD - Proyecto Final IS2
// Disparado por commit via webhook de GitHub (githubPush).
// Etapas: Build -> Analisis Estatico -> Unit Tests -> Funcionales -> Performance -> Seguridad -> Docker

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

    environment {
        APP_URL = 'http://localhost:8080'
    }

    stages {

        stage('Build') {
            // Compilacion + gestion de dependencias + empaquetado del jar (rubrica item 3)
            steps {
                sh './mvnw -B clean package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('Analisis Estatico (SonarQube)') {
            // Persona 4 - rubrica item 4. Requiere un servidor SonarQube configurado en Jenkins.
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                    withSonarQubeEnv('SonarQube') {
                        sh './mvnw -B sonar:sonar'
                    }
                }
            }
        }

        stage('Pruebas Unitarias') {
            // Persona 4 - rubrica item 5. JUnit 5 + Mockito.
            steps {
                sh './mvnw -B test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Pruebas Funcionales (Newman)') {
            // Persona 3 - rubrica item 6. Coleccion Postman ejecutada con Newman.
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                    sh '''
                        java -jar target/*.jar &
                        APP_PID=$!
                        sleep 30
                        newman run backend/src/test/resources/finance-api-tests.json
                        kill $APP_PID
                    '''
                }
            }
        }

        stage('Pruebas de Performance (JMeter)') {
            // Persona 3 - rubrica item 7.
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                    sh '''
                        java -jar target/*.jar &
                        APP_PID=$!
                        sleep 30
                        jmeter -n -t backend/src/test/jmeter/performance-test.jmx -l target/jmeter-results.jtl
                        kill $APP_PID
                    '''
                }
            }
        }

        stage('Pruebas de Seguridad (OWASP ZAP)') {
            // Persona 2 - rubrica item 8. Baseline scan contra la app levantada.
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                    sh '''
                        java -jar target/*.jar &
                        APP_PID=$!
                        sleep 30
                        bash security/zap-scan.sh
                        kill $APP_PID
                    '''
                }
            }
        }

        stage('Despliegue (Docker)') {
            // Gestion de entrega via contenedores (rubrica item 10). Solo en master.
            when {
                branch 'master'
            }
            steps {
                sh 'docker compose build'
                sh 'docker compose up -d'
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
