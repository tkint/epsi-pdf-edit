/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enums;

/**
 * Enumère l'ensemble des outils disponibles
 *
 * @author Thomas
 */
public enum Tool {
    ADDTEXT("toolbarAddText"),
    ADDIMAGE("toolbarAddImage"),
    ADDTABLE("toolbarAddTable");

    private String toolbarId;

    Tool(String toolbarId) {
        this.toolbarId = toolbarId;
    }

    public String getToolbarId() {
        return toolbarId;
    }

    public void setToolbarId(String toolbarId) {
        this.toolbarId = toolbarId;
    }
}
