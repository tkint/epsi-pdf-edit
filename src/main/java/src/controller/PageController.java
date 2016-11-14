/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

/**
 *
 * @author Thomas
 */
public class PageController {

    public void extractPage(PDDocument document, int id, String filename) {
        try {
            PDDocument doc = new PDDocument();
            if (document.getPage(id) != null) {
                doc.addPage(document.getPage(id));
            }
            doc.save(filename + ".pdf");
            doc.close();
            System.out.println("La page " + id + " a bien été extraite.");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    public void rotatePage(PDDocument document, int id, int degree) {
        PDPage page = document.getPage(id);
        page.setRotation(page.getRotation() + degree);
    }
}
