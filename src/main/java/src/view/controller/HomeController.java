/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.controller;

import static app.Config.TRANSLATOR;
import app.Instance;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import src.model.DocFile;
import src.view.Displayer;
import static src.view.Displayer.defineTabName;
import src.view.controller.menu.MenuFile;

/**
 * FXML Controller class
 *
 * @author Thomas
 */
public class HomeController implements Initializable {

    private static final Instance INSTANCE = Instance.getInstance();
    private static final MenuFile MENUFILE = MenuFile.getInstance();

    private Stage stage;

    @FXML
    public ImageView fileNewImg;

    @FXML
    public ImageView fileOpenImg;

    @FXML
    public Label fileNewText;

    @FXML
    public Label fileOpenText;

    @FXML
    public VBox recentFiles;

    @FXML
    public VBox openedFiles;

    private DropShadow hover;
    private DropShadow out;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hover = new DropShadow(((DropShadow) fileNewImg.getEffect()).getRadius(), Color.WHITE);
        out = new DropShadow(((DropShadow) fileNewImg.getEffect()).getRadius(), Color.BLACK);
        
        addRecentFiles();
        addOpenedFiles();
        setNewFileSkin();
        setOpenFileSkin();
    }

    /**
     * Défini les effets de passage de la souris sur le bouton Nouveau
     */
    private void setNewFileSkin() {
        fileNewImg.setOnMouseEntered((event) -> {
            fileNewImg.setEffect(hover);
            fileNewText.underlineProperty().set(true);
        });

        fileNewImg.setOnMouseExited((event) -> {
            fileNewImg.setEffect(out);
            fileNewText.underlineProperty().set(false);
        });

        fileNewText.setOnMouseEntered((event) -> {
            fileNewImg.setEffect(hover);
            fileNewText.underlineProperty().set(true);
        });

        fileNewText.setOnMouseExited((event) -> {
            fileNewImg.setEffect(out);
            fileNewText.underlineProperty().set(false);
        });
    }

    /**
     * Défini les effets de passage de la souris sur le bouton Ouvrir
     */
    private void setOpenFileSkin() {
        fileOpenImg.setOnMouseEntered((event) -> {
            fileOpenImg.setEffect(hover);
            fileOpenText.underlineProperty().set(true);
        });

        fileOpenImg.setOnMouseExited((event) -> {
            fileOpenImg.setEffect(out);
            fileOpenText.underlineProperty().set(false);
        });

        fileOpenText.setOnMouseEntered((event) -> {
            fileOpenImg.setEffect(hover);
            fileOpenText.underlineProperty().set(true);
        });

        fileOpenText.setOnMouseExited((event) -> {
            fileOpenImg.setEffect(out);
            fileOpenText.underlineProperty().set(false);
        });
    }

    /**
     * Peuple la liste des fichiers réçents
     */
    public void addRecentFiles() {
        for (DocFile docFile : INSTANCE.loadSaveFile(TRANSLATOR.getString("APP_NAME") + "_recent")) {
            fillList(recentFiles, docFile);
        }
    }

    /**
     * Peuple la liste des fichiers ouverts
     */
    public void addOpenedFiles() {
        for (DocFile docFile : INSTANCE.loadSaveFile(TRANSLATOR.getString("APP_NAME"))) {
            fillList(openedFiles, docFile);
        }
    }

    /**
     * Ajoute dans la liste défini le docfile donné
     *
     * @param vBox
     * @param docFile
     */
    private void fillList(VBox vBox, DocFile docFile) {
        Label label = new Label(docFile.getFileName());
        label.setStyle("-fx-text-fill: white");
        label.setPadding(new Insets(0, 0, 0, 10));
        label.setCursor(Cursor.HAND);

        label.setOnMouseEntered((event) -> {
            label.underlineProperty().set(true);
        });

        label.setOnMouseExited((event) -> {
            label.underlineProperty().set(false);
        });

        label.setOnMouseClicked((event) -> {
            try {
                goToMainScene();
                File file = docFile.getFile();
                PDDocument document = PDDocument.load(file);
                DocFile df = INSTANCE.addDocFile(document, file);

                if (!INSTANCE.isFileAlreadyOpened(file)) {
                    Displayer.displayDocFileNewTab(docFile, docFile.getShortFileName());
                    System.out.println(TRANSLATOR.getString("FILE_OPENING") + " : " + docFile.getFileName());
                } else {
                    Displayer.selectDocFileTab(docFile.getShortFileName());
                    System.out.println(TRANSLATOR.getString("FILE_ALREADY_OPENED") + " : " + docFile.getFileName());
                }
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        });

        vBox.getChildren().add(label);
    }

    /**
     * Créer un nouveau fichier
     *
     * @throws IOException
     */
    public void btnFileNew() throws IOException {
        if (this.stage != null) {
            goToMainScene();
            MENUFILE.btnFileNew();
        }
    }

    /**
     * Ouvre un fichier existant
     *
     * @throws IOException
     */
    public void btnFileOpen() throws IOException {
        if (this.stage != null) {
            goToMainScene();
            MENUFILE.btnFileOpen();
        }
    }

    /**
     * Dirige vers la scene principale
     *
     * @throws IOException
     */
    private void goToMainScene() throws IOException {
        if (this.stage != null) {
            this.stage.close();

            Stage stage = new Stage();

            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/view/fxml/main.fxml"));
            mainLoader.setResources(TRANSLATOR);

            Parent mainParent = (Parent) mainLoader.load();

            MainController mainController = (MainController) mainLoader.getController();
            mainController.setStage(stage);

            Scene mainScene = new Scene(mainParent);

            stage.setTitle(TRANSLATOR.getString("APP_NAME"));
            stage.setMaximized(true);
            stage.setScene(mainScene);
            stage.show();

            Displayer.displayDocFiles();
        }
    }

    /**
     * Retourne le stage
     *
     * @return
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Défini le stage
     *
     * @param stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
        INSTANCE.stageName = "home";
    }
}
