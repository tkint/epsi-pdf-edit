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
import src.model.DocFile;
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
            DocFile docFile = INSTANCE.addDocFile(document, new File(TRANSLATOR.getString("FILE_NAME_DEFAULT") + ".pdf"));
            Displayer.displayDocFileNewTab(docFile, TRANSLATOR.getString("FILE_NAME_DEFAULT"));
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
                    PDDocument document = PDDocument.load(file);
                    DocFile docFile = INSTANCE.addDocFile(document, file);

                    if (!INSTANCE.isFileAlreadyOpened(file)) {
                        Displayer.displayDocFileNewTab(docFile, docFile.getShortFileName());
                        System.out.println(TRANSLATOR.getString("FILE_OPENING") + " : " + docFile.getFileName());
                    } else {
                        Displayer.selectDocFileTab(defineTabName(docFile.getShortFileName()));
                        System.out.println(TRANSLATOR.getString("FILE_ALREADY_OPENED") + " : " + docFile.getFileName());
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
        DocFile docFile = null;
        if ((docFile = INSTANCE.getDocFileOpened()) != null) {
            try {
                File file = new File(docFile.getFileName());
                if (!file.isDirectory()) {
                    if (file.exists()) {
                        docFile.getDocument().save(docFile.getFileName());
                        docFile.setSaved(true);
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
        DocFile docFile = null;
        if ((docFile = INSTANCE.getDocFileOpened()) != null) {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle(TRANSLATOR.getString("FILE_SAVE"));
                fileChooser.setInitialFileName(docFile.getFileName());
                fileChooser.setInitialDirectory(new File(System.getProperty(BTN_OPEN_SAVE_DEFAULT_DIR)));
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
                File file = fileChooser.showSaveDialog(INSTANCE.stage);
                if (file != null) {
                    docFile.getDocument().save(file);
                    docFile.setSaved(true);
                    INSTANCE.updateDocFile(docFile.getDocument(), file);
                    INSTANCE.saveInSaveFile(docFile, TRANSLATOR.getString("APP_NAME") + "_recent");
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
