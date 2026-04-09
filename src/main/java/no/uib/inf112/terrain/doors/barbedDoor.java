package no.uib.inf112.terrain.doors;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import no.uib.inf112.enums.StaticObjectType;
import no.uib.inf112.enums.WallDirection;
import no.uib.inf112.interfaces.IDoor;



public class barbedDoor implements IDoor {

    private Rectangle2D.Double bounds;
    private WallDirection dir;
    private StaticObjectType type;
    private boolean isOpen;

    public barbedDoor(Rectangle2D.Double bounds, StaticObjectType type) {
        this.bounds = bounds;
        this.dir = calculateDirection(this.bounds);
        this.type = type;
        this.isOpen = false;
    }

    @Override
    public Double getBounds() {
        return this.bounds;
    }

    @Override
    public StaticObjectType getType() {
        return this.type;
    }

    @Override
    public boolean isWall() {
        // technically wall when closed.
        return !isOpen; 
    }

    @Override
    public void openDoor() {
        this.isOpen = true;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    @Override
    public WallDirection getWallDirection() {
        return this.dir;
    }

}
