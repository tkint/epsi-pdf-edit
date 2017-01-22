/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.controller.menu;

import app.Config;
import app.Instance;
import java.io.File;
import java.io.IOException;
import javafx.stage.FileChooser;
import src.controller.DocumentController;
import src.model.DocFile;
import src.view.displayer.TabDisplayer;

/**
 *
 * @author Thomas
 */
public class MenuFile implements Config {

    private static final Instance INSTANCE = Instance.getInstance();

    /**
     * Créer un nouveau fichier
     */
    public static void btnFileNew() {
        try {
            DocFile docFile = INSTANCE.addNewFile(TRANSLATOR.getString("FILE_NAME_DEFAULT") + ".pdf");
            TabDisplayer.displayDocFileInNewTab(docFile, docFile.getShortFileName());
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    /**
     * Ouvre un fichier
     */
    public static void btnFileOpen() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(TRANSLATOR.getString("FILE_OPEN"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
            fileChooser.setInitialDirectory(new File(System.getProperty(DEFAULT_DIR)));
            File file = fileChooser.showOpenDialog(INSTANCE.stage);
            if (file != null) {
                if (INSTANCE.isAlreadyOpened(file)) {
                    TabDisplayer.selectTab(TabDisplayer.defineTabName(file.getName().substring(0, file.getName().length() - 4)));
                } else {
                    DocFile docFile = INSTANCE.addFile(file);
                    TabDisplayer.displayDocFileInNewTab(docFile, docFile.getShortFileName());
                    MenuView.chooseZoom(100);
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Sauvegarde le fichier ouvert
     */
    public static void btnFileSave() {
        DocFile docFile = null;
        if ((docFile = INSTANCE.getDocFileOpened()) != null) {
            try {
                File file = new File(docFile.getFileName());
                if (!file.isDirectory()) {
                    if (file.exists()) {
                        docFile.getDocument().save(docFile.getFileName());
                        docFile.setSaved(true);
                        INSTANCE.saveInSaveFile(docFile.getFile(), TRANSLATOR.getString("APP_NAME") + "_recent");
                        System.out.println(TRANSLATOR.getString("FILE_HAS_BEEN_SAVED_1") + " " + INSTANCE.getDocFileOpened().getFileName() + " " + TRANSLATOR.getString("FILE_HAS_BEEN_SAVED_2"));
                    } else {
                        btnFileSaveAs();
                    }
                }
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        } else {
            System.out.println(TRANSLATOR.getString("FILE_SAVE_FAIL"));
        }
    }

    /**
     * Sauvegarde le fichier ouvert à l'endroit choisit
     */
    public static void btnFileSaveAs() {
        DocFile docFile = null;
        if ((docFile = INSTANCE.getDocFileOpened()) != null) {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle(TRANSLATOR.getString("FILE_SAVE"));
                fileChooser.setInitialFileName(docFile.getFileName());
                fileChooser.setInitialDirectory(new File(System.getProperty(DEFAULT_DIR)));
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
                File file = fileChooser.showSaveDialog(INSTANCE.stage);
                if (file != null) {
                    docFile.getDocument().save(file);
                    docFile.setSaved(true);
                    INSTANCE.updateDocFile(docFile.getDocument(), file);
                    INSTANCE.saveInSaveFile(file, TRANSLATOR.getString("APP_NAME") + "_recent");
                    TabDisplayer.refreshOpenedTab();
                    System.out.println(TRANSLATOR.getString("FILE_HAS_BEEN_SAVED_1") + " " + file.getName() + " " + TRANSLATOR.getString("FILE_HAS_BEEN_SAVED_2"));
                }
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        } else {
            System.out.println(TRANSLATOR.getString("FILE_SAVE_FAIL"));
        }
    }
    
    /**
     * Bouton de conversion en PNG
     */
    public static void btnConvertToPNG(){        
        
        
        DocFile docFile = null;
        DocumentController documentController = new DocumentController();
        if ((docFile = INSTANCE.getDocFileOpened()) != null) {
          
            File file = new File(docFile.getFileName());
            documentController.convertToImage(file, ".png");
   
        }
        else {
            System.out.println(TRANSLATOR.getString("CONVERT_FAIL"));
        }
        
    }
    
    /**
     * Bouton de conversion en JPEG
     */
    public static void btnConvertToJPEG(){
        
        DocFile docFile = null;
        DocumentController documentController = new DocumentController();
        if ((docFile = INSTANCE.getDocFileOpened()) != null) {
          
            File file = new File(docFile.getFileName());
            documentController.convertToImage(file, ".jpeg");
   
        }
        else {
            System.out.println(TRANSLATOR.getString("CONVERT_FAIL"));
        }
        
    }
    
    /**
     * Bouton de conversion en GIF
     */
    public static void btnConvertToGIF(){
        
        DocFile docFile = null;
        DocumentController documentController = new DocumentController();
        if ((docFile = INSTANCE.getDocFileOpened()) != null) {
          
            File file = new File(docFile.getFileName());
            documentController.convertToImage(file, ".gif");
   
        }
        else {
            System.out.println(TRANSLATOR.getString("CONVERT_FAIL"));
        }
        
    }

    /**
     * Quitte l'application
     */
    public static void btnFileExit() {
        INSTANCE.stage.close();
    }
}
