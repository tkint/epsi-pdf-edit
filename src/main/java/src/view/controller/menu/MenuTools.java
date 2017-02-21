/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.controller.menu;

import app.Config;
import static app.Config.TRANSLATOR;
import app.Instance;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import src.controller.PageController;
import src.view.controller.ExtractImageController;
import src.view.controller.MainController;
import src.view.controller.ViewTextAreaController;

/**
 *
 * @author Thomas
 */
public class MenuTools implements Config {

    private static final Instance INSTANCE = Instance.getInstance();

    /**
     * Extrait du document ouvert la page définie par l'utilisateur
     */
    public static void btnToolsExtractPage() {
        if (INSTANCE.getDocFileOpened() != null) {
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle(TRANSLATOR.getString("EXTRACT_PAGE"));
            dialog.setHeaderText(TRANSLATOR.getString("EXTRACT_PAGE_TEXT"));

            ButtonType confirmButtonType = new ButtonType(TRANSLATOR.getString("EXTRACT_PAGE_BUTTON"), ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

            List<String> pages = new ArrayList<>();
            for (int i = 0; i < INSTANCE.getDocFileOpened().getDocument().getPages().getCount(); i++) {
                pages.add(String.valueOf(i + 1));
            }

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(10));

            TextField filenameField = new TextField();
            filenameField.setPromptText(TRANSLATOR.getString("EXTRACT_PAGE_FILENAME"));
            ComboBox<String> pageBox = new ComboBox<>(FXCollections.observableList(pages));
            pageBox.getSelectionModel().select(INSTANCE.getDocFileOpened().getSelectedPage());

            gridPane.add(new Label(TRANSLATOR.getString("PAGE") + " :"), 0, 0);
            gridPane.add(pageBox, 1, 0);
            gridPane.add(new Label(TRANSLATOR.getString("SAVE_AS") + " :"), 0, 1);
            gridPane.add(filenameField, 1, 1);

            dialog.getDialogPane().setContent(gridPane);

            Platform.runLater(() -> filenameField.requestFocus());

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == confirmButtonType) {
                    return new Pair<>(filenameField.getText(), pageBox.getValue());
                }
                return null;
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();

            result.ifPresent(pair -> {
                int id = Integer.parseInt(pair.getValue());
                PageController pageController = new PageController();
                pageController.extractPage(INSTANCE.getDocFileOpened().getDocument(), id - 1, pair.getKey());
            });
        }
    }

    /**
     * Extrait toutes les pages du document ouvert
     */
    public static void btnToolsExtractPages() {
        if (INSTANCE.getDocFileOpened() != null) {
            PageController pageController = new PageController();
            for (int i = 0; i < INSTANCE.getDocFileOpened().getDocument().getPages().getCount(); i++) {
                pageController.extractPage(INSTANCE.getDocFileOpened().getDocument(), i, INSTANCE.getDocFileOpened().getShortFileName() + "_" + (i + 1));
            }
        }
    }

    public void btnToolsExtractImage() throws IOException {
        if (INSTANCE.getDocFileOpened() != null) {
            Stage stage = new Stage();

            FXMLLoader extractImageLoader = new FXMLLoader(getClass().getResource("/view/fxml/extractImage.fxml"));
            extractImageLoader.setResources(TRANSLATOR);

            Parent extractImageParent = (Parent) extractImageLoader.load();

            ExtractImageController extractImageController = (ExtractImageController) extractImageLoader.getController();
            extractImageController.setStage(stage);

            Scene extractImageScene = new Scene(extractImageParent);

            stage.setTitle(TRANSLATOR.getString("APP_NAME") + " - " + TRANSLATOR.getString("MENU_TOOLS_EXTRACT_IMAGE"));
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(INSTANCE.stage);
            stage.setScene(extractImageScene);
            stage.show();
        }
    }

    public ViewTextAreaController btnOpenTextArea() throws IOException {
        if (INSTANCE.getDocFileOpened() != null) {
            Stage stage = new Stage();

            FXMLLoader textAreaLoader = new FXMLLoader(getClass().getResource("/view/fxml/ViewTextArea.fxml"));
            textAreaLoader.setResources(TRANSLATOR);

            Parent textAreaParent = (Parent) textAreaLoader.load();

            ViewTextAreaController viewTextAreaController = (ViewTextAreaController) textAreaLoader.getController();
            viewTextAreaController.setStage(stage);

            Scene ViewTextAreaScene = new Scene(textAreaParent);

            stage.setTitle(TRANSLATOR.getString("APP_NAME") + " - " + TRANSLATOR.getString("MENU_TOOLS_TEXT_AREA"));
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(INSTANCE.stage);
            stage.setScene(ViewTextAreaScene);
            stage.show();
            return viewTextAreaController;
        }
        return null;
    }
}
