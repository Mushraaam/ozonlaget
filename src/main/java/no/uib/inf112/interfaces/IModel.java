package no.uib.inf112.interfaces;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import no.uib.inf112.enums.GameState;
import no.uib.inf112.model.items.factory.ItemFactory;
import no.uib.inf112.model.npcs.NPC;
import no.uib.inf112.model.npcs.factory.Factory;
import no.uib.inf112.model.npcs.factory.SpawnPoint;
import no.uib.inf112.model.npcs.pathfinding.Pathfinder;
import no.uib.inf112.utility.Camera;
import no.uib.inf112.utility.SoundHandler;

public interface IModel {
    

    /**
     * A list of all static objects (buildings etc)
     * @return Immuteable ArrayList
     */
    public ArrayList<IStaticObject> getStaticObjects();

    /**
     * @return total loot on map
     */
    public int getTotalDroppedLoot();

    /**
     * Increments counter for loot on max
     * Used for keeping track of item cap
     */
    public void increaseDroppedLoot();

    /**
     * Decrements counter for loot on max
     * Used for keeping track of item cap
     */
    public void decreaseDroppedLoot();

    /**
     * @return the pathfinder used for pathfinding (for NPC's)
     */
    public Pathfinder getPathfinder();

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
     * @return count of enemies on the map
     */
    public int getEnemyCount();

    /**
     * @param enemy Adds this enemy to the collection of enemies for map to keep control of.
     * Only enemies in this collection are relevant for the game (They are in the "loop")
     */
    void addEnemy(IEnemy enemy);

    /**
     * @return a list of all enemies on the level.
     */
    ArrayList<IEnemy> getEnemies();

    IVehicle getHelicopter();

    /**
     * Refreshes occupied cells
     */
    public void gatherOccupiedCells();

    /**
     * @return gets a list of all floors
     */
    public ArrayList<IFloor> getFloors();

    /**
     * Adds a floor
     */
    public void addFloor(IFloor floor);

    /**
     * @return number of the current level
     */
    public int level();

    /**
     * Sets all cells in grid to unoccupied
     */
    public void resetOccupied();

    /**
     * Removes the gunShot from the list of gunshots;
     * @param shot
     */
    public void removeShot(IGunShot shot);

    /**
     * @return iterable of all gunshots
     */
    public Iterable<IGunShot> gunShots();

    /**
     * Adds the gunshot to map
     * @param shot
     */
    public void addShot(IGunShot shot);

    /**
     * @return camera object
     */
    public Camera getCamera();

    /**
     * Removes the enemy from the list of enemies
     * @param npc
     */
    public void removeEnemy(NPC npc);

    /**
     * @return list of all IPuddles
     */
    public ArrayList<IPuddle> getAOEPuddles();

    /**
     * Removes puddle from map
     * @param puddle
     */
    public void removeAOEPuddle(IPuddle puddle);

    /**
     * Adds puddle to map
     * @param puddle
     */
    public void addAOEPuddle(IPuddle puddle);

    /**
     * @return list of all projectiles
     */
    public ArrayList<IProjectile> getProjectiles();

    /**
     * Removes projectile from map
     * @param projectile
     */
    public void removeProjectile(IProjectile projectile);

    /**
     * Adds projectile to map
     * @param projectile
     */
    public void addProjectile(IProjectile projectile);

    /**
     * @return list of all spawnpoints
     */
    public ArrayList<SpawnPoint> getSpawnPoints();

    /**
     * Adds spawnPoint to the list of spawnPoints
     * @param point
     */
    public void addSpawnPoint(SpawnPoint point);

    /**
     * @return factory (spawn factory) of current map
     */
    public Factory getFactory();

    /**
     * @return the sound handler
     */
    public SoundHandler getSoundHandler();

    /**
     * Resets the map, clears all lists etc
     */
    public void resetMap();

    /**
     * Resets the map, sets new level
     * @input int level
     */
    public void setLevel(int level);

    /**
     * @return list of all vehicles
     */
    public ArrayList<IVehicle> getVehicles();

    /**
     * @return list of items on the map
     */
    public ArrayList<ICollectable> getActiveItems();

    /**
     * Adds item to the list of items on the map
     * @param item
     */
    public void addToActiveItems(ICollectable item);

    /**
     * Removes the item from the list of items on the map
     * @param item
     */
    public void removeActiveItem(ICollectable item);

    /**
     * Sets the spawnpoints for items and buffs
     * @param buffItemSpawnPoints - a list of buff spawnpoints
     * @param itemSpawnPoints - a list of item spawnpoints
     */
    public void setItemSpawnPoints(ArrayList<Rectangle2D.Double> buffItemSpawnPoints, ArrayList<Rectangle2D.Double> itemSpawnPoints);

    /**
     * @return list of buff item spawnpoints
     */
    public List<Rectangle2D.Double> getBuffItemSpawnpoint();

    /**
     * @return list of inventory-item spawnpoints
     */
    public List<Rectangle2D.Double> getInventoryItemSpawnpoint();

    /**
     * @return the itemfactory in charge of spawning items
     */
    public ItemFactory getItemFactory();

    /**
     * Places the player on the map
     * @param player
     */
    public void setPlayer(IPlayer player);


    /**
     * 0->Eady, 1->Hard, 2->Suicide difficulty
     * @return int representing difficulty
     */
    public int getDifficulty();

    /**
     * Increment difficulty:
     * Easy -> Hard -> Suicide -> Easy
     * 0 -> 1 -> 2 -> 0
     */
    public void incrementDifficulty();

}
