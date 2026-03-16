package no.uib.inf112.terrain.water;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.StaticObjectType;
import no.uib.inf112.interfaces.IStaticDrawableObject;

public class Water implements IStaticDrawableObject {

    private Rectangle2D.Double bounds;

    public Water(Rectangle2D.Double bounds) {
        this.bounds = bounds;

        if (this.bounds.width != Config.getInt("waterWidth")
                || this.bounds.getHeight() != Config.getInt("waterHeight")) {
            throw new IllegalArgumentException("Height/Width must be consistent with config height/width");
        }
        
    }

    @Override
    public Double getBounds() {
        return this.bounds;
    }

    @Override
    public boolean isWall() {
        return false;
    }

    @Override
    public StaticObjectType getType() {
        return StaticObjectType.WATER;
    }

}
