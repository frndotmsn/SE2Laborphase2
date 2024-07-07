package de.uni_hamburg.informatik.swt.se2.kino.wertobjekte;

import java.lang.String;
import java.util.Optional;

import de.uni_hamburg.informatik.swt.se2.kino.util.LeftRight;
import de.uni_hamburg.informatik.swt.se2.kino.util.StringUtils;

import java.lang.Math;

/**
 * Repraesentiert einen Geldbetrag.
 * Der Geldbetrag kann sowohl negativ als auch positiv sein.
 * Ist durch {@link Geldbetrag.MIN_VALUE} und {@link Geldbetrag.MAX_VALUE} begrenzt.
 *
 * @author Martin & Fernando Thomsen Canales
 * @version 2.0
 */
public record Geldbetrag(int eurocent) {
    public static final Geldbetrag MAX_VALUE = new Geldbetrag(Integer.MAX_VALUE);
    public static final Geldbetrag MIN_VALUE = new Geldbetrag(Integer.MIN_VALUE);
    public static final Geldbetrag ZERO = new Geldbetrag(0);
    
    public static final int CENT_STELLEN = 2;
    public static final int MIN_EURO_STELLEN = 2;
    /**
     * Addiere diesen Geldbetrag mit einem weiteren.
     *
     * @param summand Geldbetrag zum addieren
     * @return die Summe der beiden Geldbetraege
     * 
     * @require summand != null
     */
    public Geldbetrag add(Geldbetrag summand) {
        assert summand != null : "Vorbedingung verletzt: geldbetrag != null";
        try {
            return new Geldbetrag(Math.addExact(eurocent, summand.eurocent));
        }
        catch(ArithmeticException e) {
            // positiv + positiv => positiv
            // positiv + negativ => kein arithmetischer Fehler
            // negativ + positiv => kein arithmetischer Fehler
            // negativ + negativ => negativ
            return eurocent > 0 ? Geldbetrag.MAX_VALUE : Geldbetrag.MIN_VALUE;
        }
    }

    /**
     * Subtrahiere von diesem Geldbetrag einen Anderen.
     *
     * @param subtrahend den Geldbetrag zum subtrahieren
     * @return die Differenz zwischen zwei Geldbetraegen, {@link Geldbetrag.MIN_VALUE} falls der 
     *
     * @require subtrahend != null
     */
    public Geldbetrag subtract(Geldbetrag subtrahend) {
        assert subtrahend != null : "Vorbedingung verletzt: geldbetrag != null";
        try {
            return new Geldbetrag(Math.subtractExact(eurocent, subtrahend.eurocent));
        }
        catch(ArithmeticException e) {
            // negativ - negativ => positiv, falls subtrahend kleiner als minuend, negativ sonst
            // negativ - positiv => negativ
            // positiv - negativ => positiv
            // positiv - positiv = positiv, falls subtrahend kleiner als minuend, negativ sonst
            
            // gleiche Vorzeichen
            if (eurocent > 0 == subtrahend.eurocent > 0)
            {
                return eurocent < subtrahend.eurocent ? Geldbetrag.MAX_VALUE : Geldbetrag.MIN_VALUE;
            }
            
            // ungleiche Vorzeichen
            // positiv - negativ => positiv
            if (eurocent > 0 && subtrahend.eurocent < 0)
            {
                return Geldbetrag.MAX_VALUE;
            }
            
            // negativ - positiv => negativ
            if (eurocent < 0 && subtrahend.eurocent > 0)
            {
                return Geldbetrag.MIN_VALUE;
            }
            
            // hierhin darf man nicht kommen!
            // außer 0 - x mit x = -2^31
            // weil dann ggf overflow!
            // da ja von -2^31 bis 2^31-1
            // und 0 - (-2^31) nicht abgebildet werden kann!
            // einfach Geldbetrag.MAX_VALUE zurückgeben
            
            return Geldbetrag.MAX_VALUE;
        }
    }

    /**
     * Multipliziere diesen Geldbetrag mit einem scalar.
     *
     * @param scalar den Scalar
     * @return den Geldbetrag mit dem Scalar mulitpiziert
     */
    public Geldbetrag multiply(int scalar) {
        try {
            return new Geldbetrag(Math.multiplyExact(eurocent, scalar));
        }
        catch(ArithmeticException e) {
            // Falls die Vorzeichen der Faktoren identisch sind, ist das Ergebnis positiv
            // Bei unterschiedlichen Vorzeichen ist das Ergebnis negativ
            return scalar > 0 == eurocent > 0 ? Geldbetrag.MAX_VALUE : Geldbetrag.MIN_VALUE;
        }
    }
    
    
    @Override
    public String toString()
    {
        // rundet zur 0 falls das nicht glatt aufgeht
        // geht nur weil zur 0 gerundet wird, würde abgerundet werden würde das nicht funktionieren!
        // Beispiel:
        // eurocent = -3131330
        // => euro = -31313
        // + cent = 30
        // also "-31313,30"
        //
        // Würde abgerundet werden:
        // eurocent = -3131330
        // => euro = -31314
        // + cent = 30
        // also "-31314,30" <- geht nicht auf!
        
        boolean negativ = eurocent < 0; 
        
        int euro = eurocent / 100;
        
        // Math.abs, weil wir - separat behandeln
        String euroString = Integer.toString(Math.abs(euro));
        String paddedEuroString = StringUtils.pad(euroString, MIN_EURO_STELLEN, '0', LeftRight.LEFT);
        
        // wir wollen nicht dass hier ggf. auch noch ein - steht!
        int cent = Math.abs(eurocent % 100);
        String centString = Integer.toString(cent);
        String paddedCentString = StringUtils.pad(centString, CENT_STELLEN, '0', LeftRight.LEFT);

        return new StringBuilder()
                .append(negativ ? "-" : "")
                .append(paddedEuroString)
                .append(',')
                .append(paddedCentString)
                .toString();
    }

