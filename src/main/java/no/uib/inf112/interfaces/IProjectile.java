package no.uib.inf112.interfaces;

import java.awt.geom.Rectangle2D;

import no.uib.inf112.enums.PuddleType;

public interface IProjectile {
    
    /**
     * @return type of projectile
     */
    public PuddleType getType();

    int getAnimationTick();

    /**
     * Moves projectile - creates a puddle when it reaches target
     */
    public void move();

    /**
     * @return the angle this image is to be drawn at
     */
    public Double angle();

    /**
     * @return bounds of projectile
     */
    public Rectangle2D.Double getBounds();

}
