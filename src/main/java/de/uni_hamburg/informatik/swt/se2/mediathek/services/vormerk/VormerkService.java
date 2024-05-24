package de.uni_hamburg.informatik.swt.se2.mediathek.services.vormerk;

import java.util.List;

import de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten.Kunde;
import de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten.Vormerkkarte;
import de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten.medien.Medium;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.ObservableService;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.verleih.VerleihService;

/**
 * Der VormerkService erlaubt es, Medien vorzumerken.
 * 
 * Für jedes vorgemerkte Medium wird eine neue Vormerkkarte angelegt.
 * Diese Vormerkkarten bestehen auch falls sie keine Vormerker haben
 * 
 * VerleihService ist ein ObservableService, als solcher bietet er die
 * Möglichkeit über Verleihvorgänge zu informieren. Beobachter müssen das
 * Interface ServiceObserver implementieren.
 * 
 * @author Fernando Thomsen Canales
 * @version 13-05-2024.0
 */
public interface VormerkService extends ObservableService
{
    /**
     * Fügt Kunden als Vormerker des Mediums hinzu.
     * 
     * @param kunde der Kunde, der Vormerker werden soll
     * @param medium das vorzumerkende Medium
     * @param verleihService Der verleihService
     * 
     * @require kunde != null
     * @require medium != null
     * @require {@link #istVormerkenMoeglich(kunde, medium, verleihService)}
     */
    // TODO: merkeVor soll maybe boolean zurückgeben, ob das Vormerken geklappt hat
    public void merkeVor(Kunde kunde, Medium medium,
            VerleihService verleihService);

    /**
     * Gibt zurück, ob der Kunde das Medium vormerken kann.
     * 
     * @param kunde der Kunde
     * @param medium das Medium
     * @param verleihService Der VerleihService
     * @return ob der Kunde das Medium vormerken kann
     * 
     * @require kunde != null
     * @require medium != null
     * @require verleihService != null
     */
    public boolean istVormerkenMoeglich(Kunde kunde, Medium medium,
            VerleihService verleihService);

    /**
     * Entfernt die ersten Vormerker der Medien falls dieser dem Kunden entspricht.
     * Falls es keinen ersten Vormerker gibt oder der Kunde diesem nicht entspricht passiert nichts.
     * 
     * @param kunde der Kunde, der vorher ggf. Vormerker war
     * @param medien die Medien
     * 
     * @require kunde != null
     * @require medien != null
     */
    public void entferneErstenVormerker(Kunde kunde, List<Medium> medien);

    /**
     * Gibt die zugehörige Vormerkkarte zum Medium zurück.
     * 
     * @param medium Medium
     * @return die Vormerkkarte des Mediums
     * 
     * @require medium != null
     * 
     * @ensure result != null
     */
    public Vormerkkarte getVormerkkarte(Medium medium);

    /**
     * Gibt zurück, ob der Kunde aller Vormerker der Medien ist, falls diese Vormerker haben
     * 
     * @param kunde Der kunde
     * @param medien Die Medien
     * 
     * @return ob der Kunde Vormerker der Medien ist, falls diese Vormerker haben
     */
    public boolean istVormerkerOderLeereVormerkkarte(Kunde kunde,
            List<Medium> medien);
}
