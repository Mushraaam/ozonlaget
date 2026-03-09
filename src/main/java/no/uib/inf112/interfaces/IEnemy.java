package no.uib.inf112.interfaces;

import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.map.npcs.pathfinding.Pathfinder;

import java.awt.geom.Ellipse2D;
import java.util.*;
import java.awt.geom.Rectangle2D;

/**
 * Represents an enemy entity in the game that can move and be drawn.
 * Extends IMovingDrawableObject to include enemy-specific behavior
 * such as pathfinding and hitboxes.
 */
public interface IEnemy extends IMovingDrawableObject {


    Ellipse2D.Double getTrueHitbox();

    /**
     * Requests a new path for the enemy to follow towards a specific target.
     *
     * @param grid         The current game grid.
     * @param pathfinder   The pathfinding algorithm to use.
     * @param targetBounds The hitbox of the target.
     */
    public void requestPath(IGrid grid, Pathfinder pathfinder, Rectangle2D.Double targetBounds);


    /**
     * @return The rectangle representing the enemy's collision area.
     */
    public Rectangle2D.Double getHitbox();

    /**
     * @return The type of this enemy (like Zombie, Thug, ...etc.).
     */
    public EnemyType getEnemyType();

    /**
     * @return The current frame index of the enemy's animation.
     */
    public int getAnimationIndex();

    /**
     * @return A list of cells representing the current path the enemy is following.
     */
    public List<ICell> getCurrentPath();

    /**
     * Advances the animation frame index to the next step.
     */
    public void incrementAnimationIndex();

    /**
     * @return the size category the enemy falls into (Like small, medium or lagre)
     */
    public EnemySize size();

    /**
     * @return the radians of which direction the enemy if facing
     */
    public double getFacingAngle();

    ICell getOldCell();

    boolean boundsChanged(int minR, int maxR, int minC, int maxC);

    void updateBounds(int minR, int maxR, int minC, int maxC);

    int getLastMinR();

    int getLastMaxR();

    int getLastMinC();

    int getLastMaxC();

    IEnemy getNextInCell();

    void setNextInCell(IEnemy next);

    ICell getStandingCell();

    /**
     * Deals damage to enemy
     * @param damage
     */
    public void takeDamage(int damage);

}
