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
import javafx.event.Event;
import javafx.event.EventHandler;
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

    private static final Instance INSTANCE = Instance.getInstance();

    //  <editor-fold desc="Display">
    public static void displayDocFileNewTab(String tabName) throws IOException {
        if (INSTANCE.getDocFileOpened() != null) {
            TabPane tabPane = (TabPane) INSTANCE.stage.getScene().lookup(TAB_PANE_ID);
            if (TAB_CLOSE) {
                tabPane.tabClosingPolicyProperty().set(TabPane.TabClosingPolicy.SELECTED_TAB);
            }

            Tab tab = new Tab(defineTabName(tabName));
            tab.closableProperty().set(true);

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setStyle("-fx-background: " + PDF_BACKGROUND_COLOR + ";");

            VBox vBox = new VBox();
            vBox.setSpacing(PDF_DISPLAY_PAGE_PADDING);
            vBox.setAlignment(Pos.CENTER);

            PDDocument document = INSTANCE.getDocFileOpened().getDocument();
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

            tab.setId(Integer.toString(tabPane.getTabs().size()));
            tab.setContent(scrollPane);
            tab.setOnClosed(new EventHandler<Event>() {
                @Override
                public void handle(Event event) {
                    int id = Integer.parseInt(tab.getId());
                    if (id > tabPane.getTabs().size()) {
                        id = 0;
                    }
                    System.out.println("Fermeture de l'onglet : " + id);
                    INSTANCE.closeDocFile(id);
                }
            });

            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            tabPane.getSelectionModel().selectedIndexProperty().addListener((obs, ov, nv) -> {
                if (nv != null) {
                    INSTANCE.opened = nv.intValue();
                }
            });
        }
    }

    public static void updateDocFileTab(int id) {
        if (INSTANCE.docFiles.size() > 0 && INSTANCE.docFiles.get(id) != null) {
            File file = INSTANCE.docFiles.get(id).getFile();

            TabPane tabPane = (TabPane) INSTANCE.stage.getScene().lookup(TAB_PANE_ID);
            Tab tab = tabPane.getTabs().get(id);
            tab.setText(defineTabName(file.getName().substring(0, file.getName().length() - 4)));
            tabPane.getSelectionModel().select(tab);
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
