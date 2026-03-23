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
import no.uib.inf112.map.items.factory.ItemFactory;
import no.uib.inf112.map.npcs.factory.Factory;
import no.uib.inf112.map.npcs.factory.SpawnPoint;
import no.uib.inf112.player.Player;
import no.uib.inf112.terrain.floor.WoodFloor;
import no.uib.inf112.terrain.furniture.BeigeBigBed;
import no.uib.inf112.terrain.furniture.BeigeCouch;
import no.uib.inf112.terrain.furniture.BeigeWoodChairDown;
import no.uib.inf112.terrain.furniture.BeigeWoodChairUp;
import no.uib.inf112.terrain.furniture.DarkLongDrawer;
import no.uib.inf112.terrain.furniture.DarkSmallDrawer;
import no.uib.inf112.terrain.furniture.DarkWoodenTableSmall;
import no.uib.inf112.terrain.furniture.GreyTv;
import no.uib.inf112.terrain.furniture.PlantOne;
import no.uib.inf112.terrain.walls.WoodWall;
import no.uib.inf112.terrain.water.Water;


public class Level1 implements ILevel {

    private static final int LEVELNUMBER = 1;
    // Player
    private static final int START_X = 1075;// 2 * Config.getInt("cellWidth"); //Starts in row 2 now
    private static final int START_Y = 1080;// 2 * Config.getInt("cellHeight");; //Same for 2nd col.
    private static final int PLAYERWIDTH = Config.getInt("playerWidth");
    private static final int PLAYERHEIGHT = Config.getInt("playerHeight");
    // Map
    private static final int MAPX = 0;
    private static final int MAPY = 0;
    private static final int MAPWIDTH = Config.getInt("mapWidth");
    private static final int MAPHEIGHT = Config.getInt("mapHeight");

    // Furniture
    private static final int BIGBEDWIDTH = Config.getInt("bigBedWidth");
    private static final int BIGBEDHEIGHT = Config.getInt("bigBedHeight");
    private static final int SMALLDRAWERWIDTH = Config.getInt("smallDrawerWidth");
    private static final int SMALLDRAWERHEIGHT = Config.getInt("smallDrawerHeight");
    private static final int LONGDRAWERWIDTH = Config.getInt("longDrawerWidth");
    private static final int LONGDRAWERHEIGHT = Config.getInt("longDrawerHeight");
    private static final int BIGCOUCHWIDTH = Config.getInt("couchWidth");
    private static final int BIGCOUCHHEIGHT = Config.getInt("couchHeight");
    private static final int GREYTVWIDTH = Config.getInt("tvWidth");
    private static final int GREYTVHEIGHT = Config.getInt("tvHeight");
    private static final int SMALLTABLEWIDTH = Config.getInt("smallTableWidth");
    private static final int SMALLTABLEHEIGHT = Config.getInt("smallTableHeight");
    private static final int PLANTONEWIDTH = Config.getInt("plantWidth");
    private static final int PLANTONEHEIGHT = Config.getInt("plantHeight");
    private static final int CHAIRWIDTH = Config.getInt("woodChairWidth");
    private static final int CHAIRHEIGHT = Config.getInt("woodChairHeight");
  //private static final int SQUARETABLEWIDTH = Config.getInt("tableWidth");
  //private static final int SQUARETABLEHEIGHT = Config.getInt("tableHeight");




    
    // Water
    private static final int WATER_HEIGHT = Config.getInt("waterHeight");
    private static final int WATER_WIDTH = Config.getInt("waterWidth");

    //
    private IPlayer player;
    private Rectangle2D.Double bounds;
    private ArrayList<IStaticObject> staticObjects;
    private ArrayList<IFloor> floors;
    private IMap map;
    private Factory factory;
    private ItemFactory itemFactory;

    public Level1(IMap map) {

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
        generateItemSpawnPoints();

        this.factory = new Factory(this.map);
        this.itemFactory = new ItemFactory(this.map);
    }

    public Factory getFactory() {
        return this.factory;
    }

    @Override
    public ItemFactory getItemFactory() {
        return this.itemFactory;
    }


    // midlertidig løsning -> spawner implementeres senere

