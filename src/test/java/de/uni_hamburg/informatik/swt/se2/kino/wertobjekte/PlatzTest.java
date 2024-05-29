package de.uni_hamburg.informatik.swt.se2.kino.wertobjekte;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PlatzTest
{
    @Test
    public void testPlatzNullNullIstGueltig()
    {
        Platz p = new Platz(0, 0);
        assertEquals(0, p.getReihe());
        assertEquals(0, p.getSitz());
    }

    @Test
    public void testPlatzGibtReiheUndSitzZurueck()
    {
        Platz p = new Platz(123, 456);
        assertEquals(123, p.getReihe());
        assertEquals(456, p.getSitz());
    }

    @Test
    public void testEqualsUndHashCode()
    {
        Platz p1 = new Platz(1, 2);
        Platz p2 = new Platz(1, 2);
        Platz p3 = new Platz(1, 3); // Sitz unterschiedlich
        Platz p4 = new Platz(2, 2); // Reihe unterschiedlich

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());

        assertNotEquals(p1, p3);
        assertNotEquals(p1, p4);
    }
}
