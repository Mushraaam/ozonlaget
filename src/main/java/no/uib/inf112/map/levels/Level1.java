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
import no.uib.inf112.interfaces.IVehicle;
import no.uib.inf112.map.items.factory.ItemFactory;
import no.uib.inf112.map.npcs.factory.Factory;
import no.uib.inf112.map.npcs.factory.SpawnPoint;
import no.uib.inf112.player.Helicopter;
import no.uib.inf112.player.Player;
import no.uib.inf112.terrain.floor.RockRoad;
import no.uib.inf112.terrain.floor.WoodFloor;
import no.uib.inf112.terrain.furniture.BeigeBigBed;
import no.uib.inf112.terrain.furniture.BeigeCouch;
import no.uib.inf112.terrain.furniture.BeigeSmallBed;
import no.uib.inf112.terrain.furniture.BeigeSmallCouch;
import no.uib.inf112.terrain.furniture.BeigeWoodChairDown;
import no.uib.inf112.terrain.furniture.BeigeWoodChairUp;
import no.uib.inf112.terrain.furniture.DarkLongDrawer;
import no.uib.inf112.terrain.furniture.DarkSmallDrawer;
import no.uib.inf112.terrain.furniture.DarkSmallDrawerUp;
import no.uib.inf112.terrain.furniture.DarkWoodenTableSmall;
import no.uib.inf112.terrain.furniture.DarkWoodenTableSquare;
import no.uib.inf112.terrain.furniture.GreyTv;
import no.uib.inf112.terrain.furniture.GreyTvLeft;
import no.uib.inf112.terrain.furniture.PlantOne;
import no.uib.inf112.terrain.furniture.PlantTwo;
import no.uib.inf112.terrain.furniture.RedChairLeft;
import no.uib.inf112.terrain.furniture.RedChairRight;
import no.uib.inf112.terrain.walls.BarbedFence;
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
        private static final int DARKWOODENTABLESQUAREHEIGHT = Config.getInt("tableHeight");
        private static final int DARKWOODENTABLESQUAREWIDTH = Config.getInt("tableWidth");
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
        private static final int SMALLBEDWIDTH = Config.getInt("smallBedWidth");
        private static final int SMALLBEDHEIGHT = Config.getInt("smallBedHeight");
        private static final int REDCHAIRWIDTH = Config.getInt("redChairWidth");
        private static final int REDCHAIRHEIGHT = Config.getInt("redChairHeight");

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
        private ArrayList<IVehicle> vehicles;

        public Level1(IMap map) {

                // README: For now it looks like it is easier to make floors before making walls
                // (floors are grid-locked, walls are not)

                this.map = map;

                this.staticObjects = new ArrayList<>();
                this.floors = new ArrayList<>();

                this.bounds = new Rectangle2D.Double(MAPX, MAPY, MAPWIDTH, MAPHEIGHT);
                this.player = new Player(new Rectangle2D.Double(START_X, START_Y, PLAYERWIDTH, PLAYERHEIGHT),
                                this.bounds,
                                map);
                this.map.setPlayer(this.player);
                this.vehicles = new ArrayList<>();

                generateStaticObjects();
                generateSpawnPoints();
                generateItemSpawnPoints();
                this.factory = new Factory(this.map);
                this.itemFactory = new ItemFactory(this.map);

                while (true) {
                        if (player != null) {
                                this.itemFactory.spawnInventoryItems();
                                break;
                        }
                }
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

                // Currently this covers all houses
                this.map.addSpawnPoint(
                                new SpawnPoint(this.map, new Rectangle2D.Double(410, 705, 350, (double) 1170 - 700)));
                this.map.addSpawnPoint(
                                new SpawnPoint(this.map, new Rectangle2D.Double(1710, 20, (double) 2480 - 1710, 260)));
                this.map.addSpawnPoint(new SpawnPoint(this.map,
                                new Rectangle2D.Double(1480, 680, (double) 1915 - 1480, (double) 1205 - 680)));
                this.map.addSpawnPoint(new SpawnPoint(this.map,
                                new Rectangle2D.Double(700, 1450, (double) 1510 - 700, (double) 1820 - 1450)));
                this.map.addSpawnPoint(new SpawnPoint(this.map,
                                new Rectangle2D.Double(1790, 2185, (double) 2430 - 1790, (double) 2485 - 2185)));
                this.map.addSpawnPoint(
                                new SpawnPoint(this.map, new Rectangle2D.Double(2100, 880, 400, (double) 1770 - 880)));
        }

        private void generateItemSpawnPoints() {
                if (this.map == null) {
                        throw new IllegalStateException("Map cannot be null");
                }

                ArrayList<Rectangle2D.Double> buffSpawnPoints = new ArrayList<>();

                ArrayList<Rectangle2D.Double> inventoryItemSpawnPoints = new ArrayList<>();

                double width = Config.getInt("collectableSizeW");
                double height = Config.getInt("collectableSizeH");

                // /////BUFFS
                // Outside
                buffSpawnPoints.add(new Rectangle2D.Double(1120, 90, width, height)); // North by the water
                buffSpawnPoints.add(new Rectangle2D.Double(1060, 820, width, height)); // North of spawn on path
                buffSpawnPoints.add(new Rectangle2D.Double(2460, 2450, width, height)); // SE by house 7
                buffSpawnPoints.add(new Rectangle2D.Double(175, 2452, width, height)); // SW by pond

                // Inside houses
                buffSpawnPoints.add(new Rectangle2D.Double(2290, 2270, width, height)); // NE house 7
                buffSpawnPoints.add(new Rectangle2D.Double(710, 1580, width, height)); // W room house 6
                buffSpawnPoints.add(new Rectangle2D.Double(1070, 2125, width, height));// Entrance house 6
                buffSpawnPoints.add(new Rectangle2D.Double(2170, 1700, width, height)); // South in house 5
                buffSpawnPoints.add(new Rectangle2D.Double(2155, 960, width, height)); // North in house 5
                buffSpawnPoints.add(new Rectangle2D.Double(1500, 850, width, height)); // NW room house 4
                buffSpawnPoints.add(new Rectangle2D.Double(660, 940, width, height)); // East in house 3
                buffSpawnPoints.add(new Rectangle2D.Double(440, 740, width, height)); // NW in house 3
                buffSpawnPoints.add(new Rectangle2D.Double(1780, 220, width, height)); // West in house 2
                buffSpawnPoints.add(new Rectangle2D.Double(1975, 22, width, height)); // NE in house 2

                // //////INVENTORY ITEMS

                // Inside houses
                inventoryItemSpawnPoints.add(new Rectangle2D.Double(705, 1480, width, height)); // first one is
                                                                                                // dedicated key spawn.
                                                                                                // NW house 6
                inventoryItemSpawnPoints.add(new Rectangle2D.Double(1455, 1580, width, height)); // House 6 East
                inventoryItemSpawnPoints.add(new Rectangle2D.Double(2145, 2430, width, height)); // House 7 South
                inventoryItemSpawnPoints.add(new Rectangle2D.Double(2350, 930, width, height)); // House 5 NE
                inventoryItemSpawnPoints.add(new Rectangle2D.Double(1750, 840, width, height)); // House 4 NE
                inventoryItemSpawnPoints.add(new Rectangle2D.Double(470, 890, width, height)); // House 3 NW
                inventoryItemSpawnPoints.add(new Rectangle2D.Double(2380, 40, width, height)); // House 2 NE
                // Outside houses
                //inventoryItemSpawnPoints.add(new Rectangle2D.Double(25, 40, width, height)); // NW of helipad
                inventoryItemSpawnPoints.add(new Rectangle2D.Double(2455, 820, width, height)); // NE of House 5
                inventoryItemSpawnPoints.add(new Rectangle2D.Double(1145, 40, width, height)); // By north pon
                inventoryItemSpawnPoints.add(new Rectangle2D.Double(905, 1860, width, height)); // W of house 6

                this.map.setItemSpawnPoints(buffSpawnPoints, inventoryItemSpawnPoints);
        }

        private void generateStaticObjects() {

                // start box testing

                // /////////
                // helicopter area
                this.floors.add(new RockRoad(new Rectangle2D.Double(0, 0, 545, 410)));
                
                //Fence
                staticObjects.add(new BarbedFence( // Bottom Left 1
                                new Rectangle2D.Double(0, 430, 132.5, 15), StaticObjectType.BARBED_FENCE));
                staticObjects.add(new BarbedFence( // Bottom Left 2
                                new Rectangle2D.Double(132.5, 430, 132.5, 15), StaticObjectType.BARBED_FENCE));
                staticObjects.add(new BarbedFence( // Bottom Right
                                new Rectangle2D.Double(358, 430, 202, 15), StaticObjectType.BARBED_FENCE));
                staticObjects.add(new BarbedFence( // Right Top
                                new Rectangle2D.Double(550, 0, 15, 217.5), StaticObjectType.BARBED_FENCE));
                staticObjects.add(new BarbedFence( // Right Bottom
                                new Rectangle2D.Double(550, 217.5, 15, 217.5), StaticObjectType.BARBED_FENCE));
                
                //helicopter
                this.vehicles.add(new Helicopter(new Rectangle2D.Double(
                                150, 130, 210, 162.5

                )));
                // /////////
                
                // /////////
                // house 2
                this.floors.add(new WoodFloor(new Rectangle2D.Double(1710, 20, (double) 2480 - 1710, 260)));

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

                // interior walls
                staticObjects.add(new WoodWall( // Left wall left room
                                new Rectangle2D.Double(1850, 75, 15, 190), StaticObjectType.LONG_WOODEN_WALL));
                staticObjects.add(new WoodWall( // right wall right room
                                new Rectangle2D.Double(2175, 80, 15, 100), StaticObjectType.WOODEN_WALL));
                staticObjects.add(new WoodWall( // right bottom wall right room
                                new Rectangle2D.Double(2175, 245, 15, 20), StaticObjectType.WOODEN_WALL));
                staticObjects.add(new WoodWall( // bottom left wall right room
                                new Rectangle2D.Double(1950, 75, 240, 15), StaticObjectType.LONG_WOODEN_WALL));
                staticObjects.add(new WoodWall( // top left wall right room
                                new Rectangle2D.Double(1950, 15, 15, 74), StaticObjectType.WOODEN_WALL));
                // /////////

                // furniture
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
                this.floors.add(new WoodFloor(new Rectangle2D.Double(410, 705, 350, (double) 1170 - 700)));

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

                // furniture
                this.staticObjects.add(new BeigeSmallBed( // Small bed left
                                new Rectangle2D.Double(415, 1015, SMALLBEDWIDTH, SMALLBEDHEIGHT)));
                this.staticObjects.add(new BeigeSmallBed( // Small bed right
                                new Rectangle2D.Double(695, 695, SMALLBEDWIDTH, SMALLBEDHEIGHT)));
                this.staticObjects.add(new DarkWoodenTableSmall( // small table left
                                new Rectangle2D.Double(415, 1135, SMALLTABLEWIDTH, SMALLTABLEHEIGHT)));
                this.staticObjects.add(new DarkWoodenTableSmall( // small table right
                                new Rectangle2D.Double(695, 795, SMALLTABLEWIDTH, SMALLTABLEHEIGHT)));
                this.staticObjects.add(new DarkWoodenTableSmall( // small table mid
                                new Rectangle2D.Double(415, 860, SMALLTABLEWIDTH, SMALLTABLEHEIGHT)));
                this.staticObjects.add(new RedChairRight( // red chair left
                                new Rectangle2D.Double(470, 1140, REDCHAIRWIDTH, REDCHAIRHEIGHT)));
                this.staticObjects.add(new RedChairLeft( // red chair right
                                new Rectangle2D.Double(645, 800, REDCHAIRWIDTH, REDCHAIRHEIGHT)));
                this.staticObjects.add(new BeigeCouch( // big couch
                                new Rectangle2D.Double(415, 695, BIGCOUCHWIDTH, BIGCOUCHHEIGHT)));
                this.staticObjects.add(new GreyTv( // TV
                                new Rectangle2D.Double(440, 795, GREYTVWIDTH, GREYTVHEIGHT)));
                this.staticObjects.add(new DarkWoodenTableSmall( // small table living room
                                new Rectangle2D.Double(680, 1075, SMALLTABLEWIDTH, SMALLTABLEHEIGHT)));
                this.staticObjects.add(new BeigeWoodChairDown( // living room chair facing Down
                                new Rectangle2D.Double(680, 1020, CHAIRWIDTH, CHAIRHEIGHT)));
                this.staticObjects.add(new BeigeWoodChairUp( // living room chair facing Up
                                new Rectangle2D.Double(680, 1130, CHAIRWIDTH, CHAIRHEIGHT)));
                this.staticObjects.add(new DarkSmallDrawer( // Small drawer right
                                new Rectangle2D.Double(645, 850, SMALLDRAWERWIDTH, SMALLDRAWERHEIGHT)));
                this.staticObjects.add(new DarkSmallDrawerUp( // Small drawer left
                                new Rectangle2D.Double(415, 950, SMALLDRAWERWIDTH, SMALLDRAWERHEIGHT)));
                this.staticObjects.add(new PlantOne( // Plant
                                new Rectangle2D.Double(415, 855, PLANTONEWIDTH, PLANTONEHEIGHT)));
                // /////////

                // house 4
                this.floors.add(new WoodFloor(
                                new Rectangle2D.Double(1480, 680, (double) 1915 - 1480, (double) 1205 - 680)));
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

                // interior walls
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

                // furniture
                this.staticObjects.add(new DarkSmallDrawer( // Small Drawer bottom
                                new Rectangle2D.Double(1495, 1090, SMALLDRAWERWIDTH, SMALLDRAWERHEIGHT)));
                this.staticObjects.add(new DarkWoodenTableSmall( // Small table top left
                                new Rectangle2D.Double(1495, 695, SMALLTABLEWIDTH, SMALLTABLEHEIGHT)));
                this.staticObjects.add(new RedChairRight( // Red chair top
                                new Rectangle2D.Double(1550, 695, REDCHAIRWIDTH, REDCHAIRHEIGHT)));
                this.staticObjects.add(new BeigeBigBed( // Big bed
                                new Rectangle2D.Double(1550, 800, BIGBEDWIDTH, BIGBEDHEIGHT)));
                this.staticObjects.add(new BeigeCouch( // big couch
                                new Rectangle2D.Double(1665, 750, BIGCOUCHWIDTH, BIGCOUCHHEIGHT)));
                this.staticObjects.add(new GreyTv( // TV
                                new Rectangle2D.Double(1690, 850, GREYTVWIDTH, GREYTVHEIGHT)));
                this.staticObjects.add(new DarkWoodenTableSmall( // Small table top right
                                new Rectangle2D.Double(1855, 695, SMALLTABLEWIDTH, SMALLTABLEHEIGHT)));
                this.staticObjects.add(new PlantOne( // Plant top
                                new Rectangle2D.Double(1855, 695, PLANTONEWIDTH, PLANTONEHEIGHT)));
                this.staticObjects.add(new DarkWoodenTableSquare( // Big table
                                new Rectangle2D.Double(1500, 970, 110, DARKWOODENTABLESQUAREHEIGHT)));
                this.staticObjects.add(new BeigeWoodChairDown( // chair facing down (left)
                                new Rectangle2D.Double(1500, 915, CHAIRWIDTH, CHAIRHEIGHT)));
                this.staticObjects.add(new BeigeWoodChairDown( // chair facing down (right)
                                new Rectangle2D.Double(1560, 915, CHAIRWIDTH, CHAIRHEIGHT)));
                this.staticObjects.add(new BeigeWoodChairUp( // chair facing up (left)
                                new Rectangle2D.Double(1500, 1025, CHAIRWIDTH, CHAIRHEIGHT)));
                this.staticObjects.add(new BeigeWoodChairUp( // chair facing up (right)
                                new Rectangle2D.Double(1560, 1025, CHAIRWIDTH, CHAIRHEIGHT)));
                this.staticObjects.add(new DarkWoodenTableSmall( // PC Table
                                new Rectangle2D.Double(1855, 1127, SMALLTABLEWIDTH, 100)));
                this.staticObjects.add(new GreyTvLeft( // TV facing left
                                new Rectangle2D.Double(1865, 1155, 40, 40)));
                this.staticObjects.add(new RedChairLeft( // Red chair bot
                                new Rectangle2D.Double(1805, 1155, REDCHAIRWIDTH, REDCHAIRHEIGHT)));
                
                

                // /////////
                // house 5
                this.floors.add(new WoodFloor(new Rectangle2D.Double(2100, 880, 400, (double) 1770 - 880)));
                // exterior walls
                staticObjects.add(new WoodWall( // Top
                                new Rectangle2D.Double(2080, 880, 400, 15), StaticObjectType.LONG_WOODEN_WALL));
                staticObjects.add(new WoodWall( // Top Left
                                new Rectangle2D.Double(2080, 890, 15, 660), StaticObjectType.LONG_WOODEN_WALL));
                staticObjects.add(new WoodWall( // Bottom left
                                new Rectangle2D.Double(2080, 1660, 15, 140), StaticObjectType.WOODEN_WALL));
                staticObjects.add(new WoodWall( // Right
                                new Rectangle2D.Double(2465, 890, 15, 910), StaticObjectType.LONG_WOODEN_WALL));
                staticObjects.add(new WoodWall( // Bottom
                                new Rectangle2D.Double(2080, 1785, 400, 15), StaticObjectType.WOODEN_WALL));

                // interior walls
                staticObjects.add(new WoodWall( // Top Left
                                new Rectangle2D.Double(2095, 1175, 70, 15), StaticObjectType.WOODEN_WALL));
                staticObjects.add(new WoodWall( // Top Middle Horizontal
                                new Rectangle2D.Double(2230, 1175, 110, 15), StaticObjectType.WOODEN_WALL));
                staticObjects.add(new WoodWall( // Top Middle Vertical
                                new Rectangle2D.Double(2275, 890, 15, 285), StaticObjectType.LONG_WOODEN_WALL));
                staticObjects.add(new WoodWall( // Top Right
                                new Rectangle2D.Double(2400, 1175, 65, 15), StaticObjectType.WOODEN_WALL));
                staticObjects.add(new WoodWall( // Bottom Left
                                new Rectangle2D.Double(2080, 1660, 270, 15), StaticObjectType.WOODEN_WALL));

                // furniture
                // top left room
                this.staticObjects.add(new BeigeSmallBed( // Small bed left
                                new Rectangle2D.Double(2100, 900, SMALLBEDWIDTH, SMALLBEDHEIGHT)));
                this.staticObjects.add(new BeigeSmallBed( // Small bed right
                                new Rectangle2D.Double(2225, 900, SMALLBEDWIDTH, SMALLBEDHEIGHT)));
                this.staticObjects.add(new DarkWoodenTableSmall( // Table bottom left
                                new Rectangle2D.Double(2100, 1080, SMALLTABLEWIDTH, 100)));
                this.staticObjects.add(new PlantOne( // Plant
                                new Rectangle2D.Double(2100, 1130, PLANTONEWIDTH, PLANTONEHEIGHT)));
                // top right room
                this.staticObjects.add(new BeigeSmallBed( // Small bed left
                                new Rectangle2D.Double(2290, 900, SMALLBEDWIDTH, SMALLBEDHEIGHT)));
                this.staticObjects.add(new BeigeSmallBed( // Small bed right
                                new Rectangle2D.Double(2415, 900, SMALLBEDWIDTH, SMALLBEDHEIGHT)));
                this.staticObjects.add(new DarkWoodenTableSmall( // Table bottom left
                                new Rectangle2D.Double(2290, 1080, SMALLTABLEWIDTH, 100)));
                this.staticObjects.add(new DarkWoodenTableSmall( // Table bottom right
                                new Rectangle2D.Double(2415, 1080, SMALLTABLEWIDTH, 100)));
                this.staticObjects.add(new PlantOne( // Plant
                                new Rectangle2D.Double(2415, 1130, PLANTONEWIDTH, PLANTONEHEIGHT)));
                // middle room
                this.staticObjects.add(new DarkWoodenTableSquare( // Dining table
                                new Rectangle2D.Double(2100, 1405, 170, DARKWOODENTABLESQUAREHEIGHT)));
                this.staticObjects.add(new BeigeWoodChairDown( // chair facing down (left)
                                new Rectangle2D.Double(2100, 1350, CHAIRWIDTH, CHAIRHEIGHT)));
                this.staticObjects.add(new BeigeWoodChairDown( // chair facing down (middle)
                                new Rectangle2D.Double(2160, 1350, CHAIRWIDTH, CHAIRHEIGHT)));
                this.staticObjects.add(new BeigeWoodChairDown( // chair facing down (right)
                                new Rectangle2D.Double(2220, 1350, CHAIRWIDTH, CHAIRHEIGHT)));
                this.staticObjects.add(new BeigeWoodChairUp( // chair facing Up (left)
                                new Rectangle2D.Double(2100, 1460, CHAIRWIDTH, CHAIRHEIGHT)));
                this.staticObjects.add(new BeigeWoodChairUp( // chair facing Up (middle)
                                new Rectangle2D.Double(2160, 1460, CHAIRWIDTH, CHAIRHEIGHT)));
                this.staticObjects.add(new BeigeWoodChairUp( // chair facing Up (right)
                                new Rectangle2D.Double(2220, 1460, CHAIRWIDTH, CHAIRHEIGHT)));
                this.staticObjects.add(new DarkWoodenTableSmall( // giant tv bench
                                new Rectangle2D.Double(2415, 1320, SMALLTABLEWIDTH, 220)));
                this.staticObjects.add(new GreyTvLeft( // TV facing left
                                new Rectangle2D.Double(2415, 1405, GREYTVWIDTH, GREYTVHEIGHT)));
                this.staticObjects.add(new PlantTwo( // Plant
                                new Rectangle2D.Double(2415, 1270, PLANTONEWIDTH, PLANTONEHEIGHT)));

                // bottom room
                this.staticObjects.add(new BeigeSmallBed( // Small bed 
                                new Rectangle2D.Double(2100, 1675, SMALLBEDWIDTH, SMALLBEDHEIGHT)));
                this.staticObjects.add(new DarkWoodenTableSmall( // Small Table 
                                new Rectangle2D.Double(2300, 1675, SMALLTABLEWIDTH, SMALLTABLEHEIGHT)));
                this.staticObjects.add(new PlantTwo( // Plant
                                new Rectangle2D.Double(2300, 1675, PLANTONEWIDTH, PLANTONEHEIGHT)));
                // /////////

                // /////////
                // house 6
                this.floors.add(new WoodFloor(
                                new Rectangle2D.Double(700, 1450, (double) 1510 - 700, (double) 1820 - 1450)));
                this.floors.add(new WoodFloor(
                                new Rectangle2D.Double(970, 1820, (double) 1510 - 1240, (double) 2260 - 1820)));

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

                // furniture
                // top left room
                this.staticObjects.add(new BeigeBigBed( // Big bed
                                new Rectangle2D.Double(695, 1725, BIGBEDWIDTH, BIGBEDHEIGHT)));
                this.staticObjects.add(new DarkWoodenTableSmall( // Small Table 
                                new Rectangle2D.Double(795, 1775, SMALLTABLEWIDTH, SMALLTABLEHEIGHT)));
                this.staticObjects.add(new PlantOne( // Plant
                                new Rectangle2D.Double(905, 1560, PLANTONEWIDTH, PLANTONEHEIGHT)));
                this.staticObjects.add(new DarkSmallDrawer( // Small drawer
                                new Rectangle2D.Double(795, 1560, SMALLDRAWERWIDTH, SMALLDRAWERHEIGHT)));

                // top middle room
                this.staticObjects.add(new DarkWoodenTableSquare( // Small Table left
                                new Rectangle2D.Double(1052, 1655, DARKWOODENTABLESQUAREWIDTH, DARKWOODENTABLESQUAREHEIGHT)));
                this.staticObjects.add(new BeigeWoodChairDown( // chair facing down (left)
                                new Rectangle2D.Double(1045, 1600, CHAIRWIDTH, CHAIRHEIGHT)));
                this.staticObjects.add(new BeigeWoodChairDown( // chair facing down (right)
                                new Rectangle2D.Double(1105, 1600, CHAIRWIDTH, CHAIRHEIGHT)));
                this.staticObjects.add(new BeigeWoodChairUp( // chair facing Up (left)
                                new Rectangle2D.Double(1045, 1710, CHAIRWIDTH, CHAIRHEIGHT)));
                this.staticObjects.add(new BeigeWoodChairUp( // chair facing Up (right)
                                new Rectangle2D.Double(1105, 1710, CHAIRWIDTH, CHAIRHEIGHT)));

                //top right room
                this.staticObjects.add(new BeigeSmallCouch( //couch
                                new Rectangle2D.Double(1240, 1570, BIGCOUCHWIDTH, BIGCOUCHHEIGHT)));
                this.staticObjects.add(new DarkWoodenTableSquare( // TV table
                                new Rectangle2D.Double(1240, 1680, DARKWOODENTABLESQUAREWIDTH, DARKWOODENTABLESQUAREHEIGHT)));
                this.staticObjects.add(new GreyTv( // TV
                                new Rectangle2D.Double(1260, 1680, GREYTVWIDTH, GREYTVHEIGHT)));
                this.staticObjects.add(new DarkWoodenTableSmall( // Small Table 
                                new Rectangle2D.Double(1460, 1780, SMALLTABLEWIDTH, SMALLTABLEHEIGHT)));
                this.staticObjects.add(new PlantTwo( // Plant
                                new Rectangle2D.Double(1460, 1780, PLANTONEWIDTH, PLANTONEHEIGHT)));
                this.staticObjects.add(new DarkLongDrawer( // Long Drawer / bench
                                new Rectangle2D.Double(1315, 1453, 192, LONGDRAWERHEIGHT)));
                this.staticObjects.add(new BeigeSmallBed( // Small bed 
                                new Rectangle2D.Double(1460, 1680, SMALLBEDWIDTH, SMALLBEDHEIGHT)));
                // /////////

                // /////////
                // house 7
                this.floors.add(new WoodFloor(
                                new Rectangle2D.Double(1790, 2185, (double) 2430 - 1790, (double) 2485 - 2185)));
                // exterior walls
                staticObjects.add(new WoodWall( // Top
                                new Rectangle2D.Double(1757.5, 2160, 682.5, 15), StaticObjectType.LONG_WOODEN_WALL));
                staticObjects.add(new WoodWall( // Top Left
                                new Rectangle2D.Double(1760, 2170, 15, 120), StaticObjectType.LONG_WOODEN_WALL));
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

                // furniture 
                // middle room 
                this.staticObjects.add(new GreyTv( // TV
                                new Rectangle2D.Double(2100, 2310, GREYTVWIDTH, GREYTVHEIGHT)));
                this.staticObjects.add(new BeigeSmallCouch( // Couch
                                new Rectangle2D.Double(2075, 2175, BIGCOUCHWIDTH, BIGCOUCHHEIGHT)));
                this.staticObjects.add(new DarkWoodenTableSmall( // Small Table
                                new Rectangle2D.Double(2180, 2175, SMALLTABLEWIDTH, SMALLTABLEHEIGHT)));
                this.staticObjects.add(new PlantTwo( // Plant
                                new Rectangle2D.Double(2180, 2175, PLANTONEWIDTH, PLANTONEHEIGHT)));

                // bottom room
                this.staticObjects.add(new BeigeBigBed( // Big bed
                                new Rectangle2D.Double(1775, 2385, BIGBEDWIDTH, BIGBEDHEIGHT)));
                this.staticObjects.add(new DarkWoodenTableSmall( // Small Table 
                                new Rectangle2D.Double(1873, 2430, SMALLTABLEWIDTH, SMALLTABLEHEIGHT)));
                this.staticObjects.add(new PlantOne( // Plant
                                new Rectangle2D.Double(1873, 2430, PLANTONEWIDTH, PLANTONEHEIGHT)));
                this.staticObjects.add(new DarkSmallDrawer( // Small drawer
                                new Rectangle2D.Double(2150, 2373, SMALLDRAWERWIDTH, SMALLDRAWERHEIGHT)));
                                
                //right room
                this.staticObjects.add(new DarkWoodenTableSquare( // Dining table
                                new Rectangle2D.Double(2320, 2250, DARKWOODENTABLESQUAREWIDTH, DARKWOODENTABLESQUAREHEIGHT)));
                this.staticObjects.add(new BeigeWoodChairDown( // chair facing down (left)
                                new Rectangle2D.Double(2315, 2195, CHAIRWIDTH, CHAIRHEIGHT)));
                this.staticObjects.add(new BeigeWoodChairDown( // chair facing down (right)
                                new Rectangle2D.Double(2375, 2195, CHAIRWIDTH, CHAIRHEIGHT)));
                this.staticObjects.add(new BeigeWoodChairUp( // chair facing Up (left)
                                new Rectangle2D.Double(2315, 2305, CHAIRWIDTH, CHAIRHEIGHT)));
                this.staticObjects.add(new BeigeWoodChairUp( // chair facing Up (right)
                                new Rectangle2D.Double(2375, 2305, CHAIRWIDTH, CHAIRHEIGHT)));
                this.staticObjects.add(new DarkLongDrawer( // Long Drawer / bench
                                new Rectangle2D.Double(2235, 2440, 192, LONGDRAWERHEIGHT)));
                this.staticObjects.add(new PlantTwo( // Plant
                                new Rectangle2D.Double(2245, 2437, PLANTONEWIDTH, PLANTONEHEIGHT)));

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

        @Override
        public ArrayList<IVehicle> getVehicles() {
                return this.vehicles;
        }
}
