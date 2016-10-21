/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.table;

/**
 * Created by Thomas on 21/10/2016.
 * @author t.kint
 */
public class Cell implements Cloneable {
    private int id;
    private int width;
    private int height;
    private String content;

    public Cell(int id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Cell{" + "id=" + id + ", width=" + width + ", height=" + height + ", content=" + content + '}';
    }
    
    public Cell clone() {
        Cell object = null;
        try {
            object = (Cell) super.clone();
        } catch(CloneNotSupportedException cnse) {
            cnse.printStackTrace(System.err);
        }
        return object;
    }
}
