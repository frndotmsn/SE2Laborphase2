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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import de.uni_hamburg.informatik.swt.se2.kino.wertobjekte.Geldbetrag;

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
    private JLabel _preisLabel;
    private JButton _bezahlenButton;
    private JButton _abbrechenButton;
    private JLabel _preisWertLabel;

    /**
     * Initialisert die Oberfläche. Die Parameter sind die Views der Submodule,
     * die eingebettet werden.
     */
    public BarzahlungView(Geldbetrag geldbetrag)
    {
        _dialog = new JDialog();
        _dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        _dialog.setResizable(false);
        _dialog.setModal(false);
        _dialog.getContentPane()
            .setLayout(new BorderLayout());

        JComponent topPanel = erstelleUeberschriftPanel();
        JComponent centerPanel = erstelleCenterPanel(geldbetrag);
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
        _dialog.setSize(400, 300);
        _dialog.setVisible(true);
    }

    /**
     * Schließt das Fenster.
     */
    public void schliesseFenster()
    {
        _dialog.dispose();
    }

    private JPanel erstelleUeberschriftPanel()
    {
        JPanel topPanel = new JPanel();
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
     * Erzeugt das Panel mit der Überschrift fuer das Programm.
     */
    private JPanel erstelleCenterPanel(Geldbetrag geldbetrag)
    {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        
        _preisLabel = new JLabel("Fehlermeldung", SwingConstants.CENTER);
        JComponent barzahlungPanel = erstelleBarzahlungPanel(geldbetrag);

        centerPanel.add(_preisLabel, BorderLayout.SOUTH);
        centerPanel.add(barzahlungPanel, BorderLayout.CENTER);

        return centerPanel;
    }

    /**
     * Erzeugt das Panel mit der Überschrift fuer das Programm.
     */
    private JPanel erstelleBarzahlungPanel(Geldbetrag geldbetrag)
    {
        JPanel barzahlungPanel = new JPanel();
        barzahlungPanel.setLayout(new GridLayout(2, 3));
        JLabel textPreisLabel = new JLabel("Preis", SwingConstants.CENTER);
        _preisWertLabel = new JLabel(geldbetrag.toString(),
                SwingConstants.CENTER);
        JLabel textEinzahlungLabel = new JLabel("Einzahlung",
                SwingConstants.CENTER);
        _einzahlungTextField = new JTextField("Preis",
                SwingConstants.CENTER);
        JLabel textRestbetragLabel = new JLabel("Restbetrag",
                SwingConstants.CENTER);
        _restbetragLabel = new JLabel("00,00€", SwingConstants.CENTER);

        barzahlungPanel.add(textPreisLabel);
        barzahlungPanel.add(textEinzahlungLabel);
        barzahlungPanel.add(textRestbetragLabel);
        barzahlungPanel.add(_preisWertLabel);
        barzahlungPanel.add(_einzahlungTextField);
        barzahlungPanel.add(_restbetragLabel);

        return barzahlungPanel;
    }

    /**
     * Erzeugt das Panel mit dem Beenden-Button.
     */
    private JPanel erstelleBeendenPanel()
    {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        _abbrechenButton = new JButton("Abbrechen");
        _bezahlenButton = new JButton("Bezahlen");
        
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
