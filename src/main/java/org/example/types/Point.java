package org.example.types;

import java.math.BigInteger;

public abstract class Point {
    public abstract BigInteger getX();
    public abstract BigInteger getY();

    public abstract Point op(Point q, BigInteger n);
    public abstract String toString();
}
