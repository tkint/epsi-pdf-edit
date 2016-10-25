import controller.ImageController;
import controller.TableController;
import controller.TextController;
import java.io.File;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
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
        // Instanciation du programme
        Main main = new Main();
        
        try
        {
            // Instanciation du document à créer
            PDDocument document = new PDDocument();
            
            // Sauvegarde du document
            document.save(docTitle + ".pdf");
            
            //main.testAddImage();
            //main.testExtractImage();
            main.testAddTable();
        } catch(IOException e) {
            System.out.println(e.toString());
        }
    }
    
    private void testAddImage() {
        try {
            ImageController imageController = new ImageController();
            
            // Chargement du document
            File file = new File(docTitle + ".pdf");
            PDDocument document = PDDocument.load(file);
            
            document.addPage(new PDPage());
            
            PDPageContentStream contentStream = new PDPageContentStream(document, document.getPage(document.getNumberOfPages() - 1), PDPageContentStream.AppendMode.APPEND, true);
            
            // Ajout d'une image sur la deuxième page
            imageController.addImage(document, contentStream, img, 100, 400, 0.2f);
            
            contentStream.close();
            
            document.save(docTitle + ".pdf");
        } catch(IOException e) {
            System.out.println(e.toString());
        }
    }
    
    private void testExtractImage() {
        try {
            ImageController imageController = new ImageController();
            
            // Chargement du document
            File file = new File(docTitle + ".pdf");
            PDDocument document = PDDocument.load(file);

            // Extraction de l'image définie dans le format défini
            imageController.extractImageByName(document, "Im1", "png");
            
            // Fermeture du document
            document.close();
        } catch(IOException e) {
            System.out.println(e.toString());
        }
    }
    
    private void testAddTable() {
        try {
            TableController tableController = new TableController();
            
            // Chargement du document
            File file = new File(docTitle + ".pdf");
            PDDocument document = PDDocument.load(file);
            
            document.addPage(new PDPage());
            
            PDPageContentStream contentStream = new PDPageContentStream(document, document.getPage(document.getNumberOfPages() - 1), PDPageContentStream.AppendMode.APPEND, true);
            
            // Instanciation d'un tableau
            Table table = new Table(0, 100, 600, 200, 200);
            // Génération du tableau
            table.generateTable(2, 7);
            table.addColumns(2);
            
            // Récupération d'une cellule
            Cell cell = table.getCell(0);
            // Initialisation du contenu
            cell.setContent("TEST");
            // Affichage du contenu dans la cellule
            tableController.displayCellContent(contentStream, cell, "center", "middle");
            
            // Ajout d'un tableau
            tableController.drawTable(contentStream, table);
            
            contentStream.close();
            document.save(docTitle + ".pdf");
        } catch(IOException e) {
            System.out.println(e.toString());
        }
    }
}
