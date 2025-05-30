package org.example.report;

import java.util.Map;
import java.util.LinkedHashMap;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * DTO für die Benchmark-Ergebnisse inkl. abgeleiteter Metriken und formatierter Strings.
 */
public class BenchmarkResult {
    private static final DecimalFormat formatter;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMANY);
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        formatter = new DecimalFormat("#,##0.0000", symbols);
    }

    private String name;
    private int bitSize;
    private double score;
    private double scoreError;
    private Map<String, Double> scorePercentiles;

    private double opsPerMs;
    private double latencyUs;
    private double heapMb;
    private double cpuLoad;

    // Formatierte Felder (für HTML-Ausgabe)
    public String getFormattedScore() { return format(score); }
    public String getFormattedScoreError() { return format(scoreError); }
    public String getFormattedOpsPerMs() { return format(opsPerMs); }
    public String getFormattedLatencyUs() { return format(latencyUs); }
    public String getFormattedHeapMb() { return format(heapMb); }
    public String getFormattedCpuLoad() { return format(cpuLoad); }

    private String format(double value) {
        return formatter.format(value);
    }

    public BenchmarkResult() {
        scorePercentiles = new LinkedHashMap<>();
    }

    // Getter und Setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getBitSize() { return bitSize; }
    public void setBitSize(int bitSize) { this.bitSize = bitSize; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public double getScoreError() { return scoreError; }
    public void setScoreError(double scoreError) { this.scoreError = scoreError; }

    public Map<String, Double> getScorePercentiles() { return scorePercentiles; }
    public void setScorePercentiles(Map<String, Double> scorePercentiles) {
        this.scorePercentiles = scorePercentiles;
    }

    public double getOpsPerMs() { return opsPerMs; }
    public void setOpsPerMs(double opsPerMs) { this.opsPerMs = opsPerMs; }

    public double getLatencyUs() { return latencyUs; }
    public void setLatencyUs(double latencyUs) { this.latencyUs = latencyUs; }

    public double getHeapMb() { return heapMb; }
    public void setHeapMb(double heapMb) { this.heapMb = heapMb; }

    public double getCpuLoad() { return cpuLoad; }
    public void setCpuLoad(double cpuLoad) { this.cpuLoad = cpuLoad; }
}
