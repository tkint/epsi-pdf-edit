/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.controller;

import app.Config;
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
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import src.controller.ImageController;
import src.controller.PageController;
import src.controller.TableController;
import src.model.table.Cell;
import src.model.table.Table;

/**
 * FXML Controller class
 *
 * @author t.kint
 */
public class MainController implements Config, Initializable {

    private String file;
    private String filename;
    private PDDocument document;
    private Stage stage;

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
        this.stage = stage;
    }

    //  <editor-fold desc="File">
    public void btnFileNew() throws IOException {
        if (this.document != null) {
            this.document.close();
        }
        this.document = new PDDocument();
        this.document.addPage(new PDPage());
    }

    public void btnFileOpen() throws IOException {
        if (this.document != null) {
            this.document.close();
        }
        if (this.stage != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Ouvrir un fichier");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            File file = fileChooser.showOpenDialog(this.stage);
            if (file != null) {
                this.file = file.getName();
                this.filename = this.file.substring(0, this.file.length() - 4);
                this.document = PDDocument.load(file);
                System.out.println("Filename: " + this.file);
                System.out.println("Document: " + this.document);
            }
        }
    }

    public void btnFileSave() throws IOException {
        if (this.file != null) {
            if (this.document != null) {
                this.document.save(this.file);
                this.document.close();
                System.out.println("Document " + this.file + " a été enregistré");
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
                this.file = file.getName();
                this.filename = this.file.substring(0, this.file.length() - 4);
                this.document.save(file);
                this.document.close();
            }
        }
    }

    public void btnFileExit() {
        System.exit(0);
    }
    //  </editor-fold>

    //  <editor-fold desc="Edit">
    public void btnEditDelete() {

    }
    //  </editor-fold>

    //  <editor-fold desc="Tools">
    public void btnToolsExtractPage() {
        if (this.document != null) {
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Extraire une page");
            dialog.setHeaderText("Choisissez la page à extraire");

            ButtonType confirmButtonType = new ButtonType("Extraire", ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

            List<String> pages = new ArrayList<>();
            for (int i = 0; i < this.document.getPages().getCount(); i++) {
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
                pageController.extractPage(this.document, id - 1, pair.getKey());
            });
        }
    }

    public void btnToolsExtractPages() {
        if (this.document != null) {
            PageController pageController = new PageController();
            for (int i = 0; i < this.document.getPages().getCount(); i++) {
                pageController.extractPage(this.document, i, this.filename + "_" + (i + 1));
            }
        }
    }
    //  </editor-fold>

    //  <editor-fold desc="Help">
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
            document.save(DOC_TITLE + ".pdf");
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
            File file = new File(DOC_TITLE + ".pdf");
            PDDocument document = PDDocument.load(file);

            document.addPage(new PDPage());

            PDPageContentStream contentStream = new PDPageContentStream(document, document.getPage(document.getNumberOfPages() - 1), PDPageContentStream.AppendMode.APPEND, true);

            // Ajout d'une image sur la deuxième page
            imageController.addImage(document, contentStream, IMG_NAME, 100, 400, 0.2f);

            contentStream.close();

            document.save(DOC_TITLE + ".pdf");
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
            File file = new File(DOC_TITLE + ".pdf");
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
            File file = new File(DOC_TITLE + ".pdf");
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
            document.save(DOC_TITLE + ".pdf");

            System.out.println("Tableau ajouté");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
    //  </editor-fold>
}
