package no.uib.inf112.interfaces;

import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.enums.Direction;
import no.uib.inf112.player.Inventory;

public interface IViewablePlayer extends IPlayer {

    int getAmountInInventory(CollectableType item);

    Inventory getInventory();

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
