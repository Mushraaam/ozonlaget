package no.uib.inf112.utility;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.*;
import no.uib.inf112.view.LoadStatus;

public class ImageHandler {

        private HashMap<GunType, ArrayList<BufferedImage>> playerBodySprites;
        private HashMap<GunType, ArrayList<BufferedImage>> playerFeetSprites;

        // PlayerSprite (put into hashmap layer?)
        private static final int PLAYER_SPRITE_COUNT = 20;

        // Ghoul sprite
        private static final int GHOUL_ANIMATION_COUNT = 8;

        // Sprinter sprite
        private static final int SPRINTER_ANIMATION_COUNT = 8;

        // Bighands sprite
        private static final int BIGHANDS_ANIMATION_COUNT = 8;

        // Boss sprite
        private static final int BOSS_ANIMATION_COUNT = 8;

        private static final int BUG_ANIMATION_COUNT = 9;

        // Wall images
        private HashMap<StaticObjectType, HashMap<WallDirection, BufferedImage>> walls;

        // Static Objects (not walls)
        private HashMap<StaticObjectType, BufferedImage> staticObjects;

        // Enemy image
        private HashMap<EnemyType, ArrayList<BufferedImage>> walkingEnemies;
        private HashMap<EnemyType, ArrayList<BufferedImage>> attackingEnemies;
        private HashMap<EnemyType, ArrayList<BufferedImage>> rangedAttackingEnemies;
        private HashMap<EnemyType, ArrayList<BufferedImage>> dyingEnemies;
        private HashMap<EnemyType, ArrayList<BufferedImage>> longRangedAttackingEnemies;

        // Helicopter
        private ArrayList<BufferedImage> helicopter;

        // Ground
        private HashMap<FloorType, BufferedImage> floors;
        private HashMap<Integer, BufferedImage> levelBackground;

        // Puddles
        private HashMap<PuddleType, ArrayList<BufferedImage>> puddles;

        // Projectiles
        private HashMap<PuddleType, ArrayList<BufferedImage>> projectiles;

        // UI
        private HashMap<GunType, BufferedImage> gunUI;
        private BufferedImage uiBar;
        private BufferedImage youDied;
        private BufferedImage killCountIcon;

        // Main Menu
        private BufferedImage menuBackground;
        private BufferedImage startButton;
        private BufferedImage menuTitle;
        private BufferedImage settingsButton;
        private BufferedImage helpButton;

        // Help Menu
        private BufferedImage backButton;
        private BufferedImage fartButton;
        private BufferedImage wasdImage;
        private BufferedImage mouseImage;
        private BufferedImage numbersImage;

        // Settings Menu
        private BufferedImage cageImage;

        // Collectables
        private HashMap<CollectableType, BufferedImage> collectables;

        public ImageHandler(LoadStatus status) {
                status.setStatus("Loading player sprites...", 15);
                this.longRangedAttackingEnemies = new HashMap<>();
                this.playerBodySprites = new HashMap<>();
                this.playerFeetSprites = new HashMap<>();
                loadPlayerSprites();

                status.setStatus("Loading terrain...", 20);
                this.walls = new HashMap<>();
                loadWalls();

                this.staticObjects = new HashMap<>();
                loadStaticObjects();

                this.floors = new HashMap<>();
                loadFloors();

                this.levelBackground = new HashMap<>();
                loadBackgrounds();

                status.setStatus("Loading enemy sprites...", 25);
                this.walkingEnemies = new HashMap<>();
                this.attackingEnemies = new HashMap<>();
                this.rangedAttackingEnemies = new HashMap<>();
                this.dyingEnemies = new HashMap<>();
                loadEnemies(status);

                status.setStatus("Loading UI...", 60);
                this.gunUI = new HashMap<>();
                loadGunUI();
                this.uiBar = ImageReader.fetchImage("/no/uib/inf112/UI/ui-bar.png");
                this.youDied = ImageReader.fetchImage("/no/uib/inf112/UI/youdied.png");
                this.killCountIcon = ImageReader.fetchImage("/no/uib/inf112/UI/killcountIcon.png");

                status.setStatus("Loading menus...", 65);
                loadMenu();

                status.setStatus("Loading projectiles...", 70);
                this.puddles = new HashMap<>();
                this.projectiles = new HashMap<>();
                loadPuddles();

                status.setStatus("Loading collectables...", 80);
                this.collectables = new HashMap<>();
                loadCollectables();

                status.setStatus("Loading helicopter...", 85);
                this.helicopter = new ArrayList<>();
                loadHelicopter();
        }

