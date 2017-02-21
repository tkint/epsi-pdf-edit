/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.model;

/**
 * Zone de sélection
 * @author Thomas
 */
public class AreaSelect {

    double posX;
    double posY;
    double width;
    double height;

    /**
     * Constructeur de la zone
     * @param posX
     * @param posY
     * @param width
     * @param height 
     */
    public AreaSelect(double posX, double posY, double width, double height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    /**
     * Retourne la position en X de la zone
     * @return 
     */
    public double getPosX() {
        return posX;
    }

    /**
     * Défini la position en X de la zone
     * @param posX 
     */
    public void setPosX(double posX) {
        this.posX = posX;
    }

    /**
     * Retourne la position en Y de la zone
     * @return 
     */
    public double getPosY() {
        return posY;
    }

    /**
     * Défini la position en Y de la zone
     * @param posY 
     */
    public void setPosY(double posY) {
        this.posY = posY;
    }

    /**
     * Retourne la largeur de la zone
     * @return 
     */
    public double getWidth() {
        return width;
    }

    /**
     * Défini la largeur de la zone
     * @param width 
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Retourne la hauteur de la zone
     * @return 
     */
    public double getHeight() {
        return height;
    }

    /**
     * Défini la hauteur de la zone
     * @param height 
     */
    public void setHeight(double height) {
        this.height = height;
    }
    
    public void refreshPos(float posX, float posY, float width, float height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    /**
     * Retourne la zone sous forme de string
     * @return 
     */
    @Override
    public String toString() {
        return "AreaSelect{" + "posX=" + posX + ", posY=" + posY + ", width=" + width + ", height=" + height + '}';
    }
}
