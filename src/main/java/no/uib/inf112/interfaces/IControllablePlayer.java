package no.uib.inf112.interfaces;

import no.uib.inf112.enums.Direction;

public interface IControllablePlayer extends IPlayer{
    
    /**
     * Moves the player in some direction.
     * @param direction
     */
    public void movePlayer(Direction dir);

    /**
     * Gets the current direction.
     */
    public Direction getDirection();

    /**
     * Sets the current direction.
     */
    public void setDirection(Direction dir);
}