        private void loadHelicopter() {
                for (int i = 0; i < 7; i++) {
                        this.helicopter.add(
                                        ImageReader.fetchImage(
                                                        String.format("/no/uib/inf112/helicopter/heli%s.png", i)));
                }
        }

        // for now, only vehicle is helicopter
        public BufferedImage getVehicleImage(int index) {
                return this.helicopter.get(index);
        }

        // PUDDLES AND PROJECTILES
        private void loadPuddles() {

                // ACID
                ArrayList<BufferedImage> acidPuddles = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                        BufferedImage img = ImageReader
                                        .fetchImage(String.format("/no/uib/inf112/npcs/ghoul/projectile/puddle_%s.png",
                                                        i));
                        acidPuddles.add(img);
                }
                this.puddles.put(PuddleType.ACID, acidPuddles);
                ArrayList<BufferedImage> acidList = new ArrayList<>();
                acidList.add(ImageReader.fetchImage("/no/uib/inf112/npcs/ghoul/projectile/projectile.png"));
                this.projectiles.put(PuddleType.ACID, acidList);

                // MEGABOSS FIREBALL
                ArrayList<BufferedImage> fireSpellFrames = new ArrayList<>();
                for (int i = 0; i < 8; i++) {
                        String path = String.format("/no/uib/inf112/npcs/megaboss/projectile/Fire Spell_Frame_%s.png",
                                        i);
                        fireSpellFrames.add(rotateDeg(ImageReader.fetchImage(path), 180));
                }

