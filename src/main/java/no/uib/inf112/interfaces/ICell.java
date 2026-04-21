package no.uib.inf112.interfaces;

import java.awt.geom.Rectangle2D;
import java.util.Set;

import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.FloorType;
import no.uib.inf112.enums.PathType;

public interface ICell {


    /**
     * @return bounds for cell
     */
    public Rectangle2D.Double getBounds();

    /**
     * @return which row this cell is on
     */
    public int row();

    /**
     * @return which col this cell is on
     */
    public int col();

    /**
     * @return type of floor on this cell
     */
    public FloorType floorType();

    /**
     * Sets the floortype for this cell
     * @param type
     */
    public void setFloorType(FloorType type);

    /**
     * Sets the path type / blocked type for this cell. Used for pathfinding.
     * @return
     */
    public PathType pathType();

    /**
     * Sets the path type for this cell
     * @param type
     */
    public void setPathType(PathType type);

    /**
     * Is this cell is occupied
     * @param currentEnemy - this parameter is no longer used
     * @return
     */
    public boolean isOccupied(EnemySize currentEnemy);

    /**
     * returns true if given enemy is blocking this cell.
     * Used to ensure NPC's do not block themselves
     * @param enemy
     * @return
     */
    public boolean occupiedBy(IEnemy enemy);

    /**
     * Sets an enemy as occupant of cell
     * @param enemy
     * @param size
     */
    public void setOccupant(IEnemy enemy, EnemySize size);

    /**
     * Number of enemies occupying cell
     * @param size
     * @return
     */
    public int occupiedCount(EnemySize size);

    /**
     * Deletes all occupants of cell
     */
    public void clearOccupants();

}
