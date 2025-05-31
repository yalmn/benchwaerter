#!/usr/bin/env bash

# Skript: run_bench.sh
# Zweck: Führt Build, Benchmarks und Report-Generierung automatisiert durch.

set -euo pipefail

# Farben
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# 1. Projekt bauen
echo -e "${GREEN}>>> Baue Maven-Projekt...${NC}"
mvn clean package -q

# Pfade
JAR_PATH="target/benchwaerter-1.0-SNAPSHOT.jar"
JSON_RESULTS="benchmark-results.json"
TEMPLATE_PATH="src/main/resources/templates/report-template.html"
HTML_REPORT="benchmark-report.html"

# 2. Benchmarks ausführen (JSON-Erzeugung automatisch)
echo -e "${GREEN}>>> Führe Benchmarks aus...${NC}"
java -cp "$JAR_PATH" org.example.benchmark.Main

# 3. Prüfen, ob JSON-Datei erzeugt wurde
if [[ ! -f "$JSON_RESULTS" ]]; then
  echo -e "${RED}❌ Fehler: '$JSON_RESULTS' wurde nicht erzeugt.${NC}"
  exit 1
fi

# 4. Report generieren
echo -e "${GREEN}>>> Erzeuge HTML-Report...${NC}"
java -cp "target/classes:$JAR_PATH" \
  org.example.report.ReportGenerator "$JSON_RESULTS" "$TEMPLATE_PATH" "$HTML_REPORT"

# 5. Prüfen, ob Report existiert
if [[ ! -f "$HTML_REPORT" ]]; then
  echo -e "${RED}❌ Fehler: '$HTML_REPORT' wurde nicht erstellt.${NC}"
  exit 1
fi

# 6. Erfolg
echo -e "${GREEN}✅ Benchmark und Report erfolgreich abgeschlossen.${NC}"
echo "📄 JSON-Ergebnisse: $JSON_RESULTS"
echo "🌐 HTML-Report: $HTML_REPORT"

# Optional: automatisch öffnen (macOS)
if command -v open > /dev/null; then
  open "$HTML_REPORT"
fi