                // MEGABOSS EXPLOSION
                ArrayList<BufferedImage> bossExplosion = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                        String path = String.format("/no/uib/inf112/npcs/megaboss/projectile/image_%s.png", i);
                        bossExplosion.add(ImageReader.fetchImage(path));
                }

                this.puddles.put(PuddleType.EXPLOSION, bossExplosion);
                this.projectiles.put(PuddleType.BOSS_FIREBALL, fireSpellFrames);

                // GAS EXPLOSION
                ArrayList<BufferedImage> gasExplosion = new ArrayList<>();
                for (int i = 0; i < 11; i++) {
                        String path = String.format("/no/uib/inf112/npcs/megaboss/projectile/Explosion_%s.png", i);
                        gasExplosion.add(ImageReader.fetchImage(path));
                }
                this.puddles.put(PuddleType.GASEXPLOSION, gasExplosion);

                // MEGABOSS GasBall
                ArrayList<BufferedImage> bugProjectileList = new ArrayList<>();
                for (int i = 0; i < 8; i++) {
                        String path = String.format("/no/uib/inf112/npcs/megaboss/projectile/gas_spell_%s.png", i);
                        bugProjectileList.add(rotateDeg(ImageReader.fetchImage(path), 180));
                }
                this.projectiles.put(PuddleType.BUGPROJECTILE, bugProjectileList);

        }

        public BufferedImage getProjectile(PuddleType type, int tick) {
                ArrayList<BufferedImage> frames = this.projectiles.get(type);
                if (frames.size() == 1) {
                        return frames.get(0);
                }
                int currentFrame = (tick / 4) % frames.size();

                return frames.get(currentFrame);
        }

        public BufferedImage getPuddleImage(PuddleType type, int index, int lifetime) {

                if (type == PuddleType.EXPLOSION || type == PuddleType.GASEXPLOSION) {
                        int totalImages = this.puddles.get(type).size();
                        int frame = (int) (((double) index / lifetime) * totalImages);
                        frame = Math.min(frame, totalImages - 1);

                        return this.puddles.get(type).get(frame);
                }
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
                                ImageReader.resizeExact(
                                                ImageReader.fetchImage("/no/uib/inf112/UI/gun_icons/DEagle.png"), w,
                                                h));
                this.gunUI.put(GunType.MP5,
                                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/UI/gun_icons/MP5.png"),
                                                w, h));
                this.gunUI.put(GunType.SHOTGUN,
                                ImageReader.resizeExact(
                                                ImageReader.fetchImage("/no/uib/inf112/UI/gun_icons/shotgun.png"), w,
                                                h));

        }

        public BufferedImage getGunImage(GunType type) {

                return this.gunUI.getOrDefault(type, this.gunUI.get(GunType.DEAGLE));
        }

        private void loadBackgrounds() {

                this.levelBackground.put(1,
                                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/map/ground_level1.png"),
                                                Config.getInt("mapWidth"), Config.getInt("mapHeight")));
                this.levelBackground.put(2,
                                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/map/city_grid.png"),
                                                Config.getInt("mapWidth"), Config.getInt("mapHeight")));
        }

        public BufferedImage getBackground(int level) {
                return this.levelBackground.get(level);
        }

        // ////////////////////// PLAYER METHODS //////////////////////////////////
        private void loadPlayerSprites() {
                loadPlayerSprite(GunType.DEAGLE, "handgun");
                loadPlayerSprite(GunType.MP5, "rifle");
                loadPlayerSprite(GunType.SHOTGUN, "shotgun");
        }

        private void loadPlayerSprite(GunType gunType, String folder) {
                ArrayList<BufferedImage> playerbodySprites = new ArrayList<>();
                ArrayList<BufferedImage> playerfeetSprites = new ArrayList<>();

                for (int i = 0; i < PLAYER_SPRITE_COUNT; i++) {
                        BufferedImage bodySprite = ImageReader.fetchImage(
                                        String.format("/no/uib/inf112/player/%s/player_move%s.png", folder, i + 1));

                        BufferedImage feetSprite = ImageReader.fetchImage(
                                        String.format("/no/uib/inf112/player/handgun/player_feet%s.png", i + 1));

                        playerbodySprites.add(bodySprite);
                        playerfeetSprites.add(feetSprite);

                }

                this.playerBodySprites.put(gunType, playerbodySprites);
                this.playerFeetSprites.put(gunType, playerfeetSprites);

        }

        public BufferedImage getPlayerBodySprite(GunType gunType, int index) {
                return this.playerBodySprites.get(gunType).get(index);
        }

        public BufferedImage getPlayerFeetSprite(GunType gunType, int index) {
                return this.playerFeetSprites.get(gunType).get(index);
        }

        // ////////////////////////////// END PLAYER METHODS //////////////////////////
        // /
        // / //////////////////////////// START WALL METHODS //////////////////////////
        // /

        private void loadWalls() {

                HashMap<WallDirection, BufferedImage> shortWoodenWalls = new HashMap<>();
                shortWoodenWalls.put(WallDirection.HORIZONTAL,
                                ImageReader.fetchImage("/no/uib/inf112/walls/ShortWall1_1.png"));
                shortWoodenWalls.put(WallDirection.VERTICAL,
                                ImageReader.fetchImage("/no/uib/inf112/walls/ShortWall1_2.png"));

                HashMap<WallDirection, BufferedImage> longWoodenWalls = new HashMap<>();
                longWoodenWalls.put(WallDirection.VERTICAL,
                                ImageReader.fetchImage("/no/uib/inf112/walls/LongWall1_1.png"));
                longWoodenWalls.put(WallDirection.HORIZONTAL,
                                ImageReader.fetchImage("/no/uib/inf112/walls/LongWall1_2.png"));

                HashMap<WallDirection, BufferedImage> barbedFences = new HashMap<>();
                // these are somehow flipped
                barbedFences.put(WallDirection.VERTICAL,
                                ImageReader.fetchImage("/no/uib/inf112/walls/barbedFenceHorizontal.png"));
                barbedFences.put(WallDirection.HORIZONTAL,
                                ImageReader.fetchImage("/no/uib/inf112/walls/barbedFenceVertical.png"));

                HashMap<WallDirection, BufferedImage> gateWall = new HashMap<>();
                gateWall.put(WallDirection.HORIZONTAL,
                                ImageReader.fetchImage("/no/uib/inf112/walls/GateWallHorizontal.png"));
                gateWall.put(WallDirection.VERTICAL,
                                ImageReader.fetchImage("/no/uib/inf112/walls/GateWallVertical.png"));

                this.walls.put(StaticObjectType.WOODEN_WALL, shortWoodenWalls);
                this.walls.put(StaticObjectType.LONG_WOODEN_WALL, longWoodenWalls);
                this.walls.put(StaticObjectType.BARBED_FENCE, barbedFences);

                // TODO fix correct door image
                this.walls.put(StaticObjectType.BARBED_DOOR, gateWall);

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
                                ImageReader.resizeExact(
                                                ImageReader.fetchImage("/no/uib/inf112/furniture/beige_couch.png"),
                                                Config.getInt("couchWidth"), Config.getInt("couchHeight")));
                this.staticObjects.put(StaticObjectType.BEIGE_COUCH_SMALL,
                                ImageReader.resizeExact(
                                                ImageReader.fetchImage("/no/uib/inf112/furniture/beige_smallcouch.png"),
                                                Config.getInt("couchSmallWidth"), Config.getInt("couchSmallHeight")));
                this.staticObjects.put(StaticObjectType.DARK_TABLE_ROUNDED,
                                ImageReader.resizeExact(
                                                ImageReader.fetchImage("/no/uib/inf112/furniture/darkwoodentable.png"),
                                                Config.getInt("tableWidth"), Config.getInt("tableHeight")));
                this.staticObjects.put(StaticObjectType.DARK_TABLE_SQUARE,
                                ImageReader.resizeExact(
                                                ImageReader.fetchImage(
                                                                "/no/uib/inf112/furniture/darkwoodentablesquare.png"),
                                                Config.getInt("tableWidth"), Config.getInt("tableHeight")));
                this.staticObjects.put(StaticObjectType.DARK_TABLE_SMALL,
                                ImageReader.resizeExact(
                                                ImageReader.fetchImage(
                                                                "/no/uib/inf112/furniture/darksmallwoodentable.png"),
                                                Config.getInt("smallTableWidth"), Config.getInt("smallTableHeight")));
                this.staticObjects.put(StaticObjectType.WATER,
                                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/water/water.png"),
                                                Config.getInt("waterWidth"), Config.getInt("waterHeight")));
                this.staticObjects.put(StaticObjectType.BEIGE_BIG_BED,
                                ImageReader.resizeExact(
                                                ImageReader.fetchImage("/no/uib/inf112/furniture/beige_bigbed.png"),
                                                Config.getInt("bigBedWidth"), Config.getInt("bigBedHeight")));
                this.staticObjects.put(StaticObjectType.BEIGE_SMALL_BED,
                                ImageReader.resizeExact(
                                                ImageReader.fetchImage("/no/uib/inf112/furniture/beige_smallbed.png"),
                                                Config.getInt("smallBedWidth"), Config.getInt("smallBedHeight")));
                this.staticObjects.put(StaticObjectType.DARK_DRAWER_SMALL,
                                ImageReader.resizeExact(
                                                ImageReader.fetchImage("/no/uib/inf112/furniture/darksmalldrawer.png"),
                                                Config.getInt("smallDrawerWidth"), Config.getInt("smallDrawerHeight")));
                this.staticObjects.put(StaticObjectType.DARK_DRAWER_SMALL_UP,
                                ImageReader.resizeExact(
                                                ImageReader.fetchImage(
                                                                "/no/uib/inf112/furniture/darksmalldrawerup.png"),
                                                Config.getInt("smallDrawerWidth"), Config.getInt("smallDrawerHeight")));
                this.staticObjects.put(StaticObjectType.DARK_DRAWER_LONG,
                                ImageReader.resizeExact(
                                                ImageReader.fetchImage("/no/uib/inf112/furniture/darklongdrawer.png"),
                                                Config.getInt("longDrawerWidth"), Config.getInt("longDrawerHeight")));
                this.staticObjects.put(StaticObjectType.PLANT_ONE,
                                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/furniture/plant_1.png"),
                                                Config.getInt("plantWidth"), Config.getInt("plantHeight")));
                this.staticObjects.put(StaticObjectType.PLANT_TWO,
                                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/furniture/plant_2.png"),
                                                Config.getInt("plantWidth"), Config.getInt("plantHeight")));
                this.staticObjects.put(StaticObjectType.BEIGE_CHAIR_WOOD_DOWN,
                                ImageReader.resizeExact(
                                                ImageReader.fetchImage(
                                                                "/no/uib/inf112/furniture/beige_woodchairdown.png"),
                                                Config.getInt("woodChairWidth"), Config.getInt("woodChairHeight")));
                this.staticObjects.put(StaticObjectType.BEIGE_CHAIR_WOOD_UP,
                                ImageReader.resizeExact(
                                                ImageReader.fetchImage(
                                                                "/no/uib/inf112/furniture/beige_woodchairup.png"),
                                                Config.getInt("woodChairWidth"), Config.getInt("woodChairHeight")));
                this.staticObjects.put(StaticObjectType.GREY_TV,
                                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/furniture/grey_tv.png"),
                                                Config.getInt("tvWidth"), Config.getInt("tvHeight")));
                this.staticObjects.put(StaticObjectType.GREY_TV_LEFT,
                                ImageReader.resizeExact(
                                                ImageReader.fetchImage("/no/uib/inf112/furniture/grey_tv_left.png"),
                                                Config.getInt("tvWidth"), Config.getInt("tvHeight")));
                this.staticObjects.put(StaticObjectType.RED_CHAIR_LEFT,
                                ImageReader.resizeExact(
                                                ImageReader.fetchImage("/no/uib/inf112/furniture/red_chairleft.png"),
                                                Config.getInt("redChairWidth"), Config.getInt("redChairHeight")));
                this.staticObjects.put(StaticObjectType.RED_CHAIR_RIGHT,
                                ImageReader.resizeExact(
                                                ImageReader.fetchImage("/no/uib/inf112/furniture/red_chairright.png"),
                                                Config.getInt("redChairWidth"), Config.getInt("redChairHeight")));
        }

        // / /////////// START ENEMY LOGIC //////////////

        private void loadEnemies(LoadStatus status) {

                ////////////
                // GHOUL
                // Walk
                ArrayList<BufferedImage> ghoulWalk = new ArrayList<>();
                for (int i = 0; i < GHOUL_ANIMATION_COUNT; i++) {
                        String path = String.format("/no/uib/inf112/npcs/ghoul/Walk/walk_00%s.png", i);
                        BufferedImage rawImage = ImageReader.fetchImage(path);
                        ghoulWalk.add(rawImage);
                }
                this.walkingEnemies.put(EnemyType.GHOUL, ghoulWalk);

                // Melee
                ArrayList<BufferedImage> ghoulMelee = new ArrayList<>();
                for (int i = 0; i < GHOUL_ANIMATION_COUNT; i++) {
                        String path = String.format("/no/uib/inf112/npcs/ghoul/Attack/Attack_00%s.png", i);
                        BufferedImage rawImage = ImageReader.fetchImage(path);
                        ghoulMelee.add(rawImage);
                }
                this.attackingEnemies.put(EnemyType.GHOUL, ghoulMelee);

                // Ranged
                ArrayList<BufferedImage> ghoulRanged = new ArrayList<>();
                for (int i = 0; i < GHOUL_ANIMATION_COUNT; i++) {
                        String path = String.format("/no/uib/inf112/npcs/ghoul/rangedGhoul/Attack2_00%s.png", i);
                        BufferedImage rawImage = ImageReader.fetchImage(path);
                        ghoulRanged.add(rawImage);
                }
                this.rangedAttackingEnemies.put(EnemyType.GHOUL, ghoulRanged);

                // Death
                ArrayList<BufferedImage> ghoulDeath = new ArrayList<>();
                for (int i = 0; i < 6; i++) { // ghoul death has 6 images
                        String path = String.format("/no/uib/inf112/npcs/ghoul/Death/death_00%s.png", i);
                        BufferedImage rawImage = ImageReader.fetchImage(path);
                        ghoulDeath.add(rawImage);
                }
                this.dyingEnemies.put(EnemyType.GHOUL, ghoulDeath);
                status.setStatus("Loading enemy sprites...", 30);
                ////////////

                ////////////
                // SPRINTER
                // Walk
                ArrayList<BufferedImage> sprinterWalk = new ArrayList<>();
                for (int i = 0; i < SPRINTER_ANIMATION_COUNT; i++) {
                        String path = String.format("/no/uib/inf112/npcs/sprinter/Walk/Walk_00%s.png", i);
                        BufferedImage rawImage = ImageReader.fetchImage(path);
                        sprinterWalk.add(rawImage);
                }
                this.walkingEnemies.put(EnemyType.SPRINTER, sprinterWalk);

                // Melee
                ArrayList<BufferedImage> sprinterMelee = new ArrayList<>();
                for (int i = 0; i < 14; i++) { // sprinter melee has 14 images
                        String path = String.format("/no/uib/inf112/npcs/sprinter/Attack/Attack_00%s.png", i);
                        BufferedImage rawImage = ImageReader.fetchImage(path);
                        sprinterMelee.add(rawImage);
                }
                this.attackingEnemies.put(EnemyType.SPRINTER, sprinterMelee);

                // Death
                ArrayList<BufferedImage> sprinterDeath = new ArrayList<>();
                for (int i = 0; i < 10; i++) { // sprinter death has 10 images
                        String path = String.format("/no/uib/inf112/npcs/sprinter/Death/Death_00%s.png", i);
                        BufferedImage rawImage = ImageReader.fetchImage(path);
                        sprinterDeath.add(rawImage);
                }
                this.dyingEnemies.put(EnemyType.SPRINTER, sprinterDeath);
                status.setStatus("Loading enemy sprites...", 40);
                ////////////

                ////////////
                // BIGHANDS
                // Walk
                ArrayList<BufferedImage> bigHandsWalk = new ArrayList<>();
                for (int i = 0; i < BIGHANDS_ANIMATION_COUNT; i++) {
                        String path = String.format("/no/uib/inf112/npcs/Zombie_big_hands/Walk/walk_00%s.png", i);
                        BufferedImage rawImage = ImageReader.fetchImage(path);
                        bigHandsWalk.add(rawImage);
                }
                this.walkingEnemies.put(EnemyType.BIGHANDS, bigHandsWalk);

                // Melee
                ArrayList<BufferedImage> bigHandsMelee = new ArrayList<>();
                for (int i = 0; i < BIGHANDS_ANIMATION_COUNT; i++) {
                        String path = String.format("/no/uib/inf112/npcs/Zombie_big_hands/Attack/attack_00%s.png", i);
                        BufferedImage rawImage = ImageReader.fetchImage(path);
                        bigHandsMelee.add(rawImage);
                }
                this.attackingEnemies.put(EnemyType.BIGHANDS, bigHandsMelee);

                // Death
                ArrayList<BufferedImage> bigHandsDeath = new ArrayList<>();
                for (int i = 0; i < 6; i++) { // bigHands death has 6 images
                        String path = String.format("/no/uib/inf112/npcs/Zombie_big_hands/Death/Death_00%s.png", i);
                        BufferedImage rawImage = ImageReader.fetchImage(path);
                        bigHandsDeath.add(rawImage);
                }
                this.dyingEnemies.put(EnemyType.BIGHANDS, bigHandsDeath);
                status.setStatus("Loading enemy sprites...", 50);
                ////////////

                ////////////
                // MEGABOSS
                // Walk
                ArrayList<BufferedImage> bossWalk = new ArrayList<>();
                for (int i = 0; i < BOSS_ANIMATION_COUNT; i++) {
                        String path = String.format("/no/uib/inf112/npcs/megaboss/Walk/Walk_%s.png", i);
                        bossWalk.add(rotateDeg(ImageReader.fetchImage(path), -90));
                }
                this.walkingEnemies.put(EnemyType.MEGABOSS, bossWalk);

                // Melee (Attack 1)
                ArrayList<BufferedImage> bossMelee = new ArrayList<>();
                for (int i = 0; i < BOSS_ANIMATION_COUNT; i++) {
                        String path = String.format("/no/uib/inf112/npcs/megaboss/Attack1/attack1_%s.png", i);
                        bossMelee.add(rotateDeg(ImageReader.fetchImage(path), -90));
                }
                this.attackingEnemies.put(EnemyType.MEGABOSS, bossMelee);

                // Medium Ranged (Attack 2)
                ArrayList<BufferedImage> bossMediumRanged = new ArrayList<>();
                for (int i = 0; i < BOSS_ANIMATION_COUNT; i++) {
                        String path = String.format("/no/uib/inf112/npcs/megaboss/Attack2/Attack2_%s.png", i);
                        bossMediumRanged.add(rotateDeg(ImageReader.fetchImage(path), -90));
                }
                this.rangedAttackingEnemies.put(EnemyType.MEGABOSS, bossMediumRanged);

                // Long Ranged (Attack 4)
                ArrayList<BufferedImage> bossLongRanged = new ArrayList<>();
                for (int i = 0; i < BOSS_ANIMATION_COUNT; i++) {
                        String path = String.format("/no/uib/inf112/npcs/megaboss/Attack4/Attack4_%s.png", i);
                        bossLongRanged.add(rotateDeg(ImageReader.fetchImage(path), -90));
                }
                this.longRangedAttackingEnemies.put(EnemyType.MEGABOSS, bossLongRanged);

                // Death
                ArrayList<BufferedImage> bossDeath = new ArrayList<>();
                for (int i = 0; i < 14; i++) { // Boss death has 14 images
                        String path = String.format("/no/uib/inf112/npcs/megaboss/Death/Death_%s.png", i);
                        bossDeath.add(rotateDeg(ImageReader.fetchImage(path), -90));
                }
                this.dyingEnemies.put(EnemyType.MEGABOSS, bossDeath);
                status.setStatus("Loading enemy sprites...", 55);
                ////////////
                // BUG
                // Walk
                ArrayList<BufferedImage> bugWalk = new ArrayList<>();
                for (int i = 0; i < BUG_ANIMATION_COUNT; i++) {
                        String path = String.format("/no/uib/inf112/npcs/jihadbug/bug_walk%s.png", i);
                        BufferedImage rawImage = ImageReader.fetchImage(path);
                        bugWalk.add(rotateDeg(rawImage, 90));
                }
                this.walkingEnemies.put(EnemyType.BUG, bugWalk);

                // its a basic bug, dont bully him
                this.attackingEnemies.put(EnemyType.BUG, bugWalk);
                this.rangedAttackingEnemies.put(EnemyType.BUG, bugWalk);
                this.dyingEnemies.put(EnemyType.BUG, bugWalk);
                /// //////
        }

        public BufferedImage getEnemySprites(EnemyType type, EnemyAction action, int index) {

                return switch (action) {
                        case WALK -> this.walkingEnemies.get(type).get(index);

                        case ATTACK -> this.attackingEnemies.get(type).get(index);

                        case RANGED_ATTACK -> this.rangedAttackingEnemies.get(type).get(index);

                        case DEAD -> this.dyingEnemies.get(type).get(index);

                        case LONG_RANGED_ATTACK -> this.longRangedAttackingEnemies.get(type).get(index);

                        default -> throw new IllegalArgumentException("Illegal argument: " + action);
                };
        }

        // /////////////////// END ENEMY LOGIC ////////////////////
        // /

        // /////////////////// COLLECTABLES LOGIC //////////////////////
        private void loadCollectables() {
                int buffW = 60;
                int buffH = 47;
                // BUFFS
                this.collectables.put(CollectableType.HEALTH,
                                ImageReader.resizeExact(
                                                ImageReader.fetchImage("/no/uib/inf112/map/items/healthBox.png"), buffW,
                                                buffH));
                this.collectables.put(CollectableType.ARMOR,
                                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/map/items/armorBox.png"),
                                                buffW, buffH));
                this.collectables.put(CollectableType.POWERUP_RAINBOW,
                                ImageReader.resizeExact(
                                                ImageReader.fetchImage("/no/uib/inf112/map/items/canaryBoost.png"),
                                                buffW, buffH));
                this.collectables.put(CollectableType.POWERUP_SPEED,
                                ImageReader.resizeExact(
                                                ImageReader.fetchImage("/no/uib/inf112/map/items/speedBoost.png"),
                                                buffW, buffH));
                this.collectables.put(CollectableType.POWERUP_DAMAGE,
                                ImageReader.resizeExact(
                                                ImageReader.fetchImage("/no/uib/inf112/map/items/damageBoost.png"),
                                                buffW, buffH));
                this.collectables.put(CollectableType.AMMO_PISTOL,
                                ImageReader.resizeExact(
                                                ImageReader.fetchImage("/no/uib/inf112/map/items/pistolAmmo.png"),
                                                buffW, buffH));
                this.collectables.put(CollectableType.AMMO_RIFLE,
                                ImageReader.resizeExact(
                                                ImageReader.fetchImage("/no/uib/inf112/map/items/rifleAmmo.png"), buffW,
                                                buffH));
                this.collectables.put(CollectableType.AMMO_SHOTGUN,
                                ImageReader.resizeExact(
                                                ImageReader.fetchImage("/no/uib/inf112/map/items/shotgunAmmo.png"),
                                                buffW, buffH));

                // INVENTORY SPECIFIC ITEMS

                this.collectables.put(CollectableType.GATE_KEY,
                                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/map/items/gateKey.png"),
                                                60, 53));
                this.collectables.put(CollectableType.CHOPPER_KEYCARD,
                                ImageReader.resizeExact(
                                                ImageReader.fetchImage("/no/uib/inf112/map/items/chopperKey.png"), 50,
                                                34));
                this.collectables.put(CollectableType.GASCAN,
                                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/map/items/gasCan.png"),
                                                30, 62));

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
                                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/floors/stonefloor.png"),
                                                width, height));
                this.floors.put(FloorType.GRASS_TILES,
                                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/floors/grass_tile.png"),
                                                width, height));
                this.floors.put(FloorType.WOODFLOOR,
                                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/floors/woodfloor.png"),
                                                width, height));
                this.floors.put(FloorType.ROCK_ROAD,
                                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/floors/rock_road.png"),
                                                width, height));
                this.floors.put(FloorType.GRAY_TILE,
                                ImageReader.resizeExact(ImageReader.fetchImage("/no/uib/inf112/floors/graytile.png"),
                                                width, height));

        }

        // MENY LOGIC
        private void loadMenu() {
                this.menuBackground = ImageReader.fetchImage("/no/uib/inf112/mainmenu/menu_background.png");
                this.startButton = ImageReader.fetchImage("/no/uib/inf112/mainmenu/start_button.png");
                this.menuTitle = ImageReader.fetchImage("/no/uib/inf112/mainmenu/menu_title.png");
                this.settingsButton = ImageReader.fetchImage("/no/uib/inf112/mainmenu/settings_button.png");
                this.helpButton = ImageReader.fetchImage("/no/uib/inf112/mainmenu/help_button.png");
                this.backButton = ImageReader.fetchImage("/no/uib/inf112/mainmenu/backButton.png");
                this.fartButton = ImageReader.fetchImage("/no/uib/inf112/mainmenu/fartButton.png");
                this.wasdImage = ImageReader.fetchImage("/no/uib/inf112/mainmenu/controls.png");
                this.mouseImage = ImageReader.fetchImage("/no/uib/inf112/mainmenu/mouse.png");
                this.numbersImage = ImageReader.fetchImage("/no/uib/inf112/mainmenu/numbers.png");
                this.cageImage = ImageReader.fetchImage("/no/uib/inf112/mainmenu/cage.png");
        }

        // some gippity code to rotate images instead of manually editing 60 .png
        // sprites
        public static BufferedImage rotateDeg(BufferedImage src, int deg) {
                int w = src.getWidth();
                int h = src.getHeight();
                BufferedImage dest = new BufferedImage(w, h, src.getType());
                Graphics2D g2 = dest.createGraphics();

                // Rotate deg degrees around the center of the image
                g2.rotate(Math.toRadians(deg), w / 2.0, h / 2.0);

                g2.drawImage(src, 0, 0, null);
                g2.dispose();
                return dest;
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

        public BufferedImage getbackButton() {
                return this.backButton;
        }

        public BufferedImage getfartButton() {
                return this.fartButton;
        }

        public BufferedImage youDied() {
                return this.youDied;
        }

        public BufferedImage getKillCountIcon() {
                return this.killCountIcon;
        }

        public BufferedImage getWASD() {
                return this.wasdImage;
        }

        public BufferedImage getMouseImage() {
                return this.mouseImage;
        }

        public BufferedImage getNumbersImage() {
                return this.numbersImage;
        }

        public BufferedImage getCage() {
                return this.cageImage;
        }

}
