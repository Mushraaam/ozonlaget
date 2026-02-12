package no.uib.inf112.controller;

import no.uib.inf112.enums.Direction;
import no.uib.inf112.enums.GameState;
import no.uib.inf112.interfaces.IControllablePlayer;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.view.GameDrawer;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.Set;
import java.util.EnumSet;

import javax.swing.Timer;

public class Controller implements java.awt.event.KeyListener {

    private final Set<Direction> currentDirections = EnumSet.noneOf(Direction.class);

    private IMap map;
    private IControllablePlayer player;
    private GameDrawer view;
    private Timer playerAnimationTimer;
    private Timer movementTimer;
    

    public Controller(IMap map, GameDrawer view) {

        this.map = map;
        this.player = (IControllablePlayer) map.getPlayer();
        this.view = view;

        this.view.addKeyListener(this);
        this.view.setFocusable(true);

        // TIMERS
        this.playerAnimationTimer = new Timer(100, (ActionEvent e) -> {
            if (map.getGameState() == GameState.ACTIVE_GAME) {
                this.player.incrementAnimationIndex();
                this.view.repaint();
            }
        });
        this.playerAnimationTimer.start(); //senere endre til if (moving) elns

        this.movementTimer = new Timer(16, e ->{
            if(map.getGameState() == GameState.ACTIVE_GAME){
                for (Direction dir : currentDirections){
                    player.movePlayer(dir);
                    player.setDirection(dir);
                }
                view.repaint();
            }
        });
        this.movementTimer.start();
    }


    @Override
    public void keyPressed(KeyEvent e) {
        switch (this.map.getGameState()) {

            case ACTIVE_GAME -> {
                activeGamePressEvent(e);
            }

            default -> {
            }
        }
        this.view.repaint();
    }

    //GAMESTATE BOUND KEY EVENTS FOR KEY PRESSED
    private void activeGamePressEvent(KeyEvent e) {
        switch (e.getKeyCode()) {
            // movement
            case KeyEvent.VK_W -> {
                currentDirections.add(Direction.NORTH);
                player.setDirection(Direction.NORTH);
            }
            case KeyEvent.VK_S -> {
                currentDirections.add(Direction.SOUTH);
                player.setDirection(Direction.SOUTH);
            }
            case KeyEvent.VK_A -> {
                currentDirections.add(Direction.WEST);
                player.setDirection(Direction.WEST);
            }
            case KeyEvent.VK_D -> {
                currentDirections.add(Direction.EAST);
                player.setDirection(Direction.EAST);
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

    //GAMESTATE BOUND KEY EVENTS FOR KEY RELEASED
    private void activeGameReleaseEvent(KeyEvent e){
        switch (e.getKeyCode()){
            case KeyEvent.VK_W -> {
                currentDirections.remove(Direction.NORTH);
            } 
            case KeyEvent.VK_S -> {
                currentDirections.remove(Direction.SOUTH);
            } 
            case KeyEvent.VK_A -> {
                currentDirections.remove(Direction.WEST);
            } 
            case KeyEvent.VK_D -> {
                currentDirections.remove(Direction.EAST);
            } 
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {

        // not implemented
    }
    //////////////// HELPER METHODS - THESE SHOULD BE SHORT AND SELF EXPLANATORY
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
