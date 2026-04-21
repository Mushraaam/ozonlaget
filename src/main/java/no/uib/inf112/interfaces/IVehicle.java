package no.uib.inf112.interfaces;

import java.awt.geom.Rectangle2D;

public interface IVehicle {

    /**
     * @return bounds of this object
     */
    public Rectangle2D.Double getBounds();

    /**
     * @return if IVehicle has full fuel
     */
    public boolean isFuelFull();

    /**
     * Deposits gas to IVehicle
     * Reduces inventory count for item to 0
     */
    public void depositGas();

    /**
     * @return index for vehicle animation
     */
    public int getIndex();

    /**
     * Increments animation index
     */
    public void increment();

}
