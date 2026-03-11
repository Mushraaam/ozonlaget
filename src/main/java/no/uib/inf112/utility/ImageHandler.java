package no.uib.inf112.utility;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.Direction;
import no.uib.inf112.enums.EnemyAction;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.enums.FloorType;
import no.uib.inf112.enums.GunType;
import no.uib.inf112.enums.PuddleType;
import no.uib.inf112.enums.StaticObjectType;
import no.uib.inf112.enums.WallDirection;
import no.uib.inf112.interfaces.IPuddle;

public class ImageHandler {

    private HashMap<Direction, ArrayList<BufferedImage>> playerSprites;

    // PlayerSprite (put into hashmap layer?)
    private static final int PLAYER_SPRITE_COUNT = 8;

    // Ghoul sprite
    private static final int GHOUL_ANIMATION_COUNT = 8;

    // Wall images
    private HashMap<StaticObjectType, HashMap<WallDirection, BufferedImage>> walls;

    // Static Objects (not walls)
    private HashMap<StaticObjectType, BufferedImage> staticObjects;

    // Enemy image
    private HashMap<EnemyType, ArrayList<BufferedImage>> walkingEnemies;
    private HashMap<EnemyType, ArrayList<BufferedImage>> attackingEnemies;
    private HashMap<EnemyType, ArrayList<BufferedImage>> rangedAttackingEnemies;


    private HashMap<FloorType, BufferedImage> floors;

    private HashMap<Integer, BufferedImage> levelBackground;

    // Puddles
    private HashMap<PuddleType, ArrayList<BufferedImage>> puddles;

    // Projectiles
    private HashMap<PuddleType, BufferedImage> projectiles;

    // UI
    private HashMap<GunType, BufferedImage> gunUI;
    private BufferedImage uiBar;

    // Main Menu
    private BufferedImage menuBackground;
    private BufferedImage startButton;
    private BufferedImage menuTitle;
    private BufferedImage settingsButton;
    private BufferedImage helpButton;


    public ImageHandler() {
        this.playerSprites = new HashMap<>();
        loadPlayerSprite();

        this.walls = new HashMap<>();
        loadWalls();

        this.staticObjects = new HashMap<>();
        loadStaticObjects();

        this.walkingEnemies = new HashMap<>();
        this.attackingEnemies = new HashMap<>();
        this.rangedAttackingEnemies = new HashMap<>();
        loadEnemies();

        this.floors = new HashMap<>();
        loadFloors();

        this.levelBackground = new HashMap<>();
        loadBackgrounds();

        this.gunUI = new HashMap<>();
        loadGunUI();
        this.uiBar = ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/UI/ui-bar.png"), 1200,
                Config.getInt("uiSize"));

        loadMenu();

