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
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import src.view.Displayer;

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

    public void btnFileNew() {
        try {
            PDDocument document = new PDDocument();
            document.addPage(new PDPage());
            INSTANCE.addDocFile(document, new File("Nouveau.pdf"));
            Displayer.displayDocFileNewTab("Nouveau");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void btnFileOpen() {
        if (INSTANCE.stage != null) {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle(BTN_FILE_OPEN_TITLE);
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
                fileChooser.setInitialDirectory(new File(System.getProperty(OPEN_SAVE_DIR)));
                File file = fileChooser.showOpenDialog(INSTANCE.stage);
                if (file != null) {
                    if (!Instance.isDocFileOpen(file)) {
                        PDDocument document = PDDocument.load(file);
                        INSTANCE.addDocFile(document, file);
                        INSTANCE.getDocOpened().setSaved(true);
                        Displayer.displayDocFileNewTab(INSTANCE.getDocOpened().getShortFileName());
                    } else {
                        System.out.println("Document déjà ouvert");
                    }
                }
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }
    }

    public void btnFileSave() {
        if (INSTANCE.getDocOpened() != null) {
            try {
                File file = new File(INSTANCE.getDocOpened().getFile().getName());
                if (!file.isDirectory()) {
                    if (file.exists()) {
                        INSTANCE.getDocOpened().getDocument().save(INSTANCE.getDocOpened().getFileName());
                        INSTANCE.getDocOpened().setSaved(true);
                        System.out.println("Document " + INSTANCE.getDocOpened().getFileName() + " a été enregistré");
                    } else {
                        btnFileSaveAs();
                    }
                }
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        } else {
            System.out.println("Pas de document ouvert");
        }
    }

    public void btnFileSaveAs() {
        if (INSTANCE.getDocOpened() != null) {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle(BTN_FILE_SAVE_TITLE);
                fileChooser.setInitialFileName(INSTANCE.getDocOpened().getFile().getName());
                fileChooser.setInitialDirectory(new File(System.getProperty(OPEN_SAVE_DIR)));
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
                File file = fileChooser.showSaveDialog(INSTANCE.stage);
                if (file != null) {
                    INSTANCE.getDocOpened().getDocument().save(file);
                    INSTANCE.updateDocFile(INSTANCE.getDocOpened().getDocument(), file);
                    INSTANCE.getDocOpened().setSaved(true);
                    Displayer.updateDocFileTab(INSTANCE.opened);
                    System.out.println("Document " + file.getName() + " a été enregistré");
                }
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        } else {
            System.out.println("Pas de document ouvert");
        }
    }

    /**
     * Quitte l'application
     */
    public void btnFileExit() {
        INSTANCE.save();
        System.exit(0);
    }
}
