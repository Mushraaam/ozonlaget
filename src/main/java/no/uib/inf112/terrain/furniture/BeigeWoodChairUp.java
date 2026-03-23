package no.uib.inf112.terrain.furniture;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import no.uib.inf112.enums.StaticObjectType;
import no.uib.inf112.interfaces.IStaticDrawableObject;

public class BeigeWoodChairUp implements IStaticDrawableObject {

    private Rectangle2D.Double bounds;

    public BeigeWoodChairUp(Rectangle2D.Double bounds) {
        this.bounds = bounds;
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
        return StaticObjectType.BEIGE_CHAIR_WOOD_UP;
    }

}
