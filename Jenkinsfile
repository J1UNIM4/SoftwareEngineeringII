// Pipeline CI/CD - Proyecto Final IS2 (agente Windows)
// Disparado por commit via webhook de GitHub (githubPush).
// Sin webhook publico, usar como alternativa: pollSCM('H/2 * * * *')
// Etapas: Checkout -> Detener app -> Build -> Unit Tests -> Sonar -> Despliegue
//
// REQUISITO: Jenkins debe correr en un puerto DISTINTO a APP_PORT (ej. 8081).
// Si Jenkins corre en 8080, el paso de limpieza del puerto lo mataria.
//
// La app se despliega desde DEPLOY_DIR (fuera del workspace) para que:
// - cleanWs no falle por el lock de Windows sobre el jar en ejecucion
// - el proximo build pueda hacer 'clean' sin chocar con el jar corriendo
// El proceso desplegado se rastrea por PID (app.pid), nunca se mata por puerto
// a un proceso desconocido sin verificar que sea java.

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
        DEPLOY_DIR    = 'C:\\jenkins-deploy'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: env.DEPLOY_BRANCH, url: env.REPO_URL
            }
        }

        stage('Detener version anterior') {
            // Antes de compilar: si hay una app desplegada, detenerla para
            // liberar el puerto y cualquier lock sobre archivos.
            steps {
                // 1. Matar por PID registrado en el despliegue anterior (metodo seguro)
                bat 'powershell -NoProfile -Command "$pidFile = Join-Path $env:DEPLOY_DIR \'app.pid\'; if (Test-Path $pidFile) { $procId = Get-Content $pidFile; Stop-Process -Id $procId -Force -ErrorAction SilentlyContinue; Remove-Item $pidFile -Force }; exit 0"'
                // 2. Fallback: liberar el puerto, pero SOLO si el proceso es java
                //    (evita matar Jenkins u otro servicio por accidente)
                bat 'powershell -NoProfile -Command "Get-NetTCPConnection -LocalPort $env:APP_PORT -State Listen -ErrorAction SilentlyContinue | Select-Object -Unique -ExpandProperty OwningProcess | ForEach-Object { $p = Get-Process -Id $_ -ErrorAction SilentlyContinue; if ($p -and $p.ProcessName -eq \'java\') { Stop-Process -Id $_ -Force -ErrorAction SilentlyContinue } }; exit 0"'
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
            // Rubrica item 4. Requiere servidor SonarQube configurado en Jenkins:
            // Manage Jenkins -> System -> SonarQube servers -> Add, con nombre
            // exactamente 'SonarQube', URL http://localhost:9000 y token.
            // Sin configurar queda UNSTABLE (amarillo), no rompe el build.
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                    withSonarQubeEnv('SonarQube') {
                        bat 'mvnw.cmd -B sonar:sonar'
                    }
                }
            }
        }

        stage('Despliegue') {
            // Despliegue automatico (rubrica item 2): copia el jar fuera del
            // workspace, lo lanza en segundo plano y registra su PID.
            steps {
                bat 'if not exist "%DEPLOY_DIR%" mkdir "%DEPLOY_DIR%"'
                bat 'copy /Y target\\*.jar "%DEPLOY_DIR%\\app.jar"'

                // JENKINS_NODE_COOKIE=dontKillMe evita que Jenkins mate el proceso
                // al terminar el build. Se guarda el PID para el proximo despliegue.
                bat 'powershell -NoProfile -Command "$env:JENKINS_NODE_COOKIE = \'dontKillMe\'; $jar = Join-Path $env:DEPLOY_DIR \'app.jar\'; $p = Start-Process java -ArgumentList \'-jar\', $jar -WindowStyle Hidden -PassThru; $p.Id | Out-File (Join-Path $env:DEPLOY_DIR \'app.pid\') -Encoding ascii"'

                // Health check: esperar hasta 60s. Un 401 (Spring Security) tambien
                // cuenta como app arriba.
                bat 'powershell -NoProfile -Command "$up = $false; for ($i = 0; $i -lt 30; $i++) { try { Invoke-WebRequest -UseBasicParsing (\'http://localhost:\' + $env:APP_PORT) -TimeoutSec 2 | Out-Null; $up = $true; break } catch { if ($_.Exception.Response) { $up = $true; break }; Start-Sleep -Seconds 2 } }; if ($up) { Write-Host (\'App desplegada en http://localhost:\' + $env:APP_PORT) } else { Write-Error \'La app no respondio en 60s\'; exit 1 }"'
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
