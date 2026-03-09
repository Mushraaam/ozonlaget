package no.uib.inf112.map.levels;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import no.uib.inf112.config.Config;
import no.uib.inf112.enums.StaticObjectType;
import no.uib.inf112.interfaces.IFloor;
import no.uib.inf112.interfaces.ILevel;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IPlayer;
import no.uib.inf112.interfaces.IStaticObject;
import no.uib.inf112.player.Player;
import no.uib.inf112.terrain.floor.WoodFloor;
import no.uib.inf112.terrain.furniture.BeigeCouch;
import no.uib.inf112.terrain.furniture.DarkWoodenTable;
import no.uib.inf112.terrain.static_objects.WoodWall;

public class Level1 implements ILevel {

    private static final int LEVELNUMBER = 1;

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

    public Level1(IMap map) {

        // README: For now it looks like it is easier to make floors before making walls
        // (floors are grid-locked, walls are not)

        this.staticObjects = new ArrayList<>();
        this.floors = new ArrayList<>();

        this.bounds = new Rectangle2D.Double(MAPX, MAPY, MAPWIDTH, MAPHEIGHT);
        this.player = new Player(new Rectangle2D.Double(START_X, START_Y, PLAYERWIDTH, PLAYERHEIGHT), this.bounds,
                map);

        generateStaticObjects();
    }

    // midlertidig løsning -> spawner implementeres senere

    private void generateStaticObjects() {

        // start box testing

        // House 1
        this.floors.add(new WoodFloor(new Rectangle2D.Double(900 + 10, 900 + 10, 300, 300)));

        staticObjects.add(new WoodWall( // Left wall
                new Rectangle2D.Double(900 - 30, 900 - 30, 15, 300 + 60), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Bottom wall
                new Rectangle2D.Double(900 - 30, 900 + 330, 360, 15), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Top wall
                new Rectangle2D.Double(900 - 30, 900 - 30, 300 + 60, 15), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Right top
                new Rectangle2D.Double(900 + 330, 900 - 30, 15, 100), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // Right bot
                new Rectangle2D.Double(900 + 330, 900 + 300 - 100, 15, 145), StaticObjectType.WOODEN_WALL));

        // Furniture
        staticObjects.add(new DarkWoodenTable(
                new Rectangle2D.Double(960, 940, Config.getInt("tableWidth"), Config.getInt("tableHeight"))));

        //House 3
        this.floors.add(new WoodFloor(new Rectangle2D.Double(100, 900, 400, 700)));
        this.staticObjects.add(new WoodWall(new Rectangle2D.Double(100-20, 900-20, 10, 700+20), StaticObjectType.LONG_WOODEN_WALL));
                staticObjects.add(new DarkWoodenTable(
                new Rectangle2D.Double(130, 1300, Config.getInt("tableWidth"), Config.getInt("tableHeight"))));
        this.staticObjects.add(new BeigeCouch(new Rectangle2D.Double(130, 1200, Config.getInt("couchWidth"), Config.getInt("couchHeight"))));
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
