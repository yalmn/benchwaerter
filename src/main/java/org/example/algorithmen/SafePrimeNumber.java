package org.example.algorithmen;

import org.example.benchmark.Benchmarkable;

import java.math.BigInteger;
import java.security.SecureRandom;

public class SafePrimeNumber implements Benchmarkable {

    private final FastExponential expAlgo = new FastExponential();

    @Override
    public BigInteger execute(BigInteger... inputs) {
        int bitLength = (inputs.length > 0) ? inputs[0].intValue() : 512;
        int iterations = (inputs.length > 1) ? inputs[1].intValue() : 5;

        return generateSafePrime(bitLength, iterations);
    }

    @Override
    public String name() {
        return "SafePrimeNumber";
    }

    private BigInteger generateSafePrime(int bitLength, int iterations) {
        SecureRandom secureRandom = new SecureRandom();
        BigInteger two = BigInteger.TWO;

        while (true) {
            BigInteger q = BigInteger.probablePrime(bitLength - 1, secureRandom);
            BigInteger p = q.multiply(two).add(BigInteger.ONE);

            if (MillerRabin.millerRabin(p, iterations)) {
                BigInteger g = findPrimitiveRoot(p, q, bitLength, secureRandom);
                if (g != null) {
                    return p;
                }
            }
        }
    }

    private BigInteger findPrimitiveRoot(BigInteger p, BigInteger q, int bitLength, SecureRandom random) {
        for (int i = 0; i < 100; i++) {
            BigInteger g = new BigInteger(bitLength, random).mod(p);
            if (!g.equals(BigInteger.ZERO)) {
                BigInteger result = expAlgo.execute(g, q, p);
                if (result.equals(p.subtract(BigInteger.ONE))) {
                    return g;
                }
            }
        }
        return null;
    }
}
