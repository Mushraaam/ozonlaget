package no.uib.inf112.controller;

import no.uib.inf112.enums.Direction;
import no.uib.inf112.interfaces.IControllablePlayer;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.view.GameDrawer;

import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

public class Controller implements java.awt.event.KeyListener {
    
    private IMap map;
    private IControllablePlayer player;
    private GameDrawer view;
    

    public Controller(IMap map, GameDrawer view) {

        this.map = map;
        this.player = (IControllablePlayer) map.getPlayer();
        this.view = view;

        this.view.addKeyListener(this);
        this.view.setFocusable(true);
    }


    @Override
    public void keyPressed(KeyEvent e) {
        switch (this.map.getGameState()) {

            case ACTIVE_GAME -> {
                activeGameButton(e);
            }

            default -> {
            }
        }
        this.view.repaint();
    }

    private void activeGameButton(KeyEvent e) {
        switch (e.getKeyCode()) {
            //movement
            case KeyEvent.VK_W -> {
                player.movePlayer(Direction.UP);
            }
            case KeyEvent.VK_S -> {
                player.movePlayer(Direction.DOWN);
            }
            case KeyEvent.VK_A -> {
                player.movePlayer(Direction.LEFT);
            }
            case KeyEvent.VK_D -> {
                player.movePlayer(Direction.RIGHT);
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
    public void keyTyped(KeyEvent e) {



        //not implemented
    }


    @Override
    public void keyReleased(KeyEvent e) {
        //not implemented
    }


    //////////////// HELPER METHODS - THESE SHOULD BE SHORT AND SELF EXPLANATORY ///////////////
    /// Maybe move the helpers to their classes, at a later occasion. e.g map.flipDebug() ////
    
    
    private void flipDebug() {
        if (map.debugMode()) {
            map.debugOff();
        } else {
            map.debugOn();
        }
    }
}