    private void generateSpawnPoints() {

        if (this.map == null) {
            throw new IllegalStateException("Map cannot be null");
        }

        //Currently this covers all houses
        this.map.addSpawnPoint(new SpawnPoint(this.map, new Rectangle2D.Double(100, 100, 450, 400)));
        this.map.addSpawnPoint(new SpawnPoint(this.map, new Rectangle2D.Double(410, 705, 350, (double)1170 - 700)));
        this.map.addSpawnPoint(new SpawnPoint(this.map, new Rectangle2D.Double(1710, 20, (double)2480 - 1710, 260)));
        this.map.addSpawnPoint(new SpawnPoint(this.map, new Rectangle2D.Double(1480, 680, (double)1915 - 1480, (double)1205 - 680)));
        this.map.addSpawnPoint(new SpawnPoint(this.map, new Rectangle2D.Double(700, 1450, (double)1510 - 700, (double)1820 - 1450)));
        this.map.addSpawnPoint(new SpawnPoint(this.map, new Rectangle2D.Double(1790, 2185, (double)2430 - 1790, (double)2485 - 2185)));
        this.map.addSpawnPoint(new SpawnPoint(this.map, new Rectangle2D.Double(2100, 880, 400, (double)1770 - 880)));
    }

   private void generateItemSpawnPoints() {
        if (this.map == null) {
            throw new IllegalStateException("Map cannot be null");
        }

        ArrayList<Rectangle2D.Double> itemSpawnPoints = new ArrayList<>();

        double width = Config.getInt("collectableSizeW");
       double height = Config.getInt("collectableSizeH");

        //Outside
        itemSpawnPoints.add(new Rectangle2D.Double(1120, 90, width, height)); //North by the water
        itemSpawnPoints.add(new Rectangle2D.Double(1060, 820, width, height)); //North of spawn on path
        itemSpawnPoints.add(new Rectangle2D.Double(2460, 2450, width, height)); // SE by house 7
        itemSpawnPoints.add(new Rectangle2D.Double(175, 2452, width, height)); //SW by pond


        //Inside houses
        itemSpawnPoints.add(new Rectangle2D.Double(2290, 2270, width, height)); // NE house 7
        itemSpawnPoints.add(new Rectangle2D.Double(710, 1580, width, height));  // W room house 6
        itemSpawnPoints.add(new Rectangle2D.Double(1070, 2125, width, height));// Entrance house 6
        itemSpawnPoints.add(new Rectangle2D.Double(2170, 1700, width, height)); // South in house 5
        itemSpawnPoints.add(new Rectangle2D.Double(2150, 960, width, height)); // North in house 5
        itemSpawnPoints.add(new Rectangle2D.Double(1500, 850, width, height)); // NW room house 4
        itemSpawnPoints.add(new Rectangle2D.Double(660, 940, width, height)); // East in house 3
        itemSpawnPoints.add(new Rectangle2D.Double(440, 740, width, height)); // NW in house 3
        itemSpawnPoints.add(new Rectangle2D.Double(1780, 220, width, height)); // West in house 2
        itemSpawnPoints.add(new Rectangle2D.Double(1975, 22, width, height)); // NE in house 2


        this.map.setItemSpawnPoints(itemSpawnPoints);
    }

