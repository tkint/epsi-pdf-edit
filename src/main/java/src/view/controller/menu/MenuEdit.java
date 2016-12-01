/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.view.controller.menu;

import app.Config;
import app.Instance;
import enums.Tool;
import src.view.TraceDisplayer;

/**
 *
 * @author Thomas
 */
public class MenuEdit implements Config {

    private static final Instance INSTANCE = Instance.getInstance();

    public static void btnAddText() {
        switchTool(Tool.ADDTEXT);
    }

    public static void btnEditText() {
        switchTool(Tool.EDITTEXT);
    }

    public static void btnAddImage() {
        switchTool(Tool.ADDIMAGE);
    }

    public static void btnEditImage() {
        switchTool(Tool.EDITIMAGE);
    }

    public static void btnAddTable() {
        switchTool(Tool.ADDTABLE);
    }

    public static void btnEditTable() {
        switchTool(Tool.EDITTABLE);
    }

    private static void switchTool(Tool tool) {
        if (INSTANCE.getCurrentTool() == tool) {
            TraceDisplayer.clearTrace();
            INSTANCE.setNoTool();
        } else {
            INSTANCE.setCurrentTool(tool);
        }
    }
}
