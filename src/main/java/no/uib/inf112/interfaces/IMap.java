package no.uib.inf112.interfaces;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import no.uib.inf112.core.Spawner;
import no.uib.inf112.enums.GameState;
import no.uib.inf112.map.npcs.pathfinding.Pathfinder;

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

    Pathfinder getPathfinder();

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
     * Sets what state the game is in
     * @param state GameState
     */
    public void setGameState(GameState state);

    /**
     * @return Dimensions of the map
     */
    public Rectangle2D.Double getBounds();

    /**
     * @return the grid
     */
    public IGrid getGrid();

    /**
     * @return IGrid of tiles
     */
    public IGrid getTiles();

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

    /**
     * @param enemy Adds this enemy to the collection of enemies for map to keep control of.
     * Only enemies in this collection are relevant for the game (They are in the "loop")
     */
    void addEnemy(IEnemy enemy);

    /**
     * @return the an entity spawner.
     */
    Spawner getSpawner();

    /**
     * @return a list of all enemies on the level.
     */
    ArrayList<IEnemy> getEnemies();
}
