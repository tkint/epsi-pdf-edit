/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.controller.menu;

import app.Config;
import app.Instance;
import enums.Tool;
import java.awt.image.BufferedImage;
import java.io.File;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import src.model.DocFile;
import src.model.ImagePDF;
import src.view.displayer.TraceDisplayer;

/**
 *
 * @author Thomas
 */
public class MenuEdit implements Config {

    private static final Instance INSTANCE = Instance.getInstance();

    /**
     * Bouton d'ajout de texte
     */
    public static void btnAddText() {
        switchTool(Tool.ADDTEXT);
    }

    /**
     * Bouton d'édition de texte
     */
    public static void btnEditText() {
        switchTool(Tool.EDITTEXT);
    }

    /**
     * Bouton d'ajout d'image
     */
    public static void btnAddImage() {
        switchTool(Tool.ADDIMAGE);
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(TRANSLATOR.getString("FILE_OPEN"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            fileChooser.setInitialDirectory(new File(System.getProperty(DEFAULT_DIR)));

            File file = fileChooser.showOpenDialog(INSTANCE.stage);

            if (file != null) {
                String fileName = file.getName();
                //String fileExtension = fileName.substring(fileName.indexOf(".") + 1, fileName.length());
                String fileExtension = "png";

                DocFile docFile = INSTANCE.getDocFileOpened();
                BufferedImage bufferedImage = ImageIO.read(file);
                WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);
                ImagePDF imageTemporaire = new ImagePDF(1, image, 0, 0, bufferedImage.getHeight(), bufferedImage.getWidth(), file.getPath());
                //imageTemporaire

                imageTemporaire.setWidth((float) TraceDisplayer.limitX(bufferedImage.getWidth() * docFile.getZoom() * INITIAL_SCALE / 100));
                imageTemporaire.setHeight((float) TraceDisplayer.limitY(bufferedImage.getHeight() * docFile.getZoom() * INITIAL_SCALE / 100));

                docFile.setTempImagePDF(imageTemporaire);
                docFile.setTraceImagePDF(imageTemporaire);
                TraceDisplayer.drawImage(imageTemporaire.getPosX(), imageTemporaire.getPosY(), 0);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Bouton d'édition d'image
     */
    public static void btnEditImage() {
        switchTool(Tool.EDITIMAGE);
    }

    /**
     * Bouton d'ajout de tableau
     */
    public static void btnAddTable() {
        switchTool(Tool.ADDTABLE);
    }

    /**
     * Bouton d'édition de tableau
     */
    public static void btnEditTable() {
        switchTool(Tool.EDITTABLE);
    }

    /**
     * Change l'outil actuellement sélectionné
     */
    private static void switchTool(Tool tool) {
        if (INSTANCE.getCurrentTool() == tool) {
            TraceDisplayer.clearTrace();
            INSTANCE.setNoTool();
        } else {
            INSTANCE.setCurrentTool(tool);
        }
    }
}
