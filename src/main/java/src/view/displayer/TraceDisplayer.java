/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.displayer;

import app.Config;
import app.Instance;
import java.io.IOException;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import src.model.DocFile;
import src.model.table.Cell;
import src.model.table.Row;
import src.model.table.Table;

/**
 *
 * @author Thomas
 */
public class TraceDisplayer implements Config {

    public static float scale;
    private static final Instance INSTANCE = Instance.getInstance();

    /**
     * Dessine le tableau sur le calque
     * @param fromPosX
     * @param fromPosY
     * @param toPosX
     * @param toPosY
     * @param columns
     * @param rows
     * @throws IOException 
     */
    public static void drawTable(double fromPosX, double fromPosY, double toPosX, double toPosY, int columns, int rows) throws IOException {
        ImageView imagePDF = PageDisplayer.getImagePDF();

        toPosX = limitX(toPosX);
        toPosY = limitY(toPosY);

        float traceTablePosX = (float) fromPosX;
        float traceTablePosY = (float) fromPosY;
        float traceTableWidth = (float) toPosX - (float) fromPosX - 1;
        float traceTableHeight = (float) toPosY - (float) fromPosY - 1;

        Table traceTable;
        if (INSTANCE.getDocFileOpened().getTraceTable() != null) {
            traceTable = INSTANCE.getDocFileOpened().getTraceTable();
            traceTable.refreshPos(traceTablePosX, traceTablePosY, traceTableWidth, traceTableHeight);
        } else {
            traceTable = new Table(traceTablePosX, traceTablePosY, traceTableWidth, traceTableHeight);
            traceTable.generateTable(columns, rows);
        }

        INSTANCE.getDocFileOpened().setTraceTable(traceTable);

        Pane trace = getTrace();
        clearTrace();

        for (Row row : traceTable.getRows()) {
            for (Cell cell : row.getCells()) {
                Rectangle rectangle = new Rectangle(cell.getPosX(), cell.getPosY(), cell.getWidth(), cell.getHeight());
                rectangle.setFill(TABLE_DRAW_BACKGROUND);
                rectangle.setStroke(TABLE_DRAW_BORDER);

                trace.getChildren().add(rectangle);
            }
        }

        float tempTablePosX = PageDisplayer.convertXToPDF((float) fromPosX);
        float tempTablePosY = PageDisplayer.convertYToPDF((float) imagePDF.getFitHeight() - (float) fromPosY);
        float tempTableWidth = PageDisplayer.convertXToPDF((float) toPosX - (float) fromPosX - 1);
        float tempTableHeight = PageDisplayer.convertYToPDF((float) toPosY - (float) fromPosY - 1);

        Table tempTable;
        if (INSTANCE.getDocFileOpened().getTempTable() != null) {
            tempTable = INSTANCE.getDocFileOpened().getTempTable();
            tempTable.refreshPos(tempTablePosX, tempTablePosY, tempTableWidth, tempTableHeight);
        } else {
            tempTable = new Table(tempTablePosX, tempTablePosY, tempTableWidth, tempTableHeight);
            tempTable.setInverted(true);
            tempTable.generateTable(columns, rows);
        }

        INSTANCE.getDocFileOpened().setTempTable(tempTable);
    }

    /**
     * Dessine la zone de sélection sur le tableau
     * @param fromPosX
     * @param fromPosY
     * @param toPosX
     * @param toPosY 
     */
    public static void drawAreaSelect(double fromPosX, double fromPosY, double toPosX, double toPosY) {
        DocFile docFile = INSTANCE.getDocFileOpened();

        ImageView imagePDF = PageDisplayer.getImagePDF();

        Pane trace = getTrace();
        clearTrace();

        toPosX = limitX(toPosX);
        toPosY = limitY(toPosY);

        double posX = 0;
        double posY = 0;

        double width = 0;
        double height = 0;

        if (toPosX > fromPosX) {
            posX = fromPosX;
            width = toPosX - fromPosX - 1;
        } else {
            posX = toPosX;
            width = fromPosX - toPosX;
        }

        if (toPosY > fromPosY) {
            posY = fromPosY;
            height = toPosY - fromPosY - 1;
        } else {
            posY = toPosY;
            height = fromPosY - toPosY;
        }

        // Application de la zone de sélection sur le document
        docFile.updateAreaSelect(posX, posY, width, height);

        // Définition du rectangle de sélection
        Rectangle selection = new Rectangle(posX, posY, width, height);
        selection.setFill(AREA_SELECT_BACKGROUND);
        selection.setStroke(AREA_SELECT_BORDER);

        // Ajout du rectangle sur le calque
        trace.getChildren().add(selection);
    }

    /**
     * Nettoie tous les éléments du calque
     */
    public static void clearTrace() {
        TabPane tabPane = (TabPane) INSTANCE.stage.getScene().lookup("#documents");
        Tab tab = tabPane.getSelectionModel().getSelectedItem();

        Pane trace = (Pane) tab.getContent().lookup("#trace");

        trace.getChildren().clear();
    }

    /**
     * Retourne le panneau de calque
     * @return 
     */
    public static Pane getTrace() {
        TabPane tabPane = (TabPane) INSTANCE.stage.getScene().lookup("#documents");
        Tab tab = tabPane.getSelectionModel().getSelectedItem();

        Pane trace = (Pane) tab.getContent().lookup("#trace");

        return trace;
    }
    
    /**
     * Défini le panneau de calque
     * @return 
     */
    public static Pane setTrace() {
        Pane pane = new Pane();
        pane.setId("trace");
        pane.setMouseTransparent(true);
        //pane.setStyle("-fx-border-color: red");

        return pane;
    }

    /**
     * Limite les coordonnées sur l'axe des X
     * @param x
     * @return 
     */
    private static double limitX(double x) {
        ImageView imagePDF = PageDisplayer.getImagePDF();

        if (x > imagePDF.getFitWidth()) {
            x = imagePDF.getFitWidth();
        } else if (x < 0) {
            x = 0;
        }

        return x;
    }

    /**
     * Limite les coordonnées sur l'axe des Y
     * @param y
     * @return 
     */
    private static double limitY(double y) {
        ImageView imagePDF = PageDisplayer.getImagePDF();

        if (y > imagePDF.getFitHeight()) {
            y = imagePDF.getFitHeight();
        } else if (y < 0) {
            y = 0;
        }

        return y;
    }
}
