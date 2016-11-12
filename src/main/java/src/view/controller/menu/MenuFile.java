/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.controller.menu;

import app.Config;
import app.Instance;
import static app.Instance.getDocFileOpened;
import java.io.File;
import java.io.IOException;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import src.view.Displayer;
import static src.view.Displayer.defineTabName;

/**
 *
 * @author Thomas
 */
public class MenuFile implements Config {

    private static MenuFile MENUFILE = new MenuFile();

    private static final Instance INSTANCE = Instance.getInstance();

    private MenuFile() {

    }

    /**
     * Définition du singleton
     *
     * @return
     */
    public static synchronized MenuFile getInstance() {
        if (MENUFILE == null) {
            MENUFILE = new MenuFile();
        }
        return MENUFILE;
    }

    /**
     * Créer un nouveau fichier
     */
    public void btnFileNew() {
        try {
            PDDocument document = new PDDocument();
            document.addPage(new PDPage());
            INSTANCE.addDocFile(document, new File(TRANSLATOR.getString("FILE_NAME_DEFAULT") + ".pdf"));
            Displayer.displayDocFileNewTab(INSTANCE.getDocFileOpened(), TRANSLATOR.getString("FILE_NAME_DEFAULT"));
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Ouvre un fichier
     */
    public void btnFileOpen() {
        if (INSTANCE.stage != null) {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle(TRANSLATOR.getString("FILE_OPEN"));
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
                fileChooser.setInitialDirectory(new File(System.getProperty(BTN_OPEN_SAVE_DEFAULT_DIR)));
                File file = fileChooser.showOpenDialog(INSTANCE.stage);
                if (file != null) {
                    if (!INSTANCE.isFileAlreadyOpened(file)) {
                        PDDocument document = PDDocument.load(file);
                        INSTANCE.addDocFile(document, file);
                        INSTANCE.getDocFileOpened().setSaved(true);
                        Displayer.displayDocFileNewTab(INSTANCE.getDocFileOpened(), INSTANCE.getDocFileOpened().getShortFileName());
                    } else {
                        Displayer.selectDocFileTab(defineTabName(file.getName().substring(0, file.getName().length() - 4)));
                        System.out.println(TRANSLATOR.getString("FILE_ALREADY_OPENED"));
                    }
                }
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }
    }

    /**
     * Sauvegarde le fichier ouvert
     */
    public void btnFileSave() {
        if (INSTANCE.getDocFileOpened() != null) {
            try {
                File file = new File(INSTANCE.getDocFileOpened().getFile().getName());
                if (!file.isDirectory()) {
                    if (file.exists()) {
                        INSTANCE.getDocFileOpened().getDocument().save(INSTANCE.getDocFileOpened().getFileName());
                        INSTANCE.getDocFileOpened().setSaved(true);
                        INSTANCE.saveInSaveFile(getDocFileOpened(), TRANSLATOR.getString("APP_NAME") + "_recent");
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
    public void btnFileSaveAs() {
        if (INSTANCE.getDocFileOpened() != null) {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle(TRANSLATOR.getString("FILE_SAVE"));
                fileChooser.setInitialFileName(INSTANCE.getDocFileOpened().getFile().getName());
                fileChooser.setInitialDirectory(new File(System.getProperty(BTN_OPEN_SAVE_DEFAULT_DIR)));
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
                File file = fileChooser.showSaveDialog(INSTANCE.stage);
                if (file != null) {
                    INSTANCE.getDocFileOpened().getDocument().save(file);
                    INSTANCE.updateDocFile(INSTANCE.getDocFileOpened().getDocument(), file);
                    INSTANCE.getDocFileOpened().setSaved(true);
                    INSTANCE.saveInSaveFile(getDocFileOpened(), TRANSLATOR.getString("APP_NAME") + "_recent");
                    Displayer.updateDocFileTab(INSTANCE.opened);
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
     * Quitte l'application
     */
    public void btnFileExit() {
        INSTANCE.stage.close();
    }
}
