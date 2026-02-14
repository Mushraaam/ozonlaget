package no.uib.inf112.map;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

import no.uib.inf112.interfaces.ICell;
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

    public TileGrid(IMap map) {

        this.bounds = map.getBounds();
        double height = this.bounds.getHeight();
        double width = this.bounds.getWidth();

        // Not sure if i shuld bother with ceil/floor
        this.colCount = (int) Math.floor(width / TILEWIDTH);
        this.rowCount = (int) Math.floor(height / TILEHEIGHT);
        this.tiles = makeGrid(rowCount, colCount, TILEWIDTH, TILEHEIGHT, FloorType.GRASS_TILES);
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

    private ICell getCellFromXY(double x, double y) {
        int col = (int) Math.floor(x / TILEWIDTH);
        int row = (int) Math.floor(y / TILEHEIGHT);
        return getCell(row, col);
    }

    @Override
    public ArrayList<ICell> getNeighbours(ICell cell) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNeighbours'");
    }

    @Override
    public Iterator<ICell> iterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }

    @Override
    public double distance(ICell from, ICell to) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'distance'");
    }

}
