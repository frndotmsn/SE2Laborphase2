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
public class VormerkServiceImpl extends AbstractObservableService
        implements VormerkService
{
    protected final Map<Medium, Vormerkkarte> _vormerkkarten;

    public VormerkServiceImpl()
    {
        _vormerkkarten = new HashMap<>();
    }

    @Override
    public void merkeVor(Kunde kunde, Medium medium,
            VerleihService verleihService)
    {
        assert medium != null : "Vorbedingung verletzt: medium != null";
        assert kunde != null : "Vorbedingung verletzt: kunde != null";
        assert istVormerkenMoeglich(kunde, medium,
                verleihService) : "Vorbedingung verletzt: istVormerkenMoeglich(kunde, medium)";

        Vormerkkarte vormerkkarte = getVormerkkarte(medium);
        if (vormerkkarte == null)
        {
            vormerkkarte = new Vormerkkarte(medium);
            _vormerkkarten.put(medium, vormerkkarte);
        }
        vormerkkarte.merkeVor(kunde);
        // falls vormerken erfolgreich, informieren über Änderung!
        informiereUeberAenderung();
    }

    @Override
    public boolean istVormerkenMoeglich(Kunde kunde, Medium medium,
            VerleihService verleihService)
    {
        // Der Kunde, der das Medium momentan ausgeliehen hat, darf es nicht vormerken
        if (verleihService.istVerliehenAn(kunde, medium))
        {
            return false;
        }

        Vormerkkarte vormerkkarte = getVormerkkarte(medium);

        return vormerkkarte == null || vormerkkarte.istVormerkenMoeglich(kunde);
    }

    @Override
    public Vormerkkarte getVormerkkarte(Medium medium)
    {
        Vormerkkarte vormerkkarte = _vormerkkarten.get(medium);
        if (vormerkkarte == null)
        {
            vormerkkarte = new Vormerkkarte(medium);
            _vormerkkarten.put(medium, vormerkkarte);
        }
        return vormerkkarte;
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
        if (vormerkkarte != null
                && kunde.equals(vormerkkarte.getErsterVormerker()))
        {
            vormerkkarte.entferneErstenVormerker();
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean istVormerkerOderLeereVormerkkarte(Kunde kunde,
            List<Medium> medien)
    {
        for (Medium medium : medien)
        {
            Kunde vormerker = getVormerkkarte(medium).getErsterVormerker();
            if (!kunde.equals(vormerker) && vormerker != null)
            {
                return false;
            }
        }

        return true;
    }
}
