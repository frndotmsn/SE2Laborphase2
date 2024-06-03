package de.uni_hamburg.informatik.swt.se2.kino.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstrakte Beobachtbar-Klasse für das Observer-Pattern
 * 
 * @author Ben Denikus
 * @version 03.06.2024
 */
public abstract class Beobachtbar 
{
	private final List<Beobachter> _beobachter;
	
	/**
	 * Initialisiert Felder für implementierende Klassen.
	 */
	protected Beobachtbar()
	{
		_beobachter = new ArrayList<>();
	}
	
	/**
	 * Registriert den Beobachter und fügt diesen zur Liste hinzu.
	 * 
	 * @param beobachter Der Beobachter
	 */
	public void registriereBeobachter(Beobachter beobachter)
	{
		_beobachter.add(beobachter);
	}
	
	/**
	 * Es werden alle Beobachter ueber alle Aenderungen informiert.
	 */
	protected void informiereUeberAenderung()
	{
		_beobachter.forEach(Beobachter::reagiereAufAenderung);
	}
}
