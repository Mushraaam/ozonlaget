package no.uib.inf112.interfaces;

import java.awt.geom.Rectangle2D;

/**
 * These objects are blocking objects
 */
public interface IStaticObject {

    /**
     * @return Bounds of object
     */
    public Rectangle2D.Double getBounds();

    /**
     * Used for determining bullet blocking and wall-drawing
     * 
     * @return true if object is a wall
     */
    public boolean isWall();

}
