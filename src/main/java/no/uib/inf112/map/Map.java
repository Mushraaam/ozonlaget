package no.uib.inf112.map;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import no.uib.inf112.config.Config;
import no.uib.inf112.core.Spawner;
import no.uib.inf112.enums.GameState;
import no.uib.inf112.interfaces.*;
import no.uib.inf112.map.levels.Level1;
import no.uib.inf112.map.npcs.Zombie;
import no.uib.inf112.map.npcs.pathfinding.Pathfinder;

public class Map implements IMap {

    private ILevel level;
    private IPlayer player;
    private GameState gameState;
    private Rectangle2D.Double bounds;
    private ArrayList<IStaticObject> staticObjects;
    private IGrid grid;
    private IGrid tiles;
    private boolean debug;
    private Pathfinder pathfinder;
    private ArrayList<IEnemy> enemies;
    private HashSet<ICell> occupiedCells;

    private final Spawner spawner;

    public Map() {

        this.level = new Level1(this);
        this.bounds = this.level.getBounds();
        this.player = this.level.getPlayer();

        this.enemies = new ArrayList<>();
        // Start with debug during development
        this.debug = true;

        // Implementer egen metode/meny for denne
        this.staticObjects = this.level.getStaticObjects();

        // Bør senere starte i main menu
        this.gameState = GameState.MAIN_MENU;

        this.grid = new Grid(this);
        this.tiles = new TileGrid(this);
        this.spawner = new Spawner(this); // Spawner comes after grid, or else uh-oh.
        this.pathfinder = new Pathfinder(this);

        this.occupiedCells = new HashSet<>();
        gatherOccupiedCells();
        // TODO: fjern denne, lage logikk i spawner
        spawnEnemies();
    }

    private void spawnEnemies() {
        Random random = new Random();
        for (int i = 0; i < 3;) {
            ICell cell = this.grid.getCell(0, random.nextInt(this.grid.getColCount()));
            Rectangle2D.Double b = cell.getBounds();

            Zombie zombie = new Zombie(new Rectangle2D.Double(b.x, b.y, Config.getInt("thugWidth"), Config.getInt("thugHeight")), this);
            boolean collides = false;
            for (IEnemy enemy : getEnemies()) {
                if (enemy.getHitbox().intersects(zombie.getHitbox())) {
                    collides = true;
                }

            }
            if (!collides) {
                addEnemy(zombie);
                i++;
            }

        }
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
    public int getEnemyCount(){
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
    public boolean inOccupiedCells(ICell cell) {
        return this.occupiedCells.contains(cell);
    }

    @Override
    public void gatherOccupiedCells() {
        HashSet<ICell> occupied = new HashSet<>();
        for (IEnemy enemy : this.enemies){
            ICell current = this.grid.getCellFromPos(enemy.getHitbox());
            occupied.add(current);
        }
        this.occupiedCells = occupied;
    }
}
