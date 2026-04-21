package no.uib.inf112.interfaces;

import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.enums.Direction;
import no.uib.inf112.enums.GunType;
import no.uib.inf112.player.Inventory;

public interface IViewablePlayer extends IPlayer {

    /**
     * Returns the current ammount of item in inventory
     * @param item
     * @return
     */
    int getAmountInInventory(CollectableType item);

    /**
     * Returns the current inventory of player
     * @return
     */
    public Inventory getInventory();

    /**
     * Returns type of gun equipped
     */
    public GunType gunType();

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

    /**
     * @return whether objectives tab should be visible
     */
    public boolean objectivesVisible();


    /**
     * Returns the kill count
     * enemies killed
     */
    public int getKillCount();
}
