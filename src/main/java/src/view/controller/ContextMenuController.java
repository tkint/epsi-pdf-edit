/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.controller;

import app.Config;
import app.Instance;
import java.io.IOException;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import src.controller.ImageController;
import src.controller.TableController;
import src.model.DocFile;
import src.model.ImagePDF;
import src.model.table.Table;
import src.view.displayer.ContextMenuDisplayer;
import src.view.displayer.TabDisplayer;
import src.view.displayer.TraceDisplayer;

/**
 *
 * @author tkint
 */
public class ContextMenuController implements Config {

    private static final Instance INSTANCE = Instance.getInstance();

    /**
     * Bouton de validation de l'ajout de tableau
     *
     * @param docFile
     * @return
     */
    public static MenuItem validateTable(DocFile docFile) {
        MenuItem validate = new MenuItem(TRANSLATOR.getString("VALIDATE"));
        validate.setOnAction((event) -> {
            try {
                PDDocument document = docFile.getDocument();
                PDPage page = docFile.getCurrentPage();
                PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);

                TableController tc = new TableController();
                tc.printTable(contentStream, docFile.getTempTable(), true);

                contentStream.close();

                TabDisplayer.refreshOpenedTab();
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        });
        return validate;
    }

    /**
     * Bouton d'ajout de colonne
     *
     * @param traceTable
     * @param tempTable
     * @return
     */
    public static MenuItem addColumn(Table traceTable, Table tempTable) {
        MenuItem addColumn = new MenuItem(TRANSLATOR.getString("ADD_COLUMN"));
        addColumn.setOnAction((event) -> {
            traceTable.addColumns(1);
            tempTable.addColumns(1);
            ContextMenuDisplayer.refreshTableTrace(traceTable, tempTable);
        });
        return addColumn;
    }

    /**
     * Bouton de suppression de colonne
     *
     * @param traceTable
     * @param tempTable
     * @return
     */
    public static MenuItem removeColumn(Table traceTable, Table tempTable) {
        MenuItem removeColumn = new MenuItem(TRANSLATOR.getString("REMOVE_COLUMN"));
        removeColumn.setOnAction((event) -> {
            traceTable.removeLastColumn();
            tempTable.removeLastColumn();
            ContextMenuDisplayer.refreshTableTrace(traceTable, tempTable);
        });
        return removeColumn;
    }

    /**
     * Bouton d'ajout de ligne
     *
     * @param traceTable
     * @param tempTable
     * @return
     */
    public static MenuItem addRow(Table traceTable, Table tempTable) {
        MenuItem addRow = new MenuItem(TRANSLATOR.getString("ADD_ROW"));
        addRow.setOnAction((event) -> {
            traceTable.addRows(1);
            tempTable.addRows(1);
            ContextMenuDisplayer.refreshTableTrace(traceTable, tempTable);
        });
        return addRow;
    }

    /**
     * Bouton de suppression de ligne
     *
     * @param traceTable
     * @param tempTable
     * @return
     */
    public static MenuItem removeRow(Table traceTable, Table tempTable) {
        MenuItem removeRow = new MenuItem(TRANSLATOR.getString("REMOVE_ROW"));
        removeRow.setOnAction((event) -> {
            traceTable.removeLastRow();
            tempTable.removeLastRow();
            ContextMenuDisplayer.refreshTableTrace(traceTable, tempTable);
        });
        return removeRow;
    }

    public static MenuItem cancelTable() {
        MenuItem cancel = new MenuItem(TRANSLATOR.getString("CANCEL"));
        cancel.setOnAction((event) -> {
            TraceDisplayer.clearTrace();
        });
        return cancel;
    }

    /**
     * Bouton de validation d'ajout d'image
     *
     * @param docFile
     * @param tempImage
     * @return
     */
    public static MenuItem validateImage(DocFile docFile, ImagePDF tempImage) {
        MenuItem validate = new MenuItem(TRANSLATOR.getString("VALIDATE"));
        validate.setOnAction((event) -> {
            try {
                PDDocument document = docFile.getDocument();
                PDPage page = docFile.getCurrentPage();
                PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);

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
        return validate;
    }

    /**
     * Bouton de pivot de l'image
     *
     * @return
     */
    public static MenuItem rotateImageLeft(ImagePDF tempImage) {
        MenuItem rotateLeft = new MenuItem("Tourner à gauche");
        rotateLeft.setOnAction((event) -> {
            ContextMenuDisplayer.refreshImageTrace(tempImage, -90);
        });
        return rotateLeft;
    }

    /**
     * Bouton de pivot de l'image à droite
     *
     * @return
     */
    public static MenuItem rotateImageRight(ImagePDF tempImage) {
        MenuItem rotateRight = new MenuItem("Tourner à droite");
        rotateRight.setOnAction((event) -> {
            ContextMenuDisplayer.refreshImageTrace(tempImage, 90);
        });
        return rotateRight;
    }

    /**
     * Bouton d'annulation d'ajout d'image
     *
     * @return
     */
    public static MenuItem cancelImage() {
        MenuItem cancel = new MenuItem(TRANSLATOR.getString("CANCEL"));
        cancel.setOnAction((event) -> {
            TraceDisplayer.clearTrace();
            INSTANCE.setNoTool();
            TraceDisplayer.getTrace().setMouseTransparent(true);
        });
        return cancel;
    }
}
