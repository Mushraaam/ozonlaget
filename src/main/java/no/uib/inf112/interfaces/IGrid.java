package no.uib.inf112.interfaces;

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
}
