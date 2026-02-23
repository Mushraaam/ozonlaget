package no.uib.inf112.interfaces;

import java.awt.geom.Rectangle2D;

import no.uib.inf112.enums.FloorType;

public interface IFloor{
    
    /**
     * @return the type of floor
     */
    public FloorType floorType();

    /**
     * @return the area of the floor
     */
    public Rectangle2D.Double getArea();

}
