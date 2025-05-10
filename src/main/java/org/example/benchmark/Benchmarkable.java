package org.example.benchmark;

import java.math.BigInteger;

/**
 * Gemeinsame Schnittstelle für alle benchmark-fähigen Algorithmen.
 */
public interface Benchmarkable {
    /**
     * Führt den Algorithmus mit den übergebenen Eingaben aus.
     * @param inputs Eingabe-Parameter für den Algorithmus (z.B. Basis, Exponent, Modulus)
     * @return Ergebnis der Berechnung zur Validierung
     */
    BigInteger execute(BigInteger... inputs);

    /**
     * Eindeutiger Name des Algorithmus (z.B. "FastExponential").
     * @return Name des Algorithmus
     */
    String name();
}
