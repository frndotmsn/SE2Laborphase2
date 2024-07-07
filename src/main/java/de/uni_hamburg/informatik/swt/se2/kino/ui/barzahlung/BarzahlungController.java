package de.uni_hamburg.informatik.swt.se2.kino.ui.barzahlung;

import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.uni_hamburg.informatik.swt.se2.kino.wertobjekte.Geldbetrag;

/**
 * Das Barzahlungsmodul. Mit diesem Modul kann die Benutzerin oder der Benutzer
 * Bargeld einzahlen, um den Preis der Tickets zu decken und diese somit zu kaufen.
 * 
 * @author Ben Denikus, Fernando Thomsen Canales
 * @version SoSe 2024
 */
public class BarzahlungController
{
    private final BarzahlungView _view;
    private boolean _zahlungErfolgt;
    private final Geldbetrag _preis;
    private String _letzteGueltigeEingabe;
    
    /**
     * Initialisiert das Barzahlungsmodul.
     * Blockiert, bis der Dialog geschlossen wurde
     * 
     * @param parentFrame das zugrundeliegende Frame
     * @param preis der zu zahlende Preis
     * 
     * @require parentFrame != null
     * @require preis != null
     */
    public BarzahlungController(JFrame parentFrame, Geldbetrag preis)
    {
        assert parentFrame != null : "Vorbedingung verletzt: parentFrame != null";
        assert preis != null : "Vorbedingung verletzt: preis != null";
        
        _zahlungErfolgt = false;
        _view = new BarzahlungView(parentFrame);
        _preis = preis;
        _letzteGueltigeEingabe = "";
        
        aktualisiereUI();
        registriereUIAktionen();
        
        // Weil beim Beenden des Dialogs ohne Nutzung der Abbrechen oder Bezahlen-Buttons hier auch NICHT mehr blockiert wird, passt das.
        _view.zeigeFenster();
    }
    
    /**
     * Gibt zurück, ob die Zahlung erfolgt ist
     * 
     * @return ob die Zahlung erfolgte
     */
    public boolean gibZahlungErfolgt()
    {
        return _zahlungErfolgt;
    }
    
    private void aktualisiereUI()
    {
        _view.getBezahlenButton().setEnabled(bezahlenMoeglich());
        _view.getRestbetragLabel().setText(gibRestbetrag().toString() + "€");
        _view.getPreisWertLabel().setText(_preis.toString() + "€");
    }
    
    /**
     * Registriert die Aktionen fuer die Benutzeroberflaeche.
     * Fuegt ActionListener fuer die Buttons und DocumentListener fuer ds Textfeld hinzu.
     */
    private void registriereUIAktionen() 
    {
        _view.getBezahlenButton().addActionListener(e -> versucheBezahlungVerarbeiten());
        _view.getAbbrechenButton().addActionListener(e -> abbrechen());

        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setzteEinzahlungAufGueltigenWert();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setzteEinzahlungAufGueltigenWert();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setzteEinzahlungAufGueltigenWert();
            }
        };

        _view.getEinzahlungTextField().getDocument().addDocumentListener(documentListener);
        _view.getEinzahlungTextField().addActionListener((e) -> setzeEinzahlungAufStandardFormat());
    }
    
    private boolean bezahlenMoeglich()
    {
        return gibRestbetrag().eurocent() <= Geldbetrag.ZERO.eurocent();
    }
    
    private Geldbetrag gibRestbetrag()
    {
        return _preis.subtract(letzterGueltigerGeldbetrag());
    }
    
    /**
     * Bricht ab.
     */
    private void abbrechen()
    {
        _zahlungErfolgt = false;
        _view.schliesseFenster();
    }
    
    private void setzeLetzteGueltigeEingabe(String eingabe)
    {
        assert Geldbetrag.fromString(eingabe).isPresent() :"Vorbedingung verletzt: Geldbetrag.fromString(eingabe).isPresent() :";
        
        _letzteGueltigeEingabe = eingabe;
        aktualisiereUI();
    }
    
    private Geldbetrag letzterGueltigerGeldbetrag()
    {
        return Geldbetrag.fromString(_letzteGueltigeEingabe).get();
    }
    
    private void versucheBezahlungVerarbeiten()
    {
        // Könnte man auch weglassen, da man den Button nur anklicken kann, sofern bezahlenMoeglich() gilt. (bei korrekter Implementation)
        // Wird aber geprüft, weil doppelt hält besser. :D
        if (!bezahlenMoeglich())
        {
            return;
        }
        
        _zahlungErfolgt = true;
        _view.schliesseFenster();
    }
    
    private void setzeEinzahlungAufStandardFormat()
    {
        _view.getEinzahlungTextField().setText(letzterGueltigerGeldbetrag().toString());
    }
    
    private void setzteEinzahlungAufGueltigenWert()
    {
        String eingabe = _view.getEinzahlungTextField().getText();
        Optional<Geldbetrag> geldbetragDerEingabe = Geldbetrag.fromString(eingabe);
        if (geldbetragDerEingabe.isPresent())
        {
            setzeLetzteGueltigeEingabe(eingabe);
        }
        else
        {
            // Wenn man direkt das TextField modifiziert, kann dies zu einer IllegalStateException führen, da der Aufruf dieser Methode oft in einem Listener des selben TextFields passiert.
            // https://stackoverflow.com/questions/15206586/getting-attempt-to-mutate-notification-exception
            SwingUtilities.invokeLater(() -> _view.getEinzahlungTextField().setText(_letzteGueltigeEingabe));
        }
    }
}
