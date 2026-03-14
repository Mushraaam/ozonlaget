package no.uib.inf112.terrain.walls;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import no.uib.inf112.enums.StaticObjectType;
import no.uib.inf112.enums.WallDirection;
import no.uib.inf112.interfaces.IWall;

public class WoodWall implements IWall {

    private Rectangle2D.Double bounds;
    private WallDirection dir;
    private StaticObjectType type;

    public WoodWall(Rectangle2D.Double bounds, StaticObjectType type) {
        this.bounds = bounds;
        this.dir = calculateDirection(this.bounds);
        this.type = type;
    }

    @Override
    public Double getBounds() {
        return this.bounds;
    }

    public WallDirection getWallDirection() {
        return this.dir;
    }

    @Override
    public StaticObjectType getType() {
        return this.type;
    }

    @Override
    public boolean isWall() {
        return true;
    }

}
