/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import app.Config;
import java.io.File;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import src.controller.ImageController;
import src.controller.TableController;
import src.model.table.Cell;
import src.model.table.Table;

/**
 * FXML Controller class
 *
 * @author t.kint
 */
public class MainController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void generateDocument() {
        try {
            PDDocument document = new PDDocument();
            document.save(Config.docTitle + ".pdf");
            document.close();
            System.out.println("Document généré");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
    
    public void testAddImage() {
        try {
            ImageController imageController = new ImageController();
            
            // Chargement du document
            File file = new File(Config.docTitle + ".pdf");
            PDDocument document = PDDocument.load(file);
            
            document.addPage(new PDPage());
            
            PDPageContentStream contentStream = new PDPageContentStream(document, document.getPage(document.getNumberOfPages() - 1), PDPageContentStream.AppendMode.APPEND, true);
            
            // Ajout d'une image sur la deuxième page
            imageController.addImage(document, contentStream, Config.img, 100, 400, 0.2f);
            
            contentStream.close();
            
            document.save(Config.docTitle + ".pdf");
            document.close();
            
            System.out.println("Image ajoutée");
        } catch(IOException e) {
            System.out.println(e.toString());
        }
    }
    
    public void testExtractImage() {
        try {
            ImageController imageController = new ImageController();
            
            // Chargement du document
            File file = new File(Config.docTitle + ".pdf");
            PDDocument document = PDDocument.load(file);

            // Extraction de l'image définie dans le format défini
            imageController.extractImageByName(document, "Im1", "png");
            
            // Fermeture du document
            document.close();
            
            System.out.println("Image extraite");
        } catch(IOException e) {
            System.out.println(e.toString());
        }
    }
    
    public void testAddTable() {
        try {
            TableController tableController = new TableController();
            
            // Chargement du document
            File file = new File(Config.docTitle + ".pdf");
            PDDocument document = PDDocument.load(file);
            
            document.addPage(new PDPage());
            
            PDPageContentStream contentStream = new PDPageContentStream(document, document.getPage(document.getNumberOfPages() - 1), PDPageContentStream.AppendMode.APPEND, true);
            
            // Instanciation d'un tableau
            Table table = new Table(0, 100, 600, 200, 200);
            // Génération du tableau
            table.generateTable(2, 7);
            table.addColumns(2);
            
            // Récupération d'une cellule
            Cell cell = table.getCell(0);
            // Initialisation du contenu
            cell.setContent("TEST");
            // Affichage du contenu dans la cellule
            tableController.displayCellContent(contentStream, cell, "center", "middle");
            
            // Ajout d'un tableau
            tableController.drawTable(contentStream, table);
            
            contentStream.close();
            document.save(Config.docTitle + ".pdf");
            
            System.out.println("Tableau ajouté");
        } catch(IOException e) {
            System.out.println(e.toString());
        }
    }
}
