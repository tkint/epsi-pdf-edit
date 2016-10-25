import controller.ImageController;
import controller.TableController;
import java.io.File;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.table.Cell;
import model.table.Table;

/**
 * Created by Thomas on 10/10/2016.
 * @author Thomas Kint
 */
public class Main extends Application {
    public static String docTitle = "test";
    public static String text = "Hello World!";
    public static String img = "./img/poros.jpg";
    
    @Override
    public void start(Stage primaryStage) {
        // Instanciation du programme
        Main main = new Main();
        
        Button btnGenerate = new Button();
        btnGenerate.setLayoutX(90);
        btnGenerate.setLayoutY(10);
        btnGenerate.setText("Générer document");
        btnGenerate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    PDDocument document = new PDDocument();
                    document.save(docTitle + ".pdf");
                    document.close();
                    System.out.println("Document généré");
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        Button btnAddImage = new Button();
        btnAddImage.setLayoutX(100);
        btnAddImage.setLayoutY(60);
        btnAddImage.setText("Ajouter image");
        btnAddImage.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                main.testAddImage();
                System.out.println("Image ajoutée");
            }
        });
        
        Button btnExtractImage = new Button();
        btnExtractImage.setLayoutX(100);
        btnExtractImage.setLayoutY(110);
        btnExtractImage.setText("Extraire image");
        btnExtractImage.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                main.testExtractImage();
                System.out.println("Image extraite");
            }
        });
        
        Pane root = new Pane();
        
        root.getChildren().add(btnGenerate);
        root.getChildren().add(btnAddImage);
        root.getChildren().add(btnExtractImage);
        
        Scene scene = new Scene(root, 300, 200);
        
        primaryStage.setTitle(text);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    
    public static void main(String[] args) throws IOException {
        launch(args);
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
            document.close();
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
