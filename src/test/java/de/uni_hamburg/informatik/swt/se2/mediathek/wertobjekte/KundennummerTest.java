package de.uni_hamburg.informatik.swt.se2.mediathek.wertobjekte;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author SE2-Team
 * @version SoSe 2021
 * 
 */
public class KundennummerTest
{

    @Test
    public void testEqualsUndHashcode()
    {
        Kundennummer kundennummer1 = new Kundennummer(123456);
        assertEquals("123456", kundennummer1.toString());

        Kundennummer kundennummer2 = new Kundennummer(123456);
        assertEquals(kundennummer1, kundennummer2);
        assertEquals(kundennummer1.hashCode(), kundennummer2.hashCode());

        Kundennummer kundennummer3 = new Kundennummer(654321);
        assertNotEquals(kundennummer1, kundennummer3);
        assertNotEquals(kundennummer1.hashCode(), kundennummer3.hashCode());
    }

    @Test
    public void testIstGueltig()
    {
        assertTrue(Kundennummer.istGueltig(111111));
        assertFalse(Kundennummer.istGueltig(0));
        assertFalse(Kundennummer.istGueltig(1111111));
    }
}
