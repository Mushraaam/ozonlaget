package no.uib.inf112.model.items.collectable_obj;

import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.interfaces.IModel;
import no.uib.inf112.model.items.Collectable;

import java.awt.geom.Rectangle2D;

public class InventoryItem extends Collectable {

    public InventoryItem(Rectangle2D.Double hitbox, CollectableType type, IModel map) {
        super(hitbox, type, map);
    }

    @Override
    public void affectPlayer(
    ) {
        player.addToInventory(this.type);
    }

}
