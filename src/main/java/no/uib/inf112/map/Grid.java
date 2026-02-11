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

    public Grid(IMap map){
        this.map = map;
        this.bounds = map.getBounds();

        if (this.bounds.getX() != 0 || this.bounds.getY() != 0){
            throw new IllegalArgumentException("Bounds for Map must start with x, y = 0");
        }

        double height = this.bounds.getHeight();
        double width = this.bounds.getWidth();

        //Not sure if i shuld bother with ceil/floor
        this.colCount = (int) Math.floor(width / CELLWIDTH);
        this.rowCount = (int) Math.floor(height / CELLHEIGHT);
        

        this.cellGrid = makeGrid();
        
    }

    public ArrayList<ArrayList<ICell>> makeGrid() {

        ArrayList<ArrayList<ICell>> grid = new ArrayList<>(this.rowCount);

        for (int row = 0; row < this.rowCount; row++){
            ArrayList<ICell> cellRow = new ArrayList<>(this.colCount);

            for (int col = 0; col < this.colCount; col++){

                Rectangle2D.Double cellBounds = new Rectangle2D.Double(
                    col * CELLWIDTH,
                    row * CELLHEIGHT,
                    CELLWIDTH,
                    CELLHEIGHT
                );

                cellRow.add(new Cell(cellBounds, row, col));
            }

            grid.add(cellRow);
        }

        fillGrid(grid, map.getStaticObjects());
        return grid;
    }
    
    private void fillGrid(ArrayList<ArrayList<ICell>> grid, ArrayList<IStaticObject> blockers) {
        
        for (ArrayList<ICell> row : grid){
            for (ICell cell : row){
                for (IStaticObject blocker : blockers){
                    if (cell.getBounds().intersects(blocker.getBounds())){
                        cell.block();
                    }
                }
            }
        }
    }

    public ICell getCell(int row, int col){
        return this.cellGrid.get(row).get(col);
    }

    public ArrayList<ICell> getNeighbours(ICell cell){

        if (cell == null){
            throw new NullPointerException("Cell cannot be null");
        }

        int row = cell.row();
        int col = cell.col();

        if (row < 0 || col < 0 || row >= this.rowCount || col >= this.colCount){
            throw new IllegalArgumentException(String.format("Cell: Row %s, Col %s is out of bounds", row, col));
        }

        ArrayList<ICell> neighbours = new ArrayList<>();
        if (row > 0){
            ICell over = getCell(row - 1, colCount);
            neighbours.add(over);
        }
        if (row < this.rowCount){
            ICell under = getCell(row + 1, col);
            neighbours.add(under);
        }
        if (col > 0){
            ICell left = getCell(row, col - 1);
            neighbours.add(left);
        }
        if (col < this.colCount){
            ICell right = getCell(row, col + 1);
            neighbours.add(right);
        }
        return neighbours;
    }

    @Override
    public Iterator<ICell> iterator() {
        ArrayList<ICell> flattenedList = new ArrayList<>(rowCount * colCount);
        for (ArrayList<ICell> row : this.cellGrid){
            flattenedList.addAll(row);
        }
        return flattenedList.iterator();
    }

    

}
