package no.uib.inf112.model.items.buffs;

import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.interfaces.IModel;
import no.uib.inf112.model.items.Collectable;

import java.awt.geom.Rectangle2D;

public class DamageBuff extends Collectable {
    public DamageBuff(Rectangle2D.Double hitbox, CollectableType type, IModel map) {
        super(hitbox, type, map);
    }

    @Override
    public void affectPlayer() {
        player.setBuffCounter(this);
        map.getSoundHandler().playBuffMusic(this.getBuffType());

    }
}
