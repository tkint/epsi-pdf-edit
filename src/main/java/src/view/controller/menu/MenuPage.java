/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.controller.menu;

import app.Config;
import app.Instance;
import java.io.IOException;
import src.controller.PageController;
import src.model.DocFile;
import src.view.displayer.TabDisplayer;

/**
 *
 * @author Thomas
 */
public class MenuPage implements Config {

    private static final Instance INSTANCE = Instance.getInstance();

    /**
     * Bouton de rotation de la page sur la droite
     */
    public static void btnPageRotateRight() throws IOException {
        PageController pageController = new PageController();
        DocFile docFile = null;
        if ((docFile = INSTANCE.getDocFileOpened()) != null) {
            pageController.rotatePage(docFile.getDocument(), docFile.getSelectedPage(), 90);
            TabDisplayer.refreshOpenedTab();
        }
    }

     /**
     * Bouton de rotation de la page sur la gauche
     */
    public static void btnPageRotateLeft() throws IOException {
        PageController pageController = new PageController();
        DocFile docFile = null;
        if ((docFile = INSTANCE.getDocFileOpened()) != null) {
            pageController.rotatePage(docFile.getDocument(), docFile.getSelectedPage(), - 90);
            TabDisplayer.refreshOpenedTab();
        }
    }

     /**
     * Bouton de rotation de la page sur l'horizontale
     */
    public static void btnPageFlipHorizontal() {

    }

     /**
     * Bouton de rotation de la page sur la verticale
     */
    public static void btnPageFlipVertical() {

    }
}
