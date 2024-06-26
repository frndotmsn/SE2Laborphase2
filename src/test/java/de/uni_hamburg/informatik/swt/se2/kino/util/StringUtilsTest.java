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
    public static void testPad()
    {
        assertEquals("", StringUtils.pad("", 0, 'd', LeftRight.Left));
        assertEquals("", StringUtils.pad("", 0, 'd', LeftRight.Right));

        assertEquals("dw", StringUtils.pad("dw", 2, 'd', LeftRight.Left));
        assertEquals("   dw", StringUtils.pad("dw", 5, 'd', LeftRight.Left));
        
        assertEquals("dw     ", StringUtils.pad("dw", 7, 'd', LeftRight.Right));
    }
}
