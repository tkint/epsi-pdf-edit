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
import javafx.geometry.Insets;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import org.apache.pdfbox.rendering.PDFRenderer;
import src.model.DocFile;

/**
 *
 * @author Thomas
 */
public class Displayer implements Config {

    private static final Instance INSTANCE = Instance.getInstance();

    public static void displayDocFiles() throws IOException {
        for (DocFile docFile : INSTANCE.docFiles) {
            displayDocFileNewTab(docFile, docFile.getShortFileName());
        }
    }

    /**
     * Affiche le document ouvert dans un nouvel onglet
     *
     * @param tabName
     * @throws IOException
     */
    public static void displayDocFileNewTab(DocFile docfile, String tabName) throws IOException {
        if (docfile != null && INSTANCE.stageName == "main") {
            // Récupération du tableau d'onglets
            TabPane tabPane = (TabPane) INSTANCE.stage.getScene().lookup("#documents");

            // Définition de l'onglet
            Tab tab = new Tab(defineTabName(tabName));
            // Définition de la pagination
            Pagination pagination = new Pagination(docfile.getDocument().getNumberOfPages());

            // Définition du renderer
            PDFRenderer renderer = new PDFRenderer(docfile.getDocument());

            // Paramétrage du tableau d'onglets
            if (TAB_CLOSE) {
                tabPane.tabClosingPolicyProperty().set(TabPane.TabClosingPolicy.SELECTED_TAB);
            }
            tabPane.getSelectionModel().selectedIndexProperty().addListener((obs, ov, nv) -> {
                if (obs != null) {
                    INSTANCE.opened = obs.getValue().intValue();
                }
            });

            // Paramétrage de l'onglet
            tab.setId(Integer.toString(docfile.getId()));
            tab.closableProperty().set(true);
            tab.setOnClosed((Event event) -> {
                int id = Integer.parseInt(tab.getId());
                INSTANCE.closeDocFile(id);
                System.out.println(TRANSLATOR.getString("TAB_CLOSING") + " : " + id);
            });

            // Paramétrage de la pagination
            pagination.setStyle("-fx-background-color: white");
            pagination.setPadding(new Insets(0, 0, 0, 0));
            pagination.setCurrentPageIndex(docfile.getSelectedPage());
            pagination.setPageFactory((Integer pageIndex) -> {
                ScrollPane scrollPane = new ScrollPane();
                try {
                    // Conteneur de l'image
                    ImageView imageView = setImageView(docfile, renderer, pageIndex);
                    StackPane stackPane = new StackPane(imageView);

                    // Panneau défilant
                    scrollPane = setScrollPane();
                    scrollPane.setContent(stackPane);

                    docfile.setSelectedPage(pageIndex);
                    pagination.setCurrentPageIndex(pageIndex);
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
    public static void refreshTabName(int id) {
        if (INSTANCE.docFiles.size() > 0 && INSTANCE.docFiles.get(id) != null && INSTANCE.stageName.equals("main")) {
            File file = INSTANCE.docFiles.get(id).getFile();

            TabPane tabPane = (TabPane) INSTANCE.stage.getScene().lookup("#documents");
            Tab tab = tabPane.getTabs().get(id);
            tab.setText(defineTabName(file.getName().substring(0, file.getName().length() - 4)));
            tabPane.getSelectionModel().select(tab);
        }
    }

    public static void refreshTab() {
        TabPane tabPane = (TabPane) INSTANCE.stage.getScene().lookup("#documents");
        Tab tab = tabPane.getSelectionModel().getSelectedItem();

        DocFile docFile = INSTANCE.getDocFileOpened();

        int page = docFile.getSelectedPage();

        PDFRenderer renderer = new PDFRenderer(docFile.getDocument());

        Pagination pagination = (Pagination) tab.getContent();

        pagination.setPageCount(docFile.getDocument().getNumberOfPages());
        pagination.setPageFactory((Integer pageIndex) -> {
            ScrollPane scrollPane = new ScrollPane();
            try {
                // Conteneur de l'image
                ImageView imageView = setImageView(docFile, renderer, pageIndex);
                StackPane stackPane = new StackPane(imageView);

                // Panneau défilant
                scrollPane = setScrollPane();
                scrollPane.setContent(stackPane);

                docFile.setSelectedPage(pageIndex);
                pagination.setCurrentPageIndex(pageIndex);
            } catch (IOException e) {
                System.out.println(e.toString());
            }
            return scrollPane;
        });
        pagination.setCurrentPageIndex(page);
    }

    /**
     * Ferme l'onglet désigné par l'id
     *
     * @param id
     */
    public static void closeTabByDocFileId(int id) {
        TabPane tabPane = (TabPane) INSTANCE.stage.getScene().lookup("#documents");
        Tab tab = null;
        for (Tab t : tabPane.getTabs()) {
            if (Integer.parseInt(t.getId()) == id) {
                INSTANCE.closeDocFile(id);
                tabPane.getTabs().remove(tab);
            }
        }
    }

    /**
     * Sélectionne l'onglet précisé
     *
     * @param fileName String nom du fichier dont on veut l'onglet
     */
    public static void selectDocFileTab(String fileName) {
        TabPane tabPane = (TabPane) INSTANCE.stage.getScene().lookup("#documents");
        Tab tab = tabPane.getTabs().get(0);
        for (Tab t : tabPane.getTabs()) {
            if (t.getText().equals(defineTabName(fileName))) {
                tab = t;
            }
        }
        tabPane.getSelectionModel().select(tab);
    }

    /**
     * Ajoute une page à l'onglet ouvert
     *
     * @param id Id de l'onglet
     */
    public static void addDocFilePageTab(int id) {
        if (INSTANCE.getDocFileOpened() != null) {
            // Récupération du tableau d'onglets
            TabPane tabPane = (TabPane) INSTANCE.stage.getScene().lookup("#documents");
            // Récupération de l'onglet
            Tab tab = tabPane.getTabs().get(id);
            // Récupération de la pagination
            Pagination pagination = (Pagination) tab.getContent();

            if (INSTANCE.getDocFileOpened().getDocument().getNumberOfPages() > pagination.getPageCount()) {
                pagination.setPageCount(pagination.getPageCount() + 1);
                int pageSelected = pagination.getPageCount() - 1;

                pagination.getPageFactory().call(pageSelected);
                pagination.setCurrentPageIndex(pageSelected);
                INSTANCE.getDocFileOpened().setSelectedPage(pageSelected);
            }
        }
    }

    /**
     * Supprime une page à l'onglet ouvert
     *
     * @param id Id de l'onglet
     */
    public static void removeDocFilePageTab(int id) {
        if (INSTANCE.getDocFileOpened() != null) {
            // Récupération du tableau d'onglets
            TabPane tabPane = (TabPane) INSTANCE.stage.getScene().lookup("#documents");
            // Récupération de l'onglet
            Tab tab = tabPane.getTabs().get(id);
            // Récupération de la pagination
            Pagination pagination = (Pagination) tab.getContent();

            if (INSTANCE.getDocFileOpened().getDocument().getNumberOfPages() < pagination.getPageCount()) {
                pagination.setPageCount(pagination.getPageCount() - 1);
                int pageSelected = pagination.getPageCount() - 1;

                pagination.setCurrentPageIndex(pageSelected);
                INSTANCE.getDocFileOpened().setSelectedPage(pageSelected);
            }
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
        scrollPane.setStyle("-fx-background: " + PDF_BACKGROUND_COLOR + "; -fx-background-color: " + PDF_BACKGROUND_COLOR + ";");
        return scrollPane;
    }

    /**
     * Instancie et paramètre l'imageView de la page
     *
     * @param docfile DocFile que l'on souhaite afficher
     * @param renderer PDFRenderer renderer du document ciblé
     * @param pageIndex Id de la page à afficher
     * @return ImageView
     * @throws IOException Exception
     */
    private static ImageView setImageView(DocFile docfile, PDFRenderer renderer, int pageIndex) throws IOException {
        // Transformation de la page en image
        BufferedImage bufferedImage = renderer.renderImageWithDPI(pageIndex, PDF_DISPLAY_DPI);
        WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);
        ImageView imageView = new ImageView(image);

        // Paramétrage de l'image
        imageView.setStyle("-fx-effect: dropshadow(three-pass-box, black, 100, 0, 0, 0);");
        // On met l'image sous la même forme (paysage, portrait) que la page
        if (bufferedImage.getHeight() > bufferedImage.getWidth()) {
            imageView.setFitWidth(docfile.getWidth());
            imageView.setFitHeight(docfile.getHeight());
        } else {
            imageView.setFitWidth(docfile.getHeight());
            imageView.setFitHeight(docfile.getWidth());
        }

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
                docfile.setWidth(docfile.getWidth() * factor);
                docfile.setHeight(docfile.getHeight() * factor);
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
    public static String defineTabName(String tabName) {
        String s;
        if (tabName.length() > TAB_TITLE_SIZE) {
            s = tabName.substring(0, TAB_TITLE_SIZE) + "...";
        } else {
            s = tabName;
        }
        return s;
    }
}
