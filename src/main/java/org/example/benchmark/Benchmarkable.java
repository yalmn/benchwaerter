package org.example.benchmark;

import java.math.BigInteger;

public interface Benchmarkable {
    BigInteger execute(BigInteger... params);
}
