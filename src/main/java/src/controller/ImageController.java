package src.controller;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
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
     * @param contentStream
     * @param imagePath
     * @param posX
     * @param posY
     * @param scale 
     */
    public void addImage(PDDocument document, PDPageContentStream contentStream, String imagePath, int posX, int posY, float scale) {
        try {
            PDImageXObject image = PDImageXObject.createFromFile(imagePath, document);

            contentStream.drawImage(image, posX, posY, image.getWidth() * scale, image.getHeight() * scale);
        } catch(IOException e) {
            System.out.println(e.toString());
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
        } catch(IOException e) {
            System.out.println(e.toString());
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
        } catch(IOException e) {
            System.out.println(e.toString());
        }
    }
}
