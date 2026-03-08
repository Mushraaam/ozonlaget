package no.uib.inf112.map;

import java.awt.geom.Rectangle2D;
import java.util.*;

import no.uib.inf112.enums.EnemySize;
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
    private HashSet<IEnemy> smallOccupants;
    private HashSet<IEnemy> mediumOccupants;
    private HashSet<IEnemy> largeOccupants;
    private ArrayList<HashSet<IEnemy>> occupants;

    public Cell(Rectangle2D.Double bounds, int row, int col, FloorType floorType, PathType type) {
        this.bounds = bounds;
        this.blocked = false;
        this.row = row;
        this.col = col;
        this.floorType = floorType;
        this.pathType = type;

        // Occupants for pathing
        this.smallOccupants = new HashSet<>();
        this.mediumOccupants = new HashSet<>();
        this.largeOccupants = new HashSet<>();
        this.occupants = new ArrayList<>();
        this.occupants.add(this.smallOccupants);
        this.occupants.add(this.mediumOccupants);
        this.occupants.add(this.largeOccupants);

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
        switch (size) {
            case SMALL -> {
                return (!this.smallOccupants.isEmpty());
            }
            case MEDIUM -> {
                return (!this.mediumOccupants.isEmpty());
            }
            case LARGE -> {
                return (!this.largeOccupants.isEmpty());
            }
            default -> {
                throw new IllegalArgumentException("Unknown EnemySize");
            }
        }
    }

    @Override
    public void setOccupant(IEnemy enemy, EnemySize size) {

        if (size == EnemySize.SMALL){
            this.smallOccupants.add(enemy);
            // this.mediumOccupants.add(enemy);
            // this.largeOccupants.add(enemy);
        }
        else if (size == EnemySize.MEDIUM){
            this.mediumOccupants.add(enemy);
            // this.largeOccupants.add(enemy);
        }
        else{
            this.largeOccupants.add(enemy);
        }
    }

    @Override
    public boolean occupiedBy(IEnemy enemy) {
        EnemySize size = enemy.size();
        switch (size) {
            case SMALL -> {
                return (this.smallOccupants.contains(enemy));
            }
            case MEDIUM -> {
                return (this.mediumOccupants.contains(enemy));
            }
            case LARGE -> {
                return (this.largeOccupants.contains(enemy));
            }
            default -> {
                throw new IllegalArgumentException("Unknown enemy");
            }
        }
    }

    @Override
    public int occupiedCount(EnemySize size) {
        switch (size) {
            case SMALL -> {
                return (this.smallOccupants.size());
            }
            case MEDIUM -> {
                return (this.mediumOccupants.size());
            }
            case LARGE -> {
                return (this.largeOccupants.size());
            }
            default -> {
                throw new IllegalArgumentException("Unknown enemy");
            }
        }
    }

    @Override
    public void clearOccupants() {
        for (HashSet<IEnemy> oc : this.occupants) {
            oc.clear();
        }
    }
}
