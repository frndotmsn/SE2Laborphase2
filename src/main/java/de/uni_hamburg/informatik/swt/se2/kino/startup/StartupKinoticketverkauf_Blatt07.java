package de.uni_hamburg.informatik.swt.se2.kino.startup;

import de.uni_hamburg.informatik.swt.se2.kino.entitaeten.Film;
import de.uni_hamburg.informatik.swt.se2.kino.entitaeten.Kino;
import de.uni_hamburg.informatik.swt.se2.kino.entitaeten.Kinosaal;
import de.uni_hamburg.informatik.swt.se2.kino.entitaeten.Vorstellung;
import de.uni_hamburg.informatik.swt.se2.kino.ui.kasse.KassenController;
import de.uni_hamburg.informatik.swt.se2.kino.wertobjekte.Datum;
import de.uni_hamburg.informatik.swt.se2.kino.wertobjekte.FSK;
import de.uni_hamburg.informatik.swt.se2.kino.wertobjekte.Geldbetrag;
import de.uni_hamburg.informatik.swt.se2.kino.wertobjekte.Uhrzeit;

import javax.swing.SwingUtilities;

/**
 * Startet die Anwendung.
 * 
 * @author SE2-Team
 * @version SoSe 2024
 */
public class StartupKinoticketverkauf_Blatt07
{
    /**
     * Die Main-Methode.
     * 
     * @param args die Aufrufparameter.
     */
    public static void main(String[] args)
    {
        pruefeObAssertionsAktiviert();
        final Kino kino = erzeugeKinoMitBeispieldaten();
        SwingUtilities.invokeLater(() -> new KassenController(kino));
    }

    /**
     * Erzeugt ein Kino mit einigen Vorstellungen.
     */
    private static Kino erzeugeKinoMitBeispieldaten()
    {
        final Kinosaal[] saele = {new Kinosaal("Saal 1", 20, 25),
                new Kinosaal("Saal 2", 16, 20), new Kinosaal("Saal 3", 10, 16)};

        // Filme: Top-5 Deutschland laut kino.de in der Kalenderwoche 20, 2011.
        Film[] filme = {
                new Film("Pirates of the Caribbean - Fremde Gezeiten", 136,
                        FSK.FSK12, true),
                new Film("Fast & Furious Five", 130, FSK.FSK12, true),
                new Film("Rio", 96, FSK.FSK0, false),
                new Film("Wasser für die Elefanten", 120, FSK.FSK12, false),
                new Film("Thor", 115, FSK.FSK12, false)};

        Uhrzeit nachmittag = new Uhrzeit(17, 30);
        Uhrzeit abend = new Uhrzeit(20, 0);
        Uhrzeit spaet = new Uhrzeit(22, 30);
        Uhrzeit nacht = new Uhrzeit(1, 0);

        Datum d1 = Datum.heute();
        Datum d2 = d1.naechsterTag();
        Datum d3 = d2.naechsterTag();

        final Vorstellung[] vorstellungen = {
                // Heute
                new Vorstellung(saele[0], filme[2], nachmittag, abend, d1, new Geldbetrag(595)),
                new Vorstellung(saele[0], filme[0], abend, spaet, d1, new Geldbetrag(795)),
                new Vorstellung(saele[0], filme[0], spaet, nacht, d1, new Geldbetrag(795)),

                new Vorstellung(saele[1], filme[3], nachmittag, abend, d1, new Geldbetrag(995)),
                new Vorstellung(saele[1], filme[1], spaet, nacht, d1, new Geldbetrag(895)),

                new Vorstellung(saele[2], filme[3], abend, spaet, d1, new Geldbetrag(1095)),
                new Vorstellung(saele[2], filme[4], spaet, nacht, d1, new Geldbetrag(995)),

                // Morgen
                new Vorstellung(saele[0], filme[0], abend, spaet, d2, new Geldbetrag(595)),
                new Vorstellung(saele[0], filme[0], spaet, nacht, d2, new Geldbetrag(795)),

                new Vorstellung(saele[1], filme[2], nachmittag, abend, d2, new Geldbetrag(995)),
                new Vorstellung(saele[1], filme[4], abend, nacht, d2, new Geldbetrag(895)),

                new Vorstellung(saele[2], filme[3], nachmittag, abend, d2,
                        new Geldbetrag(1095)),
                new Vorstellung(saele[2], filme[1], spaet, nacht, d2, new Geldbetrag(995)),

                // Übermorgen
                new Vorstellung(saele[0], filme[1], abend, spaet, d3, new Geldbetrag(595)),
                new Vorstellung(saele[0], filme[1], spaet, nacht, d3, new Geldbetrag(795)),

                new Vorstellung(saele[1], filme[2], nachmittag, abend, d3, new Geldbetrag(995)),
                new Vorstellung(saele[1], filme[0], abend, nacht, d3, new Geldbetrag(895)),

                new Vorstellung(saele[2], filme[3], abend, spaet, d3, new Geldbetrag(1095)),
                new Vorstellung(saele[2], filme[4], spaet, nacht, d3, new Geldbetrag(995))};

        return new Kino(saele, vorstellungen);
    }

    /**
     * prueft, ob -ea als Default VM Argument hinterlegt ist
     */
    private static void pruefeObAssertionsAktiviert()
    {

        boolean sindAssertionsAktiv = false;
        assert sindAssertionsAktiv = true;
        if (!sindAssertionsAktiv)
        {
            System.err.println("Assertions sind nicht eingeschaltet!\n"
                    + " Schalte sie ein, indem du unter Window -> Preferences-> Java -> installed JREs das genutzte JRE anwähltst, auf Edit klickst und bei Default VM Arguments \"-ea\" einträgst.");
        }
    }
}
