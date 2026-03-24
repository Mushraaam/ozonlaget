package no.uib.inf112.map.items;

import no.uib.inf112.enums.BuffType;
import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.enums.InvItemType;
import no.uib.inf112.interfaces.ICollectable;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.player.Player;

import java.awt.geom.Rectangle2D;

public abstract class Collectable implements ICollectable {

    protected Rectangle2D.Double hitbox;
    protected CollectableType type;
    protected IMap map;
    protected Player player;
    protected int amount;
    protected BuffType buffType;
    protected boolean wasDroppedLoot = false;

    protected boolean isCollected;

    protected Collectable(Rectangle2D.Double hitbox, CollectableType type, IMap map) {
        this.hitbox = hitbox;
        this.type = type;
        this.map = map;
        this.amount = type.getQuantity();
        this.buffType = type.buffType();

        this.player = (Player) map.getPlayer();
        this.isCollected = false;
    }


    @Override
    public void isItemDroppedLoot(boolean b){
        this.wasDroppedLoot = b;
    }

    @Override
    public Rectangle2D.Double getHitbox() {
        return this.hitbox;
    }

    @Override
    public CollectableType getType() {
        return this.type;
    }

    /**
     *
     * @return the amount of effect the buff has registered. Like: 10 seconds, or 15 charges, 20 bullets etc.
     */
    @Override
    public int getAmount() {
        return this.amount;
    }

    @Override
    public void setNewAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Handles removing of the object and effects given.
     */
    @Override
    public void pickUp() {
        if (isCollected) {
            return;
        }
        this.isCollected = true;

        this.map.removeActiveItem(this);
        if(wasDroppedLoot){
            map.decreaseDroppedLoot();
        }
        affectPlayer();

    }

    /**
     * Returns which bufftype this item contains.
     */
    @Override
    public BuffType getBuffType(){
        return this.type.buffType();
    }

    /**
     * Returns which item type this item contains, if it can be placed in an inventory, it returns something else than NONE.
     */
    @Override
    public InvItemType getInventoryItemType(){
        return this.type.inventoryItemType();
    }

}