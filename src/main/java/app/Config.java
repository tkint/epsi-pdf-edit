/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

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

    // INSTANCE SETTINGS
    final String INSTANCE_SAVE_DATA_SEPARATOR = "->";

    // TAB SETTINGS
    final boolean TAB_CLOSE = true;
    final int TAB_TITLE_SIZE = 10; // Longueur du titre des onglets

    // 
    final String BTN_OPEN_SAVE_DEFAULT_DIR = "user.dir";
    final String BTN_FILE_SAVE_TITLE = "Sauvegarder le fichier";
    final String BTN_FILE_OPEN_TITLE = "Ouvrir un fichier";

    // PDF SETTINGS
    final String PDF_BACKGROUND_COLOR = "#2B2B2B";
    final PDRectangle PDF_DISPLAY_INITIAL_RATIO = PDRectangle.A4;
    final float PDF_DISPLAY_ZOOM_SCALE = 1.1f;
    final float PDF_DISPLAY_ZOOMIN_LIMIT = 5.0f;
    final float PDF_DISPLAY_ZOOMOUT_LIMIT = 0.5f;
    final float PDF_DISPLAY_PAGE_PADDING = 20;

    // TEST SETTINGS
    final String TEST_DOC_TITLE = "test";
    final String TEST_IMG_NAME = "./img/poros.jpg";
}
