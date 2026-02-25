package no.uib.inf112.interfaces;

import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;

public interface ILevel {
    
    public ArrayList<IStaticObject> getStaticObjects();

    public IPlayer getPlayer();

    public Double getBounds();

    public ArrayList<IFloor> getFloor();

    public int levelNumber();
}
