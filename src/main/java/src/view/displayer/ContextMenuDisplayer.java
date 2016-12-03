/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.displayer;

import app.Config;
import app.Instance;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import src.controller.TableController;
import src.model.DocFile;
import src.model.table.Table;

/**
 *
 * @author tkint
 */
public class ContextMenuDisplayer implements Config {

    private static final Instance INSTANCE = Instance.getInstance();

    public static void displayContextMenu(double posX, double posY) {
        if (INSTANCE.hasToolActive()) {
            switch (INSTANCE.getCurrentTool()) {
                case ADDTABLE:
                    displayTableMenu(posX, posY);
                    break;
            }
        }
    }

    private static void displayTableMenu(double posX, double posY) {
        DocFile docFile = INSTANCE.getDocFileOpened();
        Table traceTable = docFile.getTraceTable();
        Table tempTable = docFile.getTempTable();

        ContextMenu contextMenu = setContextMenu(posX, posY);
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

        MenuItem addColumn = new MenuItem(TRANSLATOR.getString("ADD_COLUMN"));
        addColumn.setOnAction((event) -> {
            traceTable.addColumns(1);
            tempTable.addColumns(1);
            refreshTableTrace(traceTable, tempTable);
        });

        MenuItem removeColumn = new MenuItem(TRANSLATOR.getString("REMOVE_COLUMN"));
        removeColumn.setOnAction((event) -> {
            traceTable.removeLastColumn();
            tempTable.removeLastColumn();
            refreshTableTrace(traceTable, tempTable);
        });

        MenuItem addRow = new MenuItem(TRANSLATOR.getString("ADD_ROW"));
        addRow.setOnAction((event) -> {
            traceTable.addRows(1);
            tempTable.addRows(1);
            refreshTableTrace(traceTable, tempTable);
        });

        MenuItem removeRow = new MenuItem(TRANSLATOR.getString("REMOVE_ROW"));
        removeRow.setOnAction((event) -> {
            traceTable.removeLastRow();
            tempTable.removeLastRow();
            refreshTableTrace(traceTable, tempTable);
        });

        MenuItem cancel = new MenuItem(TRANSLATOR.getString("CANCEL"));
        cancel.setOnAction((event) -> {
            TraceDisplayer.clearTrace();
        });

        contextMenu.getItems().addAll(validate, addColumn, removeColumn, addRow, removeRow, cancel);
        contextMenu.show(INSTANCE.stage);
    }

    private static ContextMenu setContextMenu(double posX, double posY) {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setAnchorX(posX);
        contextMenu.setAnchorY(posY + 60);

        return contextMenu;
    }

    private static void refreshTableTrace(Table traceTable, Table tempTable) {
        try {
            TraceDisplayer.drawTable(traceTable.getPosX(), traceTable.getPosY(), traceTable.getPosX() + traceTable.getWidth(), traceTable.getPosY() + traceTable.getHeight(), traceTable.getLastRow().getCells().size(), traceTable.getRows().size());
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}
