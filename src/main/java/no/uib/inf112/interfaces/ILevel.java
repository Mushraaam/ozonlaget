package no.uib.inf112.interfaces;

import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;

import no.uib.inf112.map.items.factory.ItemFactory;
import no.uib.inf112.map.npcs.factory.Factory;

public interface ILevel {
    
    public ArrayList<IStaticObject> getStaticObjects();

    public IPlayer getPlayer();

    public Double getBounds();

    public ArrayList<IFloor> getFloor();

    public int levelNumber();

    public Factory getFactory();

    ItemFactory getItemFactory();
}
