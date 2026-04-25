package no.uib.inf112.model;

import java.awt.geom.Rectangle2D;
import java.util.*;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.GameState;
import no.uib.inf112.interfaces.*;
import no.uib.inf112.model.items.factory.ItemFactory;
import no.uib.inf112.model.levels.Level1;
import no.uib.inf112.model.levels.Level2;
import no.uib.inf112.model.npcs.NPC;
import no.uib.inf112.model.npcs.factory.Factory;
import no.uib.inf112.model.npcs.factory.SpawnPoint;
import no.uib.inf112.model.npcs.pathfinding.Pathfinder;
import no.uib.inf112.utility.Camera;
import no.uib.inf112.utility.SoundHandler;
import no.uib.inf112.view.LoadStatus;

public class Model implements IModel {
    private ILevel level;
    private int levelNumber;
    private IPlayer player;
    private GameState gameState;
    private Rectangle2D.Double bounds;
    private ArrayList<IStaticObject> staticObjects;
    private IGrid grid;
    private IGrid tiles;
    private boolean debug;
    private Pathfinder pathfinder;
    private ArrayList<IEnemy> enemies;
    private ArrayList<IFloor> floors;
    private Factory factory;
    private int difficulty;

    private Camera camera;
    private SoundHandler soundHandler;

    // GunLogic
    private ArrayList<IGunShot> gunShots;

    // Puddles
    private ArrayList<IPuddle> puddles;
    private ArrayList<IProjectile> projectiles;
    private ArrayList<SpawnPoint> spawnPoints;

    // items
    private ArrayList<ICollectable> activeItems;
    private ArrayList<Rectangle2D.Double> buffItemSpawnpoint;
    private ArrayList<Rectangle2D.Double> inventoryItemSpawnpoint;
    private ItemFactory itemFactory;
    private int totalDroppedLoot = 0;

    private ArrayList<IVehicle> vehicles;

    public Model(LoadStatus status) {
        status.setStatus("Loading audio...", 5);
        this.camera = new Camera(0, 0);
        this.soundHandler = new SoundHandler();

        setLevel(1);
        status.setStatus("Initiallizing grid...", 10);
        resetMap();

        gatherOccupiedCells();

    }

    @Override
    public void resetMap() {

        this.camera.update(player.getHitbox(), Config.getInt("screenWidth"), Config.getInt("screenHeight"),
                this.bounds);

        // Avoid music starting twice when restarting from gameover
        if (this.gameState != GameState.MAIN_MENU) {
            setGameState(GameState.MAIN_MENU);
        }
        // lists
        this.enemies = new ArrayList<>();
        this.gunShots = new ArrayList<>();
        this.puddles = new ArrayList<>();
        this.projectiles = new ArrayList<>();

        // grid
        this.grid = new Grid(this);
        this.tiles = new TileGrid(this);

        // enemies
        this.pathfinder = new Pathfinder(this);
        this.factory = level.getFactory();
        this.itemFactory = level.getItemFactory();
    }

    @Override
    public void setLevel(int level) {

        this.vehicles = new ArrayList<>();
        this.activeItems = new ArrayList<>();
        this.spawnPoints = new ArrayList<>();
        this.buffItemSpawnpoint = new ArrayList<>();

        switch (level) {
            case 1 -> {
                this.level = new Level1(this);
            }

            case 2 -> {
                this.level = new Level2(this);
            }

            default -> throw new IllegalStateException("Unknown level");
        }

        // initiate level
        this.bounds = this.level.getBounds();
        this.player = this.level.getPlayer();
        this.floors = this.level.getFloor();
        this.levelNumber = this.level.levelNumber();
        this.debug = false;
        this.staticObjects = this.level.getStaticObjects();
        this.vehicles = this.level.getVehicles();
    }

    @Override
    public IVehicle getHelicopter() {
        return this.vehicles.get(0);
    }

    @Override
    public void gatherOccupiedCells() {
        this.grid.gatherOccupiedCells();
    }

    @Override
    public void resetOccupied() {
        this.grid.resetOccupied();
    }

    // //////////////// GETTERS AND SETTERS

    public ArrayList<IEnemy> getEnemies() {
        return new ArrayList<>(enemies);
    }

    @Override
    public int getTotalDroppedLoot() {
        return this.totalDroppedLoot;
    }

    @Override
    public void increaseDroppedLoot() {
        totalDroppedLoot++;
    }

    @Override
    public void decreaseDroppedLoot() {
        totalDroppedLoot = Math.max(totalDroppedLoot - 1, 0);
    }

    @Override
    public Pathfinder getPathfinder() {
        return pathfinder;
    }

    @Override
    public IPlayer getPlayer() {
        return this.player;
    }

    @Override
    public GameState getGameState() {
        return this.gameState;
    }

