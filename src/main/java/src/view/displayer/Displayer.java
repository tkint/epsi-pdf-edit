/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.displayer;

import app.Config;
import app.Instance;
import java.io.IOException;
import javafx.scene.control.Pagination;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import src.model.DocFile;

/**
 *
 * @author Thomas
 */
public class Displayer implements Config {

    private static final Instance INSTANCE = Instance.getInstance();

    /**
     * Affiche tous les documents dans de nouveaux onglets
     * 
     * @throws IOException 
     */
    public static void displayAllDocFiles() throws IOException {
        for (DocFile docFile : INSTANCE.docFiles) {
            TabDisplayer.displayDocFileInNewTab(docFile, docFile.getShortFileName());
        }
    }

    /**
     * Ajoute une page à l'onglet ouvert
     *
     * @param id Id de l'onglet
     */
    public static void addPageOpenedTab() {
        if (INSTANCE.getDocFileOpened() != null) {
            // Récupération du tableau d'onglets
            TabPane tabPane = (TabPane) INSTANCE.stage.getScene().lookup("#documents");
            // Récupération de l'onglet
            Tab tab = tabPane.getSelectionModel().getSelectedItem();
            // Récupération de la pagination
            Pagination pagination = (Pagination) tab.getContent();

            if (INSTANCE.getDocFileOpened().getDocument().getNumberOfPages() > pagination.getPageCount()) {
                pagination.setPageCount(pagination.getPageCount() + 1);
                int pageSelected = pagination.getPageCount() - 1;

                pagination.getPageFactory().call(pageSelected);
                pagination.setCurrentPageIndex(pageSelected);
                INSTANCE.getDocFileOpened().setSelectedPage(pageSelected);
            }
        }
    }

    /**
     * Supprime une page à l'onglet ouvert
     *
     * @param id Id de l'onglet
     */
    public static void removePageOpenedTab() {
        if (INSTANCE.getDocFileOpened() != null) {
            // Récupération du tableau d'onglets
            TabPane tabPane = (TabPane) INSTANCE.stage.getScene().lookup("#documents");
            // Récupération de l'onglet
            Tab tab = tabPane.getSelectionModel().getSelectedItem();
            // Récupération de la pagination
            Pagination pagination = (Pagination) tab.getContent();

            if (INSTANCE.getDocFileOpened().getDocument().getNumberOfPages() < pagination.getPageCount()) {
                pagination.setPageCount(pagination.getPageCount() - 1);
                int pageSelected = pagination.getPageCount() - 1;

                pagination.setCurrentPageIndex(pageSelected);
                INSTANCE.getDocFileOpened().setSelectedPage(pageSelected);
            }
        }
    }
}
