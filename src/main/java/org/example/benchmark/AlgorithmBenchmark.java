package org.example.benchmark;

import org.example.algorithmen.*;
import org.openjdk.jmh.annotations.*;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.math.BigInteger;
import java.util.List;

@State(Scope.Benchmark)
public class AlgorithmBenchmark {

  @AuxCounters(AuxCounters.Type.EVENTS)
  @State(Scope.Thread)
  public static class Counters {
    public double heapBytes;
    public double cpuLoad;
  }

  @Param({
      "FastExponential",
      "EuclideanAlgorithm",
      "ExtendedEuclideanAlgorithm",
      "MillerRabin",
      "FastAdd",
      "SumOfTwoSquares",
      "ComputeOrderOfEllipticCurve",
      "SafePrimeNumber"
  })
  private String implName;

  // @Param({"16", "32", "64"})
  @Param({ "16", "32", "64" })
  private int bitSize;

  private Benchmarkable algorithm;
  private List<BigInteger> params;
  private OperatingSystemMXBean osBean;

  @Setup
  public void setup() {
    this.algorithm = getAlgorithmByName(implName);
    this.params = InputFactory.getInputs(implName, bitSize);
    this.osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
  }

  @Benchmark
  public BigInteger messen(Counters counters) {
    System.gc();
    long beforeHeap = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    long startTime = System.nanoTime();

    BigInteger result = algorithm.execute(params.toArray(new BigInteger[0]));

    long endTime = System.nanoTime();
    System.gc();
    long afterHeap = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

    counters.heapBytes = (afterHeap - beforeHeap) / 1024.0 / 1024.0; // MB
    counters.cpuLoad = (endTime - startTime) / 1000.0; // µs

    return result;
  }

  private Benchmarkable getAlgorithmByName(String name) {
    return switch (name) {
      case "FastExponential" -> new FastExponential();
      case "EuclideanAlgorithm" -> new EuclideanAlgorithm();
      case "ExtendedEuclideanAlgorithm" -> new ExtendedEuclideanAlgorithm();
      case "MillerRabin" -> new MillerRabin();
      case "FastAdd" -> new FastAdd();
      case "SumOfTwoSquares" -> new SumOfTwoSquares();
      case "ComputeOrderOfEllipticCurve" -> new ComputeOrderOfEllipticCurve();
      case "SafePrimeNumber" -> new SafePrimeNumber();
      default -> throw new IllegalArgumentException("Unbekannte Implementierung: " + name);
    };
  }
}
