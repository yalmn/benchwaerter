package org.example.benchmark;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

public class InputFactory {
    private static final SecureRandom random = new SecureRandom();

    public static List<BigInteger> getInputs(String implName, int bitSize) {
        BigInteger a, b, mod, lambda;

        switch (implName) {
            case "FastExponential":
                a = new BigInteger(bitSize, random);
                b = new BigInteger(bitSize, random);
                mod = BigInteger.probablePrime(bitSize, random);
                return List.of(a, b, mod);

            case "EuclideanAlgorithm":
            case "ExtendedEuclideanAlgorithm":
                a = new BigInteger(bitSize, random);
                b = new BigInteger(bitSize, random);
                return List.of(a, b);

            case "MillerRabin":
                a = BigInteger.probablePrime(bitSize, random);
                return List.of(a);

            case "FastAdd":
                BigInteger x, y, n;
                do {
                    x = new BigInteger(bitSize, random);
                    y = new BigInteger(bitSize, random);
                    n = BigInteger.probablePrime(bitSize, random);
                } while (
                        x.equals(BigInteger.ZERO) ||
                                y.equals(BigInteger.ZERO) ||
                                x.equals(y) ||
                                x.subtract(y).gcd(n).compareTo(BigInteger.ONE) != 0
                );
                BigInteger k = BigInteger.valueOf(50);
                return List.of(x, y, k, n);

            case "SumOfTwoSquares":
            case "ComputeOrderOfEllipticCurve":
                BigInteger p = BigInteger.probablePrime(bitSize, random);
                while (!p.mod(BigInteger.valueOf(4)).equals(BigInteger.ONE)) {
                    p = BigInteger.probablePrime(bitSize, random);
                }
                return List.of(p);

            case "SafePrimeNumber":
                return List.of(BigInteger.valueOf(bitSize));

            default:
                throw new IllegalArgumentException("Unbekannter Algorithmusname: " + implName);
        }
    }
}
