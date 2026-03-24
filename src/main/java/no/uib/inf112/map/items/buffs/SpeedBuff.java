package no.uib.inf112.map.items.buffs;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.map.items.Collectable;

import java.awt.geom.Rectangle2D;

public class SpeedBuff extends Collectable {
    public SpeedBuff(Rectangle2D.Double hitbox, CollectableType type, IMap map) {
        super(hitbox, type, map);
    }

    @Override
    public void affectPlayer(
    ) {
        this.player.setPlayerSpeed(Config.getInt("playerMoveSpeed") * 2);
        player.setBuffCounter(this);
        map.getSoundHandler().playBuffMusic(this.getBuffType());//double speed, ez.

    }
}
