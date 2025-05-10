# benchwaerter

**benchwaerter** ist ein modulares Java-Tool zum Benchmarken von Algorithmen (z. B. modulare Exponentiation und Euklidischer Algorithmus) mit JMH, inklusive Heap- und CPU-Metriken. Am Ende wird automatisch ein JSON-Bericht sowie ein formatiertes HTML-Report erzeugt.

---

## Inhaltsverzeichnis

- [Features](#features)
- [Projektstruktur](#projektstruktur)
- [Voraussetzungen](#voraussetzungen)
- [Installation und Verwendung](#installation-und-verwendung)
  - [1. Klonen des Repositories](#1-klonen-des-repositories)
  - [2. Lokales Ausführen mit Script](#2-lokales-ausführen-mit-script)
  - [3. Manueller Ablauf](#3-manueller-ablauf)
- [Konfiguration](#konfiguration)
- [Erweiterung um eigene Algorithmen](#erweiterung-um-eigene-algorithmen)
- [Erklärung der Metriken](#erklärung-der-metriken)
- [Lizenz](#lizenz)

---

## Features

- **Benchmarking mit JMH**: Präzise Messung von Laufzeit (ops/s), Latenz und sekundären Metriken.
- **Sekundäre Metriken**: Heap-Speicherverbrauch und CPU-Auslastung pro Operation.
- **Automatisiertes Reporting**: Ausgabe als JSON (`benchmark-results.json`) und HTML-Report (`report.html`).
- **Modularer Aufbau**: Implementierungen mittels `ServiceLoader` oder direkter Integration.
- **Einfaches Shell-Skript**: `run_bench.sh` für einen Klick-Workflow.
- **Deutscher Report**: Alle Texte und Legende vollständig auf Deutsch.

---

## Projektstruktur

```text
benchwaerter/
├── pom.xml                # Maven-Konfiguration
├── run_bench.sh           # Shell-Skript für automatisierten Ablauf
├── README.md              
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── org/example/benchmark/  # Benchmark-core und Main
│   │   │   ├── org/example/algorithmen/ # Implementierungen (FastExponential, Euclid)
│   │   │   └── org/example/report/     # ReportGenerator & DTO
│   │   └── resources/
│   │       └── templates/report.html   # Thymeleaf-Template
│   └── test/  # (optional) Unit-Tests
└── target/                # von Maven erzeugte Artefakte
```

---

## Voraussetzungen

- Java 17 oder neuer
- Maven 3.6+
- Unix-ähnliche Umgebung (Linux, macOS) für `run_bench.sh`

---

## Installation und Verwendung

### 1. Klonen des Repositories

```bash
git clone https://github.com/DEIN_USERNAME/benchwaerter.git
cd benchwaerter
```

### 2. Lokales Ausführen mit Script

Das Skript `run_bench.sh` führt den kompletten Ablauf automatisiert aus:

```bash
chmod +x run_bench.sh
./run_bench.sh
```

Am Ende findest du:
- **benchmark-results.json** mit allen Roh-Metriken.
- **report.html** als anschaulicher HTML-Report.

### 3. Manueller Ablauf

Falls du Schritt für Schritt vorgehen möchtest:

1. **Projekt bauen**
   ```bash
   mvn clean package
   ```
2. **Benchmarks ausführen**
   ```bash
   java -jar target/benchwaerter-1.0-SNAPSHOT.jar
   ```
3. **Report generieren**
   ```bash
   java -cp target/classes:target/benchwaerter-1.0-SNAPSHOT.jar org.example.report.ReportGenerator
   ```

---

## Konfiguration

- **JMH-Parameter**: In `Main` bzw. `run_bench.sh` lassen sich Forks, Iterationen, Warmup-Zyklen anpassen.
- **Bit-Größen**: In `AlgorithmBenchmark` (`@Param({"256","512","1024"})`) änderbar.
- **Template**: Das HTML-Layout in `src/main/resources/templates/report.html` anpassbar.
- **run_bench.sh**: Das Shell-Skript im Projekt-Root kann Pfade und Dateinamen bei Bedarf anpassen.

---

## Erweiterung um eigene Algorithmen

1. Implementiere das Interface:
   ```java
   public class MeinAlg implements Benchmarkable {
       public BigInteger execute(BigInteger... inputs) { /* ... */ }
       public String name() { return "MeinAlg"; }
   }
   ```
2. Füge die FQCN in `META-INF/services/org.example.benchmark.Benchmarkable` hinzu oder annotiere mit `@AutoService(Benchmarkable.class)`.
3. Projekt neu bauen – dein Algorithmus erscheint automatisch in den Benchmarks.

---

## Erklärung der Metriken

- **Bit-Größe**: Anzahl der Bits der Eingabe-Zahlen.
- **Durchsatz (ops/s)**: Anzahl der Operationen pro Sekunde.
- **Fehler (± ops/s)**: Standardfehler des Durchsatzes (95 % Konfidenzintervall).
- **Operationen pro ms**: Durchsatz in Operationen pro Millisekunde.
- **Latenz pro Op (µs)**: Mittlere Ausführungszeit einer Operation in Mikrosekunden.
- **Heap-Verbrauch (MB)**: Zunahme des Heap-Speicherverbrauchs pro Operation (in MB).
- **CPU-Auslastung (%)**: Zunahme der Prozessorauslastung pro Operation in Prozentpunkten.

---

## Lizenz

MIT License © DEIN_NAME
