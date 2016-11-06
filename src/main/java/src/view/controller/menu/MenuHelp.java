/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.controller.menu;

import app.Config;
import static app.Config.APP_AUTHORS;
import static app.Config.APP_DATE;
import static app.Config.APP_SOCIETY;
import static app.Config.APP_VERSION;
import app.Instance;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
import static app.Config.APP_NAME;

/**
 *
 * @author Thomas
 */
public class MenuHelp implements Config {

    private static MenuHelp MENUHELP = new MenuHelp();

    private static final Instance INSTANCE = Instance.getInstance();

    private MenuHelp() {

    }

    /**
     * Définition du singleton
     *
     * @return
     */
    public static synchronized MenuHelp getInstance() {
        if (MENUHELP == null) {
            MENUHELP = new MenuHelp();
        }
        return MENUHELP;
    }
    
    /**
     * Affiche une popup A propos
     */
    public void btnHelpAbout() {
        String text = APP_NAME;

        text += '\n' + "Version: " + APP_VERSION;
        text += '\n' + "Auteurs: " + '\n' + APP_AUTHORS;
        text += '\n' + "Société: " + APP_SOCIETY;
        text += '\n' + "Date: " + APP_DATE;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UNIFIED);
        alert.setTitle("A propos");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }
}
