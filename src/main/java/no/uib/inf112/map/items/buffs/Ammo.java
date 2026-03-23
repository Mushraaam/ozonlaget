package no.uib.inf112.map.items.buffs;

import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.enums.GunType;
import no.uib.inf112.interfaces.IGun;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.map.items.Collectable;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;

public class Ammo extends Collectable {
    public Ammo(Rectangle2D.Double hitbox, CollectableType type, IMap map) {
        super(hitbox, type, map);
    }

    @Override
    public void affectPlayer() {
        for (HashMap.Entry<GunType, IGun> gun : player.getOwnedGuns().entrySet()) {
            IGun thisGun = gun.getValue();
            if(thisGun.getAmmoType() == this.getType()){
                thisGun.increaseAmmo(this.getAmount());
                map.getSoundHandler().playBuffSound(this.buffType);
                break;
            }

        }

    }
}
