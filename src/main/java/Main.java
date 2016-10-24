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
import model.table.Cell;
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
            // Instanciation du document à créer
            PDDocument document = new PDDocument();
            
            // Ajout de deux pages
            document.addPage(new PDPage());
            document.addPage(new PDPage());
            document.addPage(new PDPage());
            
            // Ajout du texte sur la première page (index commence à 0)
            textController.addText(document, document.getPage(0), text, 100, 700, 15, PDType1Font.COURIER);

            // Ajout d'une image sur la deuxième page
            imageController.addImage(document, document.getPage(1), img, 100, 400, 0.2f);
            
            // Instanciation d'un tableau
            Table table = new Table(0, 100, 600, 200, 200);
            // Génération du tableau
            table.generateTable(2, 7);
            
            // Récupération d'une cellule
            Cell cell1 = table.getCell(5);
            // Initialisation du contenu
            cell1.setContent("TEST");
            // Calcul de la position du texte
            float posX = cell1.getPosX() + (cell1.getWidth() / 2);
            float posY = cell1.getPosY() - (cell1.getHeight() / 2);
            
            // Affichage du texte dans la cellule
            textController.addText(document, document.getPage(0), cell1.getContent(), posX, posY, 12, PDType1Font.COURIER);
            
            // Check du tableau
            System.out.println(table);
                    
            // Ajout d'un tableau
            tableController.drawTable(document, document.getPage(0), table);
            
            // Sauvegarde du document
            document.save(docTitle + ".pdf");
            
            // Chargement du document
            File file = new File(docTitle + ".pdf");
            PDDocument doc2 = PDDocument.load(file);
            
            // Extraction de l'image définie dans le format défini
            imageController.extractImageByName(doc2, "Im1", "png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
