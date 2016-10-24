/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | TemplatesDavid SERVANT <david.servant@appartcity.com>
 * and open the template in the editor.
 */
package controller;

import model.table.Cell;
import model.table.Row;
import model.table.Table;
import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;

/**
 * Created by Thomas on 21/10/2016.
 * @author t.kint
 */
public class TableController {
    public void drawTable(PDDocument document, PDPage page, Table table) {
        try {
            PDPageContentStream content = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
            
            float x = table.getPosX();
            float y = table.getPosY();
            for (Row row : table.getRows()) {
                for (Cell cell : row.getCells()) {
                    drawRectangle(content, x, y, cell.getWidth(), cell.getHeight());
                    x += cell.getWidth();
                }
                y -= row.getHeight();
                x = table.getPosX();
            }

            content.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void drawRectangle(PDPageContentStream content, float posX, float posY, float width, float height) {
        try {
            drawLine(content, posX, posY, posX + width, posY);
            drawLine(content, posX, posY, posX, posY - height);
            drawLine(content, posX + width, posY, posX + width, posY - height);
            drawLine(content, posX, posY - height, posX + width, posY - height);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void drawLine(PDPageContentStream content, float posX, float posY, float posLastX, float posLastY) {
        try {
            content.moveTo(posX, posY);
            content.lineTo(posLastX, posLastY);
            content.stroke();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
