/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.model.table;

/**
 * Classe abstraite définissant les objets liés aux tableaux
 * @author t.kint
 */
public abstract class TableObject {

    protected int id;
    protected float posX;
    protected float posY;
    protected float width;
    protected float height;

    /**
     * Constructeur de l'objet
     * @param id
     * @param posX
     * @param posY
     * @param width
     * @param height 
     */
    public TableObject(int id, float posX, float posY, float width, float height) {
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    /**
     * Retourne l'id de l'objet
     * @return 
     */
    public int getId() {
        return id;
    }

    /**
     * Défini l'id de l'objet
     * @param id 
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retourne la position en X de l'objet
     * @return 
     */
    public float getPosX() {
        return posX;
    }

    /**
     * Défini la position en X de l'objet
     * @param posX 
     */
    public void setPosX(float posX) {
        this.posX = posX;
    }

    /**
     * Retourne la position en Y de l'objet
     * @return 
     */
    public float getPosY() {
        return posY;
    }

    /**
     * Défini la position en Y de l'objet
     * @param posY 
     */
    public void setPosY(float posY) {
        this.posY = posY;
    }

    /**
     * Retourne la largeur de l'objet
     * @return 
     */
    public float getWidth() {
        return width;
    }

    /**
     * Défini la largeur de l'objet
     * @param width 
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * Retourne la hauteur de l'objet
     * @return 
     */
    public float getHeight() {
        return height;
    }

    /**
     * Défini la hauteur de l'objet
     * @param height 
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * Retourne l'objet sous forme de string
     * @return 
     */
    @Override
    public String toString() {
        return "TableObject{" + "id=" + id + ", posX=" + posX + ", posY=" + posY + ", width=" + width + ", height=" + height + '}';
    }
}
