package controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;

/**
 * Created by Thomas on 10/10/2016.
 */
public class ImageController {
    public void addImage(PDDocument document, PDPage page, String imagePath, int posX, int posY, float scale) throws IOException {
        PDImageXObject image = PDImageXObject.createFromFile(imagePath, document);

        PDPageContentStream content = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);

        content.drawImage(image, posX, posY, image.getWidth() * scale, image.getHeight() * scale);
        content.close();
    }
}
