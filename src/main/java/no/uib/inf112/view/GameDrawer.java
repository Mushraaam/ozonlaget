package no.uib.inf112.view;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.BuffType;
import no.uib.inf112.enums.GameState;
import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.interfaces.IModel;
import no.uib.inf112.utility.Camera;
import no.uib.inf112.utility.ImageHandler;
import no.uib.inf112.view.drawstates.DeathOverlay;
import no.uib.inf112.view.drawstates.DebugScreen;
import no.uib.inf112.view.drawstates.GameScreen;
import no.uib.inf112.view.drawstates.HelpMenu;
import no.uib.inf112.view.drawstates.MainMenu;
import no.uib.inf112.view.drawstates.RainbowBuffOverlay;
import no.uib.inf112.view.drawstates.VictoryOverlay;

public class GameDrawer extends JPanel {

    private IModel map;
    private IDrawer gameScreen;
    private IDrawer mainMenu;
    private IDrawer debugScreen;
    private IDrawer rainbowBuffOverlay;
    private IDrawer helpScreen;
    private IDrawer gameOverOverlay;
    private IDrawer victoryOverlay;

    private ImageHandler handler;
    private Camera camera;

    public GameDrawer(IModel map) {
        this.map = map;
        this.handler = new ImageHandler();
        this.camera = map.getCamera();

        // Screens
        setScreens();

        // Options
        this.setPreferredSize(new Dimension(Config.getInt("screenWidth"), Config.getInt("screenHeight")));
        this.setBackground(Color.DARK_GRAY);

    }

    public void setScreens() {
        this.gameScreen = new GameScreen(this.map, this.handler, this.camera);
        this.mainMenu = new MainMenu(this.handler);
        this.debugScreen = new DebugScreen(this.map);
        this.rainbowBuffOverlay = new RainbowBuffOverlay(map);
        this.helpScreen = new HelpMenu(handler);
        this.gameOverOverlay = new DeathOverlay(handler);
        this.victoryOverlay = new VictoryOverlay();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        GameState gameState = map.getGameState();

        switch (gameState) {
            case ACTIVE_GAME -> {
                this.gameScreen.draw(g2);
                checkBuffs(this.map.getPlayer().buffType(), g2);
            }

            case VICTORY -> {
                this.gameScreen.draw(g2);
                this.victoryOverlay.draw(g2);
            }

            case MAIN_MENU -> {
                this.mainMenu.draw(g2);
            }
            case HELP -> {
                this.helpScreen.draw(g2);
            }

            case GAME_OVER -> {
                this.gameScreen.draw(g2);
                this.gameOverOverlay.draw(g2);
            }
            default -> {
                throw new IllegalArgumentException(String.format("Unknown GameState: %s", gameState));
            }
        }

        if (map.debugMode() && gameState == GameState.ACTIVE_GAME) {
            this.debugScreen.draw(g2);
        }
    }

    private void checkBuffs(BuffType type, Graphics2D g3) {
        switch (type) {

            case NONE -> {
                /* Do nothing if none */}

            case RAINBOW -> {
                this.rainbowBuffOverlay.draw(g3);
            }

            default -> {
                /* Do nothing is the default */}
        }
    }

    // Getters for Controller
    public MainMenu getMainMenu() {
        return (MainMenu) this.mainMenu;
    }

    public HelpMenu getHelpMenu(){
        return (HelpMenu) this.helpScreen;
    }
}