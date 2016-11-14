/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.controller.menu;

import app.Config;
import app.Instance;
import org.apache.pdfbox.pdmodel.PDPage;
import src.controller.PageController;
import src.model.DocFile;
import src.view.Displayer;

/**
 *
 * @author Thomas
 */
public class MenuPage implements Config {

    private static MenuPage MENUPAGE = new MenuPage();

    private static final Instance INSTANCE = Instance.getInstance();

    private MenuPage() {

    }

    /**
     * Définition du singleton
     *
     * @return
     */
    public static synchronized MenuPage getInstance() {
        if (MENUPAGE == null) {
            MENUPAGE = new MenuPage();
        }
        return MENUPAGE;
    }

    public void btnPageRotateRight() {
        PageController pageController = new PageController();
        DocFile docFile = null;
        if ((docFile = INSTANCE.getDocFileOpened()) != null) {
            pageController.rotatePage(docFile.getDocument(), docFile.getSelectedPage(), 90);
            Displayer.refreshTab();
        }
    }

    public void btnPageRotateLeft() {
        PageController pageController = new PageController();
        DocFile docFile = null;
        if ((docFile = INSTANCE.getDocFileOpened()) != null) {
            pageController.rotatePage(docFile.getDocument(), docFile.getSelectedPage(), - 90);
            Displayer.refreshTab();
        }
    }

    public void btnPageFlipHorizontal() {

    }

    public void btnPageFlipVertical() {

    }
}
