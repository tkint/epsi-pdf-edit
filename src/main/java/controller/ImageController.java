package controller;

import java.awt.image.RenderedImage;
import java.io.File;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;

/**
 * Created by Thomas on 10/10/2016.
 * @author Thomas Kint
 */
public class ImageController {
    /**
     * Ajoute une image à la page et à la position spécifiée.
     * @param document
     * @param page
     * @param imagePath
     * @param posX
     * @param posY
     * @param scale 
     */
    public void addImage(PDDocument document, PDPage page, String imagePath, int posX, int posY, float scale) {
        try {
            PDPageContentStream content = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
            
            PDImageXObject image = PDImageXObject.createFromFile(imagePath, document);

            content.drawImage(image, posX, posY, image.getWidth() * scale, image.getHeight() * scale);
            content.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Cherche l'image spécifiée
     * @param document
     * @param imageName
     * @return RenderedImage
     */
    public RenderedImage getImageByName(PDDocument document, String imageName) {
        RenderedImage image = null;
        
        try {
            int i = 0;
            while (image == null && i < document.getPages().getCount()) {
                PDPage page = document.getPage(i);
                PDResources resources = page.getResources();
                for (COSName name : resources.getXObjectNames()) {
                    if (name.getName().equalsIgnoreCase(imageName)) {
                        image = ((PDImageXObject) resources.getXObject(name)).getImage();
                    }
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
    
    /**
     * Extraie l'image spécifiée
     * @param document
     * @param imageName
     * @param imageType 
     */
    public void extractImageByName(PDDocument document, String imageName, String imageType) {
        try {
            RenderedImage image = getImageByName(document, imageName);
            File file = new File(imageName + "." + imageType);
            if (image == null) {
                System.err.println("Can not find image " + imageName);
            } else {
                ImageIO.write(image, imageType, file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
