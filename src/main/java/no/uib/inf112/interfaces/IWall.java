package no.uib.inf112.interfaces;

import java.awt.geom.Rectangle2D;

import no.uib.inf112.enums.walls.WallDirection;

public interface IWall {
    
    default WallDirection calculateDirection(Rectangle2D.Double bounds){
        if (bounds.height > bounds.width){
            return WallDirection.VERTICAL;
        }
        return WallDirection.HORIZONTAL;
    } 
}
