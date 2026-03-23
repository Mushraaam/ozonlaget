package no.uib.inf112.utility;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.*;

public class ImageHandler {

    private ArrayList<BufferedImage> playerBodySprites;
    private ArrayList<BufferedImage> playerFeetSprites;

    // PlayerSprite (put into hashmap layer?)
    private static final int PLAYER_SPRITE_COUNT = 20;

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
    private HashMap<EnemyType, ArrayList<BufferedImage>> dyingEnemies;


    private HashMap<FloorType, BufferedImage> floors;

    private HashMap<Integer, BufferedImage> levelBackground;

    // Puddles
    private HashMap<PuddleType, ArrayList<BufferedImage>> puddles;

    // Projectiles
    private HashMap<PuddleType, BufferedImage> projectiles;

    // UI
    private HashMap<GunType, BufferedImage> gunUI;
    private BufferedImage uiBar;
    private BufferedImage youDied;

    // Main Menu
    private BufferedImage menuBackground;
    private BufferedImage startButton;
    private BufferedImage menuTitle;
    private BufferedImage settingsButton;
    private BufferedImage helpButton;

    // Collectables
    private HashMap<CollectableType, BufferedImage> collectables;

    public ImageHandler() {
        this.playerBodySprites = new ArrayList<>();
        this.playerFeetSprites = new ArrayList<>();
        loadPlayerSprite();

        this.walls = new HashMap<>();
        loadWalls();

        this.staticObjects = new HashMap<>();
        loadStaticObjects();

        this.walkingEnemies = new HashMap<>();
        this.attackingEnemies = new HashMap<>();
        this.rangedAttackingEnemies = new HashMap<>();
        this.dyingEnemies = new HashMap<>();
        loadEnemies();

        this.floors = new HashMap<>();
        loadFloors();

        this.levelBackground = new HashMap<>();
        loadBackgrounds();

        this.gunUI = new HashMap<>();
        loadGunUI();
        this.uiBar = ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/UI/ui-bar.png"), 1200,
                Config.getInt("uiSize"));
        this.youDied = ImageReader.fetchImage("/no/uib/inf112/UI/youdied.png");

        loadMenu();

        this.puddles = new HashMap<>();
        this.projectiles = new HashMap<>();
        loadPuddles();

