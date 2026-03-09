package no.uib.inf112.interfaces;

import java.awt.geom.Line2D;

import no.uib.inf112.enums.GunType;

public interface IGunShot {
    
    /**
     * Reduces the remaining lifetime of this shot, removes it when it reaches 0
     */
    public void reduceLifeTime();

    /**
     * @return Line2D bounds for this shot
     */
    public Line2D bounds();

    /**
     * @return type of gun that made this shot
     */
    public GunType gunType();
}
