package no.uib.inf112.controller;

import no.uib.inf112.interfaces.IControllablePlayer;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.view.GameDrawer;

import java.awt.event.KeyEvent;

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
            case KeyEvent.VK_P -> {
                flipDebug();
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

    // Seksjon med kode for GameState.MAIN_MENU

    // Seksjon med kode for GameState.ACTIVE_GAME

    // Seksjon med kode for GameState._____________



    //////////////// HELPER METHODS - THESE SHOULD BE SHORT AND SELF EXPLANATORY ///////////////
    
    
    private void flipDebug() {
        if (map.debugMode()) {
            map.debugOff();
        } else {
            map.debugOn();
        }
    }
}
