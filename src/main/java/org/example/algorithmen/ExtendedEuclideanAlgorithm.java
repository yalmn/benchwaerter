package org.example.algorithmen;

import org.example.benchmark.Benchmarkable;

import java.math.BigInteger;

public class ExtendedEuclideanAlgorithm implements Benchmarkable {

    @Override
    public BigInteger execute(BigInteger... inputs) {
        if (inputs.length != 2) {
            throw new IllegalArgumentException("ExtendedEuclideanAlgorithm erwartet zwei Parameter: a und b");
        }

        BigInteger a = inputs[0];
        BigInteger b = inputs[1];

        BigInteger[] result = extendedGCD(a, b);

        return result[0];
    }

    @Override
    public String name() {
        return "ExtendedEuclideanAlgorithm";
    }

    private static BigInteger[] extendedGCD(BigInteger a, BigInteger b) {
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

        return new BigInteger[]{a, x0, y0};
    }
}
