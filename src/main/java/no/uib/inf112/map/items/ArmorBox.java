package no.uib.inf112.map.items;

import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.interfaces.IMap;

import java.awt.geom.Rectangle2D;

public class ArmorBox extends Collectable{
    public ArmorBox(Rectangle2D.Double hitbox, CollectableType type, IMap map) {
        super(hitbox, type, map);
    }

    @Override
    public void affectPlayer() {
        player.increaseArmor(getAmount());
        map.getSoundHandler().playBuffSound(this.getBuffType());
    }
}
