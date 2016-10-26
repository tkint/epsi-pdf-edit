import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import app.Config;
import src.view.controller.MainController;

/**
 * Created by Thomas on 10/10/2016.
 * @author Thomas Kint
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/fxml/Main.fxml"));
        
        Parent root = (Parent)loader.load();
        
        MainController controller = (MainController)loader.getController();
        
        controller.setStage(stage);
        
        Scene scene = new Scene(root);
        
        stage.setTitle(Config.APP_TITLE);
        stage.setScene(scene);
        stage.show();
    }
    
    
    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