        this.puddles = new HashMap<>();
        this.projectiles = new HashMap<>();
        loadPuddles();
    }

    // PUDDLES AND PROJECTILES
    private void loadPuddles() {

        //ACID
        ArrayList<BufferedImage> acidPuddles = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            BufferedImage img = ImageReader.fetchImage(String.format("/no/uib/inf112/npcs/ghoul/projectile/puddle_%s.png", i));
            acidPuddles.add(img);
        }
        this.puddles.put(PuddleType.ACID, acidPuddles);
        this.projectiles.put(PuddleType.ACID, ImageReader.fetchImage("/no/uib/inf112/npcs/ghoul/projectile/projectile.png"));
    }

    public BufferedImage getProjectile(PuddleType type){
        return this.projectiles.get(type);
    }

    public BufferedImage getPuddleImage(PuddleType type, int index, int lifetime){
        
        int i = 0;
        if (index > 20){
            i = 3;
        }else if (index > 15){
            i = 2;
        }else if (index > 10){
            i = 1;
        }

        if (index > lifetime - 10){
            i = 1;
        }else if (index > lifetime - 20){
            i = 2;
        }else if (index > lifetime - 30){
            i = 3;
        }
        return this.puddles.get(type).get(i);
    }
    //GUN UI
    private void loadGunUI() {
        int w = Config.getInt("uiGunWidth");
        int h = Config.getInt("uiGunHeight");
        this.gunUI.put(GunType.DEAGLE,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/UI/gun_icons/DEagle.png"), w, h));
        this.gunUI.put(GunType.MP5,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/UI/gun_icons/MP5.png"), w, h));
    }

    public BufferedImage getGunImage(GunType type) {
        return this.gunUI.get(type);
    }

    private void loadBackgrounds() {
        // this.levelBackground.put(1,
        // ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/map/generic_grass.png"),
        // Config.getInt("mapWidth"), Config.getInt("mapHeight")));
        // this.levelBackground.put(1,
        // ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/map/ground_2500x2500.png"),
        // Config.getInt("mapWidth"), Config.getInt("mapHeight")));
        this.levelBackground.put(1, ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/map/ground_2.png"),
                Config.getInt("mapWidth"), Config.getInt("mapHeight")));
    }

    public BufferedImage getBackground(int level) {
        return this.levelBackground.get(level);
    }

    //////////////////////// PLAYER METHODS //////////////////////////////////
    private void loadPlayerSprite() {

        // this should probably be elegantified later
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
            playerNorthWest
                    .add(ImageReader.fetchImage(String.format("/no/uib/inf112/player/player%d_north_west.png", i)));
            playerNorthEast
                    .add(ImageReader.fetchImage(String.format("/no/uib/inf112/player/player%d_north_east.png", i)));
            playerSouthWest
                    .add(ImageReader.fetchImage(String.format("/no/uib/inf112/player/player%d_south_west.png", i)));
            playerSouthEast
                    .add(ImageReader.fetchImage(String.format("/no/uib/inf112/player/player%d_south_east.png", i)));
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

    private void loadWalls() {

        HashMap<WallDirection, BufferedImage> shortWoodenWalls = new HashMap<>();
        shortWoodenWalls.put(WallDirection.HORIZONTAL, ImageReader.fetchImage("/no/uib/inf112/walls/ShortWall1_1.png"));
        shortWoodenWalls.put(WallDirection.VERTICAL, ImageReader.fetchImage("/no/uib/inf112/walls/ShortWall1_2.png"));

        HashMap<WallDirection, BufferedImage> longWoodenWalls = new HashMap<>();
        longWoodenWalls.put(WallDirection.VERTICAL, ImageReader.fetchImage("/no/uib/inf112/walls/LongWall1_1.png"));
        longWoodenWalls.put(WallDirection.HORIZONTAL, ImageReader.fetchImage("/no/uib/inf112/walls/LongWall1_2.png"));

        this.walls.put(StaticObjectType.WOODEN_WALL, shortWoodenWalls);
        this.walls.put(StaticObjectType.LONG_WOODEN_WALL, longWoodenWalls);

    }

    public BufferedImage getWallImage(StaticObjectType type, WallDirection dir) {
        return this.walls.get(type).get(dir);
    }

    //////////////// END WALL LOGIC//////////////
    ///
    /////////////// START STATIC OBJECT LOGIC //////////////
    ///

    private void loadStaticObjects() {
        this.staticObjects.put(StaticObjectType.BEIGE_COUCH_DOWN,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/furniture/beige_couch.png"),
                        Config.getInt("couchWidth"), Config.getInt("couchHeight")));
        this.staticObjects.put(StaticObjectType.DARK_TABLE_ROUNDED,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/furniture/darkwoodentable.png"),
                        Config.getInt("tableWidth"), Config.getInt("tableHeight")));
        this.staticObjects.put(StaticObjectType.DARK_TABLE_SQUARE,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/furniture/darkwoodentablesquare.png"),
                        Config.getInt("tableWidth"), Config.getInt("tableHeight")));
        this.staticObjects.put(StaticObjectType.WHITEWATER,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/water/whitewater.png"),
                        Config.getInt("waterWidth"), Config.getInt("waterHeight")));

    }

    /// /////////// START ENEMY LOGIC //////////////

    private void loadEnemies() {

        // Ghoul Walk
        ArrayList<BufferedImage> ghoulWalk = new ArrayList<>();
        for (int i = 0; i < GHOUL_ANIMATION_COUNT; i++) {
            String path = String.format("/no/uib/inf112/npcs/ghoul/Walk/walk_00%s.png", i);
            BufferedImage rawImage = ImageReader.fetchImage(path);
            ghoulWalk.add(rawImage);
        }
        this.walkingEnemies.put(EnemyType.GHOUL, ghoulWalk);

        // Ghoul melee
        ArrayList<BufferedImage> ghoulMelee = new ArrayList<>();
            for (int i = 0; i < GHOUL_ANIMATION_COUNT; i++) {
            String path = String.format("/no/uib/inf112/npcs/ghoul/Attack/Attack_00%s.png", i);
            BufferedImage rawImage = ImageReader.fetchImage(path);
            ghoulMelee.add(rawImage);
        }
        this.attackingEnemies.put(EnemyType.GHOUL, ghoulMelee);

        // Ghoul ranged
        ArrayList<BufferedImage> ghoulRanged = new ArrayList<>();
            for (int i = 0; i < GHOUL_ANIMATION_COUNT; i++) {
            String path = String.format("/no/uib/inf112/npcs/ghoul/rangedGhoul/Attack2_00%s.png", i);
            BufferedImage rawImage = ImageReader.fetchImage(path);
            ghoulRanged.add(rawImage);
        }
        this.rangedAttackingEnemies.put(EnemyType.GHOUL, ghoulRanged);

    }

    public BufferedImage getEnemySprites(EnemyType type, EnemyAction action, int index){

        return switch (action){
            case WALK -> this.walkingEnemies.get(type).get(index);

            case ATTACK -> this.attackingEnemies.get(type).get(index);

            case RANGED_ATTACK -> this.rangedAttackingEnemies.get(type).get(index);

            default -> throw new IllegalArgumentException("Illegal argument: " + action);
        };
    }

    ///////////////////// END ENEMY LOGIC ////////////////////
    ///
    /// ////////////////START FLOOR LOGIC ////////////////////
    ///

    private void loadFloors() {
        int width = Config.getInt("tileWidth");
        int height = Config.getInt("tileHeight");
        this.floors.put(FloorType.STONE_TILES,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/floors/stonefloor.png"), width, height));
        this.floors.put(FloorType.GRASS_TILES,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/floors/grass_tile.png"), width, height));
        // this.floors.put(FloorType.GRASS_TILES,
        // ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/floors/grass_hd.png"),
        // width, height));
        this.floors.put(FloorType.WOODFLOOR,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/floors/woodfloor.png"), width, height));
        this.floors.put(FloorType.ROCK_ROAD,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/floors/rock_road.png"), width, height));
        this.floors.put(FloorType.GRAY_TILE,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/floors/graytile.png"), width, height));

    }

    // MAIN MENY LOGIC
    private void loadMenu() {
        this.menuBackground = ImageReader.fetchImage("/no/uib/inf112/mainmenu/menu_background.png");
        this.startButton = ImageReader.fetchImage("/no/uib/inf112/mainmenu/start_button.png");
        this.menuTitle = ImageReader.fetchImage("/no/uib/inf112/mainmenu/menu_title.png");
        this.settingsButton = ImageReader.fetchImage("/no/uib/inf112/mainmenu/settings_button.png");
        this.helpButton = ImageReader.fetchImage("/no/uib/inf112/mainmenu/help_button.png");
    }

    public BufferedImage getFloor(FloorType type) {
        return this.floors.get(type);
    }

    public BufferedImage getStaticObjectImage(StaticObjectType type) {
        return this.staticObjects.get(type);
    }

    public BufferedImage uiBar() {
        return this.uiBar;
    }

    public BufferedImage getMenuBackground() {
        return this.menuBackground;
    }

    public BufferedImage getStartButton() {
        return this.startButton;
    }

    public BufferedImage getMenuTitle() {
        return this.menuTitle;
    }

    public BufferedImage getSettingsButton() {
        return this.settingsButton;
    }

    public BufferedImage getHelpButton() {
        return this.helpButton;
    }
}
