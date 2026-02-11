package no.uib.inf112.interfaces;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public interface IGrid extends Iterable<ICell>{
    
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
     * @param from
     * @param to
     * @return Euclidean distance
     */
    public double distance(ICell from, ICell to);

    /**
     * Calculates current cell of a Rectangle object
     * @param pos - Shape from java.awt.geom
     * @return ICell that is closest to 
     */
    public ICell getCellFromPos(Rectangle2D.Double pos);

}
