/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import javafx.scene.control.TabPane.TabClosingPolicy;

/**
 *
 * @author t.kint
 */
public interface Config {

    // APP SETTINGS
    final String APP_NAME = "PDFEdit";
    final String APP_VERSION = "0";
    final String APP_AUTHORS = "Erik FREMONT, Gaetan CAPEL, Ludovic BOUVIER, Thomas KINT";
    final String APP_SOCIETY = "EPSI";
    final String APP_DATE = "2016";
    
    // 
    final String TAB_PANE_ID = "#documents";
    final boolean TAB_CLOSE = true;
    final int TAB_TITLE_SIZE = 10; // Longueur du titre des onglets
    
    // 
    final String OPEN_SAVE_DIR = "user.dir";
    final String BTN_FILE_SAVE_TITLE = "Sauvegarder le fichier";
    final String BTN_FILE_OPEN_TITLE = "Ouvrir un fichier";

    final String PDF_BACKGROUND_COLOR = "#2B2B2B";
    final double PDF_DISPLAY_ZOOM_SCALE = 1.1;
    final float PDF_DISPLAY_PAGE_PADDING = 20;
    
    // TEST SETTINGS
    final String TEST_DOC_TITLE = "test";
    final String TEST_IMG_NAME = "./img/poros.jpg";
    
    final String TEMP_DATA_SEPARATOR = "->";
}
