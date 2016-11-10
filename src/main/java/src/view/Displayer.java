/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view;

import app.Config;
import static app.Config.PDF_BACKGROUND_COLOR;
import app.Instance;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import src.model.DocFile;

/**
 *
 * @author Thomas
 */
public class Displayer implements Config {

    private static final Instance INSTANCE = Instance.getInstance();

    /**
     * Affiche le document ouvert dans un nouvel onglet
     *
     * @param tabName
     * @throws IOException
     */
    public static void displayDocFileNewTab(String tabName) throws IOException {
        if (INSTANCE.getDocFileOpened() != null) {
            // Récupération du tableau d'onglets
            TabPane tabPane = (TabPane) INSTANCE.stage.getScene().lookup("#documents");
            // Récupération du document
            PDDocument document = INSTANCE.getDocFileOpened().getDocument();
            // Initialisation du renderer
            PDFRenderer renderer = new PDFRenderer(document);

            // Définition de l'onglet
            Tab tab = new Tab(defineTabName(tabName));
            // Définition de la pagination
            Pagination pagination = new Pagination(document.getNumberOfPages());

            // Paramétrage du tableau d'onglets
            if (TAB_CLOSE) {
                tabPane.tabClosingPolicyProperty().set(TabPane.TabClosingPolicy.SELECTED_TAB);
            }
            tabPane.getSelectionModel().selectedIndexProperty().addListener((obs, ov, nv) -> {
                if (nv != null) {
                    INSTANCE.opened = nv.intValue();
                }
            });

            // Paramétrage de l'onglet
            tab.setId(Integer.toString(tabPane.getTabs().size()));
            tab.closableProperty().set(true);
            tab.setOnClosed((Event event) -> {
                int id = Integer.parseInt(tab.getId());
                if (id > tabPane.getTabs().size()) {
                    id = 0;
                }
                System.out.println("Fermeture de l'onglet : " + id);
                INSTANCE.closeDocFile(id);
            });

            // Paramétrage de la pagination
            pagination.setPadding(new Insets(0, 0, 0, 0));
            pagination.setCurrentPageIndex(INSTANCE.getDocFileOpened().getSelectedPage());
            pagination.setPageFactory((Integer pageIndex) -> {
                ScrollPane scrollPane = new ScrollPane();
                try {
                    // Conteneur de l'image
                    ImageView imageView = setImageView(renderer.renderImage(pageIndex), scrollPane);
                    StackPane stackPane = new StackPane(imageView);

                    // Panneau défilant
                    scrollPane = setScrollPane();
                    scrollPane.setContent(stackPane);

                    INSTANCE.getDocFileOpened().setSelectedPage(pageIndex);
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
                return scrollPane;
            });

            tab.setContent(pagination);

            // Ajout de l'onglet au tableau d'onglets
            tabPane.getTabs().add(tab);

            // Sélection du nouvel onglet
            tabPane.getSelectionModel().select(tab);
        }
    }

    /**
     * Met à jour le nom de l'onglet précisé
     *
     * @param id
     */
    public static void updateDocFileTab(int id) {
        if (INSTANCE.docFiles.size() > 0 && INSTANCE.docFiles.get(id) != null) {
            File file = INSTANCE.docFiles.get(id).getFile();

            TabPane tabPane = (TabPane) INSTANCE.stage.getScene().lookup("#documents");
            Tab tab = tabPane.getTabs().get(id);
            tab.setText(defineTabName(file.getName().substring(0, file.getName().length() - 4)));
            tabPane.getSelectionModel().select(tab);
        }
    }

    /**
     * Instancie et paramètre le scrollPane de la page
     *
     * @return ScrollPane
     */
    private static ScrollPane setScrollPane() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return scrollPane;
    }

    /**
     * Instancie et paramètre l'imageView de la page
     *
     * @param bufferedImage
     * @return
     */
    private static ImageView setImageView(BufferedImage bufferedImage, ScrollPane scrollPane) {
        // Transformation du résultat du renderer en imageView
        WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);
        ImageView imageView = new ImageView(image);

        // Paramétrage de l'image
//        imageView.setPreserveRatio(true);
        imageView.setFitWidth(INSTANCE.getDocFileOpened().getWidth());
        imageView.setFitHeight(INSTANCE.getDocFileOpened().getHeight());

