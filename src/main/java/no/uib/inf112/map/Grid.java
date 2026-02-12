package no.uib.inf112.map;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IStaticObject;

public class Grid implements IGrid {

    private ArrayList<ArrayList<ICell>> cellGrid;

    private Rectangle2D.Double bounds;
    private IMap map;

    // Represent pixel width
    private final static int CELLWIDTH = 40;
    private final static int CELLHEIGHT = 40;

    private int colCount;
    private int rowCount;

    public Grid(IMap map) {
        this.map = map;
        this.bounds = map.getBounds();

        if (this.bounds.getX() != 0 || this.bounds.getY() != 0) {
            throw new IllegalArgumentException("Bounds for Map must start with x, y = 0");
        }

        double height = this.bounds.getHeight();
        double width = this.bounds.getWidth();

        // Not sure if i shuld bother with ceil/floor
        this.colCount = (int) Math.floor(width / CELLWIDTH);
        this.rowCount = (int) Math.floor(height / CELLHEIGHT);

        this.cellGrid = makeGrid();

    }

    public ArrayList<ArrayList<ICell>> makeGrid() {

        ArrayList<ArrayList<ICell>> grid = new ArrayList<>(this.rowCount);

        for (int row = 0; row < this.rowCount; row++) {
            ArrayList<ICell> cellRow = new ArrayList<>(this.colCount);

            for (int col = 0; col < this.colCount; col++) {

                Rectangle2D.Double cellBounds = new Rectangle2D.Double(
                        col * CELLWIDTH,
                        row * CELLHEIGHT,
                        CELLWIDTH,
                        CELLHEIGHT);

                cellRow.add(new Cell(cellBounds, row, col));
            }

            grid.add(cellRow);
        }

        fillGrid(grid, map.getStaticObjects());
        return grid;
    }

    private void fillGrid(ArrayList<ArrayList<ICell>> grid, ArrayList<IStaticObject> blockers) {

        for (ArrayList<ICell> row : grid) {
            for (ICell cell : row) {
                for (IStaticObject blocker : blockers) {
                    if (cell.getBounds().intersects(blocker.getBounds())) {
                        cell.block();
                    }
                }
            }
        }
    }

    public ICell getCell(int row, int col) {
        return this.cellGrid.get(row).get(col);
    }

    public ArrayList<ICell> getNeighbours(ICell cell) {

        if (cell == null) {
            throw new NullPointerException("Cell cannot be null");
        }

        int row = cell.row();
        int col = cell.col();

        if (row < 0 || col < 0 || row >= this.rowCount || col >= this.colCount) {
            throw new IllegalArgumentException(
                    String.format("Cell: Row %s, Col %s is out of bounds", row, col)
            );
        }

        ArrayList<ICell> neighbours = new ArrayList<>();

        ICell over  = null;
        ICell under = null;
        ICell left  = null;
        ICell right = null;


        if (row > 0) {
            over = getCell(row - 1, col);
            neighbours.add(over);
        }
        if (row < this.rowCount - 1) {
            under = getCell(row + 1, col);
            neighbours.add(under);
        }
        if (col > 0) {
            left = getCell(row, col - 1);
            neighbours.add(left);
        }
        if (col < this.colCount - 1) {
            right = getCell(row, col + 1);
            neighbours.add(right);
        }

        if (over != null && left != null && !over.isBlocked() && !left.isBlocked()) {
            neighbours.add(getCell(row - 1, col - 1)); // up-left
        }
        if (over != null && right != null && !over.isBlocked() && !right.isBlocked()) {
            neighbours.add(getCell(row - 1, col + 1)); // up-right
        }
        if (under != null && left != null && !under.isBlocked() && !left.isBlocked()) {
            neighbours.add(getCell(row + 1, col - 1)); // down-left
        }
        if (under != null && right != null && !under.isBlocked() && !right.isBlocked()) {
            neighbours.add(getCell(row + 1, col + 1)); // down-right
        }

        return neighbours;
    }


    @Override
    public Iterator<ICell> iterator() {
        ArrayList<ICell> flattenedList = new ArrayList<>(rowCount * colCount);
        for (ArrayList<ICell> row : this.cellGrid) {
            flattenedList.addAll(row);
        }
        return flattenedList.iterator();
    }

    @Override
    public double distance(ICell from, ICell to) {
        int dx = from.col() - to.col();
        int dy = from.row() - to.row();
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public ICell getCellFromPos(Rectangle2D.Double pos) {
        int col = (int) Math.floor(pos.getCenterX() / CELLWIDTH);
        int row = (int) Math.floor(pos.getCenterY() / CELLHEIGHT);
        return getCell(row, col);
    }












    ///////////////// METHODS FOR TESTING //////////////////
    /// 

    @Override
    public int getCellWidth() {
        return CELLWIDTH;
    }

    @Override
    public int getCellHeight() {
        return CELLHEIGHT;
    }

}
