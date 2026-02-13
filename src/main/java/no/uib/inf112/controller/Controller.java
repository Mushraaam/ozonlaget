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

    private final DirectionHandler dirHandler;

    private IMap map;
    private IControllablePlayer player;
    private GameDrawer view;
    private Timer playerAnimationTimer;
    private Timer movementTimer;
    

    public Controller(IMap map, GameDrawer view) {

        this.map = map;
        this.player = (IControllablePlayer) map.getPlayer();
        this.view = view;
        this.dirHandler = new DirectionHandler();

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

                if (dirHandler.isMoving()){
                player.movePlayer(dirHandler.getDirection());
                player.setDirection(dirHandler.getDirection());}

                
                // for (Direction dir : dirHandler.getAllDirections()){
                //     player.movePlayer(dir);
                // player.setDirection(dirHandler.getDirection());
                    
                }
                view.repaint();
            
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
                dirHandler.add(Direction.NORTH);
                player.setDirection(Direction.NORTH);
            }
            case KeyEvent.VK_S -> {
                dirHandler.add(Direction.SOUTH);
                player.setDirection(Direction.SOUTH);
            }
            case KeyEvent.VK_A -> {
                dirHandler.add(Direction.WEST);
                player.setDirection(Direction.WEST);
            }
            case KeyEvent.VK_D -> {
                dirHandler.add(Direction.EAST);
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
