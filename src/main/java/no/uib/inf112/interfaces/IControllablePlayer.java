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

    /**
     * Opens/Closes the inventory tab
     */
    public void openCloseInventory();

    /**
     * Opens/Closes the objectives tab
     */
    public void openCloseObjectives();

    /**
     * Tries to move the player in the given direction
     * @param dir
     */
    public void pressMove(Direction dir);

    /**
     * Stops moving the player in the given direction
     * @param dir
     */
    public void releaseMove(Direction dir);

    /**
     * @return true if player is moving
     */
    public boolean isMoving();

    /**
     * Calculates view/world coorsinate translation and faces player accordingly
     * @param worldX
     * @param worldY
     */
    public void aimAtWorldPosition(double worldX, double worldY);

    /**
     * Moves player and sets player facing the correct direction
     * Used in a timer in controller
     */
    public void updateMovement();

    /**
     * Reloads current gun
     */
    public void reload();


}

