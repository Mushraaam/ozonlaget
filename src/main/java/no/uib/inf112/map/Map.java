package no.uib.inf112.map;

import java.util.ArrayList;

import no.uib.inf112.enums.GameState;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IMovingDrawableObject;
import no.uib.inf112.interfaces.IPlayer;

public class Map implements IMap {
    
    private IPlayer player;
    private GameState gameState;

    public Map(IPlayer player){
        this.player = player;
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
}

    

