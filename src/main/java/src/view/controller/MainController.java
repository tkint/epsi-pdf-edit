/*
 * To change instance license header, choose License Headers in Project Properties.
 * To change instance template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.controller;

import app.Config;
import app.Instance;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.*;
import src.controller.*;
import src.model.table.*;
import src.view.controller.menu.*;

/**
 * FXML Controller class
 *
 * @author t.kint
 */
public class MainController implements Config, Initializable {

    private static final Instance INSTANCE = Instance.getInstance();
    private static final MenuFile MENUFILE = MenuFile.getInstance();
    private static final MenuEdit MENUEDIT = MenuEdit.getInstance();
    private static final MenuTools MENUTOOLS = MenuTools.getInstance();
    private static final MenuHelp MENUHELP = MenuHelp.getInstance();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setStage(Stage stage) {
        INSTANCE.stage = stage;
    }

    //  <editor-fold desc="File">
    /**
     * Initialise un nouveau document
     */
    public void btnFileNew() {
        MENUFILE.btnFileNew();
    }

    /**
     * Ouvre un document déjà existant
     */
    public void btnFileOpen() {
        MENUFILE.btnFileOpen();
    }

    /**
     * Enregistre un document déjà existant, sinon fait appel à saveAs
     */
    public void btnFileSave() {
        MENUFILE.btnFileSave();
    }

    /**
     * Enregistre un nouveau document
     */
    public void btnFileSaveAs() {
        MENUFILE.btnFileSaveAs();
    }

    /**
     * Quitte l'application
     */
    public void btnFileExit() {
        MENUFILE.btnFileExit();
    }
    //  </editor-fold>

    //  <editor-fold desc="Edit">
    public void btnEditDelete() {
        MENUEDIT.btnEditDelete();
    }
    //  </editor-fold>

    //  <editor-fold desc="Tools">
    /**
     * Extrait du document ouvert la page définie par l'utilisateur
     */
    public void btnToolsExtractPage() {
        MENUTOOLS.btnToolsExtractPage();
    }

    /**
     * Extrait toutes les pages du document ouvert
     */
    public void btnToolsExtractPages() {
        MENUTOOLS.btnToolsExtractPages();
    }
    //  </editor-fold>

    //  <editor-fold desc="Help">
    /**
     * Affiche une popup A propos
     */
    public void btnHelpAbout() {
        MENUHELP.btnHelpAbout();
    }
    //  </editor-fold>

    //  <editor-fold desc="Test">
    public void testCreateFile() {
        try {
            PDDocument document = new PDDocument();
            document.save(TEST_DOC_TITLE + ".pdf");
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
            File file = new File(TEST_DOC_TITLE + ".pdf");
            PDDocument document = PDDocument.load(file);

            document.addPage(new PDPage());

            PDPageContentStream contentStream = new PDPageContentStream(document, document.getPage(document.getNumberOfPages() - 1), PDPageContentStream.AppendMode.APPEND, true);

            // Ajout d'une image sur la deuxième page
            imageController.addImage(document, contentStream, TEST_IMG_NAME, 100, 400, 0.2f);

            contentStream.close();

            document.save(TEST_DOC_TITLE + ".pdf");
            document.close();

            System.out.println("Image ajoutée");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void testExtractImage() {
        try {
            ImageController imageController = new ImageController();

            // Chargement du document
            File file = new File(TEST_DOC_TITLE + ".pdf");
            PDDocument document = PDDocument.load(file);

            // Extraction de l'image définie dans le format défini
            imageController.extractImageByName(document, "Im1", "png");

            // Fermeture du document
            document.close();

            System.out.println("Image extraite");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void testAddTable() {
        try {
            TableController tableController = new TableController();

            // Chargement du document
            File file = new File(TEST_DOC_TITLE + ".pdf");
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
            document.save(TEST_DOC_TITLE + ".pdf");

            System.out.println("Tableau ajouté");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
    //  </editor-fold>
}
