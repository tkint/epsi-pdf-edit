/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 * Created by Thomas on 21/10/2016.
 * @author Thomas Kint
 */
public class TextController {
    /**
     * Ajoute du texte dans le contentStream spécifié
     * @param contentStream
     * @param text
     * @param posX
     * @param posY
     * @param fontSize
     * @param font 
     */
    public void addText(PDPageContentStream contentStream, String text, float posX, float posY, int fontSize, PDType1Font font) {
        try
        {
            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset(posX, posY);
            contentStream.showText(text);
            contentStream.endText();
        } catch(IOException e) {
            System.out.println(e.toString());
        }
    }
}
