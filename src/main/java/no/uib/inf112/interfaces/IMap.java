package no.uib.inf112.interfaces;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import no.uib.inf112.enums.GameState;

public interface IMap {
    

    /**
     * A list of all the moving objects
     * @return Immuteable ArrayList
     */
    public ArrayList<IMovingDrawableObject> getMovingObjects();

    /**
     * A list of all static objects (buildings etc)
     * @return Immuteable ArrayList
     */
    public ArrayList<IStaticObject> getStaticObjects();

    /**
     * @return The Player object
     */
    public IPlayer getPlayer();

    /**
     * Returns what state the game is in
     * @return GameState
     */
    public GameState getGameState();

    /**
     * @return Dimensions of the map
     */
    public Rectangle2D.Double getBounds();

    /**
     * @return the grid
     */
    public IGrid getGrid();

    /**
     * @return true if debug mode active
     */
    public boolean debugMode();

    /**
     * Turns on debug mode
     */
    public void debugOn();

    /**
     * Turns off debug mode
     */
    public void debugOff();
}
