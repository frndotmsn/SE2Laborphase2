package de.uni_hamburg.informatik.swt.se2.kino.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Testet die {@link StringUtils} Helferklasse
 * 
 * @author Fernando Thomsen Canales
 * @version 1.0
 */
public class StringUtilsTest
{
    @Test
    public void testPad()
    {
        assertEquals("", StringUtils.pad("", 0, 'd', LeftRight.LEFT));
        assertEquals("", StringUtils.pad("", 0, 'd', LeftRight.RIGHT));

        assertEquals("dw", StringUtils.pad("dw", 2, 'd', LeftRight.LEFT));
        assertEquals("ddddw", StringUtils.pad("dw", 5, 'd', LeftRight.LEFT));
        
        assertEquals("dw     ", StringUtils.pad("dw", 7, ' ', LeftRight.RIGHT));
    }
}
