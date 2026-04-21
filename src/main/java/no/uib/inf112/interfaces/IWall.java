package no.uib.inf112.interfaces;

import java.awt.geom.Rectangle2D;

import no.uib.inf112.enums.WallDirection;

public interface IWall extends IStaticDrawableObject{
    
    /**
     * Calculates if the wall is a vertical or horizontal wall
     * @param bounds
     * @return
     */
    default WallDirection calculateDirection(Rectangle2D.Double bounds){
        if (bounds.height > bounds.width){
            return WallDirection.VERTICAL;
        }
        return WallDirection.HORIZONTAL;
    } 
    
    /**
     * @return direction of wall - horizontal/vertical
     */
    public WallDirection getWallDirection();
}
