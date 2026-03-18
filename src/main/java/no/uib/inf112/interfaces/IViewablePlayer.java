package no.uib.inf112.interfaces;

import no.uib.inf112.enums.Direction;

public interface IViewablePlayer extends IPlayer {

    /**
     * @return current direction
     */
    public Direction getDirection();
    
    /**
     * @return current index for sprite array
     */
    public int getAnimationIndex();

    /**
     * @return angle that player should be drawn at
     */
    public double getFacingAngle();
}
