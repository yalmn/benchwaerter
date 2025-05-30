package org.example.benchmark;

import org.example.algorithmen.*;

import org.openjdk.jmh.annotations.*;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * JMH-Benchmark-Klasse für die Algorithmen.
 * Misst Performance, Heap-Verbrauch und CPU-Auslastung für verschiedene Bit-Längen.
 */
@State(Scope.Benchmark)
public class AlgorithmBenchmark {

    @AuxCounters(AuxCounters.Type.EVENTS)
    @State(Scope.Thread)
    public static class Counters {
        /** Zugewiesene Heap-Differenz pro Invocation in Bytes */
        public double heapBytes;
        /** CPU-Last-Differenz pro Invocation in Prozent */
        public double cpuLoad;
    }

    @Param({
            "FastExponential",
            "EuclideanAlgorithm",
            "MillerRabin",
            "ExtendedEuclideanAlgorithm",
            "FastAdd",
            "SumOfTwoSquares",
            "ComputeOrderOfEllipticCurve"
    })
    private String implName;

    // @Param({"256", "512", "1024"})
    @Param({"64"})
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
        long beforeHeap = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        double beforeCpu = osBean.getProcessCpuLoad() * 100.0;

        BigInteger result;
        switch (implName) {
            case "FastExponential":
                result = new FastExponential().execute(a, b, mod);
                break;
            case "EuclideanAlgorithm":
                result = new EuclideanAlgorithm().execute(a, b);
                break;
            case "ExtendedEuclideanAlgorithm":
                result = new ExtendedEuclideanAlgorithm().execute(a, b);
                break;
            case "MillerRabin":
                result = new MillerRabin().execute(a);
                break;
            case "FastAdd":
                result = new FastAdd().execute(a, b, BigInteger.valueOf(100), mod);
                break;
            case "SumOfTwoSquares":
            case "ComputeOrderOfEllipticCurve":
                BigInteger primeP = BigInteger.probablePrime(bitSize, random);
                while (!primeP.mod(BigInteger.valueOf(4)).equals(BigInteger.ONE)) {
                    primeP = BigInteger.probablePrime(bitSize, random);
                }
                if (implName.equals("SumOfTwoSquares")) {
                    result = new SumOfTwoSquares().execute(primeP);
                } else {
                    result = new ComputeOrderOfEllipticCurve().execute(primeP);
                }
                break;
            default:
                throw new IllegalStateException("Unbekannte Implementierung: " + implName);
        }

        long afterHeap = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        double afterCpu = osBean.getProcessCpuLoad() * 100.0;

        counters.heapBytes = afterHeap - beforeHeap;
        counters.cpuLoad = afterCpu - beforeCpu;

        return result;
    }
}
