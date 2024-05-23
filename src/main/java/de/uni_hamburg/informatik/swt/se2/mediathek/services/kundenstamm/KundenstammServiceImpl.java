package de.uni_hamburg.informatik.swt.se2.mediathek.services.kundenstamm;

import java.util.ArrayList;
import java.util.List;

import de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten.Kunde;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.AbstractObservableService;
import de.uni_hamburg.informatik.swt.se2.mediathek.wertobjekte.Kundennummer;

/**
 * Ein Kundenstamm ist ein Service, der die Menge von Kunden verwaltet. Auf
 * Kunden kann über ihre Kundennummer zugegriffen werden.
 * 
 * @author SE2-Team
 * @version SoSe 2021
 */
public class KundenstammServiceImpl extends AbstractObservableService
        implements KundenstammService
{

    /**
     * Eine Liste, die alle Kunden enthält.
     */
    private List<Kunde> _kundenstamm;

    /**
     * Konstruktor. Initialisiert einen Kundenstamm.
     * 
     * @param kunden Der initiale Kundenstamm.
     * 
     * @require kunden != null
     */
    public KundenstammServiceImpl(List<Kunde> kunden)
    {
        assert kunden != null : "Vorbedingung verletzt: kunden != null";
        _kundenstamm = new ArrayList<Kunde>(kunden);
    }

    @Override
    public void entferneKunden(Kunde kunde)
    {
        assert enthaeltKunden(
                kunde) : "Vorbedingung verletzt: enthaeltKunden(kunde) ";
        _kundenstamm.remove(kunde);
        informiereUeberAenderung();
    }

    @Override
    public boolean enthaeltKunden(Kunde kunde)
    {
        assert kunde != null : "Vorbedingung verletzt: kunde != null";
        return _kundenstamm.contains(kunde);
    }

    @Override
    public void fuegeKundenEin(Kunde neuerKunde)
    {
        assert !enthaeltKunden(
                neuerKunde) : "Vorbedingung verletzt: !enthaelt(kunden)";
        _kundenstamm.add(neuerKunde);
        informiereUeberAenderung();
    }

    @Override
    public List<Kunde> getKunden()
    {
        return new ArrayList<Kunde>(_kundenstamm);
    }

    @Override
    public Kunde getKunden(Kundennummer kundennummer)
    {
        assert kundennummer != null : "Vorbedingung verletzt: kundennummer != null";

        Kunde result = null;
        for (Kunde kunde : _kundenstamm)
        {
            if (kunde.getKundennummer()
                .equals(kundennummer))
            {
                result = kunde;
                break;
            }
        }
        return result;
    }

}
