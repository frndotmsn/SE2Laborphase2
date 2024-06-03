/**
 * Beobachter nach dem Observer-Pattern
 * 
 * @author Fernando Thomsen Canales
 * @version 0.0.0 - 03.06.2024
 */
package de.uni_hamburg.informatik.swt.se2.kino.util;

/**
 * Beobachtet Beobachtbares
 */
public interface Beobachter
{
    /**
     * Was passieren soll, wenn ne Änderung geschieht
     */
    void reagiereAufAenderung();
}
