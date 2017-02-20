/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.displayer;

import app.Config;
import app.Instance;
import java.io.IOException;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import src.controller.ImageController;
import src.controller.TableController;
import src.model.DocFile;
import src.model.ImagePDF;
import src.model.table.Table;

/**
 *
 * @author tkint
 */
public class ContextMenuDisplayer implements Config {

    private static final Instance INSTANCE = Instance.getInstance();

    /**
     * Affiche le menu contextuel en fonction de l'outil sélectionné
     *
     * @param posX
     * @param posY
     */
    public static void displayContextMenu(double posX, double posY) {
        if (INSTANCE.hasToolActive()) {
            switch (INSTANCE.getCurrentTool()) {
                case ADDTABLE:
                    displayTableMenu(posX, posY);
                    break;
                case ADDIMAGE:
                    displayImageMenu(posX, posY);
                    break;
            }
        }
    }

    /**
     * Affichage du menu contextuel de l'outil de tableau
     *
     * @param posX
     * @param posY
     */
    private static void displayTableMenu(double posX, double posY) {
        DocFile docFile = INSTANCE.getDocFileOpened();
        Table traceTable = docFile.getTraceTable();
        Table tempTable = docFile.getTempTable();

        // Initialisation du menu contextuel
        ContextMenu contextMenu = setContextMenu(posX, posY);

        // Choix VALIDER
        MenuItem validate = new MenuItem(TRANSLATOR.getString("VALIDATE"));
        validate.setOnAction((event) -> {
            try {
                PDDocument document = docFile.getDocument();
                PDPage page = docFile.getCurrentPage();
                PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true);

                TableController tc = new TableController();
                tc.printTable(contentStream, docFile.getTempTable());

                contentStream.close();

                TabDisplayer.refreshOpenedTab();
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        });

        // Choix AJOUT DE COLONNE
        MenuItem addColumn = new MenuItem(TRANSLATOR.getString("ADD_COLUMN"));
        addColumn.setOnAction((event) -> {
            traceTable.addColumns(1);
            tempTable.addColumns(1);
            refreshTableTrace(traceTable, tempTable);
        });

        // Choix SUPPRESSION DE COLONNE
        MenuItem removeColumn = new MenuItem(TRANSLATOR.getString("REMOVE_COLUMN"));
        removeColumn.setOnAction((event) -> {
            traceTable.removeLastColumn();
            tempTable.removeLastColumn();
            refreshTableTrace(traceTable, tempTable);
        });

        // Choix AJOUT DE LIGNE
        MenuItem addRow = new MenuItem(TRANSLATOR.getString("ADD_ROW"));
        addRow.setOnAction((event) -> {
            traceTable.addRows(1);
            tempTable.addRows(1);
            refreshTableTrace(traceTable, tempTable);
        });

        // Choix SUPPRESSION DE LIGNE
        MenuItem removeRow = new MenuItem(TRANSLATOR.getString("REMOVE_ROW"));
        removeRow.setOnAction((event) -> {
            traceTable.removeLastRow();
            tempTable.removeLastRow();
            refreshTableTrace(traceTable, tempTable);
        });

        // Choix ANNULER
        MenuItem cancel = new MenuItem(TRANSLATOR.getString("CANCEL"));
        cancel.setOnAction((event) -> {
            TraceDisplayer.clearTrace();
        });

        // Ajout des choix au menu contextuel
        contextMenu.getItems().addAll(validate, addColumn, removeColumn, addRow, removeRow, cancel);

        // Affichage du menu contextuel
        contextMenu.show(INSTANCE.stage);
    }

    /**
     * Affichage du menu contextuel de l'outil d'image
     *
     * @param posX
     * @param posY
     */
    private static void displayImageMenu(double posX, double posY) {
        DocFile docFile = INSTANCE.getDocFileOpened();
        ImagePDF tempImage = docFile.getTempImagePDF();

        // Initialisation du menu contextuel
        ContextMenu contextMenu = setContextMenu(posX, posY);

        // Choix VALIDER
        MenuItem validate = new MenuItem(TRANSLATOR.getString("VALIDATE"));
        validate.setOnAction((event) -> {
            try {
                PDDocument document = docFile.getDocument();
                PDPage page = docFile.getCurrentPage();
                PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true);

                ImageController ic = new ImageController();
                ic.addImage(document, contentStream, tempImage);

                contentStream.close();

                TabDisplayer.refreshOpenedTab();

                INSTANCE.setNoTool();
                TraceDisplayer.getTrace().setMouseTransparent(true);
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        });

        // Choix ANNULER
        MenuItem cancel = new MenuItem(TRANSLATOR.getString("CANCEL"));
        cancel.setOnAction((event) -> {
            TraceDisplayer.clearTrace();
            INSTANCE.setNoTool();
            TraceDisplayer.getTrace().setMouseTransparent(true);
        });

        // Ajout des choix au menu contextuel
        contextMenu.getItems().addAll(validate, cancel);

        // Affichage du menu contextuel
        contextMenu.show(INSTANCE.stage);
    }

    /**
     * Défini la position du menu contextuel
     *
     * @param posX
     * @param posY
     * @return
     */
    private static ContextMenu setContextMenu(double posX, double posY) {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setAnchorX(posX);
        contextMenu.setAnchorY(posY + 60);

        return contextMenu;
    }

    /**
     * Met à jour le tableau affiché sur le calque
     *
     * @param traceTable
     * @param tempTable
     */
    private static void refreshTableTrace(Table traceTable, Table tempTable) {
        try {
            TraceDisplayer.drawTable(traceTable.getPosX(), traceTable.getPosY(), traceTable.getPosX() + traceTable.getWidth(), traceTable.getPosY() + traceTable.getHeight(), traceTable.getLastRow().getCells().size(), traceTable.getRows().size());
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}
