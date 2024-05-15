package de.uni_hamburg.informatik.swt.se2.mediathek.services.vormerk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten.Kunde;
import de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten.Verleihkarte;
import de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten.Vormerkkarte;
import de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten.medien.CD;
import de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten.medien.Medium;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.kundenstamm.KundenstammService;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.kundenstamm.KundenstammServiceImpl;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.medienbestand.MedienbestandService;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.medienbestand.MedienbestandServiceImpl;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.verleih.ProtokollierException;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.verleih.VerleihService;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.verleih.VerleihServiceImpl;
import de.uni_hamburg.informatik.swt.se2.mediathek.wertobjekte.Datum;
import de.uni_hamburg.informatik.swt.se2.mediathek.wertobjekte.Kundennummer;

public class VormerkServiceImplTest
{
    private final VerleihService _verleihService;
    private final VormerkService _vormerkService;
    private Kunde _ausleiher;
    private Kunde _ersterVormerker;
    private Kunde _zweiterVormerker;
    private Kunde _dritterVormerker;
    private Kunde _kunde;
    
    private Medium _medium;
    
    public VormerkServiceImplTest()
    {
        _ausleiher = new Kunde(new Kundennummer(123456), "Max", "Mustermann");
        _ersterVormerker = new Kunde(new Kundennummer(234567), "Max", "Melone");
        _zweiterVormerker = new Kunde(new Kundennummer(345678), "Moe", "Mustermann");
        _dritterVormerker = new Kunde(new Kundennummer(456789), "Moe", "Melone");
        _kunde = new Kunde(new Kundennummer(100110), "Bob", "Binaer");
        
        KundenstammService kundenstamm = new KundenstammServiceImpl(new ArrayList<Kunde>());
        // kundenstamm mit testkunden befüllen
        kundenstamm.fuegeKundenEin(_ausleiher);
        kundenstamm.fuegeKundenEin(_ersterVormerker);
        kundenstamm.fuegeKundenEin(_zweiterVormerker);
        kundenstamm.fuegeKundenEin(_dritterVormerker);
        kundenstamm.fuegeKundenEin(_kunde);
        
        _medium = new CD("CD1", "baz", "foo", 123);
        
        MedienbestandService medienbestand = new MedienbestandServiceImpl(new ArrayList<Medium>());
        
        // medienbestand mit testmedien befüllen
        
        medienbestand.fuegeMediumEin(_medium);
        
        _verleihService = new VerleihServiceImpl(kundenstamm, medienbestand, new ArrayList<Verleihkarte>());
        
        
        _vormerkService = new VormerkServiceImpl(_verleihService);
    }
    
    @Test
    public void testLeereVormerkkarten()
    {
        assertTrue(_vormerkService.getVormerkkarte(_medium).istLeer());
    }
    
    @Test
    public void testMerkeVor()
    {
        _vormerkService.merkeVor(_ersterVormerker, _medium);
        _vormerkService.merkeVor(_zweiterVormerker, _medium);
        _vormerkService.merkeVor(_dritterVormerker, _medium);
        Vormerkkarte vormerkkarte = _vormerkService.getVormerkkarte(_medium);
        
        assertEquals(vormerkkarte.getMedium(), _medium);
        assertEquals(vormerkkarte.getErsterVormerker(), _ersterVormerker);
        assertEquals(vormerkkarte.getZweiterVormerker(), _zweiterVormerker);
        assertEquals(vormerkkarte.getDritterVormerker(), _dritterVormerker);
    }
    
    @Test
    public void testAusleiherIstVormerkenMoeglich()
    {
        Datum datum = new Datum(3, 9, 2042);
        try
        {
            _verleihService.verleiheAn(_ausleiher, List.of(_medium), datum);
        }
        catch (ProtokollierException e)
        {
            // ignorieren, hat nix mit dem testen zu tun
        }
        
        // der Ausleiher darf die DVD NICHT vormerken
        assertFalse(_vormerkService.istVormerkenMoeglich(_ausleiher, _medium));
        
        try
        {
            _verleihService.nimmZurueck(List.of(_medium), new Datum(1, 1, 2054));            
        } catch (ProtokollierException e) {
            // wir ignorieren protokollier zeugs
        }
        
        // jetzt sollte der Ausleiher das Medium vormerken dürfen
        
        assertTrue(_vormerkService.istVormerkenMoeglich(_ausleiher, _medium));
    }
    
    @Test
    public void testIstVormerkenUnmoeglichDarfNichtDoppelt()
    {
        _vormerkService.merkeVor(_ersterVormerker, _medium);
        assertFalse(_vormerkService.istVormerkenMoeglich(_ersterVormerker, _medium));
    }
    
    @Test
    public void testIstVormerkenMoeglich()
    {
        // (0 Vormerker)
        assertTrue(_vormerkService.istVormerkenMoeglich(_kunde, _medium));
        
        _vormerkService.merkeVor(_ersterVormerker, _medium);
        assertTrue(_vormerkService.istVormerkenMoeglich(_kunde, _medium));
        
        _vormerkService.merkeVor(_zweiterVormerker, _medium);
        assertTrue(_vormerkService.istVormerkenMoeglich(_kunde, _medium));

        _vormerkService.merkeVor(_dritterVormerker, _medium);
        assertFalse(_vormerkService.istVormerkenMoeglich(_kunde, _medium));

    }
}