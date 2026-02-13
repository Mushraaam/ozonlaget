package no.uib.inf112.static_objects;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import no.uib.inf112.enums.walls.WallDirection;
import no.uib.inf112.enums.walls.WallType;
import no.uib.inf112.interfaces.IStaticDrawableObject;
import no.uib.inf112.interfaces.IStaticObject;
import no.uib.inf112.interfaces.IWall;

public class TestWall implements IStaticDrawableObject, IStaticObject, IWall{

    private Rectangle2D.Double bounds;
    private WallDirection dir;
    private WallType type;
    public TestWall(Rectangle2D.Double bounds, WallType type){
        this.bounds = bounds;
        this.dir = calculateDirection(this.bounds);
        this.type = type;
    }

    @Override
    public Double getBounds() {
        return this.bounds;
    }

    public WallDirection getWallDirection(){
        return this.dir;
    }

    public WallType wallType(){
        return this.type;
    }

    
}
