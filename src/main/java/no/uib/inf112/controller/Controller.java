package no.uib.inf112.controller;

import no.uib.inf112.enums.Direction;
import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.GameState;
import no.uib.inf112.interfaces.IControllablePlayer;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.view.GameDrawer;
import no.uib.inf112.utility.PerfTracker;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.Timer;

import org.w3c.dom.css.Rect;

public class Controller implements java.awt.event.KeyListener, java.awt.event.MouseMotionListener {

    //private final DirectionHandler dirHandler;

    private IMap map;
    private IControllablePlayer player;
    private GameDrawer view;
    private Timer playerAnimationTimer;
    private Timer movementTimer;
    private Timer pathFindingTimer;
    private IGrid grid;

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

        // TODO: Move dirHandler to player
        //this.dirHandler = new DirectionHandler();

        this.view.addKeyListener(this);
        this.view.addMouseMotionListener(this);
        this.view.setFocusable(true);

        // TIMERS
        this.playerAnimationTimer = new Timer(100, (ActionEvent e) -> {
            if (player.isMoving()) {
                this.player.incrementAnimationIndex();
            }
            for (IEnemy enemy : map.getEnemies()) {
                enemy.incrementAnimationIndex();
            }
            this.view.repaint();
        });

        this.pathFindingTimer = new Timer(600, (ActionEvent e) -> {
            PerfTracker.start("Pathfinding");
            this.map.gatherOccupiedCells();
            for (IEnemy enemy : map.getEnemies()) {
                enemy.requestPath(map.getGrid(), map.getPathfinder(), player.getHitbox());
            }
            PerfTracker.stop("Pathfinding");
        });

        this.movementTimer = new Timer(16, e -> {
            PerfTracker.tick(false);
            PerfTracker.start("Movement Logic");
            map.updateEnemyLocations(map.getEnemies());
            
            this.player.updateMovement();


            ArrayList<IEnemy> enemies = map.getEnemies();
            for (IEnemy enemy : enemies) {
                enemy.move(grid);
            }
            PerfTracker.stop("Movement Logic");
            this.view.repaint();
        });
        
        this.timers = new ArrayList<>();
        this.timers.add(playerAnimationTimer);
        this.timers.add(pathFindingTimer);
        this.timers.add(movementTimer);

        applyTimers(map.getGameState());
    }

    //STOP AND START TIMERS
    private void applyTimers(GameState state) {
        for (Timer t : timers) {
            if (t != null && t.isRunning()) t.stop();
        }
        switch (state) {
            case MAIN_MENU -> {

            }
            case ACTIVE_GAME -> {
                playerAnimationTimer.start();
                pathFindingTimer.start();
                movementTimer.start();
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
                    map.setGameState(GameState.ACTIVE_GAME);
                    applyTimers(GameState.ACTIVE_GAME);
                }
            }

            case ACTIVE_GAME -> {
                activeGamePressEvent(e);
            }

            default -> {
            }
        }
        this.view.repaint();
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

            case KeyEvent.VK_P -> {
                flipDebug();
            }
            case KeyEvent.VK_L -> {
                viewLaneSize();
            }
            case KeyEvent.VK_O -> {
                map.getSpawner().spawnThug();
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
    /// /////////////// Maybe move the helpers to their classes, at a later occasion.
    /// e.g map.flipDebug()

    private void flipDebug() {
        if (map.debugMode()) {
            map.debugOff();
        } else {
            map.debugOn();
        }
    }
    private void viewLaneSize() {
        switch(map.debugLaneSize()){
            case EnemySize.SMALL -> map.setDebugLaneSize(EnemySize.MEDIUM);
            case EnemySize.MEDIUM -> map.setDebugLaneSize(EnemySize.LARGE);
            case EnemySize.LARGE -> map.setDebugLaneSize(EnemySize.SMALL);
            default -> throw new NullPointerException();

        }
    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
        updateAimFromMouse(e);
    }

    @Override
    public void mouseDragged(java.awt.event.MouseEvent e) {
        updateAimFromMouse(e);
    }


    // This method translates mouse coordinates to world coordinates, calculates the angle from the player to the mouse, and updates the player's facing angle.
    private void updateAimFromMouse(java.awt.event.MouseEvent e) {
        // mouse in screen coordinates
        double mouseX = e.getX();
        double mouseY = e.getY();

        // player in world coordinates
        double playerCenterX = player.getHitbox().getCenterX();
        double playerCenterY = player.getHitbox().getCenterY();

        // camera logic from GameScreen
        double screenWidth = view.getWidth();
        double screenHeight = view.getHeight();

        Rectangle2D.Double mapBounds = map.getBounds();

        double translatedX = screenWidth / 2 - playerCenterX;
        double translatedY = screenHeight / 2 - playerCenterY;
        double minTranslateX = screenWidth - mapBounds.getWidth();
        double minTranslateY = screenHeight - mapBounds.getHeight();

        translatedX = Math.min(0, Math.max(translatedX, minTranslateX));
        translatedY = Math.min(0, Math.max(translatedY, minTranslateY));

        // convert mouse coordinates to world coordinates
        double worldMouseX = mouseX - translatedX;
        double worldMouseY = mouseY - translatedY;

        player.aimAtWorldPosition(worldMouseX, worldMouseY);

        view.repaint();


    }
}
