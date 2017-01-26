/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.controller.menu;

import app.Config;
import app.Instance;
import javafx.scene.control.ChoiceBox;
import src.view.displayer.PageDisplayer;

/**
 *
 * @author Thomas
 */
public class MenuView implements Config {

    private static final Instance INSTANCE = Instance.getInstance();

    /**
     * Bouton d'augmentation du zoom
     */
    public static void btnZoomIn() {
        int currentZoom = getZoomChoice().getSelectionModel().getSelectedIndex();
        if (currentZoom < ZOOMS.length - 1) {
            getZoomChoice().getSelectionModel().select(currentZoom + 1);
            PageDisplayer.setZoom(ZOOMS[currentZoom + 1]);
            INSTANCE.getDocFileOpened().setZoom(ZOOMS[currentZoom + 1]);
        }
    }

    /**
     * Bouton de réduction du zoom
     */
    public static void btnZoomOut() {
        int currentZoom = getZoomChoice().getSelectionModel().getSelectedIndex();
        if (currentZoom > 0) {
            getZoomChoice().getSelectionModel().select(currentZoom - 1);
            PageDisplayer.setZoom(ZOOMS[currentZoom - 1]);
            INSTANCE.getDocFileOpened().setZoom(ZOOMS[currentZoom - 1]);
        }
    }

    /**
     * Bouton de choix du zoom
     */
    public static void chooseZoom(int zoom) {
        if (isZoomAvailable(zoom)) {
            getZoomChoice().getSelectionModel().select(Integer.toString(zoom));
            PageDisplayer.setZoom(zoom);
            INSTANCE.getDocFileOpened().setZoom(zoom);
        }
    }

    /**
     * Retourne true si le zoom est disponible pour le niveau défini
     * @param zoom
     * @return 
     */
    private static boolean isZoomAvailable(int zoom) {
        boolean available = false;
        int i = 0;
        while (i < ZOOMS.length && !available) {
            if (ZOOMS[i] == zoom) {
                available = true;
            }
            i++;
        }
        return available;
    }

    /**
     * Retourne la liste des zooms
     * @return 
     */
    public static ChoiceBox getZoomChoice() {
        return (ChoiceBox) INSTANCE.stage.getScene().lookup("#zoomChoice");
    }
}
