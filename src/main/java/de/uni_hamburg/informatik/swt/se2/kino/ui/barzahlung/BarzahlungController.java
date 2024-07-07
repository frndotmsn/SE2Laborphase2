package de.uni_hamburg.informatik.swt.se2.kino.ui.barzahlung;

import de.uni_hamburg.informatik.swt.se2.kino.entitaeten.Vorstellung;
import de.uni_hamburg.informatik.swt.se2.kino.ui.platzverkauf.JPlatzplan;
import de.uni_hamburg.informatik.swt.se2.kino.wertobjekte.Geldbetrag;
import de.uni_hamburg.informatik.swt.se2.kino.wertobjekte.Platz;

import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
* Der Controller für Barzahlungen.
* Dieser {@link BarzahlungController} übernimmt die Verwaltung der Barzahlungstransaktionen. 
* Er ermöglicht die Anzeige des Barzahlungs-Fensters, die Berechnung des Restbetrags und 
* die Verwaltung der Abbruch- und Zahlungsvorgänge.
* 
* @author Ben Denikus
* @version SoSe 2024
*/
public class BarzahlungController 
{
	private JPlatzplan _platzplan;
	private BarzahlungView _view;
	private Geldbetrag _preis;
	private Vorstellung _vorstellung;
	
	/**
	 * Erstellt einen neuen BarzahlungController.
	 * 
	 * @param platzplan Der Platzplan, der die ausgewaehlten Plaetze enthaelt. 
	 * @param view Die Ansicht für die Barzahlung.
	 * @param vorstellung Die Vorstellung, fuer die die Plaetze verkauft werden sollen.
	 */
	public BarzahlungController(JPlatzplan platzplan, BarzahlungView view, Vorstellung vorstellung)
	{
		this._platzplan = platzplan;
		this._view = view;
		this._preis = vorstellung.getPreisFuerPlaetze(platzplan.getAusgewaehltePlaetze());
		this._vorstellung = vorstellung;
		registriereUIAktionen();
	}
	
	/**
	 * Registriert die Aktionen fuer die Benutzeroberflaeche.
	 * Fuegt ActionListener fuer die Buttons und DocumentListener fuer ds Textfeld hinzu.
	 */
	private void registriereUIAktionen() 
	{
		_view.getBezahlenButton().addActionListener(e -> verarbeiteBezahlung());
        _view.getAbbrechenButton().addActionListener(e -> abbrechen());

        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateRestbetrag();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateRestbetrag();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateRestbetrag();
            }
        };

        _view.getEinzahlungTextField().getDocument().addDocumentListener(documentListener);
    }
	    
	
	/**
	 * Zeigt das Barzahlungsfenster an. 
	 */
	public void anzeigeFenster()
	{
		_view.zeigeFenster();
	}
	
	/**
	 * Schliesst das Barzahlungsfenster. 
	 */
	public void schliesseFenster()
	{
		_view.schliesseFenster();
	}
	
	/**
	 * Aktualisiert den angezeigten Restbetrag basierend auf dem eingegebenen Einzahlungsbetrag.
	 * Aktiviert oder deaktiviert den Bezahlen-Button basierend auf dem Einzahlungsbetrag. 
	 */
	private void updateRestbetrag() 
	{
		String einzahlungText = _view.getEinzahlungTextField().getText();
        Geldbetrag einzahlung = Geldbetrag.fromString(einzahlungText).orElse(Geldbetrag.ZERO);
        Geldbetrag restbetrag = _preis.subtract(einzahlung);
        _view.getRestbetragLabel().setText(restbetrag.toString() + "€");
        
        _view.getBezahlenButton().setEnabled(einzahlung.greaterThanOrEqualTo(_preis));
    }
	
	/**
	 * Verarbeitet die Bezahlung.
	 * Zeigt eine Nachricht an, ob die Bezahlung erfolgreich war und schliesst das Fenster, wenn die Bezahlung erfolgreich war. 
	 */
	private void verarbeiteBezahlung() 
	{
        String einzahlungText = _view.getEinzahlungTextField().getText();
        Geldbetrag einzahlung = Geldbetrag.fromString(einzahlungText).orElse(Geldbetrag.ZERO);
        String nachricht = einzahlung.greaterThanOrEqualTo(_preis) ? "Bezahlung erfolgreich!" : "Einzahlung unzureichend!";
        zeigeNachricht(nachricht);
        if (einzahlung.greaterThanOrEqualTo(_preis)) 
        {
        	_vorstellung.verkaufePlaetze(_platzplan.getAusgewaehltePlaetze());
            schliesseFenster();
        }
    }
	
	/**
	 * Bricht den Bezahlungsvorgang ab.
	 * Storniert die ausgewaehlten Plaetze und setzt die Auswahl zurück.
	 * Schliesst das Barzahlungsfenster. 
	 */
	private void abbrechen()
	{
		Set<Platz> ausgewaehltePlaetze = _platzplan.getAusgewaehltePlaetze();
		if (ausgewaehltePlaetze.isEmpty())
		{
			_vorstellung.stornierePlaetze(_platzplan.getAusgewaehltePlaetze());
			_platzplan.entferneAuswahl();
		}
		schliesseFenster();
	}
	
	/**
	 * Zeigt eine Nachricht in einem Dialogfenster an.
	 * 
	 * @param nachricht Die anzuzeigende Nachricht. 
	 */
	private void zeigeNachricht(String nachricht) 
	{
        JOptionPane.showMessageDialog(null, nachricht);
    }
}
