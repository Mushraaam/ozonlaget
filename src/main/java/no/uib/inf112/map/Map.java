package no.uib.inf112.map;

import java.awt.geom.Rectangle2D;
import java.util.*;

import no.uib.inf112.config.Config;
import no.uib.inf112.core.Spawner;
import no.uib.inf112.enums.EnemySize;
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
    private EnemySize debugLaneSize = EnemySize.SMALL;
    private Pathfinder pathfinder;
    private ArrayList<IEnemy> enemies;
    private ArrayList<IFloor> floors;
    private final Spawner spawner;
    public Map() {
        // 1. Basic properties
        this.level = new Level1(this);
        this.bounds = this.level.getBounds();
        this.player = this.level.getPlayer();
        this.floors = this.level.getFloor();
        this.staticObjects = this.level.getStaticObjects();
        this.enemies = new ArrayList<>();
        this.debug = true;
        this.gameState = GameState.MAIN_MENU;

        this.grid = new Grid(this);

        this.pathfinder = new Pathfinder(this);

        this.grid.buildNavigationLayers();

        this.tiles = new TileGrid(this);
        this.spawner = new Spawner(this);

        gatherOccupiedCells();
    }

    private void spawnEnemies() {
        Random random = new Random();
        for (int i = 0; i < 3;) {
            ICell cell = this.grid.getCell(0, random.nextInt(this.grid.getColCount()));
            Rectangle2D.Double b = cell.getBounds();

            Zombie zombie = new Zombie(
                    new Rectangle2D.Double(b.x, b.y, Config.getInt("thugWidth"), Config.getInt("thugHeight")), this);
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
    public void gatherOccupiedCells() {
        /*HashSet<ICell> occupied = new HashSet<>();
        for (IEnemy enemy : this.enemies) {
            Rectangle2D.Double pos = enemy.getHitbox();
            double x1 = pos.getMinX();
            double y1 = pos.getMinY();
            double x2 = pos.getMaxX();
            double y2 = pos.getMaxY();

            ICell topLeft = this.grid.getCellFromXY(x1, y1);
            ICell botRight = this.grid.getCellFromXY(x2, y2);

            int startRow = topLeft.row();
            int startCol = topLeft.col();
            int endRow = botRight.row();
            int endCol = botRight.col();

            for (int i = startRow; i <= endRow; i++) {
                for (int j = startCol; j <= endCol; j++) {
                    occupied.add(this.grid.getCell(i, j));
                }
            }
        }
        this.occupiedCells = occupied;*/
    }


    @Override
    public ArrayList<IMovingDrawableObject> getMovingObjects() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMovingObjects'");
    }

    @Override
    public void updateEnemyLocations(List<IEnemy> allEnemies) {
        for (IEnemy enemy : allEnemies) {
            ICell cell = enemy.getStandingCell();
            if (cell != null) {
                cell.setOccupied(true);
            }
        }
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
    public EnemySize debugLaneSize() {
        return debugLaneSize;
    }

    @Override
    public void setDebugLaneSize(EnemySize size){
        this.debugLaneSize = size;
    }


}
