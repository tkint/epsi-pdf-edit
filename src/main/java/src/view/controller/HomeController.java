/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.controller;

import static app.Config.BTN_OPEN_SAVE_DEFAULT_DIR;
import static app.Config.TRANSLATOR;
import app.Instance;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventType;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import src.model.DocFile;
import src.view.Displayer;
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

        fillList(openedFiles, TRANSLATOR.getString("APP_NAME"));
        fillList(recentFiles, TRANSLATOR.getString("APP_NAME") + "_recent");
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
     * Ajoute dans la liste défini le docfile donné
     *
     * @param vBox
     * @param file
     */
    private void fillList(VBox vBox, String saveFilename) {
        for (File file : INSTANCE.loadSaveFile(saveFilename)) {
            Label label = new Label(file.getName());
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
                    if (INSTANCE.isAlreadyInSaveFile(file, saveFilename)) {
                        if (INSTANCE.isAlreadyOpened(file)) {
                            Displayer.selectDocFileTab(Displayer.defineTabName(file.getName().substring(0, file.getName().length() - 4)));
                        } else {
                            DocFile docFile = INSTANCE.addFile(file);
                            Displayer.displayDocFileNewTab(docFile, docFile.getShortFileName());
                        }
                    } else {
                        DocFile docFile = INSTANCE.addNewFile(file.getName());
                        Displayer.displayDocFileNewTab(docFile, docFile.getShortFileName());
                    }
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            });

            vBox.getChildren().add(label);
        }
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
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle(TRANSLATOR.getString("FILE_OPEN"));
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
                fileChooser.setInitialDirectory(new File(System.getProperty(BTN_OPEN_SAVE_DEFAULT_DIR)));
                File file = fileChooser.showOpenDialog(this.stage);
                if (file != null) {
                    goToMainScene();
                    if (INSTANCE.isAlreadyOpened(file)) {
                        Displayer.selectDocFileTab(Displayer.defineTabName(file.getName().substring(0, file.getName().length() - 4)));
                    } else {
                        DocFile docFile = INSTANCE.addFile(file);
                        Displayer.displayDocFileNewTab(docFile, docFile.getShortFileName());
                    }
                }
            } catch (IOException e) {
                System.out.println(e.toString());
            }
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

            mainScene.setOnKeyPressed((event) -> {
                if (event.isControlDown()) {
                    switch (event.getCode()) {
                        case S:
                            MENUFILE.btnFileSave();
                            break;
                        case T:
                            MENUFILE.btnFileNew();
                            break;
                        case W:
                            Displayer.closeTabByDocFileId(INSTANCE.opened);
                            break;
                    }
                }
            });

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
