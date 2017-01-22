/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.controller;

import static app.Config.TRANSLATOR;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

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
                    PDFMergerUtility merger = new PDFMergerUtility();
                    merger.appendDocument(document, doc);
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

    /**
     * Supprime la page spécifiée d'un document
     * @param document
     * @param id 
     */
    public void removePage(PDDocument document, int id) {
        if (document.getNumberOfPages() > 1) {
            document.removePage(id);
        } else {
            System.out.println(TRANSLATOR.getString("PAGE_DELETE_FAIL"));
        }
    }

    /**
     * Convertion en image
     *
     * @param file
     * @param extension
     */
    public void convertToImage(File file, String extension) {
        PDDocument document;
        try {
            document = PDDocument.load(file);
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            int pageCounter = 0;

            for (PDPage lapage : document.getPages()) {
                // note that the page number parameter is zero based
                BufferedImage bim = pdfRenderer.renderImageWithDPI(pageCounter, 300, ImageType.RGB);

                // suffix in filename will be used as the file format
                ImageIOUtil.writeImage(bim, file + "-" + (pageCounter++) + extension, 300);
            }
            document.close();
        } catch (IOException ex) {
            Logger.getLogger(DocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
