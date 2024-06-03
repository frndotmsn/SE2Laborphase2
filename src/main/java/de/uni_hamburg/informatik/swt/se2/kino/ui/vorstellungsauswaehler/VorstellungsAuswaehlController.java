package de.uni_hamburg.informatik.swt.se2.kino.ui.vorstellungsauswaehler;

import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.uni_hamburg.informatik.swt.se2.kino.entitaeten.Tagesplan;
import de.uni_hamburg.informatik.swt.se2.kino.entitaeten.Vorstellung;
import de.uni_hamburg.informatik.swt.se2.kino.util.Beobachtbar;

import java.util.List;

/**
 * Mit diesem UI-Modul kann der Benutzer oder die Benutzerin eine Vorstellung
 * aus einem Tagesplan auswählen.
 * 
 * Dieses UI-Modul ist ein eingebettetes Submodul. Es benachrichtigt seine
 * Beobachter, wenn sich die ausgewählte Vorstellung geändert hat.
 */
public class VorstellungsAuswaehlController extends Beobachtbar
{
    private VorstellungsAuswaehlView _view;

    // Die Entität, die durch dieses UI-Modul verwaltet wird.
    private Tagesplan _tagesplan;

    /**
     * Initialisiert das UI-Modul.
     */
    public VorstellungsAuswaehlController()
    {
        _view = new VorstellungsAuswaehlView();
        registriereUIAktionen();
    }

    /**
     * Diese Methode wird aufgerufen, wenn eine Vorstellung ausgewaehlt wurde.
     */
    private void vorstellungWurdeAusgewaehlt()
    {
    	informiereUeberAenderung();
    }

    /**
     * Gibt das Panel dieses Submoduls zurück. Das Panel sollte von einem
     * Supermodul eingebettet werden.
     * 
     * @ensure result != null
     */
    public JPanel getUIPanel()
    {
        return _view.getUIPanel();
    }

    /**
     * Gibt die derzeit ausgewählte Vorstellung zurück. Wenn keine Vorstellung
     * ausgewählt ist, wird <code>null</code> zurückgegeben.
     */
    public Vorstellung getAusgewaehlteVorstellung()
    {
        Vorstellung result = null;
        VorstellungsFormatierer adapter = (VorstellungsFormatierer) _view
            .getVorstellungAuswahlList()
            .getSelectedValue();
        if (adapter != null)
        {
            result = adapter.getVorstellung();
        }

        return result;
    }

    /**
     * Setzt den Tagesplan, dessen Vorstellungen zur Auswahl angeboten werden.
     * 
     * @require tagesplan != null
     */
    public void setTagesplan(Tagesplan tagesplan)
    {
        assert tagesplan != null : "Vorbedingung verletzt: tagesplan != null";

        _tagesplan = tagesplan;
        List<Vorstellung> vorstellungen = _tagesplan.getVorstellungen();
        aktualisiereAngezeigteVorstellungsliste(vorstellungen);
    }

    /**
     * Aktualisiert die Liste der Vorstellungen.
     */
    private void aktualisiereAngezeigteVorstellungsliste(
            List<Vorstellung> vorstellungen)
    {
        VorstellungsFormatierer[] varray = new VorstellungsFormatierer[vorstellungen
            .size()];
        for (int i = 0; i < vorstellungen.size(); i++)
        {
            varray[i] = new VorstellungsFormatierer(vorstellungen.get(i));
        }
        _view.getVorstellungAuswahlList()
            .setListData(varray);
        _view.getVorstellungAuswahlList()
            .setSelectedIndex(0);
    }

    /**
     * 
     * Verbindet die fachlichen Aktionen mit den Interaktionselementen der
     * graphischen Benutzungsoberfläche.
     */
    private void registriereUIAktionen()
    {
        _view.getVorstellungAuswahlList()
            .addListSelectionListener(new ListSelectionListener()
            {
                @Override
                public void valueChanged(ListSelectionEvent event)
                {
                    if (!event.getValueIsAdjusting())
                    {
                        vorstellungWurdeAusgewaehlt();
                    }
                }
            });
    }
}