    private void generateStaticObjects() {

        //start box testing

        // /////////
        // house 1
        this.floors.add(new WoodFloor(new Rectangle2D.Double(100, 100, 450, 410)));

        //exterior walls
        staticObjects.add(new WoodWall( // Top 
                new Rectangle2D.Double(80, 80, 480, 15), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Left 
                new Rectangle2D.Double(80, 90, 15, 415), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Right
                new Rectangle2D.Double(545, 90, 15, 415), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Bottom left 
                new Rectangle2D.Double(80, 505, 200, 15), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // Bottom right 
                new Rectangle2D.Double(360, 505, 200, 15), StaticObjectType.WOODEN_WALL));

        // /////////
        // house 2
        this.floors.add(new WoodFloor(new Rectangle2D.Double(1710, 20, (double)2480 - 1710, 260)));

        // exterior walls
        staticObjects.add(new WoodWall( // Top 
                new Rectangle2D.Double(1677.5, 0, 800, 15), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Left 
                new Rectangle2D.Double(1680, 12.5, 15, 260), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Right
                new Rectangle2D.Double(2465, 12.5, 15, 260), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Bottom left 
                new Rectangle2D.Double(1680, 265, 350, 15), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // Bottom right 
                new Rectangle2D.Double(2130, 265, 350, 15), StaticObjectType.WOODEN_WALL));
        // /////////

        //interior walls
        staticObjects.add(new WoodWall( // Left wall left room
                new Rectangle2D.Double(1850, 75, 15, 190), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // right wall right room
                new Rectangle2D.Double(2175, 80, 15, 100), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // right bottom wall right room
                new Rectangle2D.Double(2175, 245, 15, 20), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( //bottom left wall right room
                new Rectangle2D.Double(1950, 75, 240, 15), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // top left wall right room
                new Rectangle2D.Double(1950, 15, 15, 74), StaticObjectType.WOODEN_WALL));
        // /////////

        //furniture
        this.staticObjects.add(new BeigeBigBed( // Big bed
                new Rectangle2D.Double(1695, 165, BIGBEDWIDTH, BIGBEDHEIGHT)));
        this.staticObjects.add(new DarkSmallDrawer( // Small drawer
                new Rectangle2D.Double(1695, 15, SMALLDRAWERWIDTH, SMALLDRAWERHEIGHT)));
        this.staticObjects.add(new DarkLongDrawer( // long drawer
                new Rectangle2D.Double(1950, 90, LONGDRAWERWIDTH, LONGDRAWERHEIGHT)));
        this.staticObjects.add(new BeigeCouch( // big couch
                new Rectangle2D.Double(2365, 110, BIGCOUCHWIDTH, BIGCOUCHHEIGHT)));
        this.staticObjects.add(new GreyTv( // TV
                new Rectangle2D.Double(2390, 215, GREYTVWIDTH, GREYTVHEIGHT)));
        this.staticObjects.add(new DarkWoodenTableSmall( // Small Table left
                new Rectangle2D.Double(1865, 215, SMALLTABLEWIDTH, SMALLTABLEHEIGHT)));
        this.staticObjects.add(new PlantOne( // Plant
                new Rectangle2D.Double(1865, 215, PLANTONEWIDTH, PLANTONEHEIGHT)));
        this.staticObjects.add(new DarkWoodenTableSmall( // right table
                new Rectangle2D.Double(2260, 70, SMALLTABLEWIDTH, SMALLTABLEHEIGHT)));
        this.staticObjects.add(new BeigeWoodChairDown( // chair facing Down
                new Rectangle2D.Double(2260, 15, CHAIRWIDTH, CHAIRHEIGHT)));
        this.staticObjects.add(new BeigeWoodChairUp( // chair facing Up
                new Rectangle2D.Double(2260, 130, CHAIRWIDTH, CHAIRHEIGHT)));
        
        // /////////
        // house 3
        this.floors.add(new WoodFloor(new Rectangle2D.Double(410, 705, 350, (double)1170 - 700)));

        // exterior walls
        staticObjects.add(new WoodWall( // Top left
                new Rectangle2D.Double(400, 680, 150, 15), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // Top right
                new Rectangle2D.Double(610, 680, 150, 15), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // Left 
                new Rectangle2D.Double(400, 690, 15, 510), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Right
                new Rectangle2D.Double(745, 690, 15, 510), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Bottom left 
                new Rectangle2D.Double(400, 1185, 150, 15), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // Bottom right 
                new Rectangle2D.Double(610, 1185, 150, 15), StaticObjectType.WOODEN_WALL));

        // /////////

        // interior walls
        staticObjects.add(new WoodWall( // 1st room left bottom door wall
                new Rectangle2D.Double(535, 1135, 15, 50), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // 1st room right bottom door wall
                new Rectangle2D.Double(609, 1135, 15, 50), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // 1st room left top door wall
                new Rectangle2D.Double(535, 1015, 15, 50), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // 1st room right top door wall
                new Rectangle2D.Double(609, 1015, 15, 50), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall(// left 2nd room wall
                new Rectangle2D.Double(415, 1000, 135, 15), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall(// right 2nd room wall
                new Rectangle2D.Double(609, 1000, 135, 15), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // 2nd room left bottom door wall
                new Rectangle2D.Double(535, 960, 15, 40), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // 2nd room right bottom door wall
                new Rectangle2D.Double(609, 960, 15, 40), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // 2nd room left top door wall
                new Rectangle2D.Double(535, 860, 15, 40), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // 2nd room right top door wall
                new Rectangle2D.Double(609, 860, 15, 40), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // left 3rd room wall
                new Rectangle2D.Double(415, 845, 135, 15), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // right 3rd room wall
                new Rectangle2D.Double(609, 845, 135, 15), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // 3nd room left bottom door wall
                new Rectangle2D.Double(535, 805, 15, 40), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // 3nd room right bottom door wall
                new Rectangle2D.Double(609, 805, 15, 40), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // 3nd room left top door wall
                new Rectangle2D.Double(535, 695, 15, 40), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // 3nd room right top door wall
                new Rectangle2D.Double(609, 695, 15, 40), StaticObjectType.WOODEN_WALL));
        // /////////

        // /////////
        // house 4
        this.floors.add(new WoodFloor(new Rectangle2D.Double(1480, 680, (double)1915 - 1480, (double)1205 - 680)));
        // exterior walls
        staticObjects.add(new WoodWall( // Top 
                new Rectangle2D.Double(1480, 680, 440, 15), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Left 
                new Rectangle2D.Double(1480, 690, 15, 550), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Right
                new Rectangle2D.Double(1905, 690, 15, 550), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Bottom left 
                new Rectangle2D.Double(1480, 1225, 130, 15), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // Bottom right 
                new Rectangle2D.Double(1690, 1225, 230, 15), StaticObjectType.WOODEN_WALL));
        // /////////
        
        //interior walls
        staticObjects.add(new WoodWall( // Top long
                new Rectangle2D.Double(1495, 900, 300, 15), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // Top short
                new Rectangle2D.Double(1875, 900, 30, 15), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // Top mid
                new Rectangle2D.Double(1650, 750, 15, 150), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // Bottom left
                new Rectangle2D.Double(1495, 1075, 220, 15), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // Bottom right short
                new Rectangle2D.Double(1700, 1090, 15, 20), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // Bottom right long
                new Rectangle2D.Double(1700, 1175, 15, 50), StaticObjectType.WOODEN_WALL));
        // /////////

        // /////////
        // house 5 
        this.floors.add(new WoodFloor(new Rectangle2D.Double(2100, 880, 400, (double)1770 - 880)));
        // exterior walls
        staticObjects.add(new WoodWall( // Top 
                new Rectangle2D.Double(2080, 880, 400, 15), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Top Left 
                new Rectangle2D.Double(2080, 890, 15, 650), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Bottom left 
                new Rectangle2D.Double(2080, 1685, 15, 115), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // Right
                new Rectangle2D.Double(2465, 890, 15, 910), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Bottom 
                new Rectangle2D.Double(2080, 1785, 400, 15), StaticObjectType.WOODEN_WALL));

        // interior walls
        staticObjects.add(new WoodWall( // Top Left
                new Rectangle2D.Double(2095, 1175, 80, 15), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // Top Middle Horizontal
                new Rectangle2D.Double(2230, 1175, 110, 15), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // Top Middle Vertical
                new Rectangle2D.Double(2275, 890, 15, 285), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Top Right
                new Rectangle2D.Double(2390, 1175, 75, 15), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // Bottom Left
                new Rectangle2D.Double(2080, 1675, 270, 15), StaticObjectType.WOODEN_WALL));
        // /////////

        // /////////
        //house 6
        this.floors.add(new WoodFloor(new Rectangle2D.Double(700, 1450, (double)1510 - 700, (double)1820 - 1450)));
        this.floors.add(new WoodFloor(new Rectangle2D.Double(970, 1820, (double)1510 - 1240, (double)2260 - 1820)));

        // exterior walls
        // ---top rectangle
        staticObjects.add(new WoodWall( // Top 
                new Rectangle2D.Double(677.5, 1440, 842.5, 15), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Left 
                new Rectangle2D.Double(680, 1450, 15, 390), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Right
                new Rectangle2D.Double(1505, 1450, 15, 390), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Bottom left 
                new Rectangle2D.Double(680, 1825, 280, 15), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // Bottom Right
                new Rectangle2D.Double(1240, 1825, 280, 15), StaticObjectType.WOODEN_WALL));
        // ---bottom rectangle
        staticObjects.add(new WoodWall( // Left 
                new Rectangle2D.Double(960, 1825, 15, 455), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Right 
                new Rectangle2D.Double(1225, 1825, 15, 455), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Bottom Left 
                new Rectangle2D.Double(960, 2265, 110, 15), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // Bottom Right
                new Rectangle2D.Double(1130, 2265, 110, 15), StaticObjectType.WOODEN_WALL));

        // interior walls

        // ---top rectangle
        // -left side
        staticObjects.add(new WoodWall( // Left
                new Rectangle2D.Double(680, 1550, 280, 15), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Left
                new Rectangle2D.Double(960, 1550, 15, 200), StaticObjectType.LONG_WOODEN_WALL));
        // -right side
        staticObjects.add(new WoodWall( // Left
                new Rectangle2D.Double(1225, 1525, 15, 225), StaticObjectType.LONG_WOODEN_WALL));

        // ---bottom rectangle
        staticObjects.add(new WoodWall( // Top Left
                new Rectangle2D.Double(1030, 1825, 45, 15), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // Top Right
                new Rectangle2D.Double(1125, 1825, 45, 15), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // Middle Left
                new Rectangle2D.Double(1030, 1835, 15, 350), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Middle Right
                new Rectangle2D.Double(1155, 1835, 15, 350), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Bottom Middle
                new Rectangle2D.Double(1030, 2170, 140, 15), StaticObjectType.WOODEN_WALL));
        // /////////

        // /////////
        // house 7
        this.floors.add(new WoodFloor(new Rectangle2D.Double(1790, 2185, (double)2430 - 1790, (double)2485 - 2185)));
        // exterior walls
        staticObjects.add(new WoodWall( // Top 
                new Rectangle2D.Double(1757.5, 2160, 682.5, 15), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Top Left 
                new Rectangle2D.Double(1760, 2170, 15, 100), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Bottom Left
                new Rectangle2D.Double(1760, 2360, 15, 120), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // Right
                new Rectangle2D.Double(2425, 2170, 15, 310), StaticObjectType.LONG_WOODEN_WALL));
        staticObjects.add(new WoodWall( // Bottom 
                new Rectangle2D.Double(1760, 2480, 680, 15), StaticObjectType.WOODEN_WALL));

        // interior walls
        staticObjects.add(new WoodWall( // Left
                new Rectangle2D.Double(1775, 2360, 180, 15), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // Middle
                new Rectangle2D.Double(2055, 2360, 180, 15), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // Right Top
                new Rectangle2D.Double(2235, 2175, 15, 120), StaticObjectType.WOODEN_WALL));
        staticObjects.add(new WoodWall( // Right Bottom
                new Rectangle2D.Double(2235, 2360, 15, 120), StaticObjectType.WOODEN_WALL));
        // /////////

        // water

        // water top
        // row 1
        this.staticObjects.add(new Water(new Rectangle2D.Double(700, 60, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(800, 60, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(900, 60, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(950, 110, WATER_WIDTH, WATER_HEIGHT)));
        // THIS IS WHERE THE POWERUP GOES
        this.staticObjects.add(new Water(new Rectangle2D.Double(1250, 110, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(1300, 60, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(1400, 60, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(1500, 60, WATER_WIDTH, WATER_HEIGHT)));
        // row 2
        this.staticObjects.add(new Water(new Rectangle2D.Double(700, 160, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(800, 160, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(900, 160, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(1000, 160, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(1100, 160, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(1200, 160, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(1300, 160, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(1400, 160, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(1500, 160, WATER_WIDTH, WATER_HEIGHT)));
        // row 3
        this.staticObjects.add(new Water(new Rectangle2D.Double(750, 210, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(800, 260, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(900, 260, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(1000, 260, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(1100, 260, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(1200, 260, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(1300, 260, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(1400, 260, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(1450, 210, WATER_WIDTH, WATER_HEIGHT)));


        // water bottom left
        // row 0
        this.staticObjects.add(new Water(new Rectangle2D.Double(100, 1950, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(140, 1900, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(240, 1900, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(340, 1900, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(440, 1900, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(490, 1900, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(590, 1950, WATER_WIDTH, WATER_HEIGHT)));

        // row 1
        this.staticObjects.add(new Water(new Rectangle2D.Double(50, 2000, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(140, 2000, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(240, 2000, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(340, 2000, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(440, 2000, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(540, 2000, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(640, 2000, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(665, 2050, WATER_WIDTH, WATER_HEIGHT)));

        // row 2
        this.staticObjects.add(new Water(new Rectangle2D.Double(50, 2100, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(140, 2100, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(240, 2100, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(340, 2100, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(440, 2100, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(540, 2100, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(640, 2100, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(690, 2100, WATER_WIDTH, WATER_HEIGHT)));

        // row 3
        this.staticObjects.add(new Water(new Rectangle2D.Double(50, 2200, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(140, 2200, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(240, 2200, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(340, 2200, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(440, 2200, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(540, 2200, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(640, 2200, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(715, 2200, WATER_WIDTH, WATER_HEIGHT)));

        // row 4
        this.staticObjects.add(new Water(new Rectangle2D.Double(50, 2300, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(140, 2300, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(240, 2300, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(340, 2300, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(440, 2300, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(540, 2300, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(640, 2300, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(740, 2300, WATER_WIDTH, WATER_HEIGHT)));

        // row 5
        this.staticObjects.add(new Water(new Rectangle2D.Double(50, 2350, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(140, 2350, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(240, 2400, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(340, 2400, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(440, 2400, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(540, 2400, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(640, 2400, WATER_WIDTH, WATER_HEIGHT)));
        this.staticObjects.add(new Water(new Rectangle2D.Double(740, 2400, WATER_WIDTH, WATER_HEIGHT)));

        // vegetation

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
