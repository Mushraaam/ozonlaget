package no.uib.inf112.map.items.collectable_obj;

import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.map.items.Collectable;

import java.awt.geom.Rectangle2D;

public class InventoryItem extends Collectable {

    public InventoryItem(Rectangle2D.Double hitbox, CollectableType type, IMap map) {
        super(hitbox, type, map);
    }

    @Override
    public void affectPlayer(
    ) {
        player.addToInventory(this);
    }

}
