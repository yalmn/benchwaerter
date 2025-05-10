package org.example.algorithmen;

import org.example.benchmark.Benchmarkable;

import java.math.BigInteger;

public class FastExponential implements Benchmarkable {
    @Override
    public BigInteger execute(BigInteger... inputs) {
        if (inputs.length != 3) {
            throw new IllegalArgumentException("FastExponential erwartet drei Parameter: Basis, Exponent, Modulus");
        }
        BigInteger base = inputs[0];
        BigInteger exponent = inputs[1];
        BigInteger mod = inputs[2];

        if (mod.signum() < 1) {
            throw new IllegalArgumentException("Modulus muss positiv sein.");
        }
        BigInteger result = BigInteger.ONE;
        base = base.mod(mod);

        while (exponent.compareTo(BigInteger.ZERO) > 0) {
            if (exponent.testBit(0)) {
                result = result.multiply(base).mod(mod);
            }
            exponent = exponent.shiftRight(1);
            base = base.multiply(base).mod(mod);
        }

        return result;
    }

    @Override
    public String name() {
        return "FastExponential";
    }
}
