package de.uni_hamburg.informatik.swt.se2.mediathek.startup;

import java.io.File;

import javax.swing.SwingUtilities;

import de.uni_hamburg.informatik.swt.se2.mediathek.services.kundenstamm.KundenstammService;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.kundenstamm.KundenstammServiceImpl;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.medienbestand.MedienbestandService;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.medienbestand.MedienbestandServiceImpl;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.persistenz.DateiLeseException;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.persistenz.DatenEinleser;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.verleih.VerleihService;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.verleih.VerleihServiceImpl;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.vormerk.VormerkService;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.vormerk.VormerkServiceImpl;
import de.uni_hamburg.informatik.swt.se2.mediathek.ui.hauptwerkzeug.MediathekWerkzeug;

/**
 * Startet die Hauptanwendung mit grafischer Oberfläche.
 * 
 * @author SE2-Team
 * @version SoSe 2021
 */
public class StartUpMediathek_Blatt06
{

    private static final File KUNDEN_DATEI = new File(
            "./bestand/kundenstamm.txt");
    private static final File MEDIEN_DATEI = new File(
            "./bestand/medienbestand.txt");

    private static KundenstammService _kundenstamm;
    private static MedienbestandService _medienbestand;
    private static VerleihService _verleihService;
    private static VormerkService _vormerkService;

    /**
     * Main-Methode, mit der die Anwendung gestartet wird.
     */
    public static void main(String[] args)
    {
        erstelleServices();

        final MediathekWerkzeug mediathekWerkzeug = new MediathekWerkzeug(
                _medienbestand, _kundenstamm, _verleihService, _vormerkService);

        // Dies ist die korrekte Art eine Swing-Anwendnung zu starten.
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                mediathekWerkzeug.zeigeFenster();
            }
        });

    }

    /**
     * Erstellt die Services und lädt die Daten.
     */
    private static void erstelleServices()
    {
        try
        {
            DatenEinleser datenEinleser = new DatenEinleser(MEDIEN_DATEI,
                    KUNDEN_DATEI);
            datenEinleser.leseDaten();
            _medienbestand = new MedienbestandServiceImpl(
                    datenEinleser.getMedien());
            _kundenstamm = new KundenstammServiceImpl(
                    datenEinleser.getKunden());
            VormerkServiceImpl vormerkServiceImpl = new VormerkServiceImpl(null); // temporär null
            // verleihService initialisieren, übergibt den vormerkService
            _verleihService = new VerleihServiceImpl(_kundenstamm, _medienbestand, datenEinleser.getVerleihkarten(), vormerkServiceImpl);
           // Verbindung des vormerkService mit dem verleihService
            vormerkServiceImpl = new VormerkServiceImpl(_verleihService);
            _vormerkService = vormerkServiceImpl;
        }
        catch (DateiLeseException e)
        {
            e.printStackTrace();
        }
    }

}
