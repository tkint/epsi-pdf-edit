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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import org.apache.pdfbox.rendering.PDFRenderer;
import src.model.DocFile;

/**
 *
 * @author Thomas
 */
public class PageDisplayer implements Config {

    private static final Instance INSTANCE = Instance.getInstance();

    /**
     * Défini la page à afficher
     *
     * @param pagination
     * @param docFile
     * @param renderer
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
        scrollPane.setStyle("-fx-background: " + PDF_BACKGROUND_COLOR + "; -fx-background-color: " + PDF_BACKGROUND_COLOR + ";");
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
    private static ImageView setImagePDF(int pageIndex) {
        ImageView imagePDF = new ImageView();

        setImage(imagePDF, pageIndex);

        setScrollControl(imagePDF);

        setAreaSelectControl(imagePDF);

        return imagePDF;
    }

    public static void setZoom(int zoom) {
        ImageView imagePDF = getImagePDF();
        imagePDF.setFitWidth(imagePDF.getImage().getWidth() * zoom * .25 / 100);
        imagePDF.setFitHeight(imagePDF.getImage().getHeight()* zoom * .25 / 100);
    }

    /**
     * Transforme la page actuelle en image
     *
     * @param docFile
     * @param renderer
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
            if (bufferedImage.getHeight() > bufferedImage.getWidth()) {
                imagePDF.setFitWidth(bufferedImage.getWidth());
                imagePDF.setFitHeight(bufferedImage.getHeight());
            } else {
                imagePDF.setFitWidth(bufferedImage.getHeight());
                imagePDF.setFitHeight(bufferedImage.getWidth());
            }
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

                // Si on ne scroll pas, on ne fait rien
                if (scrollEvent.getDeltaY() == 0) {
                    return;
                }

                boolean canZoomIn = imagePDF.getFitWidth() * PDF_DISPLAY_ZOOM_SCALE < PDF_DISPLAY_INITIAL_RATIO.getWidth() * PDF_DISPLAY_ZOOMIN_LIMIT;
                boolean canZoomOut = imagePDF.getFitWidth() * PDF_DISPLAY_ZOOM_SCALE > PDF_DISPLAY_INITIAL_RATIO.getWidth() * PDF_DISPLAY_ZOOMOUT_LIMIT;

                float factor = 1.0f;

                // Si on scroll vers le bas, on augmente le ratio, sinon on le baisse
                if (scrollEvent.getDeltaY() > 0 && canZoomIn) {
                    factor = PDF_DISPLAY_ZOOM_SCALE;
                } else if (scrollEvent.getDeltaY() < 0 && canZoomOut) {
                    factor = 1 / PDF_DISPLAY_ZOOM_SCALE;
                }

                // Application du changement de ratio sur l'image
                imagePDF.setFitWidth(imagePDF.getFitWidth() * factor);
                imagePDF.setFitHeight(imagePDF.getFitHeight() * factor);

                // Mise à jour du ratio du document ouvert
                docFile.setWidth(docFile.getWidth() * factor);
                docFile.setHeight(docFile.getHeight() * factor);

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
                TraceDisplayer.clearTrace();
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
                                case ADDTABLE:
                                    TraceDisplayer.drawTable(fromPosX, fromPosY, toPosX, toPosY);
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

    public static ImageView getImagePDF() {
        TabPane tabPane = (TabPane) INSTANCE.stage.getScene().lookup("#documents");
        Tab tab = tabPane.getSelectionModel().getSelectedItem();

        ImageView imagePDF = (ImageView) tab.getContent().lookup("#imagePDF");

        return imagePDF;
    }
}
