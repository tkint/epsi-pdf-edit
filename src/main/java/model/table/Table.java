/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 21/10/2016.
 * @author t.kint
 */
public class Table {
    private int id;
    private int width;
    private int height;
    private List<Row> rows = new ArrayList<Row>();

    public Table(int id, int width, int height) {
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

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }
    
    /**
     * Retourne la dernière ligne
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
    
    /**
     * Génère la table avec le nombre de lignes et de cellules défini
     * @param numberRows
     * @param numberCells
     * @return 
     */
    public Table generateTable(int numberRows, int numberCells) {
        for (int i = 0; i < numberCells; i++) {
            Row row = new Row(i, this.width, this.height / numberRows);
            for (int j = 0; j < numberRows; j++) {
                Cell cell = new Cell(j + i * numberCells, this.width / numberRows, this.height / numberCells);
                row.addCell(cell);
            }
            this.addRow(row);
        }
        return this;
    }
    
    /**
     * Ajoute autant de colonnes que défini
     * @param number 
     */
    public void addColumns(int number) {
        for (int i = 0; i < number; i++) {
            for (Row row : this.rows) {
                Cell cell = row.getLastCell().clone();
                cell.setId(cell.getId() + 1);
                row.addCell(cell);
                row.setWidth(row.getWidth() + cell.getWidth());
                this.width = row.getWidth();
            }
        }
    }
    
    public void addRows(int number) {
        int id = this.getLastRow().getLastCell().getId() + 1;
        for (int i = 0; i < number; i++) {
            Row row = this.getLastRow().clone();
            row.setId(row.getId() + 1);
            for (Cell cell : row.getCells()) {
                cell.setId(id);
                id++;
            }
            this.rows.add(row);
        }
    }
    
    /**
     * Ajoute une colonne
     * @param row 
     */
    public void addRow(Row row) {
        this.rows.add(row);
    }
    
    /**
     * Retourne la colonne ciblée par l'id fourni
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

    @Override
    public String toString() {
        String s = "Table{" + "id=" + id + ", rows={";
        
        for (Row row : rows) {
            s += '\n' + row.toString();
            if (row.getId() != rows.get(rows.size() - 1).getId()) {
                s += ", ";
            }
        }
        
        s += '\n' + "}}";
        return s;
    }
}