        imageView.setOnScroll((event) -> {
            if (event.isControlDown()) {
                event.consume();

                // Si on ne scroll pas, on ne fait rien
                if (event.getDeltaY() == 0) {
                    return;
                }
                
                boolean canZoomIn = imageView.getFitWidth() * PDF_DISPLAY_ZOOM_SCALE < PDF_DISPLAY_INITIAL_RATIO.getWidth() * PDF_DISPLAY_ZOOMIN_LIMIT;
                boolean canZoomOut = imageView.getFitWidth() * PDF_DISPLAY_ZOOM_SCALE > PDF_DISPLAY_INITIAL_RATIO.getWidth() * PDF_DISPLAY_ZOOMOUT_LIMIT;

                float factor = 1.0f;

                // Si on scroll vers le bas, on augmente le ratio, sinon on le baisse
                if (event.getDeltaY() > 0 && canZoomIn) {
                    factor = PDF_DISPLAY_ZOOM_SCALE;
                } else if (event.getDeltaY() < 0 && canZoomOut) {
                    factor = 1 / PDF_DISPLAY_ZOOM_SCALE;
                }

                // Application du changement de ratio sur l'image
                imageView.setFitWidth(imageView.getFitWidth() * factor);
                imageView.setFitHeight(imageView.getFitHeight() * factor);

                // Mise à jour du ratio du document ouvert
                INSTANCE.getDocFileOpened().setWidth(INSTANCE.getDocFileOpened().getWidth() * factor);
                INSTANCE.getDocFileOpened().setHeight(INSTANCE.getDocFileOpened().getHeight() * factor);
            }
        });
        return imageView;
    }

    /**
     * Raccourci le nom de l'onglet s'il est trop long
     *
     * @param tabName
     * @return
     */
    private static String defineTabName(String tabName) {
        String s;
        if (tabName.length() > TAB_TITLE_SIZE) {
            s = tabName.substring(0, TAB_TITLE_SIZE) + "...";
        } else {
            s = tabName;
        }
        return s;
    }

    /**
     * Affiche le document dans un nouvel onglet (deprecated)
     *
     * @param tabName
     * @throws IOException
     */
    public static void displayDocFileNewTabOld(String tabName) throws IOException {
        if (INSTANCE.getDocFileOpened() != null) {
            // Récupération du tableau d'onglets
            TabPane tabPane = (TabPane) INSTANCE.stage.getScene().lookup("#documents");
            // Récupération du document
            PDDocument document = INSTANCE.getDocFileOpened().getDocument();

            // Définition de l'onglet
            Tab tab = new Tab(defineTabName(tabName));
            // Définition du conteneur d'éléments de l'onglet
            VBox content = new VBox();

            // Définition du panneau défilant
            ScrollPane scrollPane = new ScrollPane();
            // Définition du conteneur des images
            VBox vBox = new VBox();

            // Création de la barre d'outil
            AnchorPane toolbar = new AnchorPane();
            // Création de la pagination
            Pagination pagination = new Pagination(document.getNumberOfPages());

            // Paramétrage du tableau d'onglets
            if (TAB_CLOSE) {
                tabPane.tabClosingPolicyProperty().set(TabPane.TabClosingPolicy.SELECTED_TAB);
            }
            tabPane.getSelectionModel().selectedIndexProperty().addListener((obs, ov, nv) -> {
                if (nv != null) {
                    INSTANCE.opened = nv.intValue();
                }
            });

            // Paramétrage de l'onglet
            tab.setId(Integer.toString(tabPane.getTabs().size()));
            tab.closableProperty().set(true);
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

            // Paramétraye du panneau défilant
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setStyle("-fx-background: " + PDF_BACKGROUND_COLOR + ";");

            scrollPane.vvalueProperty().addListener(((obs, ov, nv) -> {
                DocFile df = INSTANCE.getDocFileOpened();
                float pageHeightOnScrollBar = (float) 1 / df.getDocument().getNumberOfPages();
                float capTop = df.getSelectedPage() * pageHeightOnScrollBar;
                float capBottom = (df.getSelectedPage() + 1) * pageHeightOnScrollBar;
                if (obs.getValue().floatValue() > capBottom && df.getNextPage() != null) {
                    INSTANCE.getDocFileOpened().setSelectedPage(df.getSelectedPage() + 1);
                } else if (obs.getValue().floatValue() < capTop && df.getPreviousPage() != null) {
                    INSTANCE.getDocFileOpened().setSelectedPage(df.getSelectedPage() - 1);
                }
                pagination.setCurrentPageIndex(INSTANCE.getDocFileOpened().getSelectedPage());
            }));

            // Paramétrage du conteneur des images
            vBox.setSpacing(PDF_DISPLAY_PAGE_PADDING);
            vBox.setAlignment(Pos.CENTER);

            PDFRenderer renderer = new PDFRenderer(document);
            if (document != null) {
                for (int i = 0; i < document.getPages().getCount(); i++) {
                    BufferedImage bufferedImage = renderer.renderImage(i);
                    WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);
                    ImageView imageView = new ImageView(image);
                    vBox.getChildren().add(imageView);
                }
            }

            // Paramétrage de la boite à outils
            // Paramétrage de la pagination
            // Ajout du contenu dans le panneau défilant
            scrollPane.setContent(vBox);
            // Ajout du panneau défilant au conteneur de l'onglet
            content.getChildren().add(scrollPane);

            // Ajout de la pagination à la barre d'outils
            toolbar.getChildren().add(pagination);
            // Ajout de la barre d'outils au conteneur de l'onglet
            content.getChildren().add(toolbar);

            // Ajout du panneau défilant à l'onglet
            tab.setContent(content);
            // Ajout de l'onglet au tableau d'onglets
            tabPane.getTabs().add(tab);

            // Sélection du nouvel onglet
            tabPane.getSelectionModel().select(tab);
        }
    }
}
