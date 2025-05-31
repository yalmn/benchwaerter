package org.example.algorithmen;

import org.example.benchmark.Benchmarkable;
import java.math.BigInteger;

public class SumOfTwoSquares implements Benchmarkable {
    @Override
    public BigInteger execute(BigInteger... params) {
        BigInteger p = params[0];
        BigInteger[] result = sumOfTwoSquares(p);
        return result[0].pow(2).add(result[1].pow(2)); // Rückgabe: p = x² + y²
    }

    public static BigInteger[] sumOfTwoSquares(BigInteger p) {
        if (!MillerRabin.millerRabin(p, 100)) {
            throw new IllegalArgumentException("p has to be prime.");
        }

        if (!p.mod(BigInteger.valueOf(4)).equals(BigInteger.ONE)) {
            throw new IllegalArgumentException("p can't represent as sum of two squares.");
        }

        BigInteger a = findSquareRootOfMinusOne(p);
        BigInteger b = BigInteger.ONE;

        BigInteger[] result = gaussianGGT(p, a, b);

        if (result[0].mod(BigInteger.valueOf(2)).equals(BigInteger.ONE)) {
            BigInteger temp = result[0];
            result[0] = result[1];
            result[1] = temp;
        }

        if (result[0].mod(BigInteger.valueOf(2)).equals(BigInteger.ONE) || result[1].mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
            throw new IllegalStateException("x has to be even. y has to be odd.");
        }

        return result;
    }

    private static BigInteger findSquareRootOfMinusOne(BigInteger p) {
        BigInteger r = BigInteger.TWO;
        BigInteger exp = p.subtract(BigInteger.ONE).divide(BigInteger.TWO);
        while (FastExponential.fastExponential(r, exp, p).equals(BigInteger.ONE)) {
            r = r.add(BigInteger.ONE);
        }
        return FastExponential.fastExponential(r, p.subtract(BigInteger.ONE).divide(BigInteger.valueOf(4)), p);
    }

    private static BigInteger[] gaussianGGT(BigInteger p, BigInteger x, BigInteger y) {
        BigInteger aReal = p;
        BigInteger aImag = BigInteger.ZERO;
        BigInteger bReal = x;
        BigInteger bImag = y;

        while (!(bReal.equals(BigInteger.ZERO) && bImag.equals(BigInteger.ZERO))) {
            BigInteger denominator = bReal.pow(2).add(bImag.pow(2));
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

        return new BigInteger[]{ aReal.abs(), aImag.abs() };
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
