/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.displayer;

import app.Config;
import app.Instance;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import org.apache.pdfbox.rendering.PDFRenderer;
import src.model.DocFile;
import src.view.controller.menu.MenuView;

/**
 *
 * @author Thomas
 */
public class PageDisplayer implements Config {

    private static final Instance INSTANCE = Instance.getInstance();

    /**
     * Défini la page à afficher
     *
     * @param docFile
     * @param pageIndex
     * @return
     */
    public static ScrollPane displayPage(DocFile docFile, int pageIndex) {
        // Panneau supérieur (calque de sélection)
        Pane pane = TraceDisplayer.setTrace();

        // Conteneur de l'image
        ImageView imageViewPDF = setImagePDF(pageIndex, docFile);

        // Création du panneau
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(imageViewPDF, pane);

        // On met le stackPane dans une grille d'alignement
        GridPane gridPane = setGridPane();
        gridPane.add(stackPane, 0, 0);

        // On met le contenu dans le scrollPane
        ScrollPane scrollPane = setScrollPane();
        scrollPane.setContent(gridPane);

        docFile.setSelectedPage(pageIndex);

        return scrollPane;
    }

    /**
     * Défini la grille d'alignement des éléments
     *
     * @return
     */
    private static GridPane setGridPane() {
        // Définition de la grille d'alignement
        GridPane gridPane = new GridPane();

        // Contrainte de lignes
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(100);
        row.setFillHeight(false);
        row.setValignment(VPos.CENTER);
        gridPane.getRowConstraints().add(row);

        // Contraintes de colonnes
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(100);
        column.setFillWidth(false);
        column.setHalignment(HPos.CENTER);
        gridPane.getColumnConstraints().add(column);

        return gridPane;
    }

