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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import src.controller.TextController;
import src.model.table.Cell;
import src.view.displayer.TabDisplayer;
import src.view.displayer.TraceDisplayer;

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

    @FXML
    public ComboBox comboBoxPoliceTextArea;

    @FXML
    public ComboBox comboBoxTailleTextArea;

    @FXML
    public ColorPicker colorPickerTextArea;

    private Integer cellId = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fillComboBoxTailleAndPolice();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void exit() {
        this.stage.close();
    }

    public void btnValider() {
        try {
            DocFile docFile = INSTANCE.getDocFileOpened();
            if (cellId == null) {
                PDDocument document = docFile.getDocument();
                PDPage page = docFile.getCurrentPage();
                PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
                TextController tc = new TextController();

                if (docFile.getTempAreaSelect() != null) {
                    tc.drawArea(contentStream, docFile.getTempAreaSelect(), jTextArea.getText(), (int) comboBoxTailleTextArea.getValue(), (PDType1Font) comboBoxPoliceTextArea.getValue());
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERREUR");
                    alert.setContentText("Veuillez sélectionner une zone de texte");
                    alert.show();
                }
                contentStream.close();
                TabDisplayer.refreshOpenedTab();
                INSTANCE.setNoTool();
                exit();
            } else {
                docFile.getTempTable().getCell(cellId).setContent(jTextArea.getText());
                
                Cell traceCell = docFile.getTraceTable().getCell(cellId);
                float posX = traceCell.getPosX() + 40;
                float posY = traceCell.getPosY() + 50;
                
                TraceDisplayer.addText(posX, posY, jTextArea.getText());
                
                this.stage.close();
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void btnAnnuler() {
        exit();
    }

    public void fillComboBoxTailleAndPolice() {
        for (int i = 5; i < 51; i++) {
            comboBoxTailleTextArea.getItems().add(i);
        }

        comboBoxPoliceTextArea.getItems().addAll(
                PDType1Font.COURIER,
                PDType1Font.COURIER_BOLD,
                PDType1Font.COURIER_BOLD_OBLIQUE,
                PDType1Font.COURIER_OBLIQUE,
                PDType1Font.HELVETICA,
                PDType1Font.HELVETICA_BOLD,
                PDType1Font.HELVETICA_BOLD_OBLIQUE,
                PDType1Font.HELVETICA_OBLIQUE,
                PDType1Font.SYMBOL,
                PDType1Font.TIMES_BOLD,
                PDType1Font.TIMES_BOLD_ITALIC,
                PDType1Font.TIMES_ROMAN,
                PDType1Font.ZAPF_DINGBATS
        );

        comboBoxTailleTextArea.getSelectionModel().select(7);
        comboBoxPoliceTextArea.getSelectionModel().select(0);
    }

    public Integer getCellId() {
        return cellId;
    }

    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }
}
