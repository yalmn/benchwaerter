package org.example.algorithmen;

import org.example.benchmark.Benchmarkable;

import java.math.BigInteger;

public class SumOfTwoSquares implements Benchmarkable {

    @Override
    public BigInteger execute(BigInteger... inputs) {
        if (inputs.length != 1) {
            throw new IllegalArgumentException("SumOfTwoSquares erwartet genau einen Parameter: eine Primzahl p ≡ 1 mod 4");
        }

        BigInteger p = inputs[0];
        BigInteger[] result = sumOfTwoSquares(p);
        return result[0].multiply(result[0]).add(result[1].multiply(result[1])); // x² + y² = p
    }

    @Override
    public String name() {
        return "SumOfTwoSquares";
    }

    public static BigInteger[] sumOfTwoSquares(BigInteger p) {
        if (!MillerRabin.millerRabin(p, 100)) {
            throw new IllegalArgumentException("p muss prim sein.");
        }

        if (!p.mod(BigInteger.valueOf(4)).equals(BigInteger.ONE)) {
            throw new IllegalArgumentException("p ≡ 1 mod 4 ist erforderlich.");
        }

        BigInteger a = findSquareRootOfMinusOne(p);
        BigInteger b = BigInteger.ONE;
        BigInteger[] result = gaussianGGT(p, a, b);

        if (result[0].mod(BigInteger.TWO).equals(BigInteger.ONE)) {
            BigInteger tmp = result[0];
            result[0] = result[1];
            result[1] = tmp;
        }

        if (result[0].mod(BigInteger.TWO).equals(BigInteger.ONE) || result[1].mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            throw new IllegalStateException("x muss gerade, y ungerade sein.");
        }

        return result;
    }

    private static BigInteger findSquareRootOfMinusOne(BigInteger p) {
        BigInteger r = BigInteger.TWO;
        BigInteger exp = p.subtract(BigInteger.ONE).divide(BigInteger.TWO);
        while (fastExponential(r, exp, p).equals(BigInteger.ONE)) {
            r = r.add(BigInteger.ONE);
        }
        return fastExponential(r, p.subtract(BigInteger.ONE).divide(BigInteger.valueOf(4)), p);
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

    private static BigInteger[] gaussianGGT(BigInteger p, BigInteger x, BigInteger y) {
        BigInteger aReal = p;
        BigInteger aImag = BigInteger.ZERO;
        BigInteger bReal = x;
        BigInteger bImag = y;

        while (!(bReal.equals(BigInteger.ZERO) && bImag.equals(BigInteger.ZERO))) {
            BigInteger denominator = bReal.multiply(bReal).add(bImag.multiply(bImag));
            BigInteger numeratorReal = aReal.multiply(bReal).add(aImag.multiply(bImag));
            BigInteger numeratorImag = aImag.multiply(bReal).subtract(aReal.multiply(bImag));
            BigInteger qReal = roundDiv(numeratorReal, denominator);
            BigInteger qImag = roundDiv(numeratorImag, denominator);

            BigInteger prodReal = qReal.multiply(bReal).subtract(qImag.multiply(bImag));
            BigInteger prodImag = qReal.multiply(bImag).add(qImag.multiply(bReal));

            BigInteger rReal = aReal.subtract(prodReal);
            BigInteger rImag = aImag.subtract(prodImag);

            aReal = bReal;
            aImag = bImag;
            bReal = rReal;
            bImag = rImag;
        }

        return new BigInteger[]{aReal.abs(), aImag.abs()};
    }

    private static BigInteger roundDiv(BigInteger numerator, BigInteger denominator) {
        BigInteger two = BigInteger.valueOf(2);
        BigInteger adjusted = numerator.multiply(two).add(denominator);
        return floorDiv(adjusted, denominator.multiply(two));
    }

    private static BigInteger floorDiv(BigInteger x, BigInteger y) {
        BigInteger[] dr = x.divideAndRemainder(y);
        BigInteger quotient = dr[0];
        BigInteger remainder = dr[1];
        if (x.signum() < 0 && !remainder.equals(BigInteger.ZERO)) {
            quotient = quotient.subtract(BigInteger.ONE);
        }
        return quotient;
    }
}
