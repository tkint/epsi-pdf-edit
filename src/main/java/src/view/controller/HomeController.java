/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.controller;

import static app.Config.TRANSLATOR;
import app.Instance;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.view.Displayer;
import src.view.controller.menu.MenuFile;

/**
 * FXML Controller class
 *
 * @author Thomas
 */
public class HomeController implements Initializable {

    private static final Instance INSTANCE = Instance.getInstance();
    private static final MenuFile MENUFILE = MenuFile.getInstance();

    private Stage stage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void btnFileNew() throws IOException {
        if (this.stage != null) {
            goToMainScene();
            MENUFILE.btnFileNew();
        }
    }

    public void btnFileOpen() throws IOException {
        if (this.stage != null) {
            goToMainScene();
            MENUFILE.btnFileOpen();
        }
    }

    private void goToMainScene() throws IOException {
        if (this.stage != null) {
            this.stage.close();

            Stage stage = new Stage();

            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/view/fxml/main.fxml"));
            mainLoader.setResources(TRANSLATOR);

            Parent mainParent = (Parent) mainLoader.load();

            MainController mainController = (MainController) mainLoader.getController();
            mainController.setStage(stage);

            Scene mainScene = new Scene(mainParent);

            stage.setTitle(TRANSLATOR.getString("APP_NAME"));
            stage.setMaximized(true);
            stage.setScene(mainScene);
            stage.show();
            
            Displayer.displayDocFiles();
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        INSTANCE.stageName = "home";
    }
}
