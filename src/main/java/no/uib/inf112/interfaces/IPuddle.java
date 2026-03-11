package no.uib.inf112.interfaces;

import java.awt.geom.Rectangle2D;

import no.uib.inf112.enums.PuddleType;

public interface IPuddle {
    
    /**
     * @return bounds for the puddle
     */
    public Rectangle2D.Double getBounds();

    /**
     * Increments animation index
     */
    public void incrementAnimationIndex();

    /**
     * @return animation index
     */
    public int getAnimationIndex();

    /**
     * @return type of puddle
     */
    public PuddleType getType();

    /**
     * Checks if player on puddle and deals damage if it does
     */
    public void dealDamage();

    /**
     * @return total lifetime of puddle
     */
    public int lifeTime();
}
