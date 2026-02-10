package no.uib.inf112.map;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;

import no.uib.inf112.enums.GameState;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IMovingDrawableObject;
import no.uib.inf112.interfaces.IPlayer;
import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.interfaces.ILevel;
import no.uib.inf112.interfaces.IStaticObject;
import no.uib.inf112.map.levels.Level1;
import no.uib.inf112.player.Player;

public class Map implements IMap {

    private ILevel level;
    private IPlayer player;
    private GameState gameState;
    private Rectangle2D.Double bounds;
    private ArrayList<IStaticObject> staticObjects;
    private IGrid grid;
    private boolean debug;

    public Map() {

        // senere: Skaffe modul som leser inn og returnerer disse verdiene fra fil
        this.player = new Player(new Rectangle2D.Double(1000, 1000, 100, 100));
        this.bounds = new Rectangle2D.Double(0, 0, 2500, 2500);
        this.debug = false;

        // Implementer egen metode/meny for denne
        this.level = new Level1();
        this.staticObjects = this.level.getStaticObjects();

        // Bør senere starte i main menu
        this.gameState = GameState.ACTIVE_GAME;

        this.grid = new Grid(this);

    }

    @Override
    public ArrayList<IMovingDrawableObject> getMovingObjects() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMovingObjects'");
    }




















    ////////////////// GETTERS AND SETTERS ////////////////////

    @Override
    public IPlayer getPlayer() {
        return this.player;
    }

    @Override
    public GameState getGameState() {
        return this.gameState;
    }

    @Override
    public Double getBounds() {
        return this.bounds;
    }

    @Override
    public ArrayList<IStaticObject> getStaticObjects() {
        return this.staticObjects;
    }

    @Override
    public IGrid getGrid() {
        return this.grid;
    }

    @Override
    public boolean debugMode() {
        return this.debug;
    }

    @Override
    public void debugOn() {
        this.debug = true;
    }

    @Override
    public void debugOff() {
        this.debug = false;
    }
}
