package no.uib.inf112.static_objects;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import no.uib.inf112.interfaces.IStaticDrawableObject;
import no.uib.inf112.interfaces.IStaticObject;

public class TestWall implements IStaticDrawableObject, IStaticObject{

    private Rectangle2D.Double bounds;
    public TestWall(Rectangle2D.Double bounds){
        this.bounds = bounds;
    }

    @Override
    public Double getBounds() {
        return this.bounds;
    }
    
}
