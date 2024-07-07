package de.uni_hamburg.informatik.swt.se2.kino.ui.platzverkauf;

import de.uni_hamburg.informatik.swt.se2.kino.entitaeten.Kinosaal;
import de.uni_hamburg.informatik.swt.se2.kino.entitaeten.Vorstellung;
import de.uni_hamburg.informatik.swt.se2.kino.ui.barzahlung.BarzahlungController;
import de.uni_hamburg.informatik.swt.se2.kino.ui.barzahlung.BarzahlungView;
import de.uni_hamburg.informatik.swt.se2.kino.wertobjekte.Geldbetrag;
import de.uni_hamburg.informatik.swt.se2.kino.wertobjekte.Platz;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.Set;

/**
 * Mit diesem UI-Modul können Plätze verkauft und storniert werden. Es arbeitet
 * mit der Entität Vorstellung. Mit ihm kann angezeigt werden, welche
 * Plätze schon verkauft und welche noch frei sind.
 * 
 * Dieses UI-Modul ist ein eingebettetes Submodul. Es kann nicht beobachtet
 * werden.
 * 
 * @author SE2-Team
 * @version SoSe 2024
 */
public class PlatzVerkaufsController
{
    // Die aktuelle Vorstellung, deren Plätze angezeigt werden. Kann null sein.
    private Vorstellung _vorstellung;

    private PlatzVerkaufsView _view;

    /**
     * Initialisiert den PlatzVerkaufsController.
     */
    public PlatzVerkaufsController()
    {
        _view = new PlatzVerkaufsView();
        registriereUIAktionen();
        // Am Anfang wird keine Vorstellung angezeigt:
        setVorstellung(null);
    }

    /**
     * Gibt das Panel dieses Submoduls zurück. Das Panel sollte von einem
     * Supermodul eingebettet werden.
     * 
     * @ensure result != null
     * 
     * @return JPanel des Platzverkaufsmoduls
     */
    public JPanel getUIPanel()
    {
        return _view.getUIPanel();
    }

    /**
     * Fügt der UI die Funktionalität hinzu mit entsprechenden Listenern.
     */
    private void registriereUIAktionen()
    {
        _view.getVerkaufenButton()
            .addActionListener(e -> verkaufePlaetze(_vorstellung));

        _view.getStornierenButton()
            .addActionListener(e -> stornierePlaetze(_vorstellung));

        _view.getPlatzplan()
            .addPlatzSelectionListener(event -> reagiereAufNeuePlatzAuswahl(
                    event.getAusgewaehltePlaetze()));
    }

    /**
     * Reagiert darauf, dass sich die Menge der ausgewählten Plätze geändert
     * hat.
     * 
     * @param plaetze die jetzt ausgewählten Plätze.
     */
    private void reagiereAufNeuePlatzAuswahl(Set<Platz> plaetze)
    {
        _view.getVerkaufenButton()
            .setEnabled(istVerkaufenMoeglich(plaetze));
        _view.getStornierenButton()
            .setEnabled(istStornierenMoeglich(plaetze));
        aktualisierePreisanzeige(plaetze);
    }

    /**
     * Aktualisiert den anzuzeigenden Gesamtpreis
     */
    private void aktualisierePreisanzeige(Set<Platz> plaetze)
    {

        if (istVerkaufenMoeglich(plaetze))
        {
            // TODO: Auf Geldbetrag Wertobjekt ändern
            Geldbetrag preis = _vorstellung.getPreisFuerPlaetze(plaetze);
            _view.getPreisLabel()
                .setText("Gesamtpreis: " + preis + "€");
        }
        else
        {
            _view.getPreisLabel()
                .setText("Gesamtpreis:");
        }
    }

    /**
     * Prüft, ob die angegebenen Plätze alle storniert werden können.
     * 
     * @return true, wenn der Platz sowohl leer als auch die Vorstellung stornierbar ist
     */
    private boolean istStornierenMoeglich(Set<Platz> plaetze)
    {
        return !plaetze.isEmpty() && _vorstellung.sindStornierbar(plaetze);
    }

    /**
     * Prüft, ob die angegebenen Plätze alle verkauft werden können.
     * 
     * @return true, wenn der Platz sowohl leer als auch die Vorstellung zum Verkauf steht
     */
    private boolean istVerkaufenMoeglich(Set<Platz> plaetze)
    {
        return !plaetze.isEmpty() && _vorstellung.sindVerkaufbar(plaetze);
    }

