package no.uib.inf112.model.items.buffs;

import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.interfaces.IModel;
import no.uib.inf112.model.items.Collectable;

import java.awt.geom.Rectangle2D;


public class HealthBox extends Collectable {
    public HealthBox(Rectangle2D.Double hitbox, CollectableType type, IModel map) {
        super(hitbox, type, map);
    }

    @Override
    public void affectPlayer() {
        this.player.healHP(getAmount());
        map.getSoundHandler().playBuffSound(this.getBuffType());
    }

    @Override
    public void pickUp() {
        if(player.getMaxHP() == player.getCurrentHP()){
            return; //dont waste it son!
        }
        if (isCollected) {
            return;
        }
        this.isCollected = true;
        this.map.removeActiveItem(this);
        affectPlayer();

    }


}
