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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.*;
import src.controller.*;
import src.model.DocFile;
import src.model.table.*;
import src.view.Displayer;
import src.view.controller.menu.*;

/**
 * FXML Controller class
 *
 * @author Thomas
 */
public class MainController implements Config, Initializable {

    private static final Instance INSTANCE = Instance.getInstance();
    private static final MenuFile MENUFILE = MenuFile.getInstance();
    private static final MenuEdit MENUEDIT = MenuEdit.getInstance();
    private static final MenuDocument MENUDOCUMENT = MenuDocument.getInstance();
    private static final MenuPage MENUPAGE = MenuPage.getInstance();
    private static final MenuTools MENUTOOLS = MenuTools.getInstance();
    private static final MenuHelp MENUHELP = MenuHelp.getInstance();

    @FXML
    public Menu menuFileOpenRecent;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addMenuFileOpenRecent();
    }

    /**
     * Ajoute les fichiers récents au menu Fichier Ouvrir...
     *
     */
    public void addMenuFileOpenRecent() {
        for (File file : INSTANCE.loadSaveFile(TRANSLATOR.getString("APP_NAME") + "_recent")) {
            if (file.exists()) {
                MenuItem menuItem = new MenuItem(file.getName());
                menuItem.setOnAction((ActionEvent event) -> {
                    try {
                        if (INSTANCE.isFileAlreadyOpened(file)) {
                            Displayer.selectDocFileTab(Displayer.defineTabName(file.getName().substring(0, file.getName().length() - 4)));
                        } else {
                            DocFile docFile = INSTANCE.addFile(file);
                            Displayer.displayDocFileNewTab(docFile, docFile.getShortFileName());
                        }
                    } catch (IOException e) {
                        System.out.println(e.toString());
                    }
                });
                menuFileOpenRecent.getItems().add(menuItem);
            }
        }
    }

    /**
     * Renseigne son stage dans l'instance
     *
     * @param stage
     */
    public void setStage(Stage stage) {
        INSTANCE.stageName = "main";
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

    //  <editor-fold desc="Document">
    public void btnDocumentAddPage() {
        MENUDOCUMENT.btnDocumentAddPage();
    }

    public void btnDocumentRemovePage() {
        MENUDOCUMENT.btnDocumentRemovePage();
    }

    public void btnDocumentAddDocument() {
        MENUDOCUMENT.btnDocumentAddDocument();
    }
    //  </editor-fold>

    //  <editor-fold desc="Page">
    public void btnPageRotateRight() {
        MENUPAGE.btnPageRotateRight();
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
            System.out.println(TRANSLATOR.getString("TEST_FILE_GENERATED"));
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

            System.out.println(TRANSLATOR.getString("TEST_IMAGE_ADDED"));
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

            System.out.println(TRANSLATOR.getString("TEST_IMAGE_EXTRACTED"));
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

            System.out.println(TRANSLATOR.getString("TEST_TABLE_ADDED"));
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
    //  </editor-fold>
}
