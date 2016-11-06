/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.model;

import java.io.File;
import org.apache.pdfbox.pdmodel.PDDocument;

/**
 *
 * @author Thomas
 */
public class DocFile {

    private int id;
    private PDDocument document;
    private File file;
    private boolean saved;

    public DocFile(int id, PDDocument document, File file) {
        this.id = id;
        this.document = document;
        this.file = file;
        this.saved = false;
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

    public String toTemp() {
        return id + "-" + file.getName();
    }
}
