package org.example.algorithmen;

import org.example.benchmark.Benchmarkable;

import java.math.BigInteger;
import java.security.SecureRandom;

public class SafePrimeNumber implements Benchmarkable {

    @Override
    public BigInteger execute(BigInteger... params) {
        int bitLength = params[0].intValue();
        int iterations = 100; // fest für Benchmark

        BigInteger[] result = safePrimeNumber(bitLength, iterations);
        return result[0]; // Rückgabe nur der Safe Prime
    }

    public static BigInteger[] safePrimeNumber(int bitLength, int iterations) {
        SecureRandom secureRandom = new SecureRandom();
        BigInteger two = BigInteger.TWO;

        while (true) {
            BigInteger q = BigInteger.probablePrime(bitLength - 1, secureRandom);
            BigInteger p = q.multiply(two).add(BigInteger.ONE);

            if (MillerRabin.millerRabin(p, iterations)) {
                BigInteger g = findPrimitiveRoot(p, q, bitLength, secureRandom);
                if (g != null) {
                    return new BigInteger[] { p, g };
                }
            }
        }
    }

    private static BigInteger findPrimitiveRoot(BigInteger p, BigInteger q, int bitLength, SecureRandom random) {
        BigInteger g;
        for (int i = 0; i < 100; i++) {
            g = new BigInteger(bitLength, random).mod(p);
            if (!g.equals(BigInteger.ZERO) && FastExponential.fastExponential(g, q, p).equals(p.subtract(BigInteger.ONE))) {
                return g;
            }
        }
        return null;
    }
}
