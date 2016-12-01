/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.controller.menu;

import app.Config;
import app.Instance;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

/**
 *
 * @author Thomas
 */
public class MenuHelp implements Config {

    private static final Instance INSTANCE = Instance.getInstance();

    /**
     * Affiche une popup A propos
     */
    public static void btnHelpAbout() {
        String text = TRANSLATOR.getString("APP_NAME");

        text += '\n' + TRANSLATOR.getString("VERSION") + " : " + TRANSLATOR.getString("APP_VERSION");
        text += '\n' + TRANSLATOR.getString("AUTHORS") + " : " + '\n' + TRANSLATOR.getString("APP_AUTHORS");
        text += '\n' + TRANSLATOR.getString("COMPANY") + " : " + TRANSLATOR.getString("APP_SOCIETY");
        text += '\n' + TRANSLATOR.getString("DATE") + " : " + TRANSLATOR.getString("APP_DATE");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UNIFIED);
        alert.setTitle(TRANSLATOR.getString("ABOUT"));
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }
}
