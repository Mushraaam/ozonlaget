package no.uib.inf112.model;

import java.awt.geom.Rectangle2D;

import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.FloorType;
import no.uib.inf112.enums.PathType;
import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IEnemy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

public class Cell implements ICell {

    private boolean blocked;
    private Rectangle2D.Double bounds;
    private int row;
    private int col;
    private FloorType floorType;
    private PathType pathType;
    private Set<IEnemy> allOccupants;
    private Set<IEnemy> occupantSet;

    public Cell(Rectangle2D.Double bounds, int row, int col, FloorType floorType, PathType type) {
        this.bounds = bounds;
        this.blocked = false;
        this.row = row;
        this.col = col;
        this.floorType = floorType;
        this.pathType = type;

        // Occupants for pathing

        this.allOccupants = ConcurrentHashMap.newKeySet();
        this.occupantSet = new HashSet<>();

    }

    public Rectangle2D.Double getBounds() {
        return this.bounds;
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
                && this.pathType == o.pathType();
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
    public PathType pathType() {
        return this.pathType;
    }

    @Override
    public void setPathType(PathType type) {
        this.pathType = type;
    }

    @Override
    public boolean isOccupied(EnemySize size) {
        return !this.occupantSet.isEmpty();
    }

    @Override
    public void setOccupant(IEnemy enemy, EnemySize size) {
        this.occupantSet.add(enemy);
    }

    @Override
    public boolean occupiedBy(IEnemy enemy) {
        return this.occupantSet.contains(enemy);
    }

    @Override
    public int occupiedCount(EnemySize size) {
        return this.occupantSet.size();
    }

    @Override
    public void clearOccupants() {
        this.occupantSet.clear();
        allOccupants.clear();
    }

    @Override
    public Set<IEnemy> getEnemies() {
        return this.allOccupants;
    }
}
