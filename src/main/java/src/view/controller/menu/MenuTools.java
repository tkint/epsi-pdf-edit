/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.controller.menu;

import app.Config;
import app.Instance;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import src.controller.PageController;

/**
 *
 * @author Thomas
 */
public class MenuTools implements Config {

    private static MenuTools MENUTOOLS = new MenuTools();

    private static final Instance INSTANCE = Instance.getInstance();

    private MenuTools() {

    }

    /**
     * Définition du singleton
     *
     * @return
     */
    public static synchronized MenuTools getInstance() {
        if (MENUTOOLS == null) {
            MENUTOOLS = new MenuTools();
        }
        return MENUTOOLS;
    }
    
    /**
     * Extrait du document ouvert la page définie par l'utilisateur
     */
    public void btnToolsExtractPage() {
        if (INSTANCE.getDocFileOpened() != null) {
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Extraire une page");
            dialog.setHeaderText("Choisissez la page à extraire");

            ButtonType confirmButtonType = new ButtonType("Extraire", ButtonBar.ButtonData.OK_DONE);
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
    public void btnToolsExtractPages() {
        if (INSTANCE.getDocFileOpened() != null) {
            PageController pageController = new PageController();
            for (int i = 0; i < INSTANCE.getDocFileOpened().getDocument().getPages().getCount(); i++) {
                pageController.extractPage(INSTANCE.getDocFileOpened().getDocument(), i, INSTANCE.getDocFileOpened().getShortFileName() + "_" + (i + 1));
            }
        }
    }
}