    /**
     * Défini le panneau de scroll
     *
     * @return
     */
    private static ScrollPane setScrollPane() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background: " + BACKGROUND_COLOR + "; -fx-background-color: " + BACKGROUND_COLOR + ";");
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        return scrollPane;
    }

    /**
     * Instancie et paramètre l'image de la page
     *
     * @param docFile DocFile que l'on souhaite afficher
     * @param renderer PDFRenderer renderer du document ciblé
     * @param pageIndex Id de la page à afficher
     * @return ImageView
     * @throws IOException Exception
     */
    private static ImageView setImagePDF(int pageIndex, DocFile docFile) {
        ImageView imagePDF = new ImageView();

        setImage(imagePDF, pageIndex, docFile);

        setScrollControl(imagePDF);

        setAreaSelectControl(imagePDF);

        imagePDF.setOnMouseMoved((mouseEvent) -> {
            if (mouseEvent.getX() <= imagePDF.getX() + 10 || mouseEvent.getX() >= imagePDF.getX() + imagePDF.getFitWidth() - 10) {
                INSTANCE.stage.getScene().setCursor(Cursor.H_RESIZE);
            } else if (mouseEvent.getY() <= imagePDF.getY() + 10 || mouseEvent.getY() >= imagePDF.getY() + imagePDF.getFitHeight() - 10) {
                INSTANCE.stage.getScene().setCursor(Cursor.V_RESIZE);
            } else {
                INSTANCE.stage.getScene().setCursor(Cursor.DEFAULT);
            }
        });

        imagePDF.setOnMouseExited((mouseEvent) -> {
            INSTANCE.stage.getScene().setCursor(Cursor.DEFAULT);
        });

        imagePDF.setOnMouseClicked((mouseEvent) -> {
            PageDisplayer.isElementUnderMouse(mouseEvent);
        });

        return imagePDF;
    }

    public static void isElementUnderMouse(MouseEvent mouseEvent) {
        
    }

    /**
     * Défini le zoom de la page
     *
     * @param zoom
     */
    public static void setZoom(int zoom) {
        ImageView imagePDF = getImagePDF();
        if (imagePDF != null) {
            imagePDF.setFitWidth(imagePDF.getImage().getWidth() * zoom * INITIAL_SCALE / 100);
            imagePDF.setFitHeight(imagePDF.getImage().getHeight() * zoom * INITIAL_SCALE / 100);
            TraceDisplayer.clearTrace();
        }
    }

    /**
     * Transforme la page actuelle en image
     *
     * @param imagePDF
     * @param pageIndex
     */
    public static void setImage(ImageView imagePDF, int pageIndex, DocFile docFile) {
        try {
            PDFRenderer renderer = new PDFRenderer(docFile.getDocument());

            // Transformation de la page en image
            BufferedImage bufferedImage = renderer.renderImageWithDPI(pageIndex, PDF_DISPLAY_DPI);
            WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);
            imagePDF.setImage(image);

            imagePDF.setId("imagePDF");
            imagePDF.setStyle("-fx-effect: dropshadow(three-pass-box, black, 100, 0, 0, 0);");

            // On met l'image sous la même forme (paysage, portrait) que la page
            imagePDF.setFitWidth(bufferedImage.getWidth() * docFile.getZoom() * INITIAL_SCALE / 100);
            imagePDF.setFitHeight(bufferedImage.getHeight() * docFile.getZoom() * INITIAL_SCALE / 100);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Paramètre les actions au scroll
     *
     * @param docFile
     * @param imagePDF
     * @param trace
     */
    private static void setScrollControl(ImageView imagePDF) {
        DocFile docFile = INSTANCE.getDocFileOpened();

        imagePDF.setOnScroll((scrollEvent) -> {
            if (scrollEvent.isControlDown()) {
                scrollEvent.consume();

                // Si on scroll vers le bas, on zoom en avant, sinon on zoom vers l'arrière
                if (scrollEvent.getDeltaY() > 0) {
                    MenuView.btnZoomIn();
                } else if (scrollEvent.getDeltaY() < 0) {
                    MenuView.btnZoomOut();
                }

                TraceDisplayer.clearTrace();
            }
        });
    }

    /**
     * Paramètre la zone de sélection
     *
     * @param docFile
     * @param imagePDF
     * @param trace
     */
    private static void setAreaSelectControl(ImageView imagePDF) {
        imagePDF.setOnMousePressed((pressEvent) -> {
            if (pressEvent.isPrimaryButtonDown()) {
                INSTANCE.getDocFileOpened().setTempTable(null);
                INSTANCE.getDocFileOpened().setTraceTable(null);
                double fromPosX = pressEvent.getX();
                double fromPosY = pressEvent.getY();
                imagePDF.setOnMouseDragged((dragEvent) -> {
                    if (dragEvent.isPrimaryButtonDown()) {
                        double toPosX = dragEvent.getX();
                        double toPosY = dragEvent.getY();

                        if (INSTANCE.hasToolActive()) {
                            switch (INSTANCE.getCurrentTool()) {
                                case ADDTEXT:
                                    TraceDisplayer.drawAreaSelect(fromPosX, fromPosY, toPosX, toPosY);
                                    break;
                                case ADDTABLE: {
                                    try {
                                        TraceDisplayer.drawTable(fromPosX, fromPosY, toPosX, toPosY, 3, 3);
                                    } catch (IOException ex) {
                                        System.out.println(ex.toString());
                                    }
                                }
                                break;
                            }
                        }
                    }
                });
            } else if (pressEvent.isSecondaryButtonDown()) {
                ContextMenuDisplayer.displayContextMenu(pressEvent.getSceneX() + 5, pressEvent.getSceneY() - 35);
            }
        });
    }

    /**
     * Récupère l'ImageView de la page en cours
     *
     * @return
     */
    public static ImageView getImagePDF() {
        TabPane tabPane = (TabPane) INSTANCE.stage.getScene().lookup("#documents");
        Tab tab = tabPane.getSelectionModel().getSelectedItem();

        ImageView imagePDF = (ImageView) tab.getContent().lookup("#imagePDF");

        return imagePDF;
    }

    /**
     * Convertit la coordonnée JavaFX sur l'axe des X en coordonnée PDFBox
     *
     * @param posX
     * @return
     */
    public static float convertXToPDF(float posX) {
        return (float) (posX * INSTANCE.getDocFileOpened().getCurrentPage().getMediaBox().getWidth() / getImagePDF().getFitWidth());
    }

    /**
     * Convertit la coordonnée JavaFX sur l'axe des Y en coordonnée PDFBox
     *
     * @param posY
     * @return
     */
    public static float convertYToPDF(float posY) {
        return (float) (posY * INSTANCE.getDocFileOpened().getCurrentPage().getMediaBox().getHeight() / getImagePDF().getFitHeight());
    }
}
