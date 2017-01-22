/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.model.table;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author t.kint
 */
public class Row extends TableObject {

    private List<Cell> cells = new ArrayList<Cell>();

    /**
     * Constructeur de la ligne
     * @param id
     * @param posX
     * @param posY
     * @param width
     * @param height 
     */
    public Row(int id, float posX, float posY, float width, float height) {
        super(id, posX, posY, width, height);
    }

    /**
     * Récupère la liste de cellules
     * @return 
     */
    public List<Cell> getCells() {
        return cells;
    }

    /**
     * Défini la liste de celulles
     * @param cells 
     */
    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    /**
     * Ajoute une cellule
     *
     * @param cell
     */
    public void addCell(Cell cell) {
        this.cells.add(cell);
    }

    /**
     * Retourne l'index d'une cellule par son id
     * @param id
     * @return 
     */
    public int getCellIndex(int id) {
        int i = 0;
        boolean finished = false;
        while (i < this.cells.size() && !finished) {
            if (this.cells.get(i).getId() == id) {
                finished = true;
            }
            i++;
        }
        return i;
    }

    /**
     * Retourne la cellule ciblée par l'id fourni
     *
     * @param id
     * @return Cell
     */
    public Cell getCellById(int id) {
        Cell cell = null;
        int i = 0;
        while (i < this.getCells().size() && cell == null) {
            if (this.cells.get(i).getId() == id) {
                cell = this.cells.get(i);
            }
            i++;
        }
        return cell;
    }

    /**
     * Retourne la dernière cellule
     *
     * @return
     */
    public Cell getLastCell() {
        return this.cells.get(this.cells.size() - 1);
    }

    /**
     * Supprime la cellule ciblée par l'id fourni
     *
     * @param id
     */
    public void removeCellById(int id) {
        int i = 0;
        boolean finished = false;
        while (i < this.getCells().size() && !finished) {
            if (this.cells.get(i).getId() == id) {
                this.cells.remove(i);
                finished = true;
            }
            i++;
        }
    }

    /**
     * Supprime la dernière cellule de la ligne
     */
    public void removeLastCell() {
        if (this.cells.size() > 1) {
            this.width -= this.getLastCell().width;
            this.cells.remove(this.cells.size() - 1);
        }
    }

    /**
     * Retourne la ligne sous forme de string
     * @return 
     */
    @Override
    public String toString() {
        String s = "Row{" + "id=" + id + ", width=" + width + ", height=" + height + ", cells={";

        for (Cell cell : cells) {
            s += '\n' + cell.toString();
            if (cell.getId() != cells.get(cells.size() - 1).getId()) {
                s += ", ";
            }
        }

        s += "}}";
        return s;
    }
}
