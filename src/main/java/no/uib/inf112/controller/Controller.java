package no.uib.inf112.controller;

import no.uib.inf112.enums.BuffType;
import no.uib.inf112.enums.Direction;
import no.uib.inf112.enums.GameState;
import no.uib.inf112.enums.GunType;
import no.uib.inf112.interfaces.IControllablePlayer;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.interfaces.IGunShot;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IProjectile;
import no.uib.inf112.interfaces.IPuddle;
import no.uib.inf112.map.items.factory.ItemFactory;
import no.uib.inf112.map.npcs.factory.Factory;
import no.uib.inf112.map.npcs.projectiles.puddles.AcidPuddle;
import no.uib.inf112.view.GameDrawer;
import no.uib.inf112.utility.PerfTracker;
import no.uib.inf112.utility.SoundHandler;
import no.uib.inf112.utility.Camera;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.awt.event.MouseEvent;
import javax.swing.Timer;
import javax.swing.SwingUtilities;

public class Controller
        implements java.awt.event.KeyListener, java.awt.event.MouseMotionListener, java.awt.event.MouseListener {

    private final Camera camera;

    private Factory factory;
    private Timer tickTimer;
    private ItemFactory itemFactory;

    private IMap map;
    private IControllablePlayer player;
    private GameDrawer view;
    private Timer playerAnimationTimer;
    private Timer movementTimer;
    private Timer pathFindingTimer;
    private IGrid grid;
    private Timer gunshotTimer;
    private int fireRate;
    private Timer shootTimer;
    private MouseEvent lastMouseEvent;

    private Timer AOETimer;
    private static SoundHandler soundHandler; //TODO make this not static, or change its place?

    //TODO remove this, integrate it better so it can follow items and buffs.
    public static SoundHandler getSoundHandler() {
        return soundHandler;
    }


    // Executor for pathfinding
    private final ExecutorService pathExecutor;
    private volatile boolean pathfindingRunning;
    private final ExecutorService drawExecutor;
    private volatile boolean drawRunning;

    // test 60fps
    private Timer repaintTimer;

    private ArrayList<Timer> timers;

    /**
     * The main controller for the game. It handles user input via the keyboard
     * and manages game loops using Swing Timers for movement, animation,
     * and pathfinding.
     */
    public Controller(IMap map, GameDrawer view) {
        this.map = map;
        this.player = (IControllablePlayer) map.getPlayer();
        this.view = view;
        this.grid = map.getGrid();
        this.fireRate = this.player.fireRate();

        this.camera = new Camera(0, 0);
        this.soundHandler = new SoundHandler();

        this.view.addKeyListener(this);
        this.view.addMouseMotionListener(this);
        this.view.addMouseListener(this);
        this.view.setFocusable(true);
        this.factory = map.getFactory();
        this.itemFactory = map.getItemFactory();

        //
        this.pathExecutor = Executors.newSingleThreadExecutor();
        this.pathfindingRunning = false;
        this.drawExecutor = Executors.newSingleThreadExecutor();
        this.drawRunning = false;

        // TIMERS
        this.playerAnimationTimer = new Timer(50, (ActionEvent e) -> {
            if (player.isMoving()) {
                this.player.incrementAnimationIndex();
            }
            for (IEnemy enemy : map.getEnemies()) {
                enemy.incrementAnimationIndex();
            }
        });

        this.pathFindingTimer = new Timer(600, (ActionEvent e) -> {
            if (pathfindingRunning) {
                return;
            }
            ArrayList<IEnemy> enemies = this.map.getEnemies();
            pathfindingRunning = true;
            pathExecutor.submit(() -> {
                try {
                    PerfTracker.start("Pathfinding");
                    this.map.gatherOccupiedCells();

                    for (IEnemy enemy : enemies) {
                        enemy.requestPath(map.getGrid(), map.getPathfinder(), player.getHitbox(), true);
                    }

                    this.map.resetOccupied();
                    PerfTracker.stop("Pathfinding");
                } finally {
                    pathfindingRunning = false;
                }
            });
        });

        this.tickTimer = new Timer(600, e -> {
            // Increment factory spawn
            this.factory.increment();
            this.itemFactory.increment();

            // handle buff countdowns
            this.player.decrementBuff(this.soundHandler);
        });

        this.movementTimer = new Timer(16, e -> {
            PerfTracker.tick(false);
            PerfTracker.start("Movement Logic");

            this.player.updateMovement();

            ArrayList<IEnemy> enemies = map.getEnemies();
            for (IEnemy enemy : enemies) {
                enemy.move(grid);
            }

            // puddles and projectiles also in movement for now

            ArrayList<IPuddle> puddles = map.getAOEPuddles();
            for (IPuddle puddle : puddles) {
                puddle.incrementAnimationIndex();
            }
            ArrayList<IProjectile> projectiles = map.getProjectiles();
            for (IProjectile projectile : projectiles) {
                projectile.move();
            }
            PerfTracker.stop("Movement Logic");

        });

        this.repaintTimer = new Timer(8, e -> {
            if (this.drawRunning) {
                return;
            }
            this.drawRunning = true;
            this.drawExecutor.submit(() -> {
                try {
                    this.view.repaint();

                } finally {
                    this.drawRunning = false;
                }
            });
        });
        this.repaintTimer.start();

        this.gunshotTimer = new Timer(5, e -> {
            for (IGunShot shot : this.map.gunShots()) {
                shot.reduceLifeTime();
            }
        });

        this.shootTimer = new Timer(this.fireRate, e -> {
            player.shoot(this.lastMouseEvent);
            soundHandler.playGunShot(player.gunType());

        });

        this.AOETimer = new Timer(500, e -> {
            ArrayList<IPuddle> puddles = map.getAOEPuddles();
            for (IPuddle puddle : puddles) {
                puddle.dealDamage();
            }
        });

        this.timers = new ArrayList<>();
        this.timers.add(playerAnimationTimer);
        this.timers.add(pathFindingTimer);
        this.timers.add(movementTimer);
        this.timers.add(gunshotTimer);
        this.timers.add(AOETimer);
        this.timers.add(tickTimer);

        this.repaintTimer.start();
        applyTimers(map.getGameState());
        soundHandler.playMusic(map.getGameState());
    }

    // STOP AND START TIMERS
    private void applyTimers(GameState state) {
        for (Timer t : timers) {
            if (t != null && t.isRunning())
                t.stop();
        }
        switch (state) {
            case MAIN_MENU -> {

            }
            case ACTIVE_GAME -> {
                playerAnimationTimer.start();
                pathFindingTimer.start();
                movementTimer.start();
                AOETimer.start();
                this.tickTimer.start();
                this.gunshotTimer.start();
            }
            default -> {

            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (this.map.getGameState()) {

            case MAIN_MENU -> {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    changeState(GameState.ACTIVE_GAME);
                } else if (e.getKeyCode() == KeyEvent.VK_R) {
                    view.getMainMenu().resetAnimation();
                }
            }

            case ACTIVE_GAME -> {
                activeGamePressEvent(e);
            }

            default -> {
            }
        }

    }

    // GAMESTATE BOUND KEY EVENTS FOR KEY PRESSED
    private void activeGamePressEvent(KeyEvent e) {
        switch (e.getKeyCode()) {
            // movement
            case KeyEvent.VK_W -> {
                player.pressMove(Direction.NORTH);
            }
            case KeyEvent.VK_S -> {
                player.pressMove(Direction.SOUTH);
            }
            case KeyEvent.VK_A -> {
                player.pressMove(Direction.WEST);
            }
            case KeyEvent.VK_D -> {
                player.pressMove(Direction.EAST);
            }

            case KeyEvent.VK_1 -> {
                this.player.setGunType(GunType.DEAGLE);
                this.shootTimer.setDelay(this.player.fireRate());
            }
            case KeyEvent.VK_2 -> {
                this.player.setGunType(GunType.MP5);
                this.shootTimer.setDelay(this.player.fireRate());
            }

            case KeyEvent.VK_I -> {
                if (this.map.debugMode()) {
                    this.player.takeDamage(10);
                }
            }

            case KeyEvent.VK_M -> {
                memoryDebug();
            }

            case KeyEvent.VK_P -> {
                flipDebug();
            }
            case KeyEvent.VK_O -> {
                this.player.setBuff(BuffType.RAINBOW, this.soundHandler);
            }
            case KeyEvent.VK_L -> {
                // place puddle on player
                this.map.addAOEPuddle(new AcidPuddle(this.player.getHitbox(), this.map));
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (this.map.getGameState()) {

            case ACTIVE_GAME -> {
                activeGameReleaseEvent(e);
            }

            default -> {
            }
        }
    }

    // GAMESTATE BOUND KEY EVENTS FOR KEY RELEASED
    private void activeGameReleaseEvent(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> {
                player.releaseMove(Direction.NORTH);
            }
            case KeyEvent.VK_S -> {
                player.releaseMove(Direction.SOUTH);
            }
            case KeyEvent.VK_A -> {
                player.releaseMove(Direction.WEST);
            }
            case KeyEvent.VK_D -> {
                player.releaseMove(Direction.EAST);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

        // not implemented
    }

    /// ///////////// HELPER METHODS - THESE SHOULD BE SHORT AND SELF EXPLANATORY
    /// /////////////// Maybe move the helpers to their classes, at a later
    /// occasion.
    /// e.g map.flipDebug()

    private void flipDebug() {
        if (map.debugMode()) {
            map.debugOff();
        } else {
            map.debugOn();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.lastMouseEvent = e;
        this.fireRate = this.player.fireRate();
        switch (this.map.getGameState()) {

            case ACTIVE_GAME -> {
                if (!this.shootTimer.isRunning()) {

                    this.player.shoot(e); // Shoot once then start timer
                    soundHandler.playGunShot(player.gunType());

                    this.shootTimer.start();
                }
            }

            case MAIN_MENU -> {
                mainMenuMousePressEvent(e);
            }

            default -> {
            }
        }
    }

    private void mainMenuMousePressEvent(MouseEvent e) {

        if (!SwingUtilities.isLeftMouseButton(e)) {
            return;
        }

        Point p = e.getPoint();
        var startButton = view.getMainMenu().getStartButton();
        if (startButton != null && startButton.contains(p)) {
            changeState(GameState.ACTIVE_GAME);
            return;
        }
        var helpButton = view.getMainMenu().getHelpButton();
        if (startButton != null && helpButton.contains(p)) {
            changeState(GameState.HELP);
        }
    }

    private void changeState(GameState state) {
        soundHandler.playMusic(state);
        map.setGameState(state);
        applyTimers(state);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (this.shootTimer.isRunning()) {
            this.shootTimer.stop();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
        this.lastMouseEvent = e;
        updateAimFromMouse(e);
    }

    @Override
    public void mouseDragged(java.awt.event.MouseEvent e) {
        this.lastMouseEvent = e;
        updateAimFromMouse(e);
    }

    // This method converts the mouse position to world coordinates and updates the
    // player's aim accordingly. It also recenters the camera on the player.
    private void updateAimFromMouse(java.awt.event.MouseEvent e) {
        camera.update(player.getHitbox(), view.getWidth(), view.getHeight(), map.getBounds());

        var worldMouse = camera.screenToWorld(e.getX(), e.getY());

        player.aimAtWorldPosition(worldMouse.x, worldMouse.y);

    }

    /* used for debugging memory usage */
    private void memoryDebug() {
        Runtime rt = Runtime.getRuntime();

        long used = rt.totalMemory() - rt.freeMemory();
        long committed = rt.totalMemory();
        long max = rt.maxMemory();

        int percentage_used = (int) ((used * 100) / max);

        System.out.println(String.format(
                "Used memory: %s \nMax memory: %s \nPercentage used: %s%%\nCommitted Memory: %s",
                used, max, percentage_used, committed));
    }
}
