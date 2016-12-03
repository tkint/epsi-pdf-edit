/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.util.ResourceBundle;
import javafx.scene.paint.Color;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

/**
 *
 * @author t.kint
 */
public interface Config {

    // Définition des resources de traduction
    final ResourceBundle TRANSLATOR = ResourceBundle.getBundle("translations/messages");

    // TAB SETTINGS
    final boolean TAB_CLOSE = true;
    final int TAB_NAME_SIZE = 10; // Longueur du titre des onglets

    // 
    final String DEFAULT_DIR = "user.dir";

    // PDF SETTINGS
    final String BACKGROUND_COLOR = "#2B2B2B";
    
    // Défaut: 72. Convention: 150, 200, 300, 600
    final float PDF_DISPLAY_DPI = 200.0f;
    final float INITIAL_SCALE = 0.60f;
    final Integer[] ZOOMS = {25, 50, 100, 150, 200, 400, 800};
    
    final Color AREA_SELECT_BORDER = Color.BLUE;
    final Color AREA_SELECT_BACKGROUND = Color.TRANSPARENT;
    
    final Color TABLE_DRAW_BORDER = Color.BLACK;
    final Color TABLE_DRAW_BACKGROUND = Color.WHITE;
    
    // TEST SETTINGS
    final String TEST_DOC_TITLE = "test";
    final String TEST_IMG_NAME = "./img/poros.jpg";
}