    @Override
    public void setGameState(GameState state) {

        //Check if we are just navigating between menues
        if (navigatingMenus(state)) {
            this.gameState = state;
        } else { //Otherwise also change music
            this.gameState = state;
            this.soundHandler.playMusic(state);
        }

    }

    private boolean navigatingMenus(GameState state){
        boolean fromAMenu = (
            this.gameState == GameState.HELP || 
            this.gameState == GameState.MAIN_MENU || 
            this.gameState == GameState.SETTINGS);
        boolean toAMenu = (
            state == GameState.HELP || 
            state == GameState.MAIN_MENU || 
            state == GameState.SETTINGS);
            
        return fromAMenu && toAMenu;
    }

    @Override
    public Rectangle2D.Double getBounds() {
        return this.bounds;
    }

    @Override
    public ArrayList<IStaticObject> getStaticObjects() {
        return this.staticObjects;
    }

    @Override
    public IGrid getGrid() {
        return this.grid;
    }

    @Override
    public boolean debugMode() {
        return this.debug;
    }

    @Override
    public void debugOn() {
        this.debug = true;
    }

    @Override
    public void debugOff() {
        this.debug = false;
    }

    @Override
    public int getEnemyCount() {
        return this.enemies.size();
    }

    @Override
    public void addEnemy(IEnemy thug) {
        enemies.add(thug);
    }

    @Override
    public IGrid getTiles() {
        return this.tiles;
    }

    @Override
    public ArrayList<IFloor> getFloors() {
        return this.floors;
    }

    @Override
    public void addFloor(IFloor floor) {
        this.floors.add(floor);
    }

    @Override
    public int level() {
        return this.levelNumber;
    }

    @Override
    public void removeShot(IGunShot shot) {
        this.gunShots.remove(shot);
    }

    @Override
    public Iterable<IGunShot> gunShots() {
        return new ArrayList<>(this.gunShots);
    }

    @Override
    public void addShot(IGunShot shot) {
        this.gunShots.add(shot);
    }

    @Override
    public Camera getCamera() {
        return this.camera;
    }

    @Override
    public void removeEnemy(NPC npc) {
        this.enemies.remove(npc);
    }

    @Override
    public ArrayList<IPuddle> getAOEPuddles() {
        return new ArrayList<>(this.puddles);
    }

    @Override
    public void removeAOEPuddle(IPuddle puddle) {
        this.puddles.remove(puddle);
    }

    @Override
    public void addAOEPuddle(IPuddle puddle) {
        this.puddles.add(puddle);
    }

    @Override
    public ArrayList<IProjectile> getProjectiles() {
        return new ArrayList<>(this.projectiles);
    }

    @Override
    public void removeProjectile(IProjectile projectile) {
        this.projectiles.remove(projectile);
    }

    @Override
    public void addProjectile(IProjectile projectile) {
        this.projectiles.add(projectile);
    }

    @Override
    public ArrayList<SpawnPoint> getSpawnPoints() {
        return this.spawnPoints;
    }

    @Override
    public void addSpawnPoint(SpawnPoint point) {
        this.spawnPoints.add(point);
    }

    @Override
    public Factory getFactory() {
        return this.factory;
    }

    @Override
    public SoundHandler getSoundHandler() {
        return this.soundHandler;
    }

    @Override
    public ArrayList<ICollectable> getActiveItems() {
        return new ArrayList<>(this.activeItems);
    }

    @Override
    public void addToActiveItems(ICollectable item) {
        this.activeItems.add(item);
    }

    @Override
    public void removeActiveItem(ICollectable item) {
        this.activeItems.remove(item);
    }

    @Override
    public void setItemSpawnPoints(ArrayList<Rectangle2D.Double> buffItemSpawnPoints,
            ArrayList<Rectangle2D.Double> itemSpawnPoints) {
        this.buffItemSpawnpoint = buffItemSpawnPoints;
        this.inventoryItemSpawnpoint = itemSpawnPoints;
    }

    @Override
    public List<Rectangle2D.Double> getBuffItemSpawnpoint() {
        return buffItemSpawnpoint;
    }

    @Override
    public List<Rectangle2D.Double> getInventoryItemSpawnpoint() {
        return inventoryItemSpawnpoint;
    }

    @Override
    public ItemFactory getItemFactory() {
        return this.itemFactory;
    }

    @Override
    public void setPlayer(IPlayer player) {
        this.player = player;
    }

    @Override
    public ArrayList<IVehicle> getVehicles() {
        return this.vehicles;
    }

    

    @Override
    public int getDifficulty() {
        return this.difficulty;
    }

    @Override
    public void incrementDifficulty() {
        this.difficulty = (this.difficulty + 1) % 3;
    }

}
