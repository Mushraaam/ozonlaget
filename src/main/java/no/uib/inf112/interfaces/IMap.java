package no.uib.inf112.interfaces;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import no.uib.inf112.enums.GameState;

public interface IMap {
    

    public ArrayList<IMovingDrawableObject> getMovingObjects();
    public IPlayer getPlayer();
    public GameState getGameState();
    public Rectangle2D.Double getBounds();

}
