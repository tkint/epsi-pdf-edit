/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.model.table;

import static app.Config.TABLE_DRAW_BACKGROUND;
import javafx.scene.paint.Color;

/**
 * Created by Thomas on 21/10/2016.
 *
 * @author t.kint
 */
public class Cell extends TableObject {

    private Color backgroundColor;
    private String content;

    public Cell(int id, float posX, float posY, float width, float height) {
        super(id, posX, posY, width, height);
        this.backgroundColor = TABLE_DRAW_BACKGROUND;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public String toString() {
        return "Cell{" + "id=" + id + ", posX=" + posX + ", posY=" + posY + ", width=" + width + ", height=" + height + ", content=" + content + '}';
    }
}
