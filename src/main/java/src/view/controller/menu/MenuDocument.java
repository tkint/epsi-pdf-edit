/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.controller.menu;

import app.Config;
import app.Instance;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
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
    }

    public void btnDocumentRemovePage() {
        PDDocument document = INSTANCE.getDocFileOpened().getDocument();
        if (document.getNumberOfPages() > 1) {
            document.removePage(INSTANCE.getDocFileOpened().getSelectedPage());
            Displayer.removeDocFilePageTab(INSTANCE.opened);
        } else {
            System.out.println("Impossible de supprimer la page : le document ne peut être vide");
        }
    }
}
