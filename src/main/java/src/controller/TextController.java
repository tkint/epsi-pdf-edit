/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.controller;

import java.awt.Color;
import java.io.IOException;
import javafx.scene.control.TextArea;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import src.model.AreaSelect;
import src.view.displayer.PageDisplayer;
import src.view.displayer.TraceDisplayer;

/**
 * Created by Thomas on 21/10/2016.
 *
 * @author Thomas Kint
 */
public class TextController {

    /**
     * Ajoute du texte dans le contentStream spécifié
     *
     * @param contentStream
     * @param text
     * @param posX
     * @param posY
     * @param fontSize
     * @param font
     */
    public void addText(PDPageContentStream contentStream, String text, float posX, float posY, int fontSize, PDType1Font font) {
        try {
            String[] lines = text.split("\n");
            for (int i = 0; i < lines.length; i++) {
                contentStream.beginText();
                contentStream.setFont(font, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(posX, posY - (fontSize * i + 5));
                contentStream.showText(lines[i]);
                contentStream.endText();
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void drawArea(PDPageContentStream contentStream, AreaSelect area, String text, int taille, PDType1Font font) {

        //try {
        float posX = (float) area.getPosX();
        float posY = (float) area.getPosY();

        addText(contentStream, text, posX, posY, taille, font);

        /*} catch (IOException e) {
            System.out.println(e.toString());
        }*/
    }
}
