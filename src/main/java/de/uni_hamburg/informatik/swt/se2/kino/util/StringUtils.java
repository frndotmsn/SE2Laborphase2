package de.uni_hamburg.informatik.swt.se2.kino.util;

/**
 * Bietet hilfreiche Helferfunktionen für Strings an
 * 
 * @author Fernando Thomsen Canales
 * @version 1.0
 */
public class StringUtils
{
    /**
     * Gibt einen gepaddeten String zurück.
     * 
     * 
     * @param string der zu paddende String, falls die Länge des Strings kleiner als die Mindestlaenge ist
     * @param minLength die Mindestlaenge des Strings der zurueckgegeben wird
     * @param padChar das Zeichen, mit dem gepaddet werden soll
     * @param padDirection die Richtung, in die der String gepadded werden soll
     * @return der gepaddete String
     * 
     * @ensure result.length() >= lengthToPadTo
     * @require string != null
     */
    public static String pad(String string, int minLength, char padChar, LeftRight padDirection)
    {
        assert string != null : "Vorbedingung verletzt: string != null";
        if (string.length() >= minLength)
        {
            return string;
        }
        
        int paddingLength = minLength - string.length();
        String padding = Character.toString(padChar).repeat(paddingLength);
        
        StringBuilder builder = new StringBuilder();
        
        switch (padDirection) {
            case LEFT:
                builder
                    .append(padding)
                    .append(string);
                break;
            case RIGHT:
                builder
                    .append(string)
                    .append(padding);
                break;
        }
        
        return builder.toString();
    }
}
