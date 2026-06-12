#!/bin/bash

# OWASP ZAP Baseline Scan - Persona 2 - Seguridad
# Se ejecuta desde el pipeline de Jenkins

TARGET_URL="http://localhost:8080"
ZAP_PORT=8090
REPORT_FILE="zap-report.html"

echo "Iniciando OWASP ZAP baseline scan contra $TARGET_URL"

docker run --rm \
    --network="host" \
    -v $(pwd)/security:/zap/wrk/:rw \
    ghcr.io/zaproxy/zaproxy:stable \
    zap-baseline.py \
    -t $TARGET_URL \
    -p $ZAP_PORT \
    -r $REPORT_FILE \
    -I

echo "Scan completado. Reporte generado en security/$REPORT_FILE"
