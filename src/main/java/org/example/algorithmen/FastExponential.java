package org.example.algorithmen;

import org.example.benchmark.Benchmarkable;

import java.math.BigInteger;

public class FastExponential implements Benchmarkable {

    @Override
    public BigInteger execute(BigInteger... params) {
        BigInteger base = params[0];
        BigInteger exponent = params[1];
        BigInteger mod = params[2];
        return fastExponential(base, exponent, mod);
    }

    public static BigInteger fastExponential(BigInteger base, BigInteger exponent, BigInteger mod) {
        if (mod.signum() < 1) {
            throw new IllegalArgumentException("Attempted to calculate fastExponential with negative modulus.");
        }

        BigInteger result = BigInteger.ONE;
        base = base.mod(mod);

        while (exponent.compareTo(BigInteger.ZERO) > 0) {
            if (exponent.mod(BigInteger.TWO).equals(BigInteger.ONE)) {
                result = result.multiply(base).mod(mod);
            }
            exponent = exponent.divide(BigInteger.TWO);
            base = base.multiply(base).mod(mod);
        }

        return result;
    }
}
