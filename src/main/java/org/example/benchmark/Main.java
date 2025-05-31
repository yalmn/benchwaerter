package org.example.benchmark;

import org.example.report.ReportGenerator;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.results.format.ResultFormatType;

/**
 * Haupt-Klasse zum Ausführen der Benchmarks per JMH und Erzeugen einer
 * JSON-Ausgabe.
 */
public class Main {
  public static void main(String[] args) throws Exception {
    Options opt = new OptionsBuilder()
        .include(AlgorithmBenchmark.class.getSimpleName())
        .forks(1)
        // .warmupIterations(3)
        // .measurementIterations(5)
        .warmupIterations(3)
        .measurementIterations(5)
        .result("benchmark-results.json")
        .resultFormat(ResultFormatType.JSON)
        .build();
    new Runner(opt).run();
    System.out.println("Benchmark beendet. Ergebnisse in benchmark-results.json");
  }
}
