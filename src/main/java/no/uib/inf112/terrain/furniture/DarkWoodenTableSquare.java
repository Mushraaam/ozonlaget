package no.uib.inf112.terrain.furniture;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.StaticObjectType;
import no.uib.inf112.interfaces.IStaticDrawableObject;

public class DarkWoodenTableSquare implements IStaticDrawableObject{

    private Rectangle2D.Double bounds;

    public DarkWoodenTableSquare(Rectangle2D.Double bounds){
        this.bounds = bounds;

        if (this.bounds.width != Config.getInt("tableWidth") || this.bounds.getHeight() != Config.getInt("tableHeight")){
            throw new IllegalArgumentException("Height/Width must be consistent with config height/width");
        }
    }
    @Override
    public Double getBounds() {
        return this.bounds;
    }

    @Override
    public boolean isWall() {
        return false; // not wall, can be shot over
    }

    @Override
    public StaticObjectType getType() {
        return StaticObjectType.DARK_TABLE_SQUARE;
    }
    
    
}
