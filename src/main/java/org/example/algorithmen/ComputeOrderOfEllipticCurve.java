package org.example.algorithmen;

import org.example.benchmark.Benchmarkable;

import java.math.BigInteger;

/**
 * Berechnet die Ordnung einer elliptischen Kurve über einem Primkörper GF(p)
 * anhand der Summe von zwei Quadraten.
 *
 * Erwartet: p ≡ 1 mod 4 und prim.
 */
public class ComputeOrderOfEllipticCurve implements Benchmarkable {

    @Override
    public BigInteger execute(BigInteger... inputs) {
        if (inputs.length != 1) {
            throw new IllegalArgumentException("ComputeOrderOfEllipticCurve erwartet genau einen Parameter: eine Primzahl p ≡ 1 mod 4");
        }

        BigInteger p = inputs[0];
        return computeOrder(p);
    }

    @Override
    public String name() {
        return "ComputeOrderOfEllipticCurve";
    }

    public static BigInteger computeOrder(BigInteger p) {
        if (!p.mod(BigInteger.valueOf(4)).equals(BigInteger.ONE)) {
            throw new IllegalArgumentException("p muss kongruent 1 mod 4 sein");
        }

        BigInteger[] xy = SumOfTwoSquares.sumOfTwoSquares(p);
        BigInteger x = xy[0];
        BigInteger y = xy[1];

        if (!x.pow(2).add(y.pow(2)).equals(p)) {
            throw new IllegalArgumentException("Zerlegung in x und y fehlerhaft!");
        }

        BigInteger h;
        if (x.mod(BigInteger.valueOf(4)).equals(BigInteger.ZERO) && y.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
            h = BigInteger.valueOf(-2).multiply(y);
        } else if (x.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(2)) && y.mod(BigInteger.valueOf(4)).equals(BigInteger.ONE)) {
            h = BigInteger.valueOf(-2).multiply(y);
        } else if (x.mod(BigInteger.valueOf(4)).equals(BigInteger.ZERO) && y.mod(BigInteger.valueOf(4)).equals(BigInteger.ONE)) {
            h = BigInteger.valueOf(2).multiply(y);
        } else if (x.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(2)) && y.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
            h = BigInteger.valueOf(2).multiply(y);
        } else {
            throw new IllegalStateException("Fehlerhafte Kongruenzen bei x und y");
        }

        return p.add(BigInteger.ONE).subtract(h);
    }
}
