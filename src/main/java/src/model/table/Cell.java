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

    /**
     * Constructeur de la cellule
     * @param id
     * @param posX
     * @param posY
     * @param width
     * @param height 
     */
    public Cell(int id, float posX, float posY, float width, float height) {
        super(id, posX, posY, width, height);
        this.backgroundColor = TABLE_DRAW_BACKGROUND;
    }

    /**
     * Récupère le contenu de la cellule
     * @return 
     */
    public String getContent() {
        return content;
    }

    /**
     * Défini le contenu de la cellule
     * @param content 
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Récupère la couleur de fond de la cellule
     * @return 
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Défini la couleur de fond de la cellule
     * @param backgroundColor 
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Retourne la celulle sous forme de string
     * @return 
     */
    @Override
    public String toString() {
        return "Cell{" + "id=" + id + ", posX=" + posX + ", posY=" + posY + ", width=" + width + ", height=" + height + ", content=" + content + '}';
    }
}
