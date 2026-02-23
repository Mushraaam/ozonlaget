package no.uib.inf112.interfaces;

import java.awt.geom.Rectangle2D;

import no.uib.inf112.enums.walls.StaticObjectType;

/**
 * These objects are blocking objects
 */
public interface IStaticObject {
    
    /**
     * @return Bounds of object
     */
    public Rectangle2D.Double getBounds();


    /**
     * @return type of this object
     */
    public StaticObjectType getType();





}
