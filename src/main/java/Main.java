import controller.ImageController;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;

/**
 * Created by Thomas on 10/10/2016.
 */
public class Main {
    public static String docTitle = "test";
    public static String text = "Hello World!";
    public static String img = "./img/poros.jpg";

    public static void main(String[] args) throws IOException
    {
        ImageController imageController = new ImageController();

        PDDocument doc = new PDDocument();
        try
        {
            PDPage page = new PDPage();
            doc.addPage(page);

            PDFont font = PDType1Font.HELVETICA_BOLD;

            PDPageContentStream contents = new PDPageContentStream(doc, page);
            contents.beginText();
            contents.setFont(font, 12);
            contents.newLineAtOffset(100, 700);
            contents.showText(text);
            contents.endText();
            contents.close();

            imageController.addImage(doc, page, img, 100, 400, 0.2f);

            doc.save(docTitle + ".pdf");
        }
        finally
        {
            doc.close();
        }
    }
}
