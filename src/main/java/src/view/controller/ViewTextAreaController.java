/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.controller;

import src.model.DocFile;
import app.Instance;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import src.controller.TextController;
import src.view.displayer.TabDisplayer;

/**
 *
 * @author Ludo
 */
public class ViewTextAreaController implements Initializable {

    private static final Instance INSTANCE = Instance.getInstance();

    private Stage stage;

    @FXML
    public TextArea jTextArea;

    @FXML
    public Button buttonAreaValider;

    @FXML
    public Button buttonAreaAnnuler;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void exit() {
        INSTANCE.setNoTool();
        this.stage.close();
    }

    public void btnValider() {
        try {
            DocFile docFile = INSTANCE.getDocFileOpened();
            PDDocument document = docFile.getDocument();
            PDPage page = docFile.getCurrentPage();
            PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
            TextController tc = new TextController();

            tc.drawArea(contentStream, docFile.getTempAreaSelect(), jTextArea.getText());

            contentStream.close();
            TabDisplayer.refreshOpenedTab();

            exit();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void btnAnnuler() {
        exit();
    }
}
