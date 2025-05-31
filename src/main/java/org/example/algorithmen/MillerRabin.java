package org.example.algorithmen;

import org.example.benchmark.Benchmarkable;

import java.math.BigInteger;
import java.security.SecureRandom;

public class MillerRabin implements Benchmarkable {
    @Override
    public BigInteger execute(BigInteger... params) {
        BigInteger n = params[0];
        int t = 100;

        boolean result = millerRabin(n, t);
        return result ? BigInteger.ONE : BigInteger.ZERO;
    }

    public static boolean millerRabin(BigInteger n, int t) {
        if (n.equals(BigInteger.ZERO) || n.equals(BigInteger.ONE))
            return false;
        else if (n.equals(BigInteger.TWO))
            return true;

        BigInteger d = n.subtract(BigInteger.ONE);
        BigInteger s = BigInteger.ZERO;
        SecureRandom random = new SecureRandom();

        while (d.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            d = d.divide(BigInteger.TWO);
            s = s.add(BigInteger.ONE);
        }

        if (s.equals(BigInteger.ZERO))
            return false;

        for (int i = 0; i < t; i++) {
            boolean isPrime = false;
            BigInteger a = BigInteger.ZERO;
            while (!(a.gcd(n).equals(BigInteger.ONE)))
                a = new BigInteger(1024, random);
            for (int r = 0; r < s.intValue(); r++) {
                BigInteger result = FastExponential.fastExponential(a, power(BigInteger.TWO, r).multiply(d), n);
                if (result.equals(BigInteger.ONE) || BigInteger.valueOf(-1).equals(result.subtract(n)))
                    isPrime = true;
            }
            if (!isPrime)
                return false;
        }
        return true;
    }

    protected static BigInteger power(BigInteger base, int exponent) {
        if (base.equals(BigInteger.ZERO))
            return BigInteger.ZERO;
        else if (base.equals(BigInteger.ONE) || exponent == 0)
            return BigInteger.ONE;

        BigInteger result = base;
        for (int i = 1; i < exponent; i++)
            result = result.multiply(base);
        return result;
    }
}
