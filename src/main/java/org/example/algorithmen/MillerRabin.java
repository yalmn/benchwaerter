package org.example.algorithmen;

import org.example.benchmark.Benchmarkable;

import java.math.BigInteger;
import java.security.SecureRandom;

public class MillerRabin implements Benchmarkable {

    private static final int ITERATIONS = 100;

    @Override
    public BigInteger execute(BigInteger... inputs) {
        if (inputs.length != 1) {
            throw new IllegalArgumentException("MillerRabin erwartet genau einen Parameter: n");
        }

        BigInteger n = inputs[0];
        boolean isPrime = millerRabin(n, ITERATIONS);
        return isPrime ? BigInteger.ONE : BigInteger.ZERO;
    }

    public static boolean millerRabin(BigInteger n, int t) {
        if (n.equals(BigInteger.ZERO) || n.equals(BigInteger.ONE)) return false;
        if (n.equals(BigInteger.TWO)) return true;

        BigInteger d = n.subtract(BigInteger.ONE);
        BigInteger s = BigInteger.ZERO;
        SecureRandom random = new SecureRandom();

        while (d.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            d = d.divide(BigInteger.TWO);
            s = s.add(BigInteger.ONE);
        }

        if (s.equals(BigInteger.ZERO)) return false;

        for (int i = 0; i < t; i++) {
            boolean isPrime = false;
            BigInteger a;
            do {
                a = new BigInteger(n.bitLength(), random);
            } while (!a.gcd(n).equals(BigInteger.ONE) || a.compareTo(BigInteger.TWO) < 0 || a.compareTo(n.subtract(BigInteger.TWO)) > 0);

            for (int r = 0; r < s.intValue(); r++) {
                BigInteger exponent = power(BigInteger.TWO, r).multiply(d);
                BigInteger result = fastExponential(a, exponent, n);
                if (result.equals(BigInteger.ONE) || result.equals(n.subtract(BigInteger.ONE))) {
                    isPrime = true;
                    break;
                }
            }

            if (!isPrime) return false;
        }

        return true;
    }

    private static BigInteger fastExponential(BigInteger base, BigInteger exponent, BigInteger modulus) {
        BigInteger result = BigInteger.ONE;
        base = base.mod(modulus);

        while (exponent.compareTo(BigInteger.ZERO) > 0) {
            if (exponent.testBit(0)) {
                result = result.multiply(base).mod(modulus);
            }
            exponent = exponent.shiftRight(1);
            base = base.multiply(base).mod(modulus);
        }

        return result;
    }

    private static BigInteger power(BigInteger base, int exponent) {
        if (base.equals(BigInteger.ZERO)) return BigInteger.ZERO;
        if (base.equals(BigInteger.ONE) || exponent == 0) return BigInteger.ONE;

        BigInteger result = base;
        for (int i = 1; i < exponent; i++) {
            result = result.multiply(base);
        }
        return result;
    }

    @Override
    public String name() {
        return "MillerRabin";
    }
}
