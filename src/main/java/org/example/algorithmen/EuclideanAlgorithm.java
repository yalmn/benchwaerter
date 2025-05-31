package org.example.algorithmen;

import org.example.benchmark.Benchmarkable;

import java.math.BigInteger;

public class EuclideanAlgorithm implements Benchmarkable {

    @Override
    public BigInteger execute(BigInteger... params) {
        BigInteger a = params[0];
        BigInteger b = params[1];
        return euclideanAlgorithm(a, b);
    }

    public static BigInteger euclideanAlgorithm(BigInteger a, BigInteger b) {
        while (!b.equals(BigInteger.ZERO)) {
            BigInteger temp = b;
            b = a.mod(b);
            a = temp;
        }
        return a;
    }
}
