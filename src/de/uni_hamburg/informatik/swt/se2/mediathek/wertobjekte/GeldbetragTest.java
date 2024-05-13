package de.uni_hamburg.informatik.swt.se2.mediathek.wertobjekte;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author SE2-Team
 * @version SoSe 2021
 * 
 */
public class GeldbetragTest
{

    @Test
    public final void testGeldbetrag()
    {
        Geldbetrag betrag = new Geldbetrag(100);
        assertEquals(1, betrag.getEuroAnteil());
        assertEquals(0, betrag.getCentAnteil());
        assertEquals("1,00", betrag.getFormatiertenString());

        betrag = new Geldbetrag(0);
        assertEquals(0, betrag.getEuroAnteil());
        assertEquals(0, betrag.getCentAnteil());
        assertEquals("0,00", betrag.getFormatiertenString());

        betrag = new Geldbetrag(99);
        assertEquals(0, betrag.getEuroAnteil());
        assertEquals(99, betrag.getCentAnteil());
        assertEquals("0,99", betrag.getFormatiertenString());

        betrag = new Geldbetrag(101);
        assertEquals(1, betrag.getEuroAnteil());
        assertEquals(1, betrag.getCentAnteil());
        assertEquals("1,01", betrag.getFormatiertenString());
    }

    @Test
    public final void testEqualsHashcode()
    {
        Geldbetrag betrag1 = new Geldbetrag(100);
        Geldbetrag betrag2 = new Geldbetrag(100);
        assertEquals(betrag1, betrag2);
        assertEquals(betrag1.hashCode(), betrag2.hashCode());

        Geldbetrag betrag3 = new Geldbetrag(101);
        assertNotEquals(betrag1, betrag3);
        assertNotEquals(betrag1.hashCode(), betrag3.hashCode());

        Geldbetrag betrag4 = new Geldbetrag(1000);
        assertNotEquals(betrag1, betrag4);
        assertNotEquals(betrag1.hashCode(), betrag4.hashCode());
    }
}
