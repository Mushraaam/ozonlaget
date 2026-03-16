package no.uib.inf112.map;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IFloor;
import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.config.Config;
import no.uib.inf112.enums.FloorType;

public class TileGrid implements IGrid {

    public static final int TILEWIDTH = Config.getInt("tileWidth");
    public static final int TILEHEIGHT = Config.getInt("tileHeight");

    private int colCount;
    private int rowCount;
    private ArrayList<ArrayList<ICell>> tiles;
    private Rectangle2D.Double bounds;
    private IMap map;

    public TileGrid(IMap map) {

        this.map = map;
        this.bounds = map.getBounds();
        double height = this.bounds.getHeight();
        double width = this.bounds.getWidth();

        // Not sure if i shuld bother with ceil/floor
        this.colCount = (int) Math.floor(width / TILEWIDTH);
        this.rowCount = (int) Math.floor(height / TILEHEIGHT);
        this.tiles = makeGrid(rowCount, colCount, TILEWIDTH, TILEHEIGHT, FloorType.NONE);
        fillGrid();
    }

    private void fillGrid() {
        for (IFloor floor : this.map.getFloors()) {
            for (ICell cell : this) {
                if (floor.getArea().intersects(cell.getBounds())) {
                    cell.setFloorType(floor.floorType());
                }
            }
        }
    }

    @Override
    public List<ICell> getNeighboursAtDepth(ICell cell, int depth) {
        return List.of();
    }

    @Override
    public ICell getCell(int row, int col) {
        return this.tiles.get(row).get(col);
    }

    @Override
    public int getCellWidth() {
        return TILEWIDTH;
    }

    @Override
    public int getCellHeight() {
        return TILEHEIGHT;
    }

    @Override
    public int getRowCount() {
        return this.rowCount;
    }

    @Override
    public int getColCount() {
        return this.colCount;
    }

    @Override
    public ICell getCellFromPos(Rectangle2D.Double pos) {
        double centerX = pos.getCenterX();
        double centerY = pos.getCenterY();
        return getCellFromXY(centerX, centerY);
    }

    public ICell getCellFromXY(double x, double y) {
        int col = (int) Math.floor(x / TILEWIDTH);
        int row = (int) Math.floor(y / TILEHEIGHT);

        col = Math.max(0, Math.min(col, colCount - 1));
        row = Math.max(0, Math.min(row, rowCount - 1));

        return getCell(row, col);
    }

    @Override
    public ArrayList<ICell> getNeighbours(ICell cell) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNeighbours'");
    }

    @Override
    public Iterator<ICell> iterator() {
        ArrayList<ICell> flattenedList = new ArrayList<>(rowCount * colCount);
        for (ArrayList<ICell> row : this.tiles) {
            flattenedList.addAll(row);
        }
        return flattenedList.iterator();
    }

    // Not used, should be abstracted out at a later date
    @Override
    public double distance(ICell from, ICell to) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'distance'");
    }

    @Override
    public void gatherOccupiedCells() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'gatherOccupiedCells'");
    }

    @Override
    public void resetOccupied() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resetOccupied'");
    }

    @Override
    public ArrayList<ICell> getNearbyCells(Double current, double distance) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNearbyCells'");
    }

}
