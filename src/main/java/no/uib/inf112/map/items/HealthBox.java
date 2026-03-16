package no.uib.inf112.map.items;

import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.interfaces.IMap;

import java.awt.geom.Rectangle2D;


public class HealthBox extends Collectable {
    private final int HEAL_AMOUNT = 50;

    public HealthBox(Rectangle2D.Double hitbox, CollectableType type, IMap map) {
        super(hitbox, type, map);
    }

    @Override
    public void affectPlayer() {
        this.player.healHP(HEAL_AMOUNT);
    }
}
