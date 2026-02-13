package no.uib.inf112.interfaces;

import java.awt.geom.Rectangle2D;

import no.uib.inf112.enums.walls.WallDirection;
import no.uib.inf112.enums.walls.WallType;

public interface IWall extends IStaticDrawableObject{
    
    default WallDirection calculateDirection(Rectangle2D.Double bounds){
        if (bounds.height > bounds.width){
            return WallDirection.VERTICAL;
        }
        return WallDirection.HORIZONTAL;
    } 

    public WallType wallType();

    public WallDirection getWallDirection();
}
