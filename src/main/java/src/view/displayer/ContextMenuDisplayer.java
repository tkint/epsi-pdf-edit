/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.displayer;

import app.Config;
import static app.Config.TRANSLATOR;
import app.Instance;
import java.io.IOException;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import src.model.AreaSelect;
import src.model.DocFile;
import src.model.ImagePDF;
import src.model.table.Table;
import src.view.controller.ContextMenuController;
import src.view.controller.menu.MenuTools;

/**
 *
 * @author tkint
 */
public class ContextMenuDisplayer implements Config {

    private static final Instance INSTANCE = Instance.getInstance();

    /**
     * Affiche le menu contextuel en fonction de l'outil sélectionné
     *
     * @param posX
     * @param posY
     */
    public static void displayContextMenu(double posX, double posY) {
        if (INSTANCE.hasToolActive()) {
            switch (INSTANCE.getCurrentTool()) {
                case ADDTABLE:
                    displayTableMenu(posX, posY);
                    break;
                case ADDIMAGE:
                    displayImageMenu(posX, posY);
                    break;
                case ADDTEXT:
                    displayTextMenu(posX, posY);
                    break;
            }
        }
    }

    /**
     * Affichage du menu contextuel de l'outil de tableau
     *
     * @param posX
     * @param posY
     */
    
    private static void displayTextMenu(double posX, double posY) {
         DocFile docFile = INSTANCE.getDocFileOpened();
         AreaSelect areaSelect = docFile.getAreaSelect ();
         
         
         
         ContextMenu contextMenu = setContextMenu(posX, posY);
         MenuItem validate = new MenuItem(TRANSLATOR.getString("VALIDATE"));
         validate.setOnAction((event) -> {
             
         });
        
        MenuItem addText = new MenuItem(TRANSLATOR.getString("ADD_TEXT"));
        addText.setOnAction((event) -> {
            try {
                
                MenuTools mt = new MenuTools();
                mt.btnOpenTextArea();
              
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        });
        
        MenuItem cancel = new MenuItem(TRANSLATOR.getString("CANCEL"));
        cancel.setOnAction((event) -> {
            TraceDisplayer.clearTrace();
        });
        
        contextMenu.getItems().addAll(validate, addText, cancel);
        contextMenu.show(INSTANCE.stage);
         
     }

    /**
     * Affichage du menu contextuel de l'outil de tableau
     *
     * @param posX
     * @param posY
     */
    private static void displayTableMenu(double posX, double posY) {
        DocFile docFile = INSTANCE.getDocFileOpened();
        Table traceTable = docFile.getTraceTable();
        Table tempTable = docFile.getTempTable();

        // Initialisation du menu contextuel
        ContextMenu contextMenu = setContextMenu(posX, posY);

        // Choix VALIDER
        MenuItem validate = ContextMenuController.validateTable(docFile);

        // Choix AJOUT DE COLONNE
        MenuItem addColumn = ContextMenuController.addColumn(traceTable, tempTable);

        // Choix SUPPRESSION DE COLONNE
        MenuItem removeColumn = ContextMenuController.removeColumn(traceTable, tempTable);

        // Choix AJOUT DE LIGNE
        MenuItem addRow = ContextMenuController.addRow(traceTable, tempTable);

        // Choix SUPPRESSION DE LIGNE
        MenuItem removeRow = ContextMenuController.removeRow(traceTable, tempTable);

        // Choix ANNULER
        MenuItem cancel = ContextMenuController.cancelTable();

        // Ajout des choix au menu contextuel
        contextMenu.getItems().addAll(validate, addColumn, removeColumn, addRow, removeRow, cancel);

        // Affichage du menu contextuel
        contextMenu.show(INSTANCE.stage);
    }

    /**
     * Affichage du menu contextuel de l'outil d'image
     *
     * @param posX
     * @param posY
     */
    private static void displayImageMenu(double posX, double posY) {
        DocFile docFile = INSTANCE.getDocFileOpened();
        ImagePDF tempImage = docFile.getTempImagePDF();

        // Initialisation du menu contextuel
        ContextMenu contextMenu = setContextMenu(posX, posY);

        // Choix VALIDER
        MenuItem validate = ContextMenuController.validateImage(docFile, tempImage);

        // Choix PIVOTER GAUCHE
        MenuItem rotateLeft = ContextMenuController.rotateImageLeft(tempImage);
        
        // Choix PIVOTER DROITE
        MenuItem rotateRight = ContextMenuController.rotateImageRight(tempImage);

        // Choix ANNULER
        MenuItem cancel = ContextMenuController.cancelImage();

        // Ajout des choix au menu contextuel
        contextMenu.getItems().addAll(validate, cancel);

        // Affichage du menu contextuel
        contextMenu.show(INSTANCE.stage);
    }

    /**
     * Défini la position du menu contextuel
     *
     * @param posX
     * @param posY
     * @return
     */
    private static ContextMenu setContextMenu(double posX, double posY) {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setAnchorX(posX);
        contextMenu.setAnchorY(posY + 60);

        return contextMenu;
    }

    /**
     * Met à jour le tableau affiché sur le calque
     *
     * @param traceTable
     * @param tempTable
     */
    public static void refreshTableTrace(Table traceTable, Table tempTable) {
        try {
            TraceDisplayer.drawTable(traceTable.getPosX(), traceTable.getPosY(), traceTable.getPosX() + traceTable.getWidth(), traceTable.getPosY() + traceTable.getHeight(), traceTable.getLastRow().getCells().size(), traceTable.getRows().size());
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
    
    public static void refreshImageTrace(ImagePDF tempImage, double rotation) {
        try {
            TraceDisplayer.drawImage(tempImage.getPosX(), tempImage.getPosY(), rotation);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}
