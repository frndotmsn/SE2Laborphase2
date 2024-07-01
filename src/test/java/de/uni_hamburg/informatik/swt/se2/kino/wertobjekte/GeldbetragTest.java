package de.uni_hamburg.informatik.swt.se2.kino.wertobjekte;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Test;

/**
 * Testet das {@link Geldbetrag } Record (Wertobjekt!)
 * 
 * @author Fernando Thomsen Canales
 * @version 1.0
 */
public class GeldbetragTest
{   
    @Test
    public void testeAdd()
    {
        Geldbetrag a = new Geldbetrag(300);
        Geldbetrag b = new Geldbetrag(440);
        assertEquals(new Geldbetrag(740), a.add(b));
    }
    
    @Test
    public void testeAddOverflow()
    {
        Geldbetrag b = new Geldbetrag(1);
        assertEquals(Geldbetrag.MAX_VALUE, Geldbetrag.MAX_VALUE.add(b));
    }
    
    @Test
    public void testeSubtract()
    {
        Geldbetrag a = new Geldbetrag(300);
        Geldbetrag b = new Geldbetrag(100);
        assertEquals(new Geldbetrag(200), a.subtract(b));
    }
    
    @Test
    public void testeSubtractUnderflow()
    {
        Geldbetrag b = new Geldbetrag(1);
        assertEquals(Geldbetrag.MIN_VALUE, Geldbetrag.MIN_VALUE.subtract(b));
    }
    
    @Test
    public void testeToString()
    {
        assertEquals("00,00", Geldbetrag.ZERO.toString());
        
        Geldbetrag negativ = new Geldbetrag(-10040);
        assertEquals("-100,40", negativ.toString());
    }
    
    @Test
    public void testeFromString()
    {
        assertEquals(Optional.of(Geldbetrag.ZERO), Geldbetrag.fromString("00,00"));
        assertEquals(Optional.of(Geldbetrag.ZERO), Geldbetrag.fromString(""));
        assertEquals(Optional.of(Geldbetrag.ZERO), Geldbetrag.fromString("0,0"));
        assertEquals(Optional.of(Geldbetrag.ZERO), Geldbetrag.fromString("00,"));
        
        // Leerer String Test!
        assertEquals(Optional.of(Geldbetrag.ZERO), Geldbetrag.fromString(""));
        
        assertEquals(Optional.of(new Geldbetrag(-34567890)), Geldbetrag.fromString("-345678,9"));
        assertEquals(Optional.of(new Geldbetrag(34567890)), Geldbetrag.fromString("345678,9"));

    }
    
    @Test
    public void testeFromStringUngueltigesFormat()
    {
        String[] ungueltigeStrings = {
                "Hallo",
                "text",
                ".",
                "z1647hzhzuhu3h2zh43z74z7",
                ",313",
                ",14234243223"
        };
        
        for (String ungueltigerString : ungueltigeStrings)
        {
            assertEquals(Optional.empty(), Geldbetrag.fromString(ungueltigerString));
        }
    }
    
    @Test
    public void testeInStandardFormat()
    {
        String[] stringsFuerNull = { "000000000", "00,00", "0,00", "00,0", "0,0", "0,", ",0", "0", "" };
        
        for (String stringFuerNull : stringsFuerNull)
        {
            assertEquals(Optional.of("00,00"), Geldbetrag.inStandardFormat(stringFuerNull));            
        }
        
        assertEquals(Optional.of("01,00"), Geldbetrag.inStandardFormat("1"));
        assertEquals(Optional.of("01,00"), Geldbetrag.inStandardFormat("1,00"));
        
        assertEquals(Optional.of("-345678,90"), Geldbetrag.inStandardFormat("-345678,9"));
        assertEquals(Optional.of("345678,90"), Geldbetrag.inStandardFormat("345678,9"));
    }
    
    @Test
    public void testeMultiply()
    {
        Geldbetrag a = new Geldbetrag(33);
        assertEquals(new Geldbetrag(99), a.multiply(3));
        assertEquals(new Geldbetrag(-33), a.multiply(-1));
        assertEquals(Geldbetrag.MAX_VALUE, a.multiply(Integer.MAX_VALUE));
        assertEquals(Geldbetrag.MIN_VALUE, a.multiply(Integer.MIN_VALUE));
        assertEquals(Geldbetrag.ZERO, a.multiply(0));
    }
}
