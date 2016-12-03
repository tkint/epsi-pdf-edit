/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | TemplatesDavid SERVANT <david.servant@appartcity.com>
 * and open the template in the editor.
 */
package src.controller;

import java.io.IOException;
import javafx.scene.paint.Color;
import src.model.table.Cell;
import src.model.table.Row;
import src.model.table.Table;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 * Created by Thomas on 21/10/2016.
 *
 * @author t.kint
 */
public class TableController {

    /**
     * Affiche le contenu d'une cellule
     *
     * @param contentStream
     * @param cell
     * @param alignX
     * @param alignY
     */
    public void printCellContent(PDPageContentStream contentStream, Cell cell, String alignX, String alignY) {
        try {
            TextController textController = new TextController();

            float posX = cell.getPosX();
            float posY = cell.getPosY();

            switch (alignX) {
                case "left":
                    posX += 3;
                    break;
                case "center":
                    posX += (cell.getWidth() - cell.getContent().length() * 7) / 2;
                    break;
                case "right":
                    posX += cell.getWidth() - cell.getContent().length() * 7 - 3;
                    break;
                default:
                    posX += (cell.getWidth() - cell.getContent().length() * 7) / 2;
                    break;
            }

            switch (alignY) {
                case "top":
                    posY -= cell.getHeight() - PDType1Font.COURIER.getBoundingBox().getHeight() / 1000 * 12 - 5;
                    break;
                case "middle":
                    posY -= (cell.getHeight() + PDType1Font.COURIER.getBoundingBox().getHeight() / 1000 * 6) / 2;
                    break;
                case "bottom":
                    posY -= cell.getHeight() - PDType1Font.COURIER.getBoundingBox().getHeight() / 1000 - 3;
                    break;
                default:
                    posY -= (cell.getHeight() + PDType1Font.COURIER.getBoundingBox().getHeight() / 1000 * 6) / 2;
                    break;
            }

            // Affichage du texte dans la cellule
            textController.addText(contentStream, cell.getContent(), posX, posY, 12, PDType1Font.COURIER);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Dessine un tableau
     *
     * @param contentStream
     * @param table
     */
    public void printTable(PDPageContentStream contentStream, Table table) {
        try {
            for (Row row : table.getRows()) {
                for (Cell cell : row.getCells()) {
                    printRectangle(contentStream, cell.getPosX(), cell.getPosY(), cell.getWidth(), cell.getHeight(), cell.getBackgroundColor());
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Dessine un rectangle
     *
     * @param contentStream
     * @param posX
     * @param posY
     * @param width
     * @param height
     */
    private void printRectangle(PDPageContentStream contentStream, float posX, float posY, float width, float height, Color backgroundColor) {
        try {
            printLine(contentStream, posX, posY, posX + width, posY);
            printLine(contentStream, posX, posY, posX, posY - height);
            printLine(contentStream, posX + width, posY, posX + width, posY - height);
            printLine(contentStream, posX, posY - height, posX + width, posY - height);
            
            contentStream.addRect(posX, posY - height, width, height);
            contentStream.setNonStrokingColor(new java.awt.Color((float) backgroundColor.getRed(), (float) backgroundColor.getGreen(), (float) backgroundColor.getBlue(), (float) backgroundColor.getOpacity()));
            contentStream.fill();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Dessine une ligne
     *
     * @param contentStream
     * @param posX
     * @param posY
     * @param posLastX
     * @param posLastY
     */
    private void printLine(PDPageContentStream contentStream, float posX, float posY, float posLastX, float posLastY) {
        try {
            contentStream.moveTo(posX, posY);
            contentStream.lineTo(posLastX, posLastY);
            contentStream.stroke();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}
