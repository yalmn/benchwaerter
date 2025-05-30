package org.example.types;

import java.math.BigInteger;

public class PointAtInfinity extends Point {

    @Override
    public BigInteger getX() {
        return BigInteger.ZERO;
    }

    @Override
    public BigInteger getY() {
        return BigInteger.ZERO;
    }

    @Override
    public Point op(Point q, BigInteger n) {
        return q;
    }

    @Override
    public String toString() {
        return "O";
    }
}