    /**
     * Uebersetzt einen String in einen Geldbetrag mit optionalen Cent stellen.
     * Der String muss dabei in einem der folgenden Formate sein:
     *  1. ganze Zahl, positiv oder negativ
     *     Die Ziffernfolge wird als Zahl in Euro interpretiert.
     *     Z.B. 12311231 oder -23132131
     *  2. Zahl, positiv oder negativ, mit max. 2 Nachkommastellen
     *     Die Vorkommastellen werden als Betrag in Euro, die Nachkommastellen als Cent interpretiert.
     *     Beispiele:
     *     "2131231,13" (2131231 Euro und 13 Cent)
     *     "-123131,2"  (123131 Euro und 20 Cent, negativ)
     *     "123,"       (123 Euro und 0 Cent)
     *  3. Leerer String, es wird {@link Geldbetrag.ZERO } zurueckgegeben
     *
     * Maximalwert ist hierbei Geldbetrag.MAX_VALUE und Minimalwert Geldbetrag.MIN_VALUE
     * Falls der eingegebene String diese Grenzen überschreitet, wird dementsprechend der jeweilige Extremwert zurückgegeben
     * Beispiele:
     * "1367736176312671678,13" => Geldbetrag.MAX_VALUE
     * "-3113221123131312" => Geldbetrag.MIN_VALUE
     * 
     * @param string den string zum uebersetzen
     * @return den Geldbetrag der von string uebersetzt wurde, {@link Optional.empty()} falls der String in einem ungueltigen Format ist.
     * 
     * @require string != null
     * @require string.split(",").length <= 1
     */
    public static Optional<Geldbetrag> fromString(String string) {
        assert string != null : "Vorbedingung verletzt: string != null";
        
        String[] parts = string.split(",", 2);
        
        String firstHalf = parts.length > 0 ? parts[0] : "";
        if (!firstHalf.matches("-{0,1}\\d*"))
        {
            return Optional.empty();
        }
        
        String secondHalf = parts.length > 1 ? parts[1] : "";
        if (!secondHalf.matches("\\d{0,2}"))
        {
            return Optional.empty();
        }
        
        // '-' am Anfang muss sowohl für Euro als auch für Cent gelten!
        // Sonst wird -300,21 als -30000 + 21 Eurocent interpretiert!
        // Einfach am Ende mit -1 multiplizieren, falls '-' am Anfang!
        boolean negativ = firstHalf.startsWith("-");
        
        // entfernt das '-' als präfix
        if (negativ)
        {
            firstHalf = firstHalf.substring(1);
        }
        
        int euro = 0;
        if (!firstHalf.isEmpty())
        {
            try
            {
                euro = Integer.parseInt(firstHalf);
            }
            catch (NumberFormatException e)
            {
                return Optional.empty();
            }
        }
        
        // eine Ziffer als Nachkommastelle
        // z.B. .....,9 sollte als .... Euro + 90 Cent interpretiert werden!
        // Ansatz: 0 anhängen, also aus "9" wird "90"
        // Mithilfe von StringUtils.pad mit padDirection = LeftRight.Right rechts 0'en ranfügen, bis man 2 Stellen hat!
        // Dann wird auch "" zu "00" gepadded und kann als int geparsed werden!
        String paddedSecondHalf = StringUtils.pad(secondHalf, 2, '0', LeftRight.RIGHT);
        
        // hier sollte niemals eine NumberFormatException auftreten
        // da vorher ja per regex geprüft wird, dass nur Ziffern drin sind
        // und der String mit 0en auf 2 Stellen gefüllt wird!
        int cent = Integer.parseInt(paddedSecondHalf);
        
        if (cent >= 100)
        {
            return Optional.empty();
        }
        
        int euroInCent;
        try
        {
            euroInCent = Math.multiplyExact(euro, 100);
        }
        catch (ArithmeticException e)
        {
            euroInCent = euro > 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
        
        int summeInEurocent;
        try
        {
            summeInEurocent = Math.addExact(euroInCent, cent);
        }
        catch (ArithmeticException e)
        {
            summeInEurocent = euroInCent > 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
        
        if (negativ)
        {
            summeInEurocent *= -1;
        }
        
        return Optional.of(new Geldbetrag(summeInEurocent));
    }
    
    /**
     * Konvertiert einen String in das Geldbetrag Standard String Repräsentatiosnformat
     * 
     * Gibt {@link Optional.empty() } zurück, falls string nicht einem von {@link #fromString} definierten Format entspricht.
     * 
     * @param string der zu formatierende String
     * @return der formatierte String im standard Format nach {@link #toString()}
     */
    public static Optional<String> inStandardFormat(String string)
    {
        return fromString(string).map(Geldbetrag::toString);
    }
}
