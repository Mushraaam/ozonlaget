package no.uib.inf112.map;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import no.uib.inf112.core.Spawner;
import no.uib.inf112.enums.GameState;
import no.uib.inf112.interfaces.*;
import no.uib.inf112.map.levels.Level1;
import no.uib.inf112.map.npcs.Thug;

public class Map implements IMap {

    private ILevel level;
    private IPlayer player;
    private GameState gameState;
    private Rectangle2D.Double bounds;
    private ArrayList<IStaticObject> staticObjects;
    private IGrid grid;
    private boolean debug;

    ArrayList<IEnemy> enemies;
    private final Spawner spawner;
    public Map() {


        this.level = new Level1();
        this.player = this.level.getPlayer();
        this.bounds = this.level.getBounds();
        this.enemies = new ArrayList<IEnemy>();
        // Start with debug during development
        this.debug = true;

        // Implementer egen metode/meny for denne
        this.staticObjects = this.level.getStaticObjects();

        // Bør senere starte i main menu
        this.gameState = GameState.ACTIVE_GAME;

        this.grid = new Grid(this);
        this.spawner = new Spawner(this); //Spawner comes after grid, or else uh-oh.

    }

    @Override
    public ArrayList<IMovingDrawableObject> getMovingObjects() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMovingObjects'");
    }

    public Spawner getSpawner(){
        return this.spawner;
    }



    ////////////////// GETTERS AND SETTERS ////////////////////

    public ArrayList<IEnemy> getEnemies(){
        return enemies;
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
    public void addEnemy(IEnemy thug) {
        enemies.add(thug);
    }
}
