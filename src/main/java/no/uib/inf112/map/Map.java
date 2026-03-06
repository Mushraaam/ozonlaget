package no.uib.inf112.map;

import java.awt.geom.Rectangle2D;
import java.util.*;

import no.uib.inf112.config.Config;
import no.uib.inf112.core.Spawner;
import no.uib.inf112.enums.GameState;
import no.uib.inf112.interfaces.*;
import no.uib.inf112.map.levels.Level1;
import no.uib.inf112.map.npcs.Zombie;
import no.uib.inf112.map.npcs.pathfinding.Pathfinder;

public class Map implements IMap {

    private static final int TILE_HEIGHT = Config.getInt("cellHeight");
    private static final int TILE_WIDTH = Config.getInt("cellWidth");
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

    public Map() {

        this.level = new Level1(this);
        this.levelNumber = this.level.levelNumber();
        this.bounds = this.level.getBounds();
        this.player = this.level.getPlayer();
        this.floors = this.level.getFloor();

        this.enemies = new ArrayList<>();

        this.debug = false;

        // Implementer egen metode/meny for denne
        this.staticObjects = this.level.getStaticObjects();

        this.gameState = GameState.MAIN_MENU;

        this.grid = new Grid(this);
        this.tiles = new TileGrid(this);
        this.spawner = new Spawner(this); // Spawner comes after grid, or else uh-oh.
        this.pathfinder = new Pathfinder(this);



        gatherOccupiedCells();
        // TODO: fjern denne, lage logikk i spawner
        //spawnEnemies();
    }

    private void spawnEnemies() {
        Random random = new Random();
        for (int i = 0; i < 3;) {
            ICell cell = this.grid.getCell(1, random.nextInt(1, this.grid.getColCount() - 1));
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
    public boolean inOccupiedCells(ICell cell) {
        return false; //null is empty, anything else is occupied
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
        int maxR = grid.getRowCount() - 1;
        int maxC = grid.getColCount() - 1;

        for (IEnemy enemy : allEnemies) {
            Rectangle2D.Double hb = enemy.getHitbox();
            int minR = Math.max(0, Math.min((int) (hb.getMinY() / TILE_HEIGHT), maxR));
            int maxR_bound = Math.max(0, Math.min((int) (hb.getMaxY() / TILE_HEIGHT), maxR));
            int minC = Math.max(0, Math.min((int) (hb.getMinX() / TILE_WIDTH), maxC));
            int maxC_bound = Math.max(0, Math.min((int) (hb.getMaxX() / TILE_WIDTH), maxC));

            if (enemy.boundsChanged(minR, maxR_bound, minC, maxC_bound)) {
                if (enemy.getLastMinR() != -1) {
                    for (int r = enemy.getLastMinR(); r <= enemy.getLastMaxR(); r++) {
                        for (int c = enemy.getLastMinC(); c <= enemy.getLastMaxC(); c++) {
                            grid.getCell(r, c).removeEnemy(enemy);
                        }
                    }
                }
                for (int r = minR; r <= maxR_bound; r++) {
                    for (int c = minC; c <= maxC_bound; c++) {
                        grid.getCell(r, c).addEnemy(enemy);
                    }
                }

                enemy.updateBounds(minR, maxR_bound, minC, maxC_bound);
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
    public int level() {
        return this.levelNumber;
    }



}
