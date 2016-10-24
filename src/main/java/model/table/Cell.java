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
public class Cell extends TableObject implements Cloneable {
    private String content;

    public Cell(int id, float posX, float posY, float width, float height) {
        super(id, posX, posY, width, height);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Cell{" + "id=" + id + ", posX=" + posX + ", posY=" + posY + ", width=" + width + ", height=" + height + ", content=" + content + '}';
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
