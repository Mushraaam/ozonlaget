package no.uib.inf112.terrain.furniture;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.StaticObjectType;
import no.uib.inf112.interfaces.IStaticDrawableObject;

public class DarkWoodenTableSquare implements IStaticDrawableObject {

    private Rectangle2D.Double bounds;

    public DarkWoodenTableSquare(Rectangle2D.Double bounds) {
        this.bounds = bounds;
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
