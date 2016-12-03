/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.model;

import app.Config;
import java.io.File;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import src.model.table.Table;

/**
 *
 * @author Thomas
 */
public class DocFile implements Config {

    private int id;
    private PDDocument document;
    private File file;
    private boolean saved;
    private int selectedPage;
    private AreaSelect areaSelect;
    private int zoom;
    private Table traceTable;
    private Table tempTable;

    public DocFile(int id, PDDocument document, File file) {
        this.id = id;
        this.document = document;
        this.file = file;
        this.saved = false;
        this.selectedPage = 0;
        this.areaSelect = new AreaSelect(0, 0, 0, 0);
        this.zoom = 100;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PDDocument getDocument() {
        return document;
    }

    public void setDocument(PDDocument document) {
        this.document = document;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return file.getName();
    }

    public String getShortFileName() {
        return file.getName().substring(0, file.getName().length() - 4);
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public int getSelectedPage() {
        return selectedPage;
    }

    public void setSelectedPage(int selectedPage) {
        this.selectedPage = selectedPage;
    }

    public PDPage getCurrentPage() {
        return this.document.getPage(this.selectedPage);
    }

    public PDPage getPreviousPage() {
        PDPage page = null;
        if (this.selectedPage > 0) {
            page = this.document.getPage(this.selectedPage - 1);
        }
        return page;
    }

    public PDPage getNextPage() {
        PDPage page = null;
        if (this.document.getNumberOfPages() > this.selectedPage + 1) {
            page = this.document.getPage(this.selectedPage + 1);
        }
        return page;
    }

    public AreaSelect getAreaSelect() {
        return areaSelect;
    }

    public void setAreaSelect(AreaSelect areaSelect) {
        this.areaSelect = areaSelect;
    }

    public void updateAreaSelect(double posX, double posY, double width, double height) {
        this.areaSelect.posX = posX;
        this.areaSelect.posY = posY;
        this.areaSelect.width = width;
        this.areaSelect.height = height;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public Table getTraceTable() {
        return traceTable;
    }

    public void setTraceTable(Table traceTable) {
        this.traceTable = traceTable;
    }

    public Table getTempTable() {
        return tempTable;
    }

    public void setTempTable(Table tempTable) {
        this.tempTable = tempTable;
    }
}
