package no.uib.inf112.map;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;

import no.uib.inf112.enums.GameState;
import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IMovingDrawableObject;
import no.uib.inf112.interfaces.IPlayer;
import no.uib.inf112.interfaces.IStaticObject;

public class TestMap implements IMap {

    private Rectangle2D.Double bounds;

    public TestMap(Rectangle2D.Double bounds){
        this.bounds = bounds;
    }

    @Override
    public Double getBounds() {
        return this.bounds;
    }

    @Override
    public ArrayList<IStaticObject> getStaticObjects() {
        return new ArrayList<IStaticObject>();
    }





















    @Override
    public ArrayList<IMovingDrawableObject> getMovingObjects() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMovingObjects'");
    }



    @Override
    public IPlayer getPlayer() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPlayer'");
    }

    @Override
    public GameState getGameState() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGameState'");
    }


    @Override
    public IGrid getGrid() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGrid'");
    }

    @Override
    public boolean debugMode() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'debugMode'");
    }

    @Override
    public void debugOn() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'debugOn'");
    }

    @Override
    public void debugOff() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'debugOff'");
    }
    
}
