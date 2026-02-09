package no.uib.inf112.view;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import no.uib.inf112.enums.GameState;
import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.view.DrawStates.GameScreen;
import no.uib.inf112.view.DrawStates.MainMenu;

public class GameDrawer extends JPanel {

    private IMap map;
    private IDrawer gameScreen;
    private IDrawer mainMenu;

    public GameDrawer(IMap map) {
        this.map = map;

        //Screens
        this.gameScreen = new GameScreen(this.map);
        this.mainMenu = new MainMenu();


        //Options
        this.setPreferredSize(new Dimension(1200, 800));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        GameState gameState = map.getGameState();

        switch (gameState) {
            case ACTIVE_GAME -> {this.gameScreen.draw(g2);}
            case MAIN_MENU -> {this.mainMenu.draw(g2);}
            default -> {
                throw new IllegalArgumentException(String.format("Unknown GameState: %s", gameState));
            }
        }

    }
}