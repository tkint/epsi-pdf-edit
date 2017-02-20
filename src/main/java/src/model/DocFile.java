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
	private ImagePDF traceImagePDF;
    private ImagePDF tempImagePDF;

    /**
     * Constructeur du DocFile
     * @param id
     * @param document
     * @param file 
     */
    public DocFile(int id, PDDocument document, File file) {
        this.id = id;
        this.document = document;
        this.file = file;
        this.saved = false;
        this.selectedPage = 0;
        this.areaSelect = new AreaSelect(0, 0, 0, 0);
        this.zoom = 100;
    }

    /**
     * Retourne l'id du DocFile
     * @return 
     */
    public int getId() {
        return id;
    }

    /**
     * Défini l'id du DocFile
     * @param id 
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retourne le document PDF du DocFile
     * @return 
     */
    public PDDocument getDocument() {
        return document;
    }

    /**
     * Défini le document PDF du DocFile
     * @param document 
     */
    public void setDocument(PDDocument document) {
        this.document = document;
    }

    /**
     * Retourne le fichier du DocFile
     * @return 
     */
    public File getFile() {
        return file;
    }

    /**
     * Défini le fichier du DocFile
     * @param file 
     */
    public void setFile(File file) {
        this.file = file;
    }
    
    /**
     * Retourne le nom du fichier
     * @return 
     */
    public String getFileName() {
        return file.getName();
    }

    /**
     * Retourne le nom du fichier sans l'extension
     * @return 
     */
    public String getShortFileName() {
        return file.getName().substring(0, file.getName().length() - 4);
    }

    /**
     * Retourne true si le DocFile est sauvegardé
     * @return 
     */
    public boolean isSaved() {
        return saved;
    }

    /**
     * Défini si le DocFile est sauvegardé
     * @param saved 
     */
    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    /**
     * Retourne le numéro de la page actuellement sélectionnée
     * @return 
     */
    public int getSelectedPage() {
        return selectedPage;
    }

    /**
     * Défini la numéro de la page à sélectionner
     * @param selectedPage 
     */
    public void setSelectedPage(int selectedPage) {
        this.selectedPage = selectedPage;
    }

    /**
     * Retourne la page actuellement sélectionnée
     * @return 
     */
    public PDPage getCurrentPage() {
        return this.document.getPage(this.selectedPage);
    }

    /**
     * Retourne la page précédente à celle sélectionnée
     * @return 
     */
    public PDPage getPreviousPage() {
        PDPage page = null;
        if (this.selectedPage > 0) {
            page = this.document.getPage(this.selectedPage - 1);
        }
        return page;
    }

    /**
     * Retourne la page suivant à celle sélectionnée
     * @return 
     */
    public PDPage getNextPage() {
        PDPage page = null;
        if (this.document.getNumberOfPages() > this.selectedPage + 1) {
            page = this.document.getPage(this.selectedPage + 1);
        }
        return page;
    }

    /**
     * Retourne la zone de sélection
     * @return 
     */
    public AreaSelect getAreaSelect() {
        return areaSelect;
    }

    /**
     * Défini la zone de sélection
     * @param areaSelect 
     */
    public void setAreaSelect(AreaSelect areaSelect) {
        this.areaSelect = areaSelect;
    }

    /**
     * Met à jour les coordonnées de la zone de sélection
     * @param posX
     * @param posY
     * @param width
     * @param height 
     */
    public void updateAreaSelect(double posX, double posY, double width, double height) {
        this.areaSelect.posX = posX;
        this.areaSelect.posY = posY;
        this.areaSelect.width = width;
        this.areaSelect.height = height;
    }

    /**
     * Retourne le niveau de zoom
     * @return 
     */
    public int getZoom() {
        return zoom;
    }

    /**
     * Défini le niveau de zoom
     * @param zoom 
     */
    public void setZoom(int zoom) {
        this.zoom = zoom;
    }
    
    public int getIdZoom() {
        int i = 0;
        int id = -1;
        while (id == -1 && i < ZOOMS.length) {
            if (ZOOMS[i] == this.zoom) {
                id = i;
            }
            i++;
        }
        return id;
    }

    /**
     * Retourne le tableau du calque
     * @return 
     */
    public Table getTraceTable() {
        return traceTable;
    }

    /**
     * Défini le tableau du calque
     * @param traceTable 
     */
    public void setTraceTable(Table traceTable) {
        this.traceTable = traceTable;
    }

    /**
     * Retourne le tableau temporaire du document
     * @return 
     */
    public Table getTempTable() {
        return tempTable;
    }

    /**
     * Défini le tableau temporaire du document
     * @param tempTable 
     */
    public void setTempTable(Table tempTable) {
        this.tempTable = tempTable;
    }
	
	
    public ImagePDF getTraceImagePDF() {
        return traceImagePDF;
    }

    public void setTraceImagePDF(ImagePDF traceImagePDF) {
        this.traceImagePDF = traceImagePDF;
    }

    public ImagePDF getTempImagePDF() {
        return tempImagePDF;
    }

    public void setTempImagePDF(ImagePDF tempImagePDF) {
        this.tempImagePDF = tempImagePDF;
    }
}
