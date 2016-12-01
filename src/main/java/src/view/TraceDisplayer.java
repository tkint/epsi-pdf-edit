/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view;

import app.Config;
import app.Instance;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import src.model.DocFile;

/**
 *
 * @author Thomas
 */
public class TraceDisplayer implements Config {

    private static final Instance INSTANCE = Instance.getInstance();

    public static Pane setTrace() {
        Pane pane = new Pane();
        pane.setId("trace");
        pane.setMouseTransparent(true);

        return pane;
    }

    public static void drawAreaSelect(double toPosX, double toPosY, double fromPosX, double fromPosY) {
        if (INSTANCE.hasToolActive()) {
            DocFile docFile = INSTANCE.getDocFileOpened();

            ImageView imagePDF = PageDisplayer.getImagePDF();

            Pane trace = getTrace();

            if (toPosX > imagePDF.getFitWidth()) {
                toPosX = imagePDF.getFitWidth();
            } else if (toPosX < 0) {
                toPosX = 0;
            }

            if (toPosY > imagePDF.getFitHeight()) {
                toPosY = imagePDF.getFitHeight();
            } else if (toPosY < 0) {
                toPosY = 0;
            }

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
            trace.getChildren().clear();
            trace.getChildren().add(selection);
        }
    }

    public static void clearTrace() {
        TabPane tabPane = (TabPane) INSTANCE.stage.getScene().lookup("#documents");
        Tab tab = tabPane.getSelectionModel().getSelectedItem();

        Pane trace = (Pane) tab.getContent().lookup("#trace");

        trace.getChildren().clear();
    }

    public static Pane getTrace() {
        TabPane tabPane = (TabPane) INSTANCE.stage.getScene().lookup("#documents");
        Tab tab = tabPane.getSelectionModel().getSelectedItem();

        Pane trace = (Pane) tab.getContent().lookup("#trace");

        return trace;
    }
}
