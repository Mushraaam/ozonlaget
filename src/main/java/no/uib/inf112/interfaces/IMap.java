package no.uib.inf112.interfaces;

import java.util.ArrayList;

import no.uib.inf112.enums.GameState;

public interface IMap {
    

    public ArrayList<IMovingDrawableObject> getMovingObjects();
    public IPlayer getPlayer();
    public GameState getGameState();

}
