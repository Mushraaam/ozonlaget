package no.uib.inf112.model.npcs.projectiles;

import java.awt.geom.Rectangle2D;

import no.uib.inf112.enums.PuddleType;
import no.uib.inf112.interfaces.IModel;
import no.uib.inf112.model.npcs.projectiles.puddles.AcidPuddle;

public class AcidPuddleProjectile extends PuddleProjectile {

    private Rectangle2D.Double target;
    private static final PuddleType TYPE = PuddleType.ACID;
    private static final int SPEED = 10;
    private static final double WIDTH = 80;
    private static final double HEIGHT = 16;

    public AcidPuddleProjectile(Rectangle2D.Double startPos, Rectangle2D.Double endPos, IModel map) {
        super(startPos, endPos, map, WIDTH, HEIGHT, SPEED, TYPE);
        this.target = endPos;
    }

    @Override
    protected void payload() {
        Rectangle2D.Double goal = new Rectangle2D.Double(this.target.getX() - 30, this.target.getY() - 30,
                this.target.getWidth() + 60, this.target.getHeight() + 60);
        map.addAOEPuddle(new AcidPuddle(goal, super.map));

    }
}
