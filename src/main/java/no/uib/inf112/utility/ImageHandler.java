package no.uib.inf112.utility;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.Direction;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.enums.FloorType;
import no.uib.inf112.enums.walls.WallDirection;
import no.uib.inf112.enums.walls.StaticObjectType;

public class ImageHandler {

    //PlayerSprite (put into hashmap layer?)
    private static final int PLAYER_SPRITE_COUNT = 8;
    private static final int ENEMY_SPRITE_COUNT = 8;
    private HashMap<Direction, ArrayList<BufferedImage>> playerSprites;


    //Wall images
    private HashMap<StaticObjectType, HashMap<WallDirection, BufferedImage>> walls;

    //Static Objects (not walls)
    private HashMap<StaticObjectType, BufferedImage> staticObjects;

    //Enemy image
    private HashMap<EnemyType, ArrayList<BufferedImage>> enemies;

    private HashMap<FloorType, BufferedImage> floors;


    private HashMap<Integer, BufferedImage> levelBackground;


    public ImageHandler(){
        this.playerSprites = new HashMap<>();
        loadPlayerSprite();

        this.walls = new HashMap<>();
        loadWalls();

        this.staticObjects = new HashMap<>();
        loadStaticObjects();

        this.enemies = new HashMap<>();
        loadEnemies();

        this.floors = new HashMap<>();
        loadFloors();

        this.levelBackground = new HashMap<>();
        loadBackgrounds();
    }



