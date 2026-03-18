package no.uib.inf112.map.items;

import no.uib.inf112.enums.BuffType;
import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.interfaces.ICollectable;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IPlayer;

import java.awt.geom.Rectangle2D;

public abstract class Collectable implements ICollectable {

    protected Rectangle2D.Double hitbox;
    protected CollectableType type;
    protected IMap map;
    protected IPlayer player;
    protected int amount;
    protected BuffType buffType;

    protected boolean isCollected;

    public Collectable(Rectangle2D.Double hitbox, CollectableType type, IMap map) {
        this.hitbox = hitbox;
        this.type = type;
        this.map = map;
        this.amount = type.getQuantity();
        this.buffType = type.buffType();

        this.player = map.getPlayer();
        this.isCollected = false;
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
     * Abstract, to be implemented by each item.
     */
    @Override
    public abstract void affectPlayer();


}