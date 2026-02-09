package no.uib.inf112.map;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;

import no.uib.inf112.enums.GameState;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IMovingDrawableObject;
import no.uib.inf112.interfaces.IPlayer;

public class Map implements IMap {
    
    private IPlayer player;
    private GameState gameState;
    private Rectangle2D.Double bounds;

    public Map(IPlayer player){
        this.player = player;
        this.gameState = GameState.ACTIVE_GAME;

        this.bounds = new Rectangle2D.Double(-1000, -1000, 3000, 3000);

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

    

