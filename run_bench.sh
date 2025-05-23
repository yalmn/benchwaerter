#!/usr/bin/env bash

# Skript: run_bench.sh
# Zweck: Baut das Maven-Projekt, führt die Benchmarks aus und erstellt den HTML-Report.

set -euo pipefail

# 1. Projekt bauen
echo ">>> Baue Maven-Projekt..."
mvn clean package -q

# Pfade
JAR_PATH="target/benchwaerter-1.0-SNAPSHOT.jar"
JSON_RESULTS="benchmark-results.json"
HTML_REPORT="report.html"

# 2. Benchmarks ausführen
echo ">>> Führe Benchmarks aus (JMH)..."
java -jar "$JAR_PATH"

# Prüfen, ob JSON-Datei existiert
if [[ ! -f "$JSON_RESULTS" ]]; then
  echo "Fehler: '$JSON_RESULTS' wurde nicht gefunden." >&2
  exit 1
fi

# 3. Report generieren
echo ">>> Erzeuge HTML-Report..."
java -cp target/classes:"$JAR_PATH" org.example.report.ReportGenerator

# Prüfen, ob Report existiert
if [[ ! -f "$HTML_REPORT" ]]; then
  echo "Fehler: '$HTML_REPORT' wurde nicht gefunden." >&2
  exit 1
fi

# 4. Erfolgsmeldung
echo "
Benchmark und Report erfolgreich abgeschlossen."
echo "- Ergebnisse: $JSON_RESULTS"
echo "- HTML-Report: $HTML_REPORT"
