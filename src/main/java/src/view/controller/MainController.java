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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.*;
import src.controller.*;
import src.model.DocFile;
import src.model.table.*;
import src.view.displayer.TabDisplayer;
import src.view.controller.menu.*;

/**
 * FXML Controller class
 *
 * @author Thomas
 */
public class MainController implements Config, Initializable {

    private static final Instance INSTANCE = Instance.getInstance();

    @FXML
    public Menu menuFileOpenRecent;

    @FXML
    public ChoiceBox zoomChoice;

    @FXML
    public Button toolbarAddText;

    @FXML
    public Button toolbarAddImage;

    @FXML
    public Button toolbarAddTable;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addMenuFileOpenRecent();
        addZoomChoice();
    }

    /**
     * Ajoute les fichiers récents au menu Fichier Ouvrir...
     *
     */
    private void addMenuFileOpenRecent() {
        for (File file : INSTANCE.loadSaveFile(TRANSLATOR.getString("APP_NAME") + "_recent")) {
            if (file.exists()) {
                MenuItem menuItem = new MenuItem(file.getName());
                menuItem.setOnAction((ActionEvent event) -> {
                    try {
                        if (INSTANCE.isAlreadyOpened(file)) {
                            TabDisplayer.selectTab(TabDisplayer.defineTabName(file.getName().substring(0, file.getName().length() - 4)));
                        } else {
                            DocFile docFile = INSTANCE.addFile(file);
                            TabDisplayer.displayDocFileInNewTab(docFile, docFile.getShortFileName());
                        }
                    } catch (IOException e) {
                        System.out.println(e.toString());
                    }
                });
                menuFileOpenRecent.getItems().add(menuItem);
            }
        }
    }

    private void addZoomChoice() {
        for (Integer zoom : ZOOMS) {
            zoomChoice.getItems().add(Integer.toString(zoom));
        }
        zoomChoice.getSelectionModel().select(Integer.toString(100));
        zoomChoice.setOnAction((actionEvent) -> {
            if (zoomChoice.isShowing()) {
                MenuView.chooseZoom(Integer.parseInt(zoomChoice.getSelectionModel().getSelectedItem().toString()));
            }
        });

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
        MenuFile.btnFileNew();
    }

    /**
     * Ouvre un document déjà existant
     */
    public void btnFileOpen() {
        MenuFile.btnFileOpen();
    }

    /**
     * Enregistre un document déjà existant, sinon fait appel à saveAs
     */
    public void btnFileSave() {
        MenuFile.btnFileSave();
    }

    /**
     * Enregistre un nouveau document
     */
    public void btnFileSaveAs() {
        MenuFile.btnFileSaveAs();
    }

    /**
     * Converti un document en PNG
     */
    public void btnConvertToPNG() {
        MenuFile.btnConvertToPNG();
    }

    /**
     * Converti un document en JPEG
     */
    public void btnConvertToJPEG() {
        MenuFile.btnConvertToJPEG();
    }

    /**
     * Converti un document en GIF
     */
    public void btnConvertToGIF() {
        MenuFile.btnConvertToGIF();
    }
    
    public void btnConvertToDocx() {
        MenuFile.btnConvertToDocx();
    }

    /**
     * Quitte l'application
     */
    public void btnFileExit() {
        MenuFile.btnFileExit();
    }
    //  </editor-fold>

    //  <editor-fold desc="Edit">
    public void btnAddText() {
        MenuEdit.btnAddText(toolbarAddText);
    }

    public void btnEditText() {
        MenuEdit.btnEditText();
    }

    public void btnEditImage() {
        MenuEdit.btnEditImage();
    }

    public void btnAddImage() {
        MenuEdit.btnAddImage(toolbarAddImage);
    }

    public void btnEditTable() {
        MenuEdit.btnEditTable();
    }

    public void btnAddTable() {
        MenuEdit.btnAddTable(toolbarAddTable);
    }
    //  </editor-fold>

    //  <editor-fold desc="View">
    public void btnViewZoomIn() {
        MenuView.btnZoomIn();
    }

    public void btnViewZoomOut() {
        MenuView.btnZoomOut();
    }

    public void btnViewZoomDefault() {
        MenuView.chooseZoom(100);
    }
    //  </editor-fold>

    //  <editor-fold desc="Document">
    public void btnDocumentAddPage() {
        MenuDocument.btnDocumentAddPage();
    }

    public void btnDocumentRemovePage() {
        MenuDocument.btnDocumentRemovePage();
    }

    public void btnDocumentAddDocument() {
        MenuDocument.btnDocumentAddDocument();
    }
    //  </editor-fold>

    //  <editor-fold desc="Page">
    public void btnPageRotateRight() throws IOException {
        MenuPage.btnPageRotateRight();
    }

    public void btnPageRotateLeft() throws IOException {
        MenuPage.btnPageRotateLeft();
    }

    public void btnPageFlipHorizontal() {

    }

    public void btnPageFlipVertical() {

    }
    //  </editor-fold>

    //  <editor-fold desc="Tools">
    /**
     * Extrait du document ouvert la page définie par l'utilisateur
     */
    public void btnToolsExtractPage() {
        MenuTools.btnToolsExtractPage();
    }

    /**
     * Extrait toutes les pages du document ouvert
     */
    public void btnToolsExtractPages() {
        MenuTools.btnToolsExtractPages();
    }

    public void btnToolsExtractImage() throws IOException {
        MenuTools menuTools = new MenuTools();
        menuTools.btnToolsExtractImage();
    }

    //  </editor-fold>
    //  <editor-fold desc="Help">
    /**
     * Affiche une popup A propos
     */
    public void btnHelpAbout() {
        MenuHelp.btnHelpAbout();
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
            Table table = new Table(100, 600, 200, 200);
            // Génération du tableau
            table.generateTable(2, 7);
            table.addColumns(2);

            // Récupération d'une cellule
            Cell cell = table.getCell(0);
            // Initialisation du contenu
            cell.setContent("TEST");
            // Affichage du contenu dans la cellule
            tableController.printCellContent(contentStream, cell, "center", "middle");

            // Ajout d'un tableau
            tableController.printTable(contentStream, table, false);

            contentStream.close();
            document.save(TEST_DOC_TITLE + ".pdf");

            System.out.println(TRANSLATOR.getString("TEST_TABLE_ADDED"));
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
    //  </editor-fold>
}
