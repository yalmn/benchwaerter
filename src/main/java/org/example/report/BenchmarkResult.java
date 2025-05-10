package org.example.report;

import java.util.Map;
import java.util.LinkedHashMap;

/**
 * DTO für die Benchmark-Ergebnisse inkl. abgeleiteter Metriken.
 */
public class BenchmarkResult {
    private String name;
    private int bitSize;
    private double score;           // ops/s
    private double scoreError;      // ± ops/s
    private Map<String, Double> scorePercentiles; // z.B. T50, T90

    // Abgeleitete Metriken
    private double opsPerMs;
    private double latencyUs;

    // Neue Metriken
    private double heapMb;     // Heap-Verbrauch in MB
    private double cpuLoad;    // CPU-Auslastung in Prozent

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
