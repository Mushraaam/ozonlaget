package no.uib.inf112.map;

import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.Iterator;

import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IGrid;

public class TileGrid implements IGrid{

    @Override
    public Iterator<ICell> iterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }

    @Override
    public ICell getCell(int row, int col) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCell'");
    }

    @Override
    public ArrayList<ICell> getNeighbours(ICell cell) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNeighbours'");
    }

    @Override
    public double distance(ICell from, ICell to) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'distance'");
    }

    @Override
    public ICell getCellFromPos(Double pos) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCellFromPos'");
    }

    @Override
    public int getCellWidth() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCellWidth'");
    }

    @Override
    public int getCellHeight() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCellHeight'");
    }
    
}
