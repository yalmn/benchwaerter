package org.example.algorithmen;

import org.example.benchmark.Benchmarkable;

import java.math.BigInteger;

public class EuclideanAlgorithm implements Benchmarkable {

    @Override
    public BigInteger execute(BigInteger... inputs) {
        if (inputs.length != 2) {
            throw new IllegalArgumentException("EuclideanAlgorithm erwartet zwei Parameter: a und b");
        }
        return computeGCD(inputs[0], inputs[1]);
    }

    /**
     * Statische Methode zur Berechnung des größten gemeinsamen Teilers (ggT)
     *
     * @param a Erste Zahl
     * @param b Zweite Zahl
     * @return Der ggT von a und b
     */
    public static BigInteger computeGCD(BigInteger a, BigInteger b) {
        while (!b.equals(BigInteger.ZERO)) {
            BigInteger temp = b;
            b = a.mod(b);
            a = temp;
        }
        return a;
    }

    @Override
    public String name() {
        return "EuclideanAlgorithm";
    }
}

