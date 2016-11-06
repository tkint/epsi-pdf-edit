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
public class MenuEdit implements Config {

    private static MenuEdit MENUEDIT = new MenuEdit();

    private static final Instance INSTANCE = Instance.getInstance();

    private MenuEdit() {

    }

    /**
     * Définition du singleton
     *
     * @return
     */
    public static synchronized MenuEdit getInstance() {
        if (MENUEDIT == null) {
            MENUEDIT = new MenuEdit();
        }
        return MENUEDIT;
    }

    public void btnEditDelete() {

    }
}
