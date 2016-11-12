
import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import app.Config;
import app.Instance;
import src.view.controller.HomeController;

/**
 * Created by Thomas on 10/10/2016.
 *
 * @author Thomas Kint
 */
public class Main extends Application implements Config {

    private static final Instance INSTANCE = Instance.getInstance();
    
    @Override
    public void start(Stage stage) throws Exception {
        // Définition de la scene d'accueil
        FXMLLoader homeLoader = new FXMLLoader(getClass().getResource("view/fxml/home.fxml"));
        homeLoader.setResources(TRANSLATOR);
        
        Parent homeParent = (Parent) homeLoader.load();
        
        HomeController homeController = (HomeController) homeLoader.getController();
        homeController.setStage(stage);
        
        Scene homeScene = new Scene(homeParent);
        
        stage.setTitle(TRANSLATOR.getString("APP_NAME"));
        stage.setResizable(false);
        stage.setScene(homeScene);
        stage.show();
        
        INSTANCE.load();
    }
    
    @Override
    public void stop() throws IOException {
        INSTANCE.save();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
