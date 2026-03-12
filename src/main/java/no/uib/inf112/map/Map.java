package no.uib.inf112.map;

import java.awt.geom.Rectangle2D;
import java.util.*;

import no.uib.inf112.core.Spawner;
import no.uib.inf112.enums.GameState;
import no.uib.inf112.interfaces.*;
import no.uib.inf112.map.levels.Level1;
import no.uib.inf112.map.npcs.NPC;
import no.uib.inf112.map.npcs.pathfinding.Pathfinder;
import no.uib.inf112.utility.Camera;

public class Map implements IMap {
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
    private final Spawner spawner;

    private Camera camera;

    //GunLogic
    private ArrayList<IGunShot> gunShots;

    //Puddles
    private ArrayList<IPuddle> puddles;
    private ArrayList<IProjectile> projectiles;

    public Map(Camera camera) {

        this.level = new Level1(this);
        this.levelNumber = this.level.levelNumber();
        this.bounds = this.level.getBounds();
        this.player = this.level.getPlayer();
        this.floors = this.level.getFloor();

        this.enemies = new ArrayList<>();
        this.gunShots = new ArrayList<>();
        this.puddles = new ArrayList<>();
        this.projectiles = new ArrayList<>();

        this.debug = false;

        // Implementer egen metode/meny for denne
        this.staticObjects = this.level.getStaticObjects();

        this.gameState = GameState.MAIN_MENU;

        this.grid = new Grid(this);
        this.tiles = new TileGrid(this);
        this.spawner = new Spawner(this); // Spawner comes after grid, or else uh-oh.
        this.pathfinder = new Pathfinder(this);

        
        this.camera = camera;
        gatherOccupiedCells();
    }

    @Override
    public void gatherOccupiedCells() {
        this.grid.gatherOccupiedCells();
    }

    @Override
    public void resetOccupied(){
        this.grid.resetOccupied();
    }


    @Override
    public ArrayList<IMovingDrawableObject> getMovingObjects() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMovingObjects'");
    }

    public Spawner getSpawner() {
        return this.spawner;
    }

    ////////////////// GETTERS AND SETTERS

    public ArrayList<IEnemy> getEnemies() {
        return new ArrayList<>(enemies);
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
        this.gameState = state;
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
    public void addShot(IGunShot shot){
        this.gunShots.add(shot);
    }

    @Override
    public Camera getCamera(){
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



}
