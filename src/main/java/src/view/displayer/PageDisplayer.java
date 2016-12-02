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
import javafx.event.EventType;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
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
        ImageView imageViewPDF = setImagePDF(pageIndex);

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

    private static ScrollPane setScrollPane() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background: " + BACKGROUND_COLOR + "; -fx-background-color: " + BACKGROUND_COLOR + ";");
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

//        scrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
//            int direction = 0;
//            if (scrollPane.getVvalue() >= scrollPane.getVmax()) {
//                if (newValue.doubleValue() >= oldValue.doubleValue() || observable.getValue().doubleValue() >= oldValue.doubleValue()) {
//                    direction = 1;
//                } else {
//                    direction = -1;
//                }
//            } else if (scrollPane.getVvalue() <= scrollPane.getVmin()) {
//                direction = -1;
//            }
//            TabDisplayer.getPagination().setCurrentPageIndex(TabDisplayer.getPagination().getCurrentPageIndex() + direction);
//        });
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
    private static ImageView setImagePDF(int pageIndex) {
        ImageView imagePDF = new ImageView();

        setImage(imagePDF, pageIndex);

        setScrollControl(imagePDF);

        setAreaSelectControl(imagePDF);

        return imagePDF;
    }

    public static void setZoom(int zoom) {
        ImageView imagePDF = getImagePDF();
        imagePDF.setFitWidth(imagePDF.getImage().getWidth() * zoom * INITIAL_SCALE / 100);
        imagePDF.setFitHeight(imagePDF.getImage().getHeight() * zoom * INITIAL_SCALE / 100);
    }

    /**
     * Transforme la page actuelle en image
     *
     * @param imagePDF
     * @param pageIndex
     */
    public static void setImage(ImageView imagePDF, int pageIndex) {
        try {
            DocFile docFile = INSTANCE.getDocFileOpened();

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
                                        TraceDisplayer.drawTable(fromPosX, fromPosY, toPosX, toPosY);
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
    public static float calculateXAxis(float posX) {
        DocFile docFile = INSTANCE.getDocFileOpened();
        PDPage page = docFile.getCurrentPage();

        float x = 0;

        switch (page.getRotation()) {
            case 0:
                x = (float) (posX * page.getMediaBox().getWidth() / getImagePDF().getFitWidth());
                break;
            case 90:
                x = (float) (posX * page.getMediaBox().getWidth() / getImagePDF().getFitWidth());
                break;
            case 180:
                x = (float) (posX * page.getMediaBox().getWidth() / getImagePDF().getFitWidth());
                break;
            case 270:
                x = (float) (posX * page.getMediaBox().getWidth() / getImagePDF().getFitWidth());
                break;
        }

        return x;
    }

    /**
     * Convertit la coordonnée JavaFX sur l'axe des Y en coordonnée PDFBox
     *
     * @param posY
     * @return
     */
    public static float calculateYAxis(float posY) {
        DocFile docFile = INSTANCE.getDocFileOpened();
        PDPage page = docFile.getCurrentPage();

        float y = 0;

        switch (page.getRotation()) {
            case 0:
                y = (float) (posY * page.getMediaBox().getHeight() / getImagePDF().getFitHeight());
                break;
            case 90:
                y = (float) (posY * page.getMediaBox().getHeight() / getImagePDF().getFitHeight());
                break;
            case 180:
                y = (float) (posY * page.getMediaBox().getHeight() / getImagePDF().getFitHeight());
                break;
            case 270:
                y = (float) (posY * page.getMediaBox().getHeight() / getImagePDF().getFitHeight());
                break;
        }

        return y;
    }
}
