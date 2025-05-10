package org.example.report;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Liest das JMH-JSON (benchmark-results.json) ein, berechnet abgeleitete Metriken
 * inklusive Heap-Verbrauch und CPU-Auslastung, und erzeugt einen HTML-Report.
 */
public class ReportGenerator {
    public static void main(String[] args) throws Exception {
        String jmhJson = "benchmark-results.json";
        String outHtml = "report.html";

        // JSON mit Jackson einlesen
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File(jmhJson));

        List<BenchmarkResult> results = new ArrayList<>();
        Iterator<JsonNode> nodes;
        if (root.isArray()) {
            nodes = root.elements();
        } else if (root.has("benchmarks")) {
            nodes = root.get("benchmarks").elements();
        } else {
            throw new IllegalStateException("Unbekanntes JSON-Format: Weder Array noch 'benchmarks'-Feld vorhanden.");
        }

        // Durchlaufe alle Benchmark-Einträge
        while (nodes.hasNext()) {
            JsonNode node = nodes.next();
            BenchmarkResult br = new BenchmarkResult();

            // Basisdaten aus 'params'
            JsonNode params = node.get("params");
            br.setName(params.get("implName").asText());
            br.setBitSize(params.get("bitSize").asInt());

            // Primäre Metriken aus 'primaryMetric'
            JsonNode pm = node.get("primaryMetric");
            double score = pm.get("score").asDouble();
            br.setScore(score);
            br.setScoreError(pm.get("scoreError").asDouble());

            // Abgeleitete Metriken
            br.setOpsPerMs(score / 1000.0);
            br.setLatencyUs(1_000_000.0 / score);

            // Sekundäre Metriken aus 'secondaryMetrics'
            JsonNode secondary = node.get("secondaryMetrics");
            if (secondary != null) {
                // Heap in Bytes unter 'heapBytes'
                JsonNode heapNode = secondary.get("heapBytes");
                if (heapNode != null && heapNode.has("score")) {
                    double bytes = heapNode.get("score").asDouble();
                    br.setHeapMb(bytes / (1024.0 * 1024.0));
                }
                // CPU-Last in Prozent unter 'cpuLoad'
                JsonNode cpuNode = secondary.get("cpuLoad");
                if (cpuNode != null && cpuNode.has("score")) {
                    br.setCpuLoad(cpuNode.get("score").asDouble());
                }
            }

            results.add(br);
        }

        // Thymeleaf-Template konfigurieren
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);

        // Template-Kontext füllen
        Context context = new Context();
        context.setVariable("results", results);
        context.setVariable("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        context.setVariable("architecture", System.getProperty("os.arch"));
        context.setVariable("osName", System.getProperty("os.name") + " " + System.getProperty("os.version"));

        // HTML rendern und speichern
        String html = engine.process("report", context);
        Files.write(Paths.get(outHtml), html.getBytes("UTF-8"));

        System.out.println("Report erstellt: " + outHtml);
    }
}
