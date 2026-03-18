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
    protected int duration;
    protected BuffType buffType;

    protected boolean isCollected;

    public Collectable(Rectangle2D.Double hitbox, CollectableType type, IMap map) {
        this.hitbox = hitbox;
        this.type = type;
        this.map = map;
        this.duration = type.getQuantity();
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

    @Override
    public int getDuration() {
        return this.duration;
    }

    @Override
    public void setNewDuration(int seconds) {
        this.duration = seconds;
    }

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