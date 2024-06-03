package de.uni_hamburg.informatik.swt.se2.kino.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BeobachtbarTest
{
    public Beobachtbar _beobachtbar;
    boolean _wurdeVeraendert = false;

    public BeobachtbarTest()
    {
        _beobachtbar = new Beobachtbar()
        {
        };
    }

    @Test
    public void informiereUeberAenderungTest()
    {
        Beobachter _beobachter = () -> _wurdeVeraendert = true;
        _beobachtbar.registriereBeobachter(_beobachter);
        _beobachter.reagiereAufAenderung();
        assertEquals(_wurdeVeraendert, true);
    }
}