    private void loadBackgrounds() {
        // this.levelBackground.put(1, ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/map/generic_grass.png"), Config.getInt("mapWidth"), Config.getInt("mapHeight")));
        this.levelBackground.put(1, ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/map/grass_dirt.png"), Config.getInt("mapWidth"), Config.getInt("mapHeight")));
    }

    public BufferedImage getBackground(int level){
        return this.levelBackground.get(level);
    }



    //////////////////////// PLAYER METHODS //////////////////////////////////
    private void loadPlayerSprite() {

        //this should probably be elegantified later
        ArrayList<BufferedImage> playerNorth = new ArrayList<>(PLAYER_SPRITE_COUNT);
        ArrayList<BufferedImage> playerSouth = new ArrayList<>(PLAYER_SPRITE_COUNT);
        ArrayList<BufferedImage> playerWest = new ArrayList<>(PLAYER_SPRITE_COUNT);
        ArrayList<BufferedImage> playerEast = new ArrayList<>(PLAYER_SPRITE_COUNT);
        ArrayList<BufferedImage> playerNorthWest = new ArrayList<>(PLAYER_SPRITE_COUNT);
        ArrayList<BufferedImage> playerNorthEast = new ArrayList<>(PLAYER_SPRITE_COUNT);
        ArrayList<BufferedImage> playerSouthWest = new ArrayList<>(PLAYER_SPRITE_COUNT);
        ArrayList<BufferedImage> playerSouthEast = new ArrayList<>(PLAYER_SPRITE_COUNT);

        this.playerSprites.put(Direction.NORTH, playerNorth);

        for (int i = 1; i <= PLAYER_SPRITE_COUNT; i++) {
            playerNorth.add(ImageReader.fetchImage(String.format("/no/uib/inf112/player/player%d_north.png", i)));
            playerSouth.add(ImageReader.fetchImage(String.format("/no/uib/inf112/player/player%d_south.png", i)));
            playerWest.add(ImageReader.fetchImage(String.format("/no/uib/inf112/player/player%d_west.png", i)));
            playerEast.add(ImageReader.fetchImage(String.format("/no/uib/inf112/player/player%d_east.png", i)));
            playerNorthWest.add(ImageReader.fetchImage(String.format("/no/uib/inf112/player/player%d_north_west.png", i)));
            playerNorthEast.add(ImageReader.fetchImage(String.format("/no/uib/inf112/player/player%d_north_east.png", i)));
            playerSouthWest.add(ImageReader.fetchImage(String.format("/no/uib/inf112/player/player%d_south_west.png", i)));
            playerSouthEast.add(ImageReader.fetchImage(String.format("/no/uib/inf112/player/player%d_south_east.png", i)));
        }


        this.playerSprites.put(Direction.NORTH, playerNorth);
        this.playerSprites.put(Direction.SOUTH, playerSouth);
        this.playerSprites.put(Direction.WEST, playerWest);
        this.playerSprites.put(Direction.EAST, playerEast);
        this.playerSprites.put(Direction.NORTH_WEST, playerNorthWest);
        this.playerSprites.put(Direction.NORTH_EAST, playerNorthEast);
        this.playerSprites.put(Direction.SOUTH_WEST, playerSouthWest);
        this.playerSprites.put(Direction.SOUTH_EAST, playerSouthEast);
    }

    public BufferedImage getPlayerSprite(Direction dir, int index) {
            return this.playerSprites.get(Direction.EAST).get(index);
        }
    


    //////////////////////////////// END PLAYER METHODS //////////////////////////
    /// 
    /// //////////////////////////// START WALL METHODS //////////////////////////
    /// 
    
    private void loadWalls(){

        HashMap<WallDirection, BufferedImage> shortWoodenWalls = new HashMap<>();
        shortWoodenWalls.put(WallDirection.HORIZONTAL, ImageReader.fetchImage("/no/uib/inf112/walls/ShortWall1_1.png"));
        shortWoodenWalls.put(WallDirection.VERTICAL, ImageReader.fetchImage("/no/uib/inf112/walls/ShortWall1_2.png"));

        HashMap<WallDirection, BufferedImage> longWoodenWalls = new HashMap<>();
        longWoodenWalls.put(WallDirection.VERTICAL, ImageReader.fetchImage("/no/uib/inf112/walls/LongWall1_1.png"));
        longWoodenWalls.put(WallDirection.HORIZONTAL, ImageReader.fetchImage("/no/uib/inf112/walls/LongWall1_2.png"));

        this.walls.put(StaticObjectType.WOODEN_WALL, shortWoodenWalls);
        this.walls.put(StaticObjectType.LONG_WOODEN_WALL, longWoodenWalls);

    }

    public BufferedImage getWallImage(StaticObjectType type, WallDirection dir){
        return this.walls.get(type).get(dir);
    }


    //////////////// END WALL LOGIC//////////////
    /// 
    /////////////// START STATIC OBJECT LOGIC //////////////
    /// 
    
    
    private void loadStaticObjects() {
        this.staticObjects.put(StaticObjectType.DARK_TABLE_ROUNDED, ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/furniture/darkwoodentable.png"), Config.getInt("tableWidth"), Config.getInt("tableHeight")));
    }





    /// /////////// START ENEMY LOGIC //////////////
    
    private void loadEnemies(){
        ArrayList<BufferedImage> zombies = new ArrayList<>();
        int width = Config.getInt("thugWidth");
        int height = Config.getInt("thugHeight");
        for (int i = 0; i < ENEMY_SPRITE_COUNT; i++) {
            String path = String.format("/no/uib/inf112/map/npcs/zombie/zombie_%d.png", i);
            BufferedImage rawImage = ImageReader.fetchImage(path);
            zombies.add(ImageReader.resizeExact(rawImage, width, height));
        }
            this.enemies.put(EnemyType.ZOMBIE, zombies);

    }

    public BufferedImage getEnemySprites(EnemyType type, int Index){
        return this.enemies.get(type).get(Index);
    }

    ///////////////////// END ENEMY LOGIC ////////////////////
    /// 
    /// ////////////////START FLOOR LOGIC ////////////////////
    /// 
    
    private void loadFloors(){
        int width = Config.getInt("tileWidth");
        int height = Config.getInt("tileHeight");
        this.floors.put(FloorType.STONE_TILES, ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/floors/stonefloor.png"), width, height));
        this.floors.put(FloorType.GRASS_TILES, ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/floors/grass_tile.png"), width, height));
        // this.floors.put(FloorType.GRASS_TILES, ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/floors/grass_hd.png"), width, height));
        this.floors.put(FloorType.WOODFLOOR, ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/floors/woodfloor.png"), width, height));
        this.floors.put(FloorType.GRAY_TILE, ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/floors/graytile.png"), width, height));

    }

    public BufferedImage getFloor(FloorType type){
        return this.floors.get(type);
    }


    public BufferedImage getStaticObjectImage(StaticObjectType type) {
        return this.staticObjects.get(type);
    }
}


