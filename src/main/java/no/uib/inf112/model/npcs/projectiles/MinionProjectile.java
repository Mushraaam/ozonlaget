package no.uib.inf112.model.npcs.projectiles;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.PuddleType;
import no.uib.inf112.interfaces.IModel;
import no.uib.inf112.model.npcs.Ghoul; // Make sure to import your actual Ghoul/Sprinter class!
import no.uib.inf112.model.npcs.KamikazeBug;
import no.uib.inf112.model.npcs.NPC;

import java.awt.geom.Rectangle2D;

public class MinionProjectile extends PuddleProjectile {

    private Rectangle2D.Double target;
    private static final PuddleType TYPE = PuddleType.BUGPROJECTILE;
    private static final int SPEED = 8;
    private static final double WIDTH = 14;
    private static final double HEIGHT = 48;

    public MinionProjectile(Rectangle2D.Double startPos, Rectangle2D.Double endPos, IModel map) {
        super(startPos, endPos, map, WIDTH, HEIGHT, SPEED, TYPE);
        this.target = endPos;
    }

    @Override
    protected void payload() {
        Rectangle2D.Double spawnBounds = new Rectangle2D.Double(
                this.target.getX(),
                this.target.getY(),
                20,
                20

        );
        NPC newMinion = new KamikazeBug(spawnBounds, super.map);
        super.map.addEnemy(newMinion);
    }
}