    /**
     * Setzt die Vorstellung. Sie ist die von diesem UI-Modul vewaltete 
     * Entität. Wenn die Vorstellung gesetzt wird, muss die Anzeige 
     * aktualisiert werden. Die Vorstellung darf auch null sein.
     */
    public void setVorstellung(Vorstellung vorstellung)
    {
        _vorstellung = vorstellung;
        aktualisierePlatzplan();
    }

    /**
     * Aktualisiert den Platzplan basierend auf der ausgwählten Vorstellung.
     */
    private void aktualisierePlatzplan()
    {
        if (_vorstellung != null)
        {
            Kinosaal saal = _vorstellung.getKinosaal();
            _view.getPlatzplan()
                .setAnzahlPlaetze(saal.getAnzahlReihen(),
                        saal.getAnzahlSitzeProReihe());

            for (Platz platz : saal.getPlaetze())
            {
                if (_vorstellung.istPlatzVerkauft(platz))
                {
                    _view.getPlatzplan()
                        .markierePlatzAlsVerkauft(platz);
                }
            }
        }
        else
        {
            _view.getPlatzplan()
                .setAnzahlPlaetze(0, 0);
        }
    }

    /**
     * Verkauft die ausgewählten Plaetze nach Bestaetigung durch Barzahlung.
     * 
     * @param vorstellung Die aktuelle Vorstellung. 
     */
    private void verkaufePlaetze(Vorstellung vorstellung)
    {
        Set<Platz> plaetze = _view.getPlatzplan()
            .getAusgewaehltePlaetze();
        
        // TODO: BarzahlungsDialog mithilfe des Controllers erstellen / starten
        if (istVerkaufenMoeglich(plaetze))
        {
        	zeigeBarzahlungsDialog(plaetze, _view.getPlatzplan());
        } 
        else 
        {
            JOptionPane.showMessageDialog(null, "Die ausgewählten Plätze können nicht verkauft werden.");
        }
   
    }

    /**
     * Zeigt den Barzahlungsdialog an und ueberprueft die Bezahlung.
     * 
     * @param plaetze Die zu verkaufenden Plaetze
     * @return true, wenn die Bezahlung erfolgreich war, andernfalls false. 
     */
    private void zeigeBarzahlungsDialog(Set<Platz> plaetze, JPlatzplan platzplan) 
    {
    	Geldbetrag preis = _vorstellung.getPreisFuerPlaetze(plaetze);
        BarzahlungView barzahlungView = new BarzahlungView(preis);
        BarzahlungController barzahlungController = new BarzahlungController(platzplan, barzahlungView, _vorstellung);

        barzahlungController.anzeigeFenster();

        // Den Listener für den Bezahlbutton registrieren, um die erfolgreiche Zahlung zu überprüfen
        barzahlungView.getBezahlenButton().addActionListener(e -> 
        {
            String einzahlungText = barzahlungView.getEinzahlungTextField().getText();
            Geldbetrag einzahlung = Geldbetrag.fromString(einzahlungText).orElse(Geldbetrag.ZERO);

            if (einzahlung.greaterThanOrEqualTo(preis)) 
            {
                _vorstellung.verkaufePlaetze(plaetze);
                aktualisierePlatzplan();
                barzahlungController.schliesseFenster();
            } 
            else 
            {
                JOptionPane.showMessageDialog(null, "Einzahlung unzureichend! Bitte geben Sie einen ausreichenden Betrag ein.");
            }
        });

        // Den Listener für den Abbrechen-Button registrieren, um die Transaktion abzubrechen
        barzahlungView.getAbbrechenButton().addActionListener(e -> 
        {
            // Sicherstellen, dass die ausgewählten Plätze freigegeben werden
            if (istStornierenMoeglich(platzplan.getAusgewaehltePlaetze())) 
            {
                _vorstellung.stornierePlaetze(platzplan.getAusgewaehltePlaetze());
            }
            platzplan.entferneAuswahl();
            barzahlungController.schliesseFenster();
        });
    }
        
	/**
     * Storniert die ausgewählten Plaetze.
     */
    private void stornierePlaetze(Vorstellung vorstellung)
    {
        Set<Platz> plaetze = _view.getPlatzplan()
            .getAusgewaehltePlaetze();
        
        if (istStornierenMoeglich(plaetze))
        	{
        	_vorstellung.stornierePlaetze(plaetze);
        	aktualisierePlatzplan();
        	}
        else 
        {
        	JOptionPane.showMessageDialog(null, "Die ausgewählten Plätze können nicht storniert werden.");

        }
        
    }
}
