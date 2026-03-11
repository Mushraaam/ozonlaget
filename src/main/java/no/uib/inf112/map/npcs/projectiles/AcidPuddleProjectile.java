package no.uib.inf112.map.npcs.projectiles;

import java.awt.geom.Rectangle2D;

import no.uib.inf112.enums.PuddleType;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IProjectile;
import no.uib.inf112.map.npcs.projectiles.puddles.AcidPuddle;

public class AcidPuddleProjectile extends PuddleProjectile {


    private IMap map;
    private Rectangle2D.Double target;
    private static final PuddleType TYPE = PuddleType.ACID;
    private static final int SPEED = 10;
    private static final double WIDTH = 80;
    private static final double HEIGHT = 16;

    public AcidPuddleProjectile(Rectangle2D.Double startPos, Rectangle2D.Double endPos, IMap map) {
        super(startPos, endPos, map, WIDTH, HEIGHT, SPEED, TYPE);
    }

    @Override
    protected void payload() {
        map.addAOEPuddle(new AcidPuddle(target, map));

    }
}
