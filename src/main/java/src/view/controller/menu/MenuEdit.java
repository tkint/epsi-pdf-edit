/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.controller.menu;

import app.Config;
import app.Instance;
import enums.Tool;
import src.view.displayer.TraceDisplayer;

/**
 *
 * @author Thomas
 */
public class MenuEdit implements Config {

    private static final Instance INSTANCE = Instance.getInstance();

    /**
     * Bouton d'ajout de texte
     */
    public static void btnAddText() {
        switchTool(Tool.ADDTEXT);
    }

    /**
     * Bouton d'édition de texte
     */
    public static void btnEditText() {
        switchTool(Tool.EDITTEXT);
    }

    /**
     * Bouton d'ajout d'image
     */
    public static void btnAddImage() {
        switchTool(Tool.ADDIMAGE);
    }

    /**
     * Bouton d'édition d'image
     */
    public static void btnEditImage() {
        switchTool(Tool.EDITIMAGE);
    }

    /**
     * Bouton d'ajout de tableau
     */
    public static void btnAddTable() {
        switchTool(Tool.ADDTABLE);
    }

    /**
     * Bouton d'édition de tableau
     */
    public static void btnEditTable() {
        switchTool(Tool.EDITTABLE);
    }

    /**
     * Change l'outil actuellement sélectionné
     */
    private static void switchTool(Tool tool) {
        if (INSTANCE.getCurrentTool() == tool) {
            TraceDisplayer.clearTrace();
            INSTANCE.setNoTool();
        } else {
            INSTANCE.setCurrentTool(tool);
        }
    }
}
