package no.uib.inf112.map;

import java.util.ArrayList;

import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IMovingDrawableObject;
import no.uib.inf112.interfaces.IPlayer;

public class Map implements IMap {
    
    private IPlayer player;

    public Map(IPlayer player){
        this.player = player;
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
}

    

