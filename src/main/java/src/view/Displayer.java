/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view;

import app.Config;
import app.Instance;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 *
 * @author Thomas
 */
public class Displayer implements Config {

    private static final Instance instance = Instance.getInstance();

    //  <editor-fold desc="Display">
    public static void displayPDFTab() throws IOException {
        if (instance.documents.size() > 0) {
            TabPane tabPane = (TabPane) instance.stage.getScene().lookup("#documents");

            int doc = 0;
            for (PDDocument document : instance.documents) {
                Tab tab = new Tab("Doc " + doc);

                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setFitToWidth(true);
                scrollPane.setFitToHeight(true);

                VBox vBox = new VBox();
                vBox.setSpacing(PDF_DISPLAY_PAGE_PADDING);
                vBox.setAlignment(Pos.CENTER);

                PDFRenderer renderer = new PDFRenderer(document);

                for (int id = 0; id < document.getPages().getCount(); id++) {
                    BufferedImage bufferedImage = renderer.renderImage(id);
                    WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);
                    ImageView imageView = new ImageView(image);
                    vBox.getChildren().add(imageView);
                }

                scrollPane.setContent(vBox);
                tab.setContent(scrollPane);
                tabPane.getTabs().add(tab);
            }
        }
    }

    public static void displayPDFNewTab(int id, String tabName) throws IOException {
        if (instance.documents.size() > 0 && instance.documents.get(id) != null) {
            TabPane tabPane = (TabPane) instance.stage.getScene().lookup("#documents");
            //tabPane.tabClosingPolicyProperty().set(TabPane.TabClosingPolicy.ALL_TABS);

            Tab tab = new Tab(defineTabName(tabName));
            //tab.closableProperty().set(true);

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setStyle("-fx-background: #2B2B2B;");

            VBox vBox = new VBox();
            vBox.setSpacing(PDF_DISPLAY_PAGE_PADDING);
            vBox.setAlignment(Pos.CENTER);

            PDDocument document = instance.documents.get(id);
            PDFRenderer renderer = new PDFRenderer(document);
            if (document != null) {
                for (int i = 0; i < document.getPages().getCount(); i++) {
                    BufferedImage bufferedImage = renderer.renderImage(i);
                    WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);
                    ImageView imageView = new ImageView(image);
                    vBox.getChildren().add(imageView);
                }
            }

            scrollPane.setContent(vBox);

            tab.setContent(scrollPane);

            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);

            tabPane.getSelectionModel().selectedIndexProperty().addListener((obs, ov, nv) -> {
                instance.opened = nv.intValue();
                instance.updateDocument(null, null);
            });
        }
    }

    public static void updatePDFTab(int id) {
        if (instance.files.size() > 0 && instance.files.get(id) != null) {
            File file = instance.files.get(id);

            TabPane tabPane = (TabPane) instance.stage.getScene().lookup("#documents");
            Tab tab = tabPane.getTabs().get(id);
            tab.setText(defineTabName(file.getName().substring(0, file.getName().length() - 4)));
        }
    }

    public static void displayPDF() {
        if (instance.documentOpened != null) {
            try {
                ScrollPane scrollPane = (ScrollPane) instance.stage.getScene().lookup("#display");
                scrollPane.setFitToHeight(true);
                scrollPane.setFitToWidth(true);

                VBox vBox = new VBox();
                vBox.getChildren().clear();
                vBox.setSpacing(PDF_DISPLAY_PAGE_PADDING);
                vBox.setAlignment(Pos.CENTER);

//                vBox.setOnScroll(new EventHandler<ScrollEvent>() {
//                    @Override
//                    public void handle(ScrollEvent scrollEvent) {
//                        scrollEvent.consume();
//
//                        if (scrollEvent.getDeltaY() == 0) {
//                            return;
//                        }
//
//                        double scaleFactor = 0;
//                        if (scrollEvent.getDeltaY() > 0) {
//                            scaleFactor = PDF_DISPLAY_ZOOM_SCALE;
//                        } else {
//                            scaleFactor = 1 / PDF_DISPLAY_ZOOM_SCALE;
//                        }
//
//                        vBox.setScaleX(vBox.getScaleX() * scaleFactor);
//                        vBox.setScaleY(vBox.getScaleY() * scaleFactor);
//                    }
//                });
                PDFRenderer renderer = new PDFRenderer(instance.documentOpened);

                for (int id = 0; id < instance.documentOpened.getPages().getCount(); id++) {
                    BufferedImage bufferedImage = renderer.renderImage(id);
                    WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);
                    ImageView imageView = new ImageView(image);
                    vBox.getChildren().add(imageView);
                }

                scrollPane.setContent(vBox);
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }
    }
//  </editor-fold>

    private static String defineTabName(String tabName) {
        String s;
        if (tabName.length() > TAB_TITLE_SIZE) {
            s = tabName.substring(0, TAB_TITLE_SIZE) + "...";
        } else {
            s = tabName;
        }
        return s;
    }
}
