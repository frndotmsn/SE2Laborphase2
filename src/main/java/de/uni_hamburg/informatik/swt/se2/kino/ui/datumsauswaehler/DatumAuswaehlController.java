package de.uni_hamburg.informatik.swt.se2.kino.ui.datumsauswaehler;

import javax.swing.JPanel;

import de.uni_hamburg.informatik.swt.se2.kino.util.Beobachtbar;
import de.uni_hamburg.informatik.swt.se2.kino.wertobjekte.Datum;

/**
 * Mit diesem UI-Modul kann ein Datum ausgewählt werden.
 * 
 * Dieses UI-Modul ist ein eingebettetes Submodul. Es benachrichtigt sein
 * Supermodul, wenn sich das ausgewählte Datum geändert hat.
 * 
 * @author SE2-Team
 * @version SoSe 2024
 */
public class DatumAuswaehlController extends Beobachtbar
{
    private DatumAuswaehlView _view;
    private Datum _ausgewaehltesDatum;

    /**
     * Initialisiert dieses UI-Modul. Das initial ausgewählte Datum ist der
     * heutige Tag.
     */
    public DatumAuswaehlController()
    {
        _ausgewaehltesDatum = Datum.heute();
        _view = new DatumAuswaehlView(
                _ausgewaehltesDatum.getFormatiertenString());
        registriereUIAktionen();
    }

    /**
     * Diese Methode wird aufgerufen, wenn der Zurueck-Button gedrueckt wurde.
     */
    private void zurueckButtonWurdeGedrueckt()
    {
        _ausgewaehltesDatum = _ausgewaehltesDatum.vorherigerTag();
        _view.getDatumLabel()
            .setText(_ausgewaehltesDatum.getFormatiertenString());
        
        informiereUeberAenderung();
    }

    /**
     * Diese Methode wird aufgerufen, wenn der Weiter-Button gedrueckt wurde.
     */
    private void weiterButtonWurdeGedrueckt()
    {
        _ausgewaehltesDatum = _ausgewaehltesDatum.naechsterTag();
        _view.getDatumLabel()
            .setText(_ausgewaehltesDatum.getFormatiertenString());
        
        informiereUeberAenderung();
    }

    /**
     * Gibt das Panel dieses UI-Moduls zurück. Das Panel sollte von einem
     * Supermodul eingebettet werden.
     * 
     * @ensure result != null
     */
    public JPanel getUIPanel()
    {
        return _view.getUIPanel();
    }

    /**
     * Gibt das im UI-Modul ausgewählte Datum zurück.
     * 
     * @ensure result != null
     */
    public Datum getSelektiertesDatum()
    {
        return _ausgewaehltesDatum;
    }

    /**
     * Verbindet die fachlichen Aktionen mit den Interaktionselementen der
     * graphischen Benutzungsoberfläche.
     */
    private void registriereUIAktionen()
    {
        _view.getZurueckButton()
            .addActionListener(e -> zurueckButtonWurdeGedrueckt());

        _view.getWeiterButton()
            .addActionListener(e -> weiterButtonWurdeGedrueckt());
    }
}
