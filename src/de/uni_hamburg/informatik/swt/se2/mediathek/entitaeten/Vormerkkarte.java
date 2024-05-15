package de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten;

import java.util.LinkedList;

import de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten.medien.Medium;

/**
 * Mit Hilfe von Vormerkkarten werden beim Vormerken eines Mediums alle relevanten
 * Daten notiert.
 * Die maximale Anzahl der Vormerker ist fix auf 3. 
 * 
 * Sie beantwortet die folgenden Fragen:
 * Wer merkte das Medium vor?
 * Kann das Medium weiterhin Vorgemerkt werden?
 * 
 * Wenn Medien zurück gegeben werden, soll nur der erste Vormerker das Medium ausleihen
 * können. Um die Verwaltung der Karten kümmert sich der VormerkService
 * 
 * @author Fernando Thomsen Canales
 * @version 13-05-2024.0
 */
public class Vormerkkarte
{
    private final Medium _medium;
    private final LinkedList<Kunde> _vormerkende;
    
    /**
     * Initialisert eine neue Vormerkkarte mit den gegebenen Daten.
     * 
     * 
     * @param medium Ein (vorgemerktes) Medium
     * 
     * @require medium != null
     * 
     * @ensure {@link #getMedium()} == medium
     * @ensure {@link #istLeer()}
     */
    public Vormerkkarte(Medium medium)
    {
        _medium = medium;
        _vormerkende = new LinkedList<Kunde>();
    }
    
    /**
     * Gibt das Medium der Vormerkkarte zurück.
     * 
     * @return das Medium der Vormerkkaret
     * 
     * @ensure result != null
     */
    public Medium getMedium()
    {
        return _medium;
    }
    
    /**
     * Gibt den ersten Vormerker zurück.
     * 
     * @return der erste Vormerker, null falls es keinen gibt
     */
    public Kunde getErsterVormerker()
    {
        if (_vormerkende.size() < 1)
        {
            return null;
        }
        
        return _vormerkende.get(0);
    }
    
    /**
     * Gibt den zweiten Vormerker zurück.
     * 
     * @return der zweite Vormerker, null falls es keinen gibt
     */
    public Kunde getZweiterVormerker()
    {
        if (_vormerkende.size() < 2)
        {
            return null;
        }
        
        return _vormerkende.get(1);
    }
    
    /**
     * Gibt den dritten Vormerker zurück.
     * 
     * @return der dritte Vormerker, null falls es keinen gibt
     */
    public Kunde getDritterVormerker()
    {
        if (_vormerkende.size() < 3)
        {
            return null;
        }
        
        return _vormerkende.get(2);
    }
    
    /**
     * Entfernt den ersten Vormerker.
     * 
     * @require !{@link #istLeer()}
     */
    public void entferneErstenVormerker()
    {
        assert !istLeer() : "Vorbedingung verletzt: !istLeer()";
        
        _vormerkende.removeFirst();
    }
    
    
    /**
     * Gibt zurück, ob Vormerken möglich ist.
     * Dies trifft zu, wenn die Anzahl der Vormerkenden unter 3 ist.
     *  
     * @return ob Vormerken Möglich ist
     * 
     * @require kunde != null
     * 
     * @deprecated es sollte istVormerkenMoeglich(Kunde) verwendet werden
     */
    public boolean istVormerkenMoeglich()
    {
        return maximaleAnzahlVormerkendeErreicht();
    }
    
    /**
     * Gibt zurück, ob Vormerken durch einen bestimmten Kunden möglich ist.
     * Dies trifft zu, wenn die Anzahl der Vormerkenden unter 3 ist.
     * Der Kunde darf zusätzlich nicht Vormerker sein.
     * 
     * @param kunde Kunde
     * 
     * @return ob Vormerken Möglich ist
     * 
     * @require kunde != null
     */
    public boolean istVormerkenMoeglich(Kunde kunde)
    {
        assert kunde != null : "Vorbedingung verletzt: kunde != null";
        return maximaleAnzahlVormerkendeErreicht() && !istVormerker(kunde);
    }
    
    /**
     * Gibt zurück, ob die Vormerkkarte leer ist.
     * Dass heißt, ob sie keine Vormerker hat.
     * 
     * @return ob die Vormerkkarte leer ist
     */
    public boolean istLeer()
    {
        return _vormerkende.size() == 0;
    }
    
    /**
     * Merkt den Kunden als Vormerker vor.
     * 
     * @param kunde
     * 
     * @require {@link #istVormerkenMoeglich()}
     * @require kunde != null
     * @require !{@link #istVormerker(kunde)}
     * 
     * @ensure !{@link #istLeer()}
     */
    
    // TODO: merkeVor soll maybe boolean zurückgeben, ob das Vormerken geklappt hat
    public void merkeVor(Kunde kunde)
    {
        assert istVormerkenMoeglich() : "Vorbedingung verletzt: istVormerkenMoeglich()";
        assert kunde != null : "Vorbedingung verletzt: kunde != null";
        // Kunde darf nicht schon vorgemerkt haben
        assert !istVormerker(kunde) : "Vorbedingung verletzt: !istVormerker(kunde)";
        
        _vormerkende.addLast(kunde);
    }
    
    /**
     * Gibt zurück, ob der Kunde ein Vormerker ist.
     * 
     * @param kunde der Kunde
     * @return ob der Kunde ein Vormerker ist
     */
    public boolean istVormerker(Kunde kunde)
    {
        return _vormerkende.contains(kunde);
    }
    
    /**
     * Gibt zurück, ob diese Vormerkkarte schon die maximale Anzahl der Vormerker erreicht hat.
     * Die Maximale Anzahl der Vormerker ist 3.
     *
     * @return ob die Anzahl der Vormerker größer gleich der Maximalen ist.
     */
    private boolean maximaleAnzahlVormerkendeErreicht()
    {
        return _vormerkende.size() < 3;
    }
}
