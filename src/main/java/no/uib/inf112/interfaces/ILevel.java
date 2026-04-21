package no.uib.inf112.interfaces;

import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;

import no.uib.inf112.model.items.factory.ItemFactory;
import no.uib.inf112.model.npcs.factory.Factory;

public interface ILevel {
    
    /**
     * @return list of static objects (usually walls and funiture) on map
     */
    public ArrayList<IStaticObject> getStaticObjects();

    /**
     * @return Player object to be placed on map
     */
    public IPlayer getPlayer();

    /**
     * @return bounds for the level
     */
    public Double getBounds();

    /**
     * @return list of all the floors
     */
    public ArrayList<IFloor> getFloor();

    /**
     * @return the number of this level
     */
    public int levelNumber();

    /**
     * @return the factory responsible for spawning enemies
     */
    public Factory getFactory();

    /**
     * @return the factory responsible for spawning items
     */
    public ItemFactory getItemFactory();

    /**
     * @return list of all vehicles on map
     */
    public ArrayList<IVehicle> getVehicles();
}
