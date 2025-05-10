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
 * Liest das JMH-JSON (benchmark-results.json) ein, berechnet abgeleitete Metriken und erzeugt einen HTML-Report.
 */
public class ReportGenerator {
    public static void main(String[] args) throws Exception {
        // Pfade
        String jmhJson = "benchmark-results.json";
        String outHtml = "report.html";

        // Jackson zum Parsen
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File(jmhJson));

        List<BenchmarkResult> results = new ArrayList<>();
        Iterator<JsonNode> benchmarks = root.get("benchmarks").elements();
        while (benchmarks.hasNext()) {
            JsonNode node = benchmarks.next();
            BenchmarkResult br = new BenchmarkResult();

            // Basisdaten aus JSON
            String implName = node.get("params").get("implName").asText();
            int bitSize = node.get("params").get("bitSize").asInt();
            double score = node.get("primaryMetric").get("score").asDouble();
            double scoreError = node.get("primaryMetric").get("scoreError").asDouble();

            br.setName(implName);
            br.setBitSize(bitSize);
            br.setScore(score);
            br.setScoreError(scoreError);

            // Perzentile auslesen
            JsonNode percentiles = node.get("primaryMetric").get("scorePercentiles");
            for (Map.Entry<String, JsonNode> entry : iterable(percentiles.fields())) {
                br.getScorePercentiles().put(entry.getKey(), entry.getValue().asDouble());
            }

            // Abgeleitete Metriken berechnen
            br.setOpsPerMs(score / 1000.0);
            br.setLatencyUs(1_000_000.0 / score);

            results.add(br);
        }

        // Thymeleaf konfigurieren
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);

        // Kontext füllen
        Context context = new Context();
        context.setVariable("results", results);
        context.setVariable("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        context.setVariable("architecture", System.getProperty("os.arch"));
        context.setVariable("osName", System.getProperty("os.name") + " " + System.getProperty("os.version"));

        // HTML rendern und speichern
        String htmlContent = engine.process("report", context);
        Files.write(Paths.get(outHtml), htmlContent.getBytes("UTF-8"));

        System.out.println("Report erstellt: " + outHtml);
    }

    // Helfer-Methode für Iterator -> Iterable
    private static <T> Iterable<T> iterable(Iterator<T> iterator) {
        return () -> iterator;
    }
}
