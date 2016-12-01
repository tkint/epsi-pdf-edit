/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.displayer;

import app.Config;
import app.Instance;
import java.io.File;
import java.io.IOException;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.control.Pagination;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import src.model.DocFile;

/**
 *
 * @author Thomas
 */
public class TabDisplayer implements Config {

    private static final Instance INSTANCE = Instance.getInstance();

    /**
     * Affiche le document ouvert dans un nouvel onglet
     *
     * @param tabName
     * @throws IOException
     */
    public static void displayDocFileInNewTab(DocFile docFile, String tabName) throws IOException {
        // Récupération du tableau d'onglets
        TabPane tabPane = (TabPane) INSTANCE.stage.getScene().lookup("#documents");

        // Définition de l'onglet
        Tab tab = new Tab(defineTabName(tabName));
        // Définition de la pagination
        Pagination pagination = new Pagination(docFile.getDocument().getNumberOfPages());

        // Paramétrage du tableau d'onglets
        if (TAB_CLOSE) {
            tabPane.tabClosingPolicyProperty().set(TabPane.TabClosingPolicy.SELECTED_TAB);
        }
        tabPane.getSelectionModel().selectedIndexProperty().addListener((obs, ov, nv) -> {
            if (obs != null) {
                INSTANCE.opened = obs.getValue().intValue();
            }
        });

        // Paramétrage de l'onglet
        tab.setId(Integer.toString(docFile.getId()));
        tab.closableProperty().set(true);
        tab.setOnClosed((Event event) -> {
            int id = Integer.parseInt(tab.getId());
            INSTANCE.closeDocFile(id);
        });

        // Paramétrage de la pagination
        pagination.setStyle("-fx-background-color: white");
        pagination.setPadding(new Insets(0, 0, 0, 0));
        pagination.setCurrentPageIndex(docFile.getSelectedPage());
        pagination.setPageFactory((Integer pageIndex) -> {
            return PageDisplayer.displayPage(docFile, pageIndex);
        });

        tab.setContent(pagination);

        // Ajout de l'onglet au tableau d'onglets
        tabPane.getTabs().add(tab);

        // Sélection du nouvel onglet
        tabPane.getSelectionModel().select(tab);
    }

    /**
     * Met à jour l'affichage de l'onglet ouvert
     *
     * @throws IOException
     */
    public static void refreshOpenedTab() throws IOException {
        TabPane tabPane = (TabPane) INSTANCE.stage.getScene().lookup("#documents");
        Tab tab = tabPane.getSelectionModel().getSelectedItem();

        ImageView imageView = (ImageView) tab.getContent().lookup("#imagePDF");

        PageDisplayer.setImage(imageView, INSTANCE.getDocFileOpened().getSelectedPage());

        TraceDisplayer.clearTrace();
    }

    /**
     * Ferme l'onglet ouvert
     *
     * @param id
     */
    public static void closeOpenedTab() {
        TabPane tabPane = (TabPane) INSTANCE.stage.getScene().lookup("#documents");
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        tabPane.getTabs().remove(tab);
    }

    /**
     * Sélectionne l'onglet précisé
     *
     * @param fileName String nom du fichier dont on veut l'onglet
     */
    public static void selectTab(String fileName) {
        TabPane tabPane = (TabPane) INSTANCE.stage.getScene().lookup("#documents");
        Tab tab = tabPane.getTabs().get(0);
        for (Tab t : tabPane.getTabs()) {
            if (t.getText().equals(defineTabName(fileName))) {
                tab = t;
            }
        }
        tabPane.getSelectionModel().select(tab);
    }

    /**
     * Raccourci le nom de l'onglet s'il est trop long
     *
     * @param tabName
     * @return
     */
    public static String defineTabName(String tabName) {
        String name;
        if (tabName.length() > TAB_TITLE_SIZE) {
            name = tabName.substring(0, TAB_TITLE_SIZE) + "...";
        } else {
            name = tabName;
        }
        return name;
    }

    /**
     * Met à jour le nom de l'onglet précisé
     *
     * @param id
     */
    public static void refreshTabName(int id) {
        if (INSTANCE.docFiles.size() > 0 && INSTANCE.docFiles.get(id) != null && INSTANCE.stageName.equals("main")) {
            File file = INSTANCE.docFiles.get(id).getFile();

            TabPane tabPane = (TabPane) INSTANCE.stage.getScene().lookup("#documents");
            Tab tab = tabPane.getTabs().get(id);
            tab.setText(defineTabName(file.getName().substring(0, file.getName().length() - 4)));
            tabPane.getSelectionModel().select(tab);
        }
    }
}
