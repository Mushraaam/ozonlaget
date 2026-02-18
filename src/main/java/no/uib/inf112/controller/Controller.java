package no.uib.inf112.controller;

import no.uib.inf112.enums.Direction;
import no.uib.inf112.enums.GameState;
import no.uib.inf112.interfaces.IControllablePlayer;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.view.GameDrawer;
import no.uib.inf112.utility.PerfTracker;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.Timer;

public class Controller implements java.awt.event.KeyListener {

    private final DirectionHandler dirHandler;

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
        this.dirHandler = new DirectionHandler();

        this.view.addKeyListener(this);
        this.view.setFocusable(true);

        // TIMERS
        this.playerAnimationTimer = new Timer(100, (ActionEvent e) -> {
            if (dirHandler.isMoving()) {
                this.player.incrementAnimationIndex();
            }
            for (IEnemy enemy : map.getEnemies()) {
                enemy.incrementAnimationIndex();
            }
            this.view.repaint();
        });

        this.pathFindingTimer = new Timer(300, (ActionEvent e) -> {
            PerfTracker.start("Pathfinding");
            grid.resetWeightedCells();
            this.map.gatherOccupiedCells();
            for (IEnemy enemy : map.getEnemies()) {
                enemy.requestPath(map.getGrid(), map.getPathfinder(), player.getHitbox());
            }
            PerfTracker.stop("Pathfinding");
        });

        this.movementTimer = new Timer(16, e -> {
            PerfTracker.tick(false);
            PerfTracker.start("Movement Logic");
            if (dirHandler.isMoving()) {
                Direction dir = dirHandler.getDirection();
                if (dir != null) {
                    this.player.movePlayer(dir);
                    this.player.setDirection(dir);
                }
            }


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
                dirHandler.add(Direction.NORTH);
            }
            case KeyEvent.VK_S -> {
                dirHandler.add(Direction.SOUTH);
            }
            case KeyEvent.VK_A -> {
                dirHandler.add(Direction.WEST);
            }
            case KeyEvent.VK_D -> {
                dirHandler.add(Direction.EAST);
            }

            case KeyEvent.VK_P -> {
                flipDebug();
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
                dirHandler.remove(Direction.NORTH);
            }
            case KeyEvent.VK_S -> {
                dirHandler.remove(Direction.SOUTH);
            }
            case KeyEvent.VK_A -> {
                dirHandler.remove(Direction.WEST);
            }
            case KeyEvent.VK_D -> {
                dirHandler.remove(Direction.EAST);
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
}
