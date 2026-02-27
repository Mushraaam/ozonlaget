package no.uib.inf112.view;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import no.uib.inf112.enums.GameState;
import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.utility.ImageHandler;
import no.uib.inf112.view.DrawStates.DebugScreen;
import no.uib.inf112.view.DrawStates.GameScreen;
import no.uib.inf112.view.DrawStates.MainMenu;

public class GameDrawer extends JPanel {

    private IMap map;
    private IDrawer gameScreen;
    private IDrawer mainMenu;
    private IDrawer debugScreen;
    private ImageHandler handler;
    private BufferedImage uiBar;
    public GameDrawer(IMap map) {
        this.map = map;
        this.handler = new ImageHandler();
        this.uiBar = handler.uiBar();


        // Screens
        this.gameScreen = new GameScreen(this.map, this.handler);
        this.mainMenu = new MainMenu();
        this.debugScreen = new DebugScreen(this.map);


        // Options
        this.setPreferredSize(new Dimension(1200, 800));
        this.setBackground(Color.DARK_GRAY);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        GameState gameState = map.getGameState();

        switch (gameState) {
            case ACTIVE_GAME -> {
                this.gameScreen.draw(g2);
            }
            case MAIN_MENU -> {
                this.mainMenu.draw(g2);
            }
            default -> {
                throw new IllegalArgumentException(String.format("Unknown GameState: %s", gameState));
            }
        }

        if (map.debugMode() && gameState == GameState.ACTIVE_GAME){
            this.debugScreen.draw(g2);
        }

    }
}