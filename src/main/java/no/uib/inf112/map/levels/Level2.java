package no.uib.inf112.map.levels;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import no.uib.inf112.config.Config;
import no.uib.inf112.enums.StaticObjectType;
import no.uib.inf112.interfaces.IFloor;
import no.uib.inf112.interfaces.ILevel;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IPlayer;
import no.uib.inf112.interfaces.IStaticObject;
import no.uib.inf112.map.npcs.factory.Factory;
import no.uib.inf112.map.npcs.factory.SpawnPoint;
import no.uib.inf112.player.Player;
import no.uib.inf112.terrain.floor.RockRoad;
import no.uib.inf112.terrain.floor.WoodFloor;
import no.uib.inf112.terrain.furniture.BeigeCouch;
import no.uib.inf112.terrain.furniture.DarkWoodenTable;
import no.uib.inf112.terrain.furniture.DarkWoodenTableSquare;
import no.uib.inf112.terrain.walls.WoodWall;
import no.uib.inf112.terrain.water.Water;


public class Level2 implements ILevel {

    private static final int LEVELNUMBER = 2;

    private IPlayer player;
    private Rectangle2D.Double bounds;
    private ArrayList<IStaticObject> staticObjects;
    private ArrayList<IFloor> floors;

    // Player
    private static final int START_X = 1200;// 2 * Config.getInt("cellWidth"); //Starts in row 2 now
    private static final int START_Y = 1010;// 2 * Config.getInt("cellHeight");; //Same for 2nd col.
    private static final int PLAYERWIDTH = Config.getInt("playerWidth");
    private static final int PLAYERHEIGHT = Config.getInt("playerHeight");

    // Map
    private static final int MAPX = 0;
    private static final int MAPY = 0;
    private static final int MAPWIDTH = Config.getInt("mapWidth");
    private static final int MAPHEIGHT = Config.getInt("mapHeight");

    private IMap map;
    private Factory factory;

    public Level2(IMap map) {

        // README: For now it looks like it is easier to make floors before making walls
        // (floors are grid-locked, walls are not)

        this.map = map;

        this.staticObjects = new ArrayList<>();
        this.floors = new ArrayList<>();

        this.bounds = new Rectangle2D.Double(MAPX, MAPY, MAPWIDTH, MAPHEIGHT);
        this.player = new Player(new Rectangle2D.Double(START_X, START_Y, PLAYERWIDTH, PLAYERHEIGHT), this.bounds,
                map);

        generateStaticObjects();
        generateSpawnPoints();

        this.factory = new Factory(this.map);
    }

    public Factory getFactory(){
        return this.factory;
    }


    // midlertidig løsning -> spawner implementeres senere

    private void generateSpawnPoints() {

        if (this.map == null){
                throw new IllegalStateException("Map cannot be null");
        }
        //top left, top right, bot left, bot right
        this.map.addSpawnPoint(new SpawnPoint(this.map, new Rectangle2D.Double(10, 10, 150, 150)));
        this.map.addSpawnPoint(new SpawnPoint(this.map, new Rectangle2D.Double(50, MAPHEIGHT - 150, 100, 100)));
        this.map.addSpawnPoint(new SpawnPoint(this.map, new Rectangle2D.Double(MAPWIDTH - 150, MAPHEIGHT - 150, 100, 100)));
        this.map.addSpawnPoint(new SpawnPoint(this.map, new Rectangle2D.Double(MAPWIDTH - 150, 50, 100, 100)));
}

    private void generateStaticObjects() {

        this.floors.add(new RockRoad(new Rectangle2D.Double(1100, 1100, 200, 300)));
        this.staticObjects.add(new WoodWall(new Rectangle2D.Double(1000, 1000, 10, 100), StaticObjectType.LONG_WOODEN_WALL));
        this.staticObjects.add(new DarkWoodenTable(new Rectangle2D.Double(1100, 1100, Config.getInt("tableWidth"), Config.getInt("tableHeight"))));

    }

    @Override
    public ArrayList<IStaticObject> getStaticObjects() {
        return this.staticObjects;
    }

    @Override
    public IPlayer getPlayer() {
        return this.player;
    }

    @Override
    public Rectangle2D.Double getBounds() {
        return this.bounds;
    }

    @Override
    public ArrayList<IFloor> getFloor() {
        return this.floors;
    }

    @Override
    public int levelNumber() {
        return LEVELNUMBER;
    }
}
