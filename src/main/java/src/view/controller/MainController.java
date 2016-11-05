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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;
import org.apache.pdfbox.pdmodel.*;
import src.controller.*;
import src.model.table.*;
import src.view.*;

/**
 * FXML Controller class
 *
 * @author t.kint
 */
public class MainController implements Config, Initializable {

    private static final Instance instance = Instance.getInstance();

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
        instance.stage = stage;
    }

    //  <editor-fold desc="File">
    /**
     * Initialise un nouveau document
     * @throws IOException 
     */
    public void btnFileNew() throws IOException {
        PDDocument document = new PDDocument();
        document.addPage(new PDPage());
        instance.addDocument(document, new File("Nouveau.pdf"));
        Displayer.displayPDFNewTab(instance.documents.size() - 1, "Nouveau");
    }

    /**
     * Ouvre un document déjà existant
     * @throws IOException 
     */
    public void btnFileOpen() throws IOException {
        if (instance.stage != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(BTN_FILE_OPEN_TITLE);
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
            fileChooser.setInitialDirectory(new File(System.getProperty(OPEN_SAVE_DIR)));
            File file = fileChooser.showOpenDialog(instance.stage);
            if (file != null) {
                if (!Instance.isOpen(file)) {
                    PDDocument document = PDDocument.load(file);
                    instance.addDocument(document, file);
                    Displayer.displayPDFNewTab(instance.documents.size() - 1, instance.filenameOpened);
                } else {
                    System.out.println("Document déjà ouvert");
                }
            }
        }
    }

    /**
     * Enregistre un document déjà existant, sinon fait appel à saveAs
     * @throws IOException 
     */
    public void btnFileSave() throws IOException {
        if (instance.fileOpened != null && instance.documentOpened != null) {
            File f = new File(instance.fileOpened);
            if (!f.isDirectory()) {
                if (f.exists()) {
                    instance.documentOpened.save(instance.filenameOpened + ".pdf");
                    System.out.println("Document " + instance.fileOpened + " a été enregistré");
                } else {
                    btnFileSaveAs();
                }
            }
        } else {
            System.out.println("Pas de document ouvert");
        }
    }

    /**
     * Enregistre un nouveau document
     * @throws IOException 
     */
    public void btnFileSaveAs() throws IOException {
        if (instance.fileOpened != null && instance.documentOpened != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(BTN_FILE_SAVE_TITLE);
            fileChooser.setInitialFileName(instance.filenameOpened);
            fileChooser.setInitialDirectory(new File(System.getProperty(OPEN_SAVE_DIR)));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
            File file = fileChooser.showSaveDialog(instance.stage);
            if (file != null) {
                instance.documentOpened.save(file);
                instance.updateDocument(instance.documentOpened, file);
                Displayer.updatePDFTab(instance.opened);
                System.out.println("Document " + file.getName() + " a été enregistré");
            }
        } else {
            System.out.println("Pas de document ouvert");
        }
    }

    /**
     * Quitte l'application
     */
    public void btnFileExit() {
        System.exit(0);
    }
    //  </editor-fold>

    //  <editor-fold desc="Edit">
    public void btnEditDelete() {

    }
    //  </editor-fold>

    //  <editor-fold desc="Tools">
    /**
     * Extrait du document ouvert la page définie par l'utilisateur
     */
    public void btnToolsExtractPage() {
        if (instance.documentOpened != null) {
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Extraire une page");
            dialog.setHeaderText("Choisissez la page à extraire");

            ButtonType confirmButtonType = new ButtonType("Extraire", ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

            List<String> pages = new ArrayList<>();
            for (int i = 0; i < instance.documentOpened.getPages().getCount(); i++) {
                pages.add(String.valueOf(i + 1));
            }

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(10));

            TextField filenameField = new TextField();
            filenameField.setPromptText("Nom du fichier");
            ComboBox<String> pageBox = new ComboBox<>(FXCollections.observableList(pages));

            gridPane.add(new Label("Page :"), 0, 0);
            gridPane.add(pageBox, 1, 0);
            gridPane.add(new Label("Enregistrer sous :"), 0, 1);
            gridPane.add(filenameField, 1, 1);

            dialog.getDialogPane().setContent(gridPane);

            Platform.runLater(() -> filenameField.requestFocus());

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == confirmButtonType) {
                    return new Pair<>(filenameField.getText(), pageBox.getValue().toString());
                }
                return null;
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();

            result.ifPresent(pair -> {
                int id = Integer.parseInt(pair.getValue());
                PageController pageController = new PageController();
                pageController.extractPage(instance.documentOpened, id - 1, pair.getKey());
            });
        }
    }

    /**
     * Extrait toutes les pages du document ouvert
     */
    public void btnToolsExtractPages() {
        if (instance.documentOpened != null) {
            PageController pageController = new PageController();
            for (int i = 0; i < instance.documentOpened.getPages().getCount(); i++) {
                pageController.extractPage(instance.documentOpened, i, instance.filenameOpened + "_" + (i + 1));
            }
        }
    }
    //  </editor-fold>

    //  <editor-fold desc="Help">
    /**
     * Affiche une popup A propos
     */
    public void btnHelpAbout() {
        String text = APP_TITLE;

        text += '\n' + "Version: " + APP_VERSION;
        text += '\n' + "Auteurs: " + '\n' + APP_AUTHORS;
        text += '\n' + "Société: " + APP_SOCIETY;
        text += '\n' + "Date: " + APP_DATE;

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.initStyle(StageStyle.UNIFIED);
        alert.setTitle("A propos");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
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
