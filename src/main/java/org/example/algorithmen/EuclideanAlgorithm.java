package org.example.algorithmen;

import org.example.benchmark.Benchmarkable;

import java.math.BigInteger;

public class EuclideanAlgorithm implements Benchmarkable {
    @Override
    public BigInteger execute(BigInteger... inputs) {
        if (inputs.length != 2) {
            throw new IllegalArgumentException("EuclideanAlgorithm erwartet zwei Parameter: a und b");
        }
        BigInteger a = inputs[0];
        BigInteger b = inputs[1];

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
