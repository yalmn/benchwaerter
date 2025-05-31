package org.example.algorithmen;

import org.example.benchmark.Benchmarkable;
import org.example.types.Point;
import org.example.types.PointAtInfinity;

import java.math.BigInteger;

public class FastAdd implements Benchmarkable {

    @Override
    public BigInteger execute(BigInteger... params) {
        BigInteger x = params[0];
        BigInteger y = params[1];
        BigInteger k = params[2];
        BigInteger n = params[3];

        Point p = new SimplePoint(x, y);
        Point result = add(p, k, n);

        return result.getX(); // oder getY(), je nachdem was du messen willst
    }

    public static Point add(Point p, BigInteger k, BigInteger n, Point result) {
        if (k.signum() <= 0)
            return result;
        else if (k.mod(BigInteger.valueOf(2)).equals(BigInteger.ONE)) {
            return add(p.op(p, n), k.divide(BigInteger.valueOf(2)), n, result.op(p, n));
        } else {
            return add(p.op(p, n), k.divide(BigInteger.valueOf(2)), n, result);
        }
    }

    public static Point add(Point p, BigInteger k, BigInteger n) {
        return add(p, k, n, new PointAtInfinity());
    }

    // sichere Punktklasse mit modInverse-Schutz
    static class SimplePoint extends Point {
        private final BigInteger x;
        private final BigInteger y;

        public SimplePoint(BigInteger x, BigInteger y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public BigInteger getX() {
            return x;
        }

        @Override
        public BigInteger getY() {
            return y;
        }

        @Override
        public Point op(Point q, BigInteger n) {
            BigInteger dx = q.getX().subtract(this.x);
            BigInteger dy = q.getY().subtract(this.y);

            // schütze gegen modInverse-Fehler
            if (dx.gcd(n).compareTo(BigInteger.ONE) != 0) {
                return new PointAtInfinity();
            }

            try {
                BigInteger lambda = dx.modInverse(n).multiply(dy).mod(n);
                BigInteger xr = lambda.pow(2).subtract(this.x).subtract(q.getX()).mod(n);
                BigInteger yr = lambda.multiply(this.x.subtract(xr)).subtract(this.y).mod(n);
                return new SimplePoint(xr, yr);
            } catch (ArithmeticException e) {
                return new PointAtInfinity();
            }
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
}
