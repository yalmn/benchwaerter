package org.example.algorithmen;

import org.example.benchmark.Benchmarkable;
import org.example.types.Point;
import org.example.types.PointAtInfinity;

import java.math.BigInteger;

public class FastAdd implements Benchmarkable {

    @Override
    public BigInteger execute(BigInteger... inputs) {
        if (inputs.length != 4) {
            throw new IllegalArgumentException("FastAdd erwartet vier Parameter: x, y, k, n");
        }

        BigInteger x = inputs[0];
        BigInteger y = inputs[1];
        BigInteger k = inputs[2];
        BigInteger n = inputs[3];

        Point p = new SimplePoint(x, y);
        Point result = add(p, k, n);

        return result.getX();
    }

    @Override
    public String name() {
        return "FastAdd";
    }

    private Point add(Point p, BigInteger k, BigInteger n, Point result) {
        if (k.signum() <= 0) return result;

        if (k.testBit(0)) {
            return add(p.op(p, n), k.shiftRight(1), n, result.op(p, n));
        } else {
            return add(p.op(p, n), k.shiftRight(1), n, result);
        }
    }

    private Point add(Point p, BigInteger k, BigInteger n) {
        return add(p, k, n, new PointAtInfinity());
    }

    private static class SimplePoint extends Point {
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
            BigInteger newX = this.x.add(q.getX()).mod(n);
            BigInteger newY = this.y.add(q.getY()).mod(n);
            return new SimplePoint(newX, newY);
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
}
