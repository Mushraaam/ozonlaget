package no.uib.inf112.map;

import java.awt.geom.Rectangle2D;
import java.util.List;

import no.uib.inf112.enums.FloorType;
import no.uib.inf112.enums.PathType;
import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IEnemy;

public class Cell implements ICell {

    private boolean blocked;
    private Rectangle2D.Double bounds;
    private int row;
    private int col;
    private FloorType floorType;
    private PathType pathType;
    private List<ICell> neighbours;
    private IEnemy firstEnemy = null;
    private boolean occupied = false;


    public Cell(Rectangle2D.Double bounds, int row, int col, FloorType floorType, PathType type) {
        this.bounds = bounds;
        this.blocked = false;
        this.row = row;
        this.col = col;
        this.floorType = floorType;
        this.pathType = type;
    }


    public void setNeighbours(List<ICell> neighbours) {
        this.neighbours = List.copyOf(neighbours);
    }

    @Override
    public List<ICell> getNeighbours() {
        return this.neighbours;
    }

    public Rectangle2D.Double getBounds() {
        return this.bounds;
    }

    public boolean isBlocked() {
        return this.blocked;
    }

    public void block() {
        this.blocked = true;
    }

    public void unblock() {
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
    public void setFloorType(FloorType type) {
        this.floorType = type;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(row, col, bounds, blocked);
    }

    // FOR TESTING BELOW //
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Cell)) {
            return false;
        }
        Cell o = (Cell) obj;

        return this.col == o.col() && this.row == o.row() && this.bounds.equals(o.getBounds())
                && this.blocked == o.isBlocked();
    }

    @Override
    public String toString() {
        return String.format("Row: %s, Col: %s, Bounds: %s, Blocked: %s",
                this.row,
                this.col,
                this.bounds,
                this.blocked);
    }

    @Override
    public IEnemy getFirstEnemy() { return firstEnemy; }

    @Override
    public void setFirstEnemy(IEnemy enemy) { this.firstEnemy = enemy; }

    @Override
    public PathType pathType() {
        return this.pathType;
    }

    @Override
    public void setPathType(PathType type) {
        this.pathType = type;
    }

    @Override
    public void setOccupied(boolean b) { this.occupied = b; }

    @Override
    public boolean isOccupied() { return this.occupied; }
}
