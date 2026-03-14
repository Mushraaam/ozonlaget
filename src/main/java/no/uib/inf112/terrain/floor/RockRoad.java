package no.uib.inf112.terrain.floor;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import no.uib.inf112.enums.FloorType;
import no.uib.inf112.interfaces.IFloor;

public class RockRoad implements IFloor {

    private Rectangle2D.Double area;

    public RockRoad(Rectangle2D.Double area) {
        this.area = area;
    }

    @Override
    public FloorType floorType() {
        return FloorType.ROCK_ROAD;
    }

    @Override
    public Double getArea() {
        return this.area;
    }

}
