
import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import app.Config;
import app.Instance;
import src.view.controller.MainController;

/**
 * Created by Thomas on 10/10/2016.
 *
 * @author Thomas Kint
 */
public class Main extends Application implements Config {

    private static final Instance INSTANCE = Instance.getInstance();
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/fxml/main.fxml"));
        loader.setResources(TRANSLATOR);

        Parent root = (Parent) loader.load();

        MainController controller = (MainController) loader.getController();

        controller.setStage(stage);

        Scene scene = new Scene(root);

        stage.setTitle(TRANSLATOR.getString("APP_NAME"));
        stage.setScene(scene);
        stage.show();
        
        INSTANCE.load();
    }
    
    @Override
    public void stop() {
        INSTANCE.save();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