        this.collectables = new HashMap<>();
        loadCollectables();
    }

    // PUDDLES AND PROJECTILES
    private void loadPuddles() {

        // ACID
        ArrayList<BufferedImage> acidPuddles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            BufferedImage img = ImageReader
                    .fetchImage(String.format("/no/uib/inf112/npcs/ghoul/projectile/puddle_%s.png", i));
            acidPuddles.add(img);
        }
        this.puddles.put(PuddleType.ACID, acidPuddles);
        this.projectiles.put(PuddleType.ACID,
                ImageReader.fetchImage("/no/uib/inf112/npcs/ghoul/projectile/projectile.png"));
    }

    public BufferedImage getProjectile(PuddleType type) {
        return this.projectiles.get(type);
    }

    public BufferedImage getPuddleImage(PuddleType type, int index, int lifetime) {

        int i = 0;
        if (index > 20) {
            i = 3;
        } else if (index > 15) {
            i = 2;
        } else if (index > 10) {
            i = 1;
        }

        if (index > lifetime - 10) {
            i = 1;
        } else if (index > lifetime - 20) {
            i = 2;
        } else if (index > lifetime - 30) {
            i = 3;
        }
        return this.puddles.get(type).get(i);
    }

    // GUN UI
    private void loadGunUI() {
        int w = Config.getInt("uiGunWidth");
        int h = Config.getInt("uiGunHeight");
        this.gunUI.put(GunType.DEAGLE,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/UI/gun_icons/DEagle.png"), w, h));
        this.gunUI.put(GunType.MP5,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/UI/gun_icons/MP5.png"), w, h));
        this.gunUI.put(GunType.SHOTGUN,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/UI/gun_icons/shotgun.png"), w, h));
        
    }



    public BufferedImage getGunImage(GunType type) {
    
        return this.gunUI.getOrDefault(type, this.gunUI.get(GunType.DEAGLE));
    }

    private void loadBackgrounds() {

        this.levelBackground.put(1, ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/map/ground_level1.png"),
                Config.getInt("mapWidth"), Config.getInt("mapHeight")));
        this.levelBackground.put(2, ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/map/city_grid.png"),
                Config.getInt("mapWidth"), Config.getInt("mapHeight")));
    }

    public BufferedImage getBackground(int level) {
        return this.levelBackground.get(level);
    }

    // ////////////////////// PLAYER METHODS //////////////////////////////////
    private void loadPlayerSprite() {

        for (int i = 0; i < PLAYER_SPRITE_COUNT; i++) {
            BufferedImage body = ImageReader
                    .fetchImage(String.format("/no/uib/inf112/player/player_move%s.png", i + 1));
            BufferedImage feet = ImageReader
                    .fetchImage(String.format("/no/uib/inf112/player/player_feet%s.png", i + 1));
            this.playerBodySprites.add(body);
            this.playerFeetSprites.add(feet);
        }
    }

    public BufferedImage getPlayerBodySprite(int index) {
        return this.playerBodySprites.get(index);
    }

    public BufferedImage getPlayerFeetSprite(int index) {
        return this.playerFeetSprites.get(index);
    }

    // ////////////////////////////// END PLAYER METHODS //////////////////////////
    // /
    // / //////////////////////////// START WALL METHODS //////////////////////////
    // /

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

    // ////////////// END WALL LOGIC//////////////
    // /
    // ///////////// START STATIC OBJECT LOGIC //////////////
    // /

    private void loadStaticObjects() {
        this.staticObjects.put(StaticObjectType.BEIGE_COUCH_DOWN,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/furniture/beige_couch.png"),
                        Config.getInt("couchWidth"), Config.getInt("couchHeight")));
        this.staticObjects.put(StaticObjectType.BEIGE_COUCH_SMALL,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/furniture/beige_smallcouch.png"),
                        Config.getInt("couchSmallWidth"), Config.getInt("couchSmallHeight")));
        this.staticObjects.put(StaticObjectType.DARK_TABLE_ROUNDED,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/furniture/darkwoodentable.png"),
                        Config.getInt("tableWidth"), Config.getInt("tableHeight")));
        this.staticObjects.put(StaticObjectType.DARK_TABLE_SQUARE,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/furniture/darkwoodentablesquare.png"),
                        Config.getInt("tableWidth"), Config.getInt("tableHeight")));
        this.staticObjects.put(StaticObjectType.WATER,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/water/water.png"),
                        Config.getInt("waterWidth"), Config.getInt("waterHeight")));
        this.staticObjects.put(StaticObjectType.BEIGE_BIG_BED,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/furniture/beige_bigbed.png"),
                        Config.getInt("bigBedWidth"), Config.getInt("bigBedHeight")));
        this.staticObjects.put(StaticObjectType.BEIGE_SMALL_BED,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/furniture/beige_smallbed.png"),
                        Config.getInt("smallBedWidth"), Config.getInt("smallBedHeight")));
        this.staticObjects.put(StaticObjectType.DARK_DRAWER_SMALL,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/furniture/darksmalldrawer.png"),
                        Config.getInt("smallDrawerWidth"), Config.getInt("smallDrawerHeight")));
        this.staticObjects.put(StaticObjectType.DARK_DRAWER_LONG,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/furniture/darklongdrawer.png"),
                        Config.getInt("longDrawerWidth"), Config.getInt("longDrawerHeight")));
        this.staticObjects.put(StaticObjectType.PLANT_ONE,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/furniture/plant_1.png"),
                        Config.getInt("plantWidth"), Config.getInt("plantHeight")));
        this.staticObjects.put(StaticObjectType.PLANT_TWO,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/furniture/plant_2.png"),
                        Config.getInt("plantWidth"), Config.getInt("plantHeight")));
        this.staticObjects.put(StaticObjectType.BEIGE_CHAIR_WOOD,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/furniture/beige_woodchair.png"),
                        Config.getInt("woodChairWidth"), Config.getInt("woodChairHeight")));
    }

    // / /////////// START ENEMY LOGIC //////////////

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

        // Ghoul death
        ArrayList<BufferedImage> ghoulDeath = new ArrayList<>();
        for (int i = 0; i < 6; i++) { //ghoul death has 6 images
            String path = String.format("/no/uib/inf112/npcs/ghoul/Death/death_00%s.png", i);
            BufferedImage rawImage = ImageReader.fetchImage(path);
            ghoulDeath.add(rawImage);
        }
        this.dyingEnemies.put(EnemyType.GHOUL, ghoulDeath);

    }

    public BufferedImage getEnemySprites(EnemyType type, EnemyAction action, int index) {

        return switch (action) {
            case WALK -> this.walkingEnemies.get(type).get(index);

            case ATTACK -> this.attackingEnemies.get(type).get(index);

            case RANGED_ATTACK -> this.rangedAttackingEnemies.get(type).get(index);

            case DEAD -> this.dyingEnemies.get(type).get(index);

            default -> throw new IllegalArgumentException("Illegal argument: " + action);
        };
    }

    // /////////////////// END ENEMY LOGIC ////////////////////
    // /

    // /////////////////// COLLECTABLES LOGIC //////////////////////
    private void loadCollectables() {
        this.collectables.put(CollectableType.HEALTH,
                ImageReader.fetchImage("/no/uib/inf112/map/items/healthBox.png"));
        this.collectables.put(CollectableType.ARMOR,
                ImageReader.fetchImage("/no/uib/inf112/map/items/armorBox.png"));
        this.collectables.put(CollectableType.POWERUP_RAINBOW,
                ImageReader.fetchImage("/no/uib/inf112/map/items/canaryBoost.png"));
        this.collectables.put(CollectableType.POWERUP_SPEED,
                ImageReader.fetchImage("/no/uib/inf112/map/items/speedBoost.png"));
        this.collectables.put(CollectableType.POWERUP_DAMAGE,
                ImageReader.fetchImage("/no/uib/inf112/map/items/damageBoost.png"));
        this.collectables.put(CollectableType.AMMO_PISTOL,
                ImageReader.fetchImage("/no/uib/inf112/map/items/pistolAmmo.png"));
        this.collectables.put(CollectableType.AMMO_RIFLE,
                ImageReader.fetchImage("/no/uib/inf112/map/items/rifleAmmo.png"));
        this.collectables.put(CollectableType.AMMO_SHOTGUN,
                ImageReader.fetchImage("/no/uib/inf112/map/items/shotgunAmmo.png"));



    }

    public BufferedImage getCollectableImage(CollectableType type) {
        return this.collectables.get(type);
    }
    // /////////////////// END COLLECTABLES LOGIC //////////////////////


    // / ////////////////START FLOOR LOGIC ////////////////////
    // /

    private void loadFloors() {
        int width = Config.getInt("tileWidth");
        int height = Config.getInt("tileHeight");
        this.floors.put(FloorType.STONE_TILES,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/floors/stonefloor.png"), width, height));
        this.floors.put(FloorType.GRASS_TILES,
                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/floors/grass_tile.png"), width, height));
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

    public BufferedImage youDied(){
        return this.youDied;
    }
}
