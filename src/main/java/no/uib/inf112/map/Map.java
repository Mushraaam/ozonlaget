package no.uib.inf112.map;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;

import no.uib.inf112.enums.GameState;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IMovingDrawableObject;
import no.uib.inf112.interfaces.IPlayer;
import no.uib.inf112.player.Player;

public class Map implements IMap {
    
    private IPlayer player;
    private GameState gameState;
    private Rectangle2D.Double bounds;

    public Map(){

        //senere: Skaffe modul som leser inn og returnerer disse verdiene fra fil
        this.player = new Player(new Rectangle2D.Double(950, 250, 100, 100));
        this.bounds = new Rectangle2D.Double(-1000, -1000, 3000, 3000);


        // Bør senere starte i main menu
        this.gameState = GameState.ACTIVE_GAME;

    }

    

    @Override
    public ArrayList<IMovingDrawableObject> getMovingObjects() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMovingObjects'");
    }

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
}

    

