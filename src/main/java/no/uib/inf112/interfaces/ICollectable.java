package no.uib.inf112.interfaces;

import no.uib.inf112.enums.BuffType;
import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.enums.InvItemType;

import java.awt.geom.Rectangle2D;

public interface ICollectable {

    /**
     * Sets loot to be of dropped loot or not 
     * Honestly i have no idea what the fuck this means, i didnt write the code
     * @param b
     */
    public void isItemDroppedLoot(boolean b);

    /**
     * @return bounds/hitbox for the item
     */
    public Rectangle2D.Double getHitbox();

    /**
     * @return type of the item
     */
    public CollectableType getType();

    /**
     * @return the amount of effect the buff has registered. Like: 10 seconds, or 15 charges, 20 bullets etc.
     */
    public int getAmount();

    /**
     * Sets the amount of effect the buff has registered. Like: 10 seconds, or 15 charges, 20 bullets etc.
     * @param amount
     */
    public void setNewAmount(int amount);

    /**
     * Player picks up item
     */
    public void pickUp();

    /**
     * @return type of buff for this item
     */
    public BuffType getBuffType();

    /**
     * Makes the item affect player
     */
    public void affectPlayer();

    /**
     * @return which type of inventory item this is
     */
    public InvItemType getInventoryItemType();
}

