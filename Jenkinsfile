// Pipeline CI/CD - Proyecto Final IS2 (agente Windows)
// Disparado por commit via webhook de GitHub (githubPush).
// Sin webhook publico, usar como alternativa: pollSCM('H/2 * * * *')
// Etapas: Checkout -> Build -> Unit Tests -> Analisis Estatico -> Despliegue
//
// El despliegue detiene la instancia anterior y levanta el jar empaquetado
// en http://localhost:8080. Para despliegue por contenedores (rubrica item 10)
// el repo incluye docker-compose.yml: docker compose build && docker compose up -d

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
        REPO_URL      = 'https://github.com/J1UNIM4/SoftwareEngineeringII.git'
        DEPLOY_BRANCH = 'desarrollo'
        APP_PORT      = '8080'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: env.DEPLOY_BRANCH, url: env.REPO_URL
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

        stage('Despliegue') {
            // Despliegue automatico (rubrica item 2): detiene la version anterior
            // y levanta el jar recien construido en segundo plano.
            steps {
                // 1. Detener la instancia anterior escuchando en APP_PORT (si existe)
                bat 'powershell -NoProfile -Command "Get-NetTCPConnection -LocalPort %APP_PORT% -State Listen -ErrorAction SilentlyContinue | Select-Object -Unique -ExpandProperty OwningProcess | ForEach-Object { Stop-Process -Id $_ -Force -ErrorAction SilentlyContinue }; exit 0"'

                // 2. Lanzar la nueva version en segundo plano.
                //    JENKINS_NODE_COOKIE=dontKillMe evita que Jenkins mate el proceso al terminar el build.
                bat '''
                    set JENKINS_NODE_COOKIE=dontKillMe
                    for %%f in (target\\*.jar) do start "finance-app" /MIN java -jar "%%f"
                '''

                // 3. Health check: esperar hasta 60s a que la app responda.
                //    Una respuesta 401 (Spring Security) tambien cuenta como "app arriba".
                bat 'powershell -NoProfile -Command "$up=$false; for($i=0;$i -lt 30;$i++){ try { Invoke-WebRequest -UseBasicParsing http://localhost:%APP_PORT% -TimeoutSec 2 | Out-Null; $up=$true; break } catch { if($_.Exception.Response){ $up=$true; break }; Start-Sleep -Seconds 2 } }; if($up){ Write-Host (\'App desplegada en http://localhost:\' + $env:APP_PORT) } else { Write-Error \'La app no respondio en 60s\'; exit 1 }"'
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
