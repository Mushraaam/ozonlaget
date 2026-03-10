package no.uib.inf112.controller;

import no.uib.inf112.enums.Direction;
import no.uib.inf112.enums.GameState;
import no.uib.inf112.enums.GunType;
import no.uib.inf112.interfaces.IControllablePlayer;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.interfaces.IGunShot;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.view.GameDrawer;
import no.uib.inf112.utility.PerfTracker;
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
    private final ExecutorService pathExecutor;
    private IMap map;
    private IControllablePlayer player;
    private GameDrawer view;
    private Timer movementTimer;
    private Timer pathFindingTimer;
    private IGrid grid;
    private int fireRate;
    private Timer shootTimer;
    private MouseEvent lastMouseEvent;
    private volatile boolean pathfindingRunning;

    private ArrayList<Timer> timers;

    private long lastTime;
    private int tickCount;

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

        this.view.addKeyListener(this);
        this.view.addMouseMotionListener(this);
        this.view.addMouseListener(this);
        this.view.setFocusable(true);

        this.pathExecutor = Executors.newSingleThreadExecutor();
        this.pathfindingRunning = false;

        this.lastTime = System.nanoTime();
        this.tickCount = 0;

        // Shoot Timer
        this.shootTimer = new Timer(this.fireRate, e -> {
            player.shoot(this.lastMouseEvent);
        });

        //master Timer
        this.movementTimer = new Timer(16, e -> {
            long now = System.nanoTime();
            double dt = (now - lastTime) / 1_000_000_000.0;
            lastTime = now;

            if (dt > 0.1) dt = 0.1;

            tickCount++;
            PerfTracker.tick(false);
            PerfTracker.start("Master Logic");

            this.player.updateMovement(dt);
            for (IEnemy enemy : map.getEnemies()) {
                enemy.move(grid, dt);
            }

            for (IGunShot shot : this.map.gunShots()) {
                shot.reduceLifeTime();
            }

            if (tickCount % 6 == 0) {
                if (player.isMoving()) {
                    this.player.incrementAnimationIndex();
                }
                for (IEnemy enemy : map.getEnemies()) {
                    enemy.incrementAnimationIndex();
                }
            }

            this.view.repaint();

            PerfTracker.stop("Master Logic");
        });

        this.pathFindingTimer = new Timer(600, (ActionEvent e) -> {
            if (pathfindingRunning) return;
            pathfindingRunning = true;
            pathExecutor.submit(() -> {
                try {
                    PerfTracker.start("Pathfinding");
                    this.map.gatherOccupiedCells();
                    for (IEnemy enemy : map.getEnemies()) {
                        enemy.requestPath(map.getGrid(), map.getPathfinder(), player.getHitbox());
                    }
                    this.map.resetOccupied();
                    PerfTracker.stop("Pathfinding");
                } finally {
                    pathfindingRunning = false;
                }
            });
        });

        this.timers = new ArrayList<>();
        this.timers.add(movementTimer);
        this.timers.add(pathFindingTimer);

        applyTimers(map.getGameState());
    }

    private void applyTimers(GameState state) {
        for (Timer t : timers) {
            if (t != null && t.isRunning()) t.stop();
        }
        if (state == GameState.ACTIVE_GAME) {
            lastTime = System.nanoTime();
            movementTimer.start();
            pathFindingTimer.start();
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
            case KeyEvent.VK_W -> player.pressMove(Direction.NORTH);
            case KeyEvent.VK_S -> player.pressMove(Direction.SOUTH);
            case KeyEvent.VK_A -> player.pressMove(Direction.WEST);
            case KeyEvent.VK_D -> player.pressMove(Direction.EAST);
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
            case KeyEvent.VK_P -> flipDebug();
            case KeyEvent.VK_O -> map.getSpawner().spawnGhoul();
            case KeyEvent.VK_L -> map.getSpawner().spawnZombie();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (this.map.getGameState() == GameState.ACTIVE_GAME) {
            activeGameReleaseEvent(e);
        }
    }

    // GAMESTATE BOUND KEY EVENTS FOR KEY RELEASED
    private void activeGameReleaseEvent(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> player.releaseMove(Direction.NORTH);
            case KeyEvent.VK_S -> player.releaseMove(Direction.SOUTH);
            case KeyEvent.VK_A -> player.releaseMove(Direction.WEST);
            case KeyEvent.VK_D -> player.releaseMove(Direction.EAST);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // not implemented
    }

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
                    this.shootTimer.start();
                }
            }
            case MAIN_MENU -> mainMenuMousePressEvent(e);
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
        }
    }

    private void changeState(GameState state) {
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

    private void updateAimFromMouse(java.awt.event.MouseEvent e) {
        camera.update(player.getHitbox(), view.getWidth(), view.getHeight(), map.getBounds());
        var worldMouse = camera.screenToWorld(e.getX(), e.getY());
        player.aimAtWorldPosition(worldMouse.x, worldMouse.y);
    }
}