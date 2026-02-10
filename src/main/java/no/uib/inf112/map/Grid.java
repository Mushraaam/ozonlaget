package no.uib.inf112.map;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

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

    @Override
    public ArrayList<ArrayList<ICell>> getGrid() {
        return this.cellGrid;
    }

    

}
