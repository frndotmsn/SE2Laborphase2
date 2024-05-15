package de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten;

import org.junit.Test;

import de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten.medien.CD;
import de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten.medien.Medium;
import de.uni_hamburg.informatik.swt.se2.mediathek.wertobjekte.Kundennummer;

import static org.junit.Assert.*;

public class VormerkkarteTest 
{
	private Vormerkkarte _vormerkkarte;
	private Medium _medium;
	private Kunde _kunde1;
	private Kunde _kunde2;
	private Kunde _kunde3;
	
	public VormerkkarteTest()
	{
		_medium = new CD("Titel", "Kommentar", "Interpret", 123);
		_vormerkkarte = new Vormerkkarte(_medium);
		
		_kunde1 = new Kunde(new Kundennummer(111111), "Max", "Mustermann");
		_kunde2 = new Kunde(new Kundennummer(222222), "Lisa", "Mueller");
		_kunde3 = new Kunde(new Kundennummer(333333), "Thorsten", "Schmidt");
	}
	
	@Test
	public void testeKonstruktor()
	{
		assertEquals(_medium, _vormerkkarte.getMedium());
		assertTrue(_vormerkkarte.istLeer());
		assertFalse(_vormerkkarte.istVormerker(_kunde1));
	}
	
	@Test
	public void testgetVormerkerundEntferneVormerker()
	{
		_vormerkkarte.merkeVor(_kunde1);
        assertFalse(_vormerkkarte.istLeer());
        assertTrue(_vormerkkarte.istVormerker(_kunde1));
        assertEquals(_kunde1, _vormerkkarte.getErsterVormerker());

        _vormerkkarte.merkeVor(_kunde2);
        assertTrue(_vormerkkarte.istVormerker(_kunde2));
        assertEquals(_kunde1, _vormerkkarte.getErsterVormerker());
        assertEquals(_kunde2, _vormerkkarte.getZweiterVormerker());

        _vormerkkarte.merkeVor(_kunde3);
        assertTrue(_vormerkkarte.istVormerker(_kunde3));
        assertEquals(_kunde1, _vormerkkarte.getErsterVormerker());
        assertEquals(_kunde2, _vormerkkarte.getZweiterVormerker());
        assertEquals(_kunde3, _vormerkkarte.getDritterVormerker());
        
        // da nur drei Vormerker zulässig sind, sollte ein vierter nicht mehr hinzugefügt werden können
        assertFalse(_vormerkkarte.istVormerkenMoeglich());
        
        _vormerkkarte.entferneErstenVormerker();
		assertFalse(_vormerkkarte.istVormerker(_kunde1));
		assertEquals(_kunde2, _vormerkkarte.getErsterVormerker());
		assertEquals(_kunde3, _vormerkkarte.getZweiterVormerker());
		assertNull(_vormerkkarte.getDritterVormerker());
		
		_vormerkkarte.entferneErstenVormerker();
		assertFalse(_vormerkkarte.istVormerker(_kunde2));
		assertEquals(_kunde3, _vormerkkarte.getErsterVormerker());
		assertNull(_vormerkkarte.getZweiterVormerker());
		assertNull(_vormerkkarte.getDritterVormerker());
		
		_vormerkkarte.entferneErstenVormerker();
		assertFalse(_vormerkkarte.istVormerker(_kunde3));
		assertTrue(_vormerkkarte.istLeer());
	}
	
	@Test
	public void testistVormerkenMoeglich()
	{
		assertTrue(_vormerkkarte.istVormerkenMoeglich());
		_vormerkkarte.merkeVor(_kunde1);
		_vormerkkarte.merkeVor(_kunde2);
		_vormerkkarte.merkeVor(_kunde3);
		assertFalse(_vormerkkarte.istVormerkenMoeglich());
	}
	
	@Test
	public void testIstVormerker()
	{
		_vormerkkarte.merkeVor(_kunde1);
		assertTrue(_vormerkkarte.istVormerker(_kunde1));
		assertFalse(_vormerkkarte.istVormerker(_kunde2));
	}
	
}
