package no.uib.inf112.map.npcs.projectiles.puddles;

import java.awt.geom.Rectangle2D.Double;

import no.uib.inf112.enums.PuddleType;
import no.uib.inf112.interfaces.IMap;

public class AcidPuddle extends Puddle{

    private static final int DAMAGE = 2;
    private static final PuddleType TYPE = PuddleType.ACID;

    public AcidPuddle(Double bounds, IMap map) {
        super(bounds, map, TYPE, DAMAGE);
    }
    
}
