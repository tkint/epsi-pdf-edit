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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
    private String filename;
    private PDDocument document;
    private Stage stage;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void btnHelpAbout() {
        String text = Config.APP_TITLE;
        
        text += '\n' + "Version: " + Config.APP_VERSION;
        text += '\n' + "Auteurs: " + '\n' + Config.APP_AUTHORS;
        text += '\n' + "Société: " + Config.APP_SOCIETY;
        text += '\n' + "Date: " + Config.APP_DATE;
        
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.initStyle(StageStyle.UNIFIED);
        alert.setTitle("A propos");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }
    
    public void btnFileNew() {
        this.document = new PDDocument();
        this.document.addPage(new PDPage());
    }
    
    public void btnFileOpen() throws IOException {
        if (this.stage != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Ouvrir un fichier");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            File file = fileChooser.showOpenDialog(this.stage);
            if (file != null) {
                this.filename = file.getName();
                this.document = PDDocument.load(file);
                System.out.println("Filename: " + this.filename);
                System.out.println("Document: " + this.document);
            }
        }
    }
    
    public void btnFileSave() throws IOException {
        if (this.filename != null) {
            if (this.document != null) {
                this.document.save(this.filename);
                this.document.close();
                System.out.println("Document " + this.filename + " a été enregistré");
            } else {
                System.out.println("Pas de document");
            }
        } else {
            btnFileSaveAs();
        }
    }
    
    public void btnFileSaveAs() throws IOException {
        if (this.document != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sauvegarder le fichier");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            File file = fileChooser.showSaveDialog(this.stage);
            if (file != null) {
                this.filename = file.getName();
                this.document.save(file);
                this.document.close();
            }
        }
    }
    
    public void btnFileExit() {
        System.exit(0);
    }
        
    public void testCreateFile() {
        try {
            PDDocument document = new PDDocument();
            document.save(Config.DOC_TITLE + ".pdf");
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
            File file = new File(Config.DOC_TITLE + ".pdf");
            PDDocument document = PDDocument.load(file);
            
            document.addPage(new PDPage());
            
            PDPageContentStream contentStream = new PDPageContentStream(document, document.getPage(document.getNumberOfPages() - 1), PDPageContentStream.AppendMode.APPEND, true);
            
            // Ajout d'une image sur la deuxième page
            imageController.addImage(document, contentStream, Config.IMG_NAME, 100, 400, 0.2f);
            
            contentStream.close();
            
            document.save(Config.DOC_TITLE + ".pdf");
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
            File file = new File(Config.DOC_TITLE + ".pdf");
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
            File file = new File(Config.DOC_TITLE + ".pdf");
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
            document.save(Config.DOC_TITLE + ".pdf");
            
            System.out.println("Tableau ajouté");
        } catch(IOException e) {
            System.out.println(e.toString());
        }
    }
}
