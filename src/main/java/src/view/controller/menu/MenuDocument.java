/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.controller.menu;

import app.Config;
import static app.Config.BTN_OPEN_SAVE_DEFAULT_DIR;
import static app.Config.TRANSLATOR;
import app.Instance;
import java.io.File;
import java.io.IOException;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import src.controller.DocumentController;
import src.model.DocFile;
import src.view.Displayer;

/**
 *
 * @author Thomas
 */
public class MenuDocument implements Config {

    private static MenuDocument MENUDOCUMENT = new MenuDocument();

    private static final Instance INSTANCE = Instance.getInstance();

    private MenuDocument() {

    }

    /**
     * Définition du singleton
     *
     * @return
     */
    public static synchronized MenuDocument getInstance() {
        if (MENUDOCUMENT == null) {
            MENUDOCUMENT = new MenuDocument();
        }
        return MENUDOCUMENT;
    }

    public void btnDocumentAddPage() {
        PDDocument document = INSTANCE.getDocFileOpened().getDocument();
        document.addPage(new PDPage());
        Displayer.addDocFilePageTab(INSTANCE.opened);
        System.out.println(TRANSLATOR.getString("PAGE_ADD_SUCCESS"));
    }

    public void btnDocumentRemovePage() {
        DocFile docFile = INSTANCE.getDocFileOpened();
        DocumentController documentController = new DocumentController();
        documentController.removePage(docFile.getDocument(), docFile.getSelectedPage());
        docFile.setSelectedPage(docFile.getSelectedPage() - 1);
        Displayer.removeDocFilePageTab(INSTANCE.opened);
    }

    public void btnDocumentAddDocument() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(TRANSLATOR.getString("FILE_OPEN"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
            fileChooser.setInitialDirectory(new File(System.getProperty(BTN_OPEN_SAVE_DEFAULT_DIR)));
            File file = fileChooser.showOpenDialog(INSTANCE.stage);
            if (file != null) {
                DocFile docFile = INSTANCE.getDocFileOpened();
                DocumentController documentController = new DocumentController();
                documentController.addPDFToDocument(docFile.getDocument(), file);
                Displayer.refreshTab();
                System.out.println("Document " + file.getName() + " ajouté");
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}
