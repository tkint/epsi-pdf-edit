/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.util.ResourceBundle;
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
    final int TAB_TITLE_SIZE = 10; // Longueur du titre des onglets

    // 
    final String BTN_OPEN_SAVE_DEFAULT_DIR = "user.dir";

    // PDF SETTINGS
    final String PDF_BACKGROUND_COLOR = "#2B2B2B";
    // Défaut: 72. Convention: 150, 200, 300, 600
    final float PDF_DISPLAY_DPI = 200.0f;
    final PDRectangle PDF_DISPLAY_INITIAL_RATIO = PDRectangle.A4;
    final float PDF_DISPLAY_ZOOM_SCALE = 1.1f;
    final float PDF_DISPLAY_ZOOMIN_LIMIT = 5.0f;
    final float PDF_DISPLAY_ZOOMOUT_LIMIT = 0.5f;
    final float PDF_DISPLAY_PAGE_PADDING = 20;

    // TEST SETTINGS
    final String TEST_DOC_TITLE = "test";
    final String TEST_IMG_NAME = "./img/poros.jpg";
}
