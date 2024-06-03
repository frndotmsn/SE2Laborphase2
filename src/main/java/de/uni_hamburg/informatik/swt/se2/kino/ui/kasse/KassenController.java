package de.uni_hamburg.informatik.swt.se2.kino.ui.kasse;

import de.uni_hamburg.informatik.swt.se2.kino.entitaeten.Kino;
import de.uni_hamburg.informatik.swt.se2.kino.entitaeten.Tagesplan;
import de.uni_hamburg.informatik.swt.se2.kino.entitaeten.Vorstellung;
import de.uni_hamburg.informatik.swt.se2.kino.ui.datumsauswaehler.DatumAuswaehlController;
import de.uni_hamburg.informatik.swt.se2.kino.ui.platzverkauf.PlatzVerkaufsController;
import de.uni_hamburg.informatik.swt.se2.kino.ui.vorstellungsauswaehler.VorstellungsAuswaehlController;
import de.uni_hamburg.informatik.swt.se2.kino.wertobjekte.Datum;

/**
 * Das Kassenmodul. Mit diesem Modul kann die Benutzerin oder der Benutzer
 * eine Vorstellung auswählen und Karten für diese Vorstellung verkaufen und
 * stornieren.
 * 
 * @author SE2-Team
 * @version SoSe 2024
 */
public class KassenController
{
    // Die Entität, die durch dieses UI-Modul verwaltet wird.
    private Kino _kino;

    // View dieses UI-Moduls
    private KassenView _view;

    // Die Submodule
    private PlatzVerkaufsController _platzVerkaufsController;
    private DatumAuswaehlController _datumAuswaehlController;
    private VorstellungsAuswaehlController _vorstellungAuswaehlController;

    /**
     * Initialisiert das Kassenmodul.
     * 
     * @param kino das Kino, mit dem das UI-Modul arbeitet.
     * 
     * @require kino != null
     */
    public KassenController(Kino kino)
    {
        assert kino != null : "Vorbedingung verletzt: kino != null";

        _kino = kino;

        // Submodule erstellen
        _platzVerkaufsController = new PlatzVerkaufsController();
        _datumAuswaehlController = new DatumAuswaehlController();
        _vorstellungAuswaehlController = new VorstellungsAuswaehlController();

        _datumAuswaehlController.registriereBeobachter(this::setzeTagesplanFuerAusgewaehltesDatum);
        _vorstellungAuswaehlController.registriereBeobachter(this::setzeAusgewaehlteVorstellung);
        
        // View erstellen (mit eingebetteten Views der direkten Submodule)
        _view = new KassenView(_platzVerkaufsController.getUIPanel(),
                _datumAuswaehlController.getUIPanel(),
                _vorstellungAuswaehlController.getUIPanel());

        registriereUIAktionen();
        setzeTagesplanFuerAusgewaehltesDatum();
        setzeAusgewaehlteVorstellung();

        _view.zeigeFenster();
    }

    /**
     * Fügt die Funktionalitat zum Beenden-Button hinzu.
     */
    private void registriereUIAktionen()
    {
        _view.getBeendenButton()
            .addActionListener(e -> reagiereAufBeendenButton());
    }

    /**
     * Setzt den in diesem UI-Modul angezeigten Tagesplan basierend auf dem
     * derzeit im DatumsAuswahlController ausgewählten Datum.
     */
    private void setzeTagesplanFuerAusgewaehltesDatum()
    {
        Tagesplan tagesplan = _kino.getTagesplan(getAusgewaehltesDatum());
        _vorstellungAuswaehlController.setTagesplan(tagesplan);
    }

    /**
     * Passt die Anzeige an, wenn eine andere Vorstellung gewählt wurde.
     */
    private void setzeAusgewaehlteVorstellung()
    {
        _platzVerkaufsController.setVorstellung(getAusgewaehlteVorstellung());
    }

    /**
     * Beendet die Anwendung.
     */
    private void reagiereAufBeendenButton()
    {
        _view.schliesseFenster();
    }

    /**
     * Gibt das derzeit gewählte Datum zurück.
     */
    private Datum getAusgewaehltesDatum()
    {
        return _datumAuswaehlController.getSelektiertesDatum();
    }

    /**
     * Gibt die derzeit ausgewaehlte Vorstellung zurück. Wenn keine Vorstellung
     * ausgewählt ist, wird <code>null</code> zurückgegeben.
     */
    private Vorstellung getAusgewaehlteVorstellung()
    {
        return _vorstellungAuswaehlController.getAusgewaehlteVorstellung();
    }
}
