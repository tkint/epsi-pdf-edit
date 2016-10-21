/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 * Created by Thomas on 21/10/2016.
 * @author Thomas Kint
 */
public class TextController {
    /**
     * Ajoute du texte dans le document à la page et position spécifiée
     * @param document
     * @param page
     * @param text
     * @param posX
     * @param posY
     * @param fontSize
     * @param font 
     */
    public void addText(PDDocument document, PDPage page, String text, int posX, int posY, int fontSize, PDType1Font font) {
        try
        {
            PDPageContentStream contents = new PDPageContentStream(document, page);
            contents.beginText();
            contents.setFont(font, fontSize);
            contents.newLineAtOffset(posX, posY);
            contents.showText(text);
            contents.endText();
            contents.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
