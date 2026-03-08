package no.uib.inf112.interfaces;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import no.uib.inf112.enums.FloorType;
import no.uib.inf112.enums.PathType;
import no.uib.inf112.map.Cell;

public interface IGrid extends Iterable<ICell> {

    default ArrayList<ArrayList<ICell>> makeGrid(int rows, int cols, int width, int height, FloorType type) {

        ArrayList<ArrayList<ICell>> grid = new ArrayList<>(rows);

        for (int row = 0; row < rows; row++) {
            ArrayList<ICell> cellRow = new ArrayList<>(cols);

            for (int col = 0; col < cols; col++) {

                Rectangle2D.Double cellBounds = new Rectangle2D.Double(
                        col * width,
                        row * height,
                        width,
                        height);
                cellRow.add(new Cell(cellBounds, row, col, type, PathType.UNBLOCKED));
            }
            grid.add(cellRow);
        }
        return grid;
    }

    List<ICell> getNeighboursAtDepth(ICell cell, int depth);

    /**
     * @param row
     * @param col
     * @return cell at given row, col
     */
    public ICell getCell(int row, int col);

    /**
     * @param cell
     * @return list of neighbours
     * @throws NullPointerException
     * @throws IllegalArgumentException when out of bounds
     */
    public ArrayList<ICell> getNeighbours(ICell cell);

    /**
     * Calculates the Euclidean distance between two cells
     * 
     * @param from
     * @param to
     * @return Euclidean distance
     */
    public double distance(ICell from, ICell to);

    /**
     * Calculates current cell of a Rectangle object
     * 
     * @param pos - Shape from java.awt.geom
     * @return ICell that is closest to
     */
    public ICell getCellFromPos(Rectangle2D.Double pos);

    /**
     * @param x
     * @param y
     * @return ICell that corresponds to x, y
     */
    public ICell getCellFromXY(double x, double y);

    /**
     * @return width of each cell
     *         Used for testing
     */
    public int getCellWidth();

    /**
     * @return height of each cell
     *         Used for testing
     */
    public int getCellHeight();

    /**
     * @return number of rows
     */
    public int getRowCount();

    /**
     * @return number of cols
     */
    public int getColCount();

    /**
     * Sets cells near enemies to occupied for pathing purposes
     */
    public void gatherOccupiedCells();

    /**
     * Sets all cells to unoccupied
     */
    public void resetOccupied();

}
