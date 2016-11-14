/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.controller;

import static app.Config.TRANSLATOR;
import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

/**
 *
 * @author Thomas
 */
public class DocumentController {

    /**
     * Ajoute un PDF au document spécifié
     *
     * @param document
     * @param file
     * @throws IOException
     */
    public void addPDFToDocument(PDDocument document, File file) throws IOException {
        PDDocument doc = null;
        try {
            if (document != null && file.exists()) {
                doc = PDDocument.load(file);
                if (doc != null) {
                    for (PDPage page : doc.getPages()) {
                        document.importPage(page);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            if (doc != null) {
                doc.close();
            }
        }
    }
    
    public void removePage(PDDocument document, int id) {
        if (document.getNumberOfPages() > 1) {
            document.removePage(id);
        } else {
            System.out.println(TRANSLATOR.getString("PAGE_DELETE_FAIL"));
        }
    }
}
