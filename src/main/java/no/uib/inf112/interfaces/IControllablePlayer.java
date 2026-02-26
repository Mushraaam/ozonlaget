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
    /**
     * Sets the angle the player is facing in radians.
     */
    public void setFacingAngle(double angle);

    /**
     * Used by controller, increments animation.
     */
    public void incrementAnimationIndex();

  
    public void pressMove(Direction north);

    public void releaseMove(Direction north);

    public boolean isMoving();

    public void aimAtWorldPosition(double worldX, double worldY);

    public void updateMovement();
}


