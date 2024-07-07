package de.uni_hamburg.informatik.swt.se2.kino.ui.barzahlung;

import de.uni_hamburg.informatik.swt.se2.kino.entitaeten.Vorstellung;
import de.uni_hamburg.informatik.swt.se2.kino.ui.platzverkauf.JPlatzplan;
import de.uni_hamburg.informatik.swt.se2.kino.wertobjekte.Geldbetrag;
import de.uni_hamburg.informatik.swt.se2.kino.wertobjekte.Platz;

import java.util.Optional;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
* Der Controller für Barzahlungen.
* Dieser {@link BarzahlungController} uebernimmt die Verwaltung der Barzahlungstransaktionen. 
* Er ermoeglicht die Anzeige des Barzahlungs-Fensters, die Berechnung des Restbetrags und 
* die Verwaltung der Abbruch- und Zahlungsvorgaenge.
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
	 * @param view Die Ansicht fuer die Barzahlung.
	 * @param vorstellung Die Vorstellung, fuer die die Plaetze verkauft werden sollen.
	 */
	public BarzahlungController(JPlatzplan platzplan, BarzahlungView view, Vorstellung vorstellung)
	{
		this._platzplan = platzplan;
		this._view = view;
		this._preis = vorstellung.getPreisFuerPlaetze(platzplan.getAusgewaehltePlaetze());
		this._vorstellung = vorstellung;
		
		_view.getRestbetragLabel().setText(Geldbetrag.ZERO.toString() + "€");
		
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
        Optional<Geldbetrag> optionalEinzahlung = Geldbetrag.fromString(einzahlungText);

        if (optionalEinzahlung.isPresent()) 
        {
            Geldbetrag einzahlung = optionalEinzahlung.get();
            Geldbetrag restbetrag = _preis.subtract(einzahlung);
            _view.getRestbetragLabel().setText(restbetrag.toString() + "€");
            _view.getBezahlenButton().setEnabled(einzahlung.greaterThanOrEqualTo(_preis));
            _view.getFehlermeldungLabel().setText("");
        } 
        else 
        {
            // Ungültige Eingabe, entsprechende Fehlermeldung anzeigen.
            _view.getRestbetragLabel().setText(Geldbetrag.ZERO.toString() + "€");
            _view.getFehlermeldungLabel().setText("Ungültige Eingabe");
            _view.getBezahlenButton().setEnabled(false);
        }
    }
    
	
	/**
	 * Verarbeitet die Bezahlung.
	 * Zeigt eine Nachricht an, ob die Bezahlung erfolgreich war und schliesst das Fenster, wenn die Bezahlung erfolgreich war. 
	 */
	private void verarbeiteBezahlung() 
	{
		String einzahlungText = _view.getEinzahlungTextField().getText();
        Optional<Geldbetrag> optionalEinzahlung = Geldbetrag.fromString(einzahlungText);

        if (optionalEinzahlung.isPresent()) 
        {
            Geldbetrag einzahlung = optionalEinzahlung.get();

            if (einzahlung.greaterThanOrEqualTo(_preis)) 
            {
                _vorstellung.verkaufePlaetze(_platzplan.getAusgewaehltePlaetze());
                zeigeNachricht("Bezahlung erfolgreich!");
                aktualisierePlatzplan();
                schliesseFenster();
            } 
            else 
            {
                _view.getFehlermeldungLabel().setText("Einzahlung unzureichend! Bitte geben Sie einen ausreichenden Betrag ein.");
            }
        } 
        else 
        {
            _view.getFehlermeldungLabel().setText("Ungültige Eingabe! Bitte geben Sie einen gültigen Betrag ein.");
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
			_vorstellung.stornierePlaetze(ausgewaehltePlaetze);
			_platzplan.entferneAuswahl();
		}
		_platzplan.entferneAuswahl();
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
	
	/**
     * Aktualisiert den Platzplan.
     */
    private void aktualisierePlatzplan() 
    {
        for (Platz platz : _platzplan.getAusgewaehltePlaetze()) 
        {
            _platzplan.markierePlatzAlsVerkauft(platz);
        }
        _platzplan.entferneAuswahl();
    }
}
