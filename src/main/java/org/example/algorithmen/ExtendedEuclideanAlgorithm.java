package org.example.algorithmen;

import org.example.benchmark.Benchmarkable;

import java.math.BigInteger;

public class ExtendedEuclideanAlgorithm implements Benchmarkable {

    @Override
    public BigInteger execute(BigInteger... params) {
        BigInteger a = params[0];
        BigInteger b = params[1];
        return extendedGCD(a, b)[0]; // Nur ggT als Benchmark-Rückgabe
    }

    public static BigInteger[] extendedGCD(BigInteger a, BigInteger b) {
        BigInteger x0 = BigInteger.ONE, y0 = BigInteger.ZERO;
        BigInteger x1 = BigInteger.ZERO, y1 = BigInteger.ONE;

        while (!b.equals(BigInteger.ZERO)) {
            BigInteger[] divmod = a.divideAndRemainder(b);
            BigInteger q = divmod[0];
            BigInteger r = divmod[1];

            BigInteger tempX = x0.subtract(q.multiply(x1));
            BigInteger tempY = y0.subtract(q.multiply(y1));

            a = b;
            b = r;
            x0 = x1;
            y0 = y1;
            x1 = tempX;
            y1 = tempY;
        }

        return new BigInteger[]{ a, x0, y0 };
    }
}
