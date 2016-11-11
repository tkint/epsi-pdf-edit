/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.controller.menu;

import app.Config;
import app.Instance;

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

    }
}
