package de.uni_hamburg.informatik.swt.se2.mediathek.ui.subwerkzeuge.kundenauflister;

import java.util.List;

import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.uni_hamburg.informatik.swt.se2.mediathek.entitaeten.Kunde;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.kundenstamm.KundenstammService;
import de.uni_hamburg.informatik.swt.se2.mediathek.ui.ObservableSubWerkzeug;

/**
 * Ein KundenauflisterWerkzeug ist ein Werkzeug zum auflisten von Kunden.
 * 
 * Das Werkzeug ist beobachtbar und informiert über Änderungen des selektieren
 * Kunden.
 * 
 * @author SE2-Team
 * @version SoSe 2021
 */
public class KundenauflisterWerkzeug extends ObservableSubWerkzeug
{
    private KundenauflisterUI _ui;
    private KundenstammService _kundenstamm;

    /**
     * Initialisiert ein neues KundenauflisterWerkzeug. Es wird die
     * Benutzungsoberfläche mit zum Darstellen der Kunden erzeugt.
     * 
     * @param kundenstamm Der Kundenstamm.
     * 
     * @require kundenstamm != null
     */
    public KundenauflisterWerkzeug(KundenstammService kundenstamm)
    {
        assert kundenstamm != null : "Vorbedingung verletzt: kundenstamm != null";

        _kundenstamm = kundenstamm;

        // UI wird erzeugt.
        _ui = new KundenauflisterUI();

        // Die Ausleihaktionen werden erzeugt und an der UI registriert.
        registriereUIAktionen();

        // Die anzuzeigenden Kunden werden in den UI-Widgets gesetzt.
        setzeAnzuzeigendeKunden();
    }

    /**
     * Registriert die Aktionen, die bei bestimmten UI-Events ausgeführt werden.
     */
    private void registriereUIAktionen()
    {
        registriereKundenAnzeigenAktion();
    }

    /**
     * Holt die anzuzeigenden Kunden aus den Services und setzt diese bei dem
     * TableModel.
     */
    private void setzeAnzuzeigendeKunden()
    {
        List<Kunde> kunden = _kundenstamm.getKunden();
        _ui.getKundenAuflisterTableModel()
            .setKunden(kunden);
    }

    /**
     * Registiert die Aktion, die ausgeführt wird, wenn ein Kunde ausgewählt
     * wird.
     */
    private void registriereKundenAnzeigenAktion()
    {
        _ui.getKundenTable()
            .getSelectionModel()
            .addListSelectionListener(new ListSelectionListener()
            {
                @Override
                public void valueChanged(ListSelectionEvent e)
                {
                    informiereUeberAenderung();
                }
            });
    }

    /**
     * Gibt den vom Benutzer selektierten Kunden zurück. Falls kein Kunde
     * selektiert ist, gebe null zurück.
     * 
     * @return Den aktuell selektierten Kunden, oder null wenn keine Selektion
     *         existiert.
     */
    public Kunde getSelectedKunde()
    {
        Kunde result = null;
        int selectedRow = _ui.getKundenTable()
            .getSelectedRow();
        KundenTableModel kundenTableModel = _ui.getKundenAuflisterTableModel();
        if (kundenTableModel.zeileExistiert(selectedRow))
        {
            result = kundenTableModel.getKundeFuerZeile(selectedRow);
        }
        return result;
    }

    /**
     * Gibt das Panel dieses Subwerkzeugs zurück.
     * 
     * @ensure result != null
     */
    public JPanel getUIPanel()
    {
        return _ui.getUIPanel();
    }
}
