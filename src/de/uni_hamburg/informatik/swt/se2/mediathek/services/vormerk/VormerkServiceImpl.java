package de.uni_hamburg.informatik.swt.se2.mediathek.services.vormerk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten.Kunde;
import de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten.Vormerkkarte;
import de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten.medien.Medium;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.AbstractObservableService;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.verleih.VerleihService;

/**
 * Diese Klasse implementiert das Interface VormerkService. Siehe dortiger
 * Kommentar.
 */
public class VormerkServiceImpl extends AbstractObservableService implements VormerkService
{
    private final VerleihService _verleihService;
    private final Map<Medium, Vormerkkarte> _vormerkkarten;
    
    public VormerkServiceImpl(VerleihService verleihService)
    {
        _verleihService = verleihService;
        _vormerkkarten = new HashMap<Medium, Vormerkkarte>();
    }
    
    @Override
    public void merkeVor(Kunde kunde, Medium medium)
    {
        assert medium != null : "Vorbedingung verletzt: medium != null";
        assert kunde != null : "Vorbedingung verletzt: kunde != null";
        assert istVormerkenMoeglich(kunde, medium) : "Vorbedingung verletzt: istVormerkenMoeglich(kunde, medium)";
        
        Vormerkkarte vormerkkarte = getVormerkkarte(medium);
        vormerkkarte.merkeVor(kunde);
        // falls vormerken erfolgreich, informieren über Änderung!
        informiereUeberAenderung();
    }

    @Override
    public boolean istVormerkenMoeglich(Kunde kunde, Medium medium)
    {
        // Der Kunde, der das Medium momentan ausgeliehen hat, darf es nicht vormerken
        if (_verleihService.istVerliehenAn(kunde, medium))
        {
            return false;
        }
        
        Vormerkkarte vormerkkarte = getVormerkkarte(medium);
        
        return vormerkkarte.istVormerkenMoeglich(kunde);
    }

    @Override
    public Vormerkkarte getVormerkkarte(Medium medium)
    {
        return _vormerkkarten.computeIfAbsent(medium, (x) -> new Vormerkkarte(x));
    }
    
    @Override
    public void entferneErstenVormerker(Kunde kunde, List<Medium> medien)
    {
        assert kunde != null : "Vorbedingung verletzt: kunde != null";
        assert medien != null : "Vorbedingung verletzt: medien != null";
        
        for (Medium medium : medien)
        {
            entferneErstenVormerker(kunde, medium);
        }
        informiereUeberAenderung();
    }
    
    /**
     * Entfernt den ersten Vormerker eines Mediums.
     * 
     * @param kunde der Kunde, der ggf. Vormerker war
     * @param medium das Medium
     * 
     * @require kunde != null
     * @require medium != null
     * 
     * @return ob das Entfernen des ersten Vormerkers erfolgreich war
     */
    private boolean entferneErstenVormerker(Kunde kunde, Medium medium)
    {
        assert kunde != null : "Vorbedingung verletzt: kunde != null";
        assert medium != null : "Vorbedingung verletzt: medium != null";
        
        Vormerkkarte vormerkkarte = getVormerkkarte(medium);
        if (vormerkkarte.getErsterVormerker() == kunde)
        {
            vormerkkarte.entferneErstenVormerker();
            return true;
        }
        else
        {
            return false;
        }
    }
}
