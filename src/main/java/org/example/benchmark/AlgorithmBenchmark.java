package org.example.benchmark;

import org.example.algorithmen.FastExponential;
import org.example.algorithmen.EuclideanAlgorithm;
import org.openjdk.jmh.annotations.*;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * JMH-Benchmark-Klasse für die ALgoirthmen.
 * Misst Performance, Heap-Verbrauch und CPU-Auslastung für verschiedene Bit-Längen.
 */
@State(Scope.Benchmark)
public class AlgorithmBenchmark {

    /**
     * Hilfsklasse für sekundäre Kennzahlen (Heap und CPU).
     */
    @AuxCounters(AuxCounters.Type.EVENTS)
    @State(Scope.Thread)
    public static class Counters {
        /** Zugewiesene Heap-Differenz pro Invocation in Bytes */
        public double heapBytes;
        /** CPU-Last-Differenz pro Invocation in Prozent */
        public double cpuLoad;
    }

    @Param({"FastExponential", "EuclideanAlgorithm"})
    private String implName;

    @Param({"256", "512", "1024"})
    private int bitSize;

    private BigInteger a;
    private BigInteger b;
    private BigInteger mod;
    private SecureRandom random;
    private OperatingSystemMXBean osBean;

    @Setup
    public void setup() {
        random = new SecureRandom();
        a = new BigInteger(bitSize, random);
        b = new BigInteger(bitSize, random);
        mod = BigInteger.probablePrime(bitSize, random);
        osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    }

    @Benchmark
    public BigInteger messen(Counters counters) {
        // Vorher Metriken erfassen
        long beforeHeap = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        double beforeCpu = osBean.getProcessCpuLoad() * 100.0;

        // Algorithmus ausführen
        BigInteger result;
        switch (implName) {
            case "FastExponential":
                result = new FastExponential().execute(a, b, mod);
                break;
            case "EuclideanAlgorithm":
                result = new EuclideanAlgorithm().execute(a, b);
                break;
            default:
                throw new IllegalStateException("Unbekannte Implementierung: " + implName);
        }

        // Nachher Metriken erfassen
        long afterHeap = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        double afterCpu = osBean.getProcessCpuLoad() * 100.0;

        counters.heapBytes = afterHeap - beforeHeap;
        counters.cpuLoad = afterCpu - beforeCpu;

        return result;
    }
}