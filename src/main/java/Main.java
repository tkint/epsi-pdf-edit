import controller.ImageController;
import controller.TableController;
import controller.TextController;
import java.io.File;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import model.table.Table;

/**
 * Created by Thomas on 10/10/2016.
 * @author Thomas Kint
 */
public class Main {
    public static String docTitle = "test";
    public static String text = "Hello World!";
    public static String img = "./img/poros.jpg";

    public static void main(String[] args) throws IOException
    {
        // Instanciation des controllers
        TextController textController = new TextController();
        ImageController imageController = new ImageController();
        TableController tableController = new TableController();
        
        try
        {
            // Instanciation du document � cr�er
            PDDocument document = new PDDocument();
            
            // Ajout de deux pages
            document.addPage(new PDPage());
            document.addPage(new PDPage());
            document.addPage(new PDPage());
            
            // Ajout du texte sur la premi�re page (index commence � 0)
            textController.addText(document, document.getPage(0), text, 100, 700, 15, PDType1Font.COURIER);

            // Ajout d'une image sur la deuxi�me page
            imageController.addImage(document, document.getPage(1), img, 100, 400, 0.2f);
            
            // Instanciation d'un tableau de 500 * 500
            Table table = new Table(0, 200, 200);
            // G�n�ration du tableau
            table.generateTable(2, 3);
            table.addColumns(1);
            table.addRows(2);
            System.out.println(table);
            System.out.println(table.getCell(28));
            
            // Ajout d'un tableau
            tableController.drawTable(document, document.getPage(0), table, 100, 600);
            
            // Sauvegarde du document
            document.save(docTitle + ".pdf");
            
            // Chargement du document
            File file = new File(docTitle + ".pdf");
            PDDocument doc2 = PDDocument.load(file);
            
            // Extraction de l'image d�finie dans le format d�fini
            imageController.extractImageByName(doc2, "Im1", "png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
