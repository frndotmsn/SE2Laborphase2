package de.uni_hamburg.informatik.swt.se2.kino.ui.barzahlung;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Das UI des {@link BarzahlungView}.
 * 
 * @author Jonas Schwanzer
 * @version SoSe 2024
 */
public class BarzahlungView
{
    // Die Widgets, aus denen das UI sich zusammensetzt
    private JDialog _dialog;
    private JTextField _einzahlungTextField;
    private JLabel _restbetragLabel;
    private JLabel _fehlermeldungLabel;
    private JLabel _preisWertLabel;
    private JLabel _textRestbetragLabel;
    private JLabel _textEinzahlungLabel;
    private JLabel _textPreisLabel;
    private JButton _bezahlenButton;
    private JButton _abbrechenButton;
    private JPanel _barzahlungPanel;

    /**
     * Initialisert die Bezahlungs-Oberflaeche und nimmt einen Geldbetrag
     * entgegen, welcher gedeckt werden muss.
     * 
     * @param frame Frame des Aufrufendenfensters
     */
    public BarzahlungView(JFrame frame)
    {
        _dialog = new JDialog(frame, "Barzahlung");
        _dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        _dialog.setResizable(false);
        _dialog.setModal(true);
        _dialog.getContentPane()
            .setLayout(new BorderLayout());

        JComponent topPanel = erstelleUeberschriftPanel();
        JComponent centerPanel = erstelleCenterPanel();
        JComponent bottomPanel = erstelleBeendenPanel();

        _dialog.getContentPane()
            .add(topPanel, BorderLayout.NORTH);
        _dialog.getContentPane()
            .add(centerPanel, BorderLayout.CENTER);
        _dialog.getContentPane()
            .add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Zeigt das Fenster an.
     */
    public void zeigeFenster()
    {
        _dialog.pack();
        _dialog.setVisible(true);
    }

    /**
     * Schließt das Fenster.
     */
    public void schliesseFenster()
    {
        _dialog.dispose();
    }

    /**
     * Erzeugt das Panel mit der Überschrift fuer das Programm.
     */
    private JPanel erstelleUeberschriftPanel()
    {
        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        JLabel label = new JLabel("Barzahlung", SwingConstants.CENTER);

        Font font = label.getFont()
            .deriveFont(Font.BOLD + Font.ITALIC, 20);
        label.setFont(font);
        label.setForeground(Color.blue);

        topPanel.setLayout(new BorderLayout());
        topPanel.add(label, BorderLayout.CENTER);

        return topPanel;
    }

    /**
     * Erzeugt das zentrale Panel mit den Bezahlungsfunktionen
     * und einem Label für Fehlermeldungen.
     */
    private JPanel erstelleCenterPanel()
    {
        JPanel centerPanel = new JPanel();
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        centerPanel.setLayout(new BorderLayout());

        _fehlermeldungLabel = new JLabel("", SwingConstants.CENTER);
        _fehlermeldungLabel
            .setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JComponent barzahlungPanel = erstelleBarzahlungPanel();

        centerPanel.add(_fehlermeldungLabel, BorderLayout.SOUTH);
        centerPanel.add(barzahlungPanel, BorderLayout.CENTER);

        return centerPanel;
    }

    /**
     * Erzeugt das Panel mit den Funktionen für die Barbezahlung.
     */
    private JPanel erstelleBarzahlungPanel()
    {
        _barzahlungPanel = new JPanel();
        _barzahlungPanel
            .setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 10));
        _barzahlungPanel.setLayout(new GridLayout(2, 3, 10, 10));
        _textPreisLabel = new JLabel("Preis", SwingConstants.CENTER);
        _preisWertLabel = new JLabel("", SwingConstants.CENTER);
        _textEinzahlungLabel = new JLabel("Einzahlung", SwingConstants.CENTER);
        _textEinzahlungLabel
            .setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        _einzahlungTextField = new JTextField(SwingConstants.CENTER);
        _textRestbetragLabel = new JLabel("Restbetrag", SwingConstants.CENTER);
        _restbetragLabel = new JLabel("", SwingConstants.CENTER);

        _barzahlungPanel.add(_textPreisLabel);
        _barzahlungPanel.add(_textEinzahlungLabel);
        _barzahlungPanel.add(_textRestbetragLabel);
        _barzahlungPanel.add(_preisWertLabel);
        _barzahlungPanel.add(_einzahlungTextField);
        _barzahlungPanel.add(_restbetragLabel);

        return _barzahlungPanel;
    }

    /**
     * Erzeugt das Panel mit dem Abbrechen- und Bezahlen-Button.
     */
    private JPanel erstelleBeendenPanel()
    {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        _abbrechenButton = new JButton("Abbrechen");
        _bezahlenButton = new JButton("Bezahlen");
        _bezahlenButton.setEnabled(false);

        bottomPanel.add(_abbrechenButton);
        bottomPanel.add(_bezahlenButton);

        return bottomPanel;
    }

    /**
     * Gibt das Textfeld für die Eingabe der Einzahlung zurück.
     * 
     * @return Das Textfeld für die Einzahlung.
     */
    public JTextField getEinzahlungTextField()
    {
        return _einzahlungTextField;
    }

    /**
     * Gibt das Label für den Restbetrag zurück.
     * 
     * @return Das Label für den Restbetrag.
     */
    public JLabel getRestbetragLabel()
    {
        return _restbetragLabel;
    }

    /**
     * Gibt das Label für den Preis zurück.
     * 
     * @return Das Label für den Preis.
     */
    public JLabel getPreisWertLabel()
    {
        return _preisWertLabel;
    }

    /**
     * Gibt den "Bezahlen" Button zurück.
     * 
     * @return Der "Bezahlen" Button.
     */
    public JButton getBezahlenButton()
    {
        return _bezahlenButton;
    }

    /**
     * Gibt den "Abbrechen" Button zurück.
     * 
     * @return Der "Abbrechen" Button.
     */
    public JButton getAbbrechenButton()
    {
        return _abbrechenButton;
    }

}
