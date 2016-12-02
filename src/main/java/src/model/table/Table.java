/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.model.table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 21/10/2016.
 *
 * @author t.kint
 */
public class Table extends TableObject {

    private boolean inverted = false;
    private static int idStatus = 0;

    private List<Row> rows = new ArrayList<Row>();

    public Table(float posX, float posY, float width, float height) {
        super(idStatus, posX, posY, width, height);
        idStatus++;
    }

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    /**
     * Retourne la dernière ligne
     *
     * @return
     */
    public Row getLastRow() {
        return this.rows.get(this.rows.size() - 1);
    }

    public Cell getCell(int id) {
        Cell cell = null;
        int i = 0;
        while (i < this.rows.size() && cell == null) {
            cell = this.rows.get(i).getCellById(id);
            i++;
        }
        return cell;
    }

    public void updateIds() {
        int i = 0;
        int id = 0;
        for (Row row : this.rows) {
            row.setId(i);
            for (Cell cell : row.getCells()) {
                cell.setId(id);
                id++;
            }
            i++;
        }
    }

    /**
     * Génère la table avec le nombre de lignes et de cellules défini
     *
     * @param numberCells
     * @param numberRows
     * @return Table
     */
    public Table generateTable(int numberCells, int numberRows) {
        float cellWidth = (float) this.width / (float) numberCells;
        float cellHeight = (float) this.height / (float) numberRows;
        float posX = this.posX;
        float posY = this.posY;
        for (int i = 0; i < numberRows; i++) {
            Row row = new Row(i, posX, posY, this.width, cellHeight);
            for (int j = 0; j < numberCells; j++) {
                Cell cell = new Cell(j + i * numberCells, posX, posY, cellWidth, cellHeight);
                row.addCell(cell);
                posX += cellWidth;
            }
            this.rows.add(row);
            if (inverted) {
                posY -= cellHeight;
            } else {
                posY += cellHeight;
            }
            posX = this.posX;
        }
        return this;
    }

    /**
     * Ajoute autant de colonnes que défini
     *
     * @param number
     */
    public void addColumns(int number) {
        for (int i = 0; i < number; i++) {
            for (Row row : this.rows) {
                Cell lastCell = row.getLastCell();
                Cell cell = new Cell(0, lastCell.posX + lastCell.width, lastCell.posY, lastCell.width, lastCell.height);
                row.addCell(cell);
                row.setWidth(row.getWidth() + cell.getWidth());
                this.width = row.getWidth();
            }
        }
        this.updateIds();
    }

    /**
     * Ajoute autant de lignes que défini
     *
     * @param number
     */
    public void addRows(int number) {
        for (int i = 0; i < number; i++) {
            Row lastRow = this.getLastRow();
            Row row = new Row(0, lastRow.posX, lastRow.posY - lastRow.height, lastRow.width, lastRow.height);
            float x = lastRow.posX;
            for (Cell cell : lastRow.getCells()) {
                row.addCell(new Cell(0, cell.posX, lastRow.posY - lastRow.height, cell.width, cell.height));
            }
            this.rows.add(row);
            this.height += lastRow.height;
        }
        this.updateIds();
    }

    /**
     * Retourne la colonne ciblée par l'id fourni
     *
     * @param id
     * @return Row
     */
    public Row getRowById(int id) {
        Row row = null;
        int i = 0;
        while (i < this.getRows().size() && row == null) {
            if (this.rows.get(i).getId() == id) {
                row = this.rows.get(i);
            }
            i++;
        }
        return row;
    }

    /**
     * Supprime la colonne ciblée par l'id fourni
     *
     * @param id
     */
    public void removeRowById(int id) {
        int i = 0;
        boolean finished = false;
        while (i < this.getRows().size() && !finished) {
            if (this.rows.get(i).getId() == id) {
                this.rows.remove(i);
                finished = true;
            }
            i++;
        }
    }

    /**
     * Met à jour la positions des éléments du tableau
     */
    public void refresh() {
        float posX = this.posX;
        float posY = this.posY;
        for (Row row : this.rows) {
            row.posX = posX;
            row.posY = posY;
            row.width = this.width;
            row.height = this.height / this.rows.size();
            for (Cell cell : row.getCells()) {
                cell.posX = posX;
                cell.posY = posY;
                cell.width = row.width / row.getCells().size();
                cell.height = row.height;
                posX += cell.width;
            }
            if (inverted) {
                posY -= row.height;
            } else {
                posY += row.height;
            }
            posX = this.posX;
        }
    }

    @Override
    public String toString() {
        String s = "Table{" + "id=" + id + ", width=" + width + ", height=" + height + ", rows={";

        for (Row row : rows) {
            s += '\n' + row.toString();
            if (row.getId() != rows.get(rows.size() - 1).getId()) {
                s += ", " + '\n';
            }
        }

        s += '\n' + "}}";

        return s;
    }
}
