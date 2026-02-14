package no.uib.inf112.map;

import java.awt.geom.Rectangle2D;

import no.uib.inf112.enums.FloorType;
import no.uib.inf112.interfaces.ICell;

public class Cell implements ICell {

    private boolean blocked;
    private Rectangle2D.Double bounds;
    private int row;
    private int col;
    private FloorType floorType;

    public Cell(Rectangle2D.Double bounds, int row, int col, FloorType floorType){
        this.bounds = bounds;
        this.blocked = false;
        this.row = row;
        this.col = col;
        this.floorType = floorType;
    }

    public Rectangle2D.Double getBounds(){
        return this.bounds;
    }

    public boolean isBlocked(){
        return this.blocked;
    }

    public void block(){
        this.blocked = true;
    }

    public void unblock(){
        this.blocked = false;
    }

    @Override
    public int row() {
        return this.row;
    }

    @Override
    public int col() {
        return this.col;
    }

    @Override
    public FloorType floorType() {
        return this.floorType;
    }

    @Override
    public void setFloorType(FloorType type){
        this.floorType = type;
    }



    // FOR TESTING BELOW //
    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if (!(obj instanceof Cell)){
            return false;
        }
        Cell o = (Cell) obj;

        return this.col==o.col()&&this.row==o.row()&&this.bounds.equals(o.getBounds())&&this.blocked==o.isBlocked();
    }

    @Override
    public String toString() {
        return String.format("Row: %s, Col: %s, Bounds: %s, Blocked: %s", 
            this.row,
            this.col,
            this.bounds,
            this.blocked
        );
    }


    
}
