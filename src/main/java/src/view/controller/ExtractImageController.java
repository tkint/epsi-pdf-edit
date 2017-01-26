/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.controller;

import app.Instance;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import src.controller.ImageController;
import src.model.DocFile;

/**
 * FXML Controller class
 *
 * @author tkint
 */
public class ExtractImageController implements Initializable {

    private static final Instance INSTANCE = Instance.getInstance();

    private Stage stage;

    @FXML
    public ListView listImages;

    @FXML
    public ImageView image;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addImages();
    }

    /**
     * Rempli la liste des images
     */
    private void addImages() {
        if (INSTANCE.getDocFileOpened() != null) {
            DocFile docFile = INSTANCE.getDocFileOpened();

            ObservableList<String> items = FXCollections.observableArrayList();

            ImageController imageController = new ImageController();

            HashMap<String, RenderedImage> map = imageController.getImages(docFile.getDocument());

            for (Map.Entry<String, RenderedImage> entry : map.entrySet()) {
                items.add(entry.getKey());
            }

            listImages.setItems(items);

            listImages.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                BufferedImage bufferedImage = (BufferedImage) map.get(newValue);
                WritableImage writableImage = SwingFXUtils.toFXImage(bufferedImage, null);

                if (bufferedImage.getHeight() > 350) {
                    image.setFitHeight(350);
                } else if (bufferedImage.getWidth() > 400) {
                    image.setFitWidth(400);
                } else {
                    image.setFitHeight(0);
                    image.setFitWidth(0);
                }

                image.setImage(writableImage);
            });
        }
    }

    /**
     * Retourne le stage
     *
     * @return
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Défini le stage
     *
     * @param stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Ferme la fenêtre d'extraction
     */
    public void exit() {
        this.stage.close();
    }
    
    /**
     * Extrait l'image actuellement sélectionnée
     * @throws IOException 
     */
    public void extract() throws IOException {
        if (listImages.getSelectionModel().getSelectedItem() != null) {
            DocFile docFile = INSTANCE.getDocFileOpened();
            
            ImageController imageController = new ImageController();
            
            HashMap<String, RenderedImage> map = imageController.getImages(docFile.getDocument());
            
            RenderedImage image = map.get(listImages.getSelectionModel().getSelectedItem());
            
            File file = new File(listImages.getSelectionModel().getSelectedItem() + ".jpg");
            if (image == null) {
                System.err.println("Can not find image " + listImages.getSelectionModel().getSelectedItem());
            } else {
                ImageIO.write(image, "jpg", file);
            }
        }
    }
}
