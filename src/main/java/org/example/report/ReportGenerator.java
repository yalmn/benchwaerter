package org.example.report;

import com.google.gson.*;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReportGenerator {

    public static void generateReport(String jsonPath, String templatePath, String outputPath) throws IOException {
        // JSON und Template einlesen
        String jsonContent = Files.readString(Paths.get(jsonPath));
        JsonArray benchmarks = JsonParser.parseString(jsonContent).getAsJsonArray();
        String template = Files.readString(Paths.get(templatePath));

        StringBuilder rows = new StringBuilder();
        double maxThroughput = 0;
        String maxAlgoName = "";
        String maxBitSize = "";
        Set<String> uniqueAlgos = new HashSet<>();

        // Gruppierung nach Algorithmusnamen
        Map<String, List<JsonObject>> grouped = new LinkedHashMap<>();
        for (JsonElement elem : benchmarks) {
            JsonObject obj = elem.getAsJsonObject();
            JsonObject params = obj.getAsJsonObject("params");
            String algo = params.get("implName").getAsString();
            grouped.computeIfAbsent(algo, k -> new ArrayList<>()).add(obj);
            uniqueAlgos.add(algo);
        }

        // Gruppierte Ausgabe pro Algorithmus
        for (String algo : grouped.keySet()) {
            rows.append("<tr><td colspan=\"6\"><strong>* ").append(algo).append("</strong></td></tr>\n");

            for (JsonObject obj : grouped.get(algo)) {
                JsonObject params = obj.getAsJsonObject("params");
                JsonObject primary = obj.getAsJsonObject("primaryMetric");
                JsonObject secondary = obj.getAsJsonObject("secondaryMetrics");

                String bit = params.get("bitSize").getAsString();
                double throughput = primary.get("score").getAsDouble();
                JsonElement itersElem = primary.get("measuredIterations");
                int measurements = (itersElem != null && !itersElem.isJsonNull()) ? itersElem.getAsInt() : 1;
                double latencyUs = 1_000_000.0 / throughput;

                double cpu = 0.0;
                if (secondary.has("cpuLoad")) {
                    cpu = secondary.getAsJsonObject("cpuLoad").get("score").getAsDouble();
                }

                if (throughput > maxThroughput) {
                    maxThroughput = throughput;
                    maxAlgoName = algo;
                    maxBitSize = bit;
                }

                String perfClass = classifyPerformance(throughput);
                String latencyClass = classifyLatency(latencyUs);
                String cpuClass = classifyCpu(cpu);

                rows.append("<tr>")
                        .append("<td class=\"algorithm-name\">").append(algo).append("</td>")
                        .append("<td>").append(bit).append(" bit</td>")
                        .append("<td class=\"metric-value ").append(perfClass).append("\">")
                        .append(String.format("%.2f", throughput)).append("</td>")
                        .append("<td class=\"metric-value ").append(latencyClass).append("\">")
                        .append(String.format("%.1f", latencyUs)).append("</td>")
                        .append("<td class=\"metric-value ").append(cpuClass).append("\">")
                        .append(String.format("%.1f", cpu)).append("</td>")
                        .append("<td>").append(measurements).append("</td>")
                        .append("</tr>\n");
            }
        }

        // Metrik-Legende anhängen
        String rowsAndLegend = rows + """
            <tr><td colspan="6">
                <div class="legend">
                    <div class="legend-item"><strong>Algorithmus:</strong> Name des getesteten Algorithmus.</div>
                    <div class="legend-item"><strong>Bit-Größe:</strong> Länge der Eingabe (z. B. Schlüssellänge) in Bit.</div>
                    <div class="legend-item"><strong>Durchsatz (ops/s):</strong> Anzahl der Operationen pro Sekunde (höher = besser).</div>
                    <div class="legend-item"><strong>Latenz (µs):</strong> Zeit für eine einzelne Operation in Mikrosekunden (niedriger = besser).</div>
                    <div class="legend-item"><strong>CPU Load:</strong> Durchschnittliche CPU-Auslastung während der Benchmark-Ausführung in Prozent.</div>
                    <div class="legend-item"><strong>Messungen:</strong> Anzahl der durchgeführten Einzelmessungen.</div>
                </div>
            </td></tr>
            """;

        // Systeminfos und Zeitstempel
        String os = System.getProperty("os.name") + " " + System.getProperty("os.version");
        String arch = System.getProperty("os.arch");
        String java = System.getProperty("java.version");
        int cores = Runtime.getRuntime().availableProcessors();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));

        // Template-Platzhalter ersetzen
        String result = template
                .replace("<!-- TIMESTAMP -->", timestamp)
                .replace("<!-- OS -->", os)
                .replace("<!-- ARCH -->", arch)
                .replace("<!-- JAVA -->", java)
                .replace("<!-- CORES -->", String.valueOf(cores))
                .replace("<!-- TOTAL_ALGORITHMS -->", String.valueOf(uniqueAlgos.size()))
                .replace("<!-- MAX_THROUGHPUT -->", String.format("<div> %s | %s Bit</div><div>%.2f</div>", maxAlgoName, maxBitSize, maxThroughput))
                .replace("<!-- BENCHMARK_ROWS -->", rowsAndLegend);

        // HTML schreiben
        Files.writeString(Paths.get(outputPath), result);
        System.out.println(" HTML-Report erfolgreich erstellt unter: " + outputPath);
    }

    // Klassifizierungen
    private static String classifyPerformance(double throughput) {
        if (throughput > 1000) return "performance-high";
        if (throughput > 100) return "performance-medium";
        return "performance-low";
    }

    private static String classifyLatency(double latencyUs) {
        if (latencyUs < 100) return "latency-low";
        if (latencyUs < 1000) return "latency-medium";
        return "latency-high";
    }

    private static String classifyCpu(double cpu) {
        if (cpu < 30) return "cpu-low";
        if (cpu < 70) return "cpu-medium";
        return "cpu-high";
    }

    public static void main(String[] args) {
        try {
            generateReport(
                    "benchmark-results.json",
                    "src/main/resources/templates/report-template.html",
                    "benchmark-report.html"
            );
        } catch (IOException e) {
            System.err.println(" Fehler beim Erstellen des Reports: " + e.getMessage());
        }
    }
}
