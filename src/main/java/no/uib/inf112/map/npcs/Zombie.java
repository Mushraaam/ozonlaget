package no.uib.inf112.map.npcs;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.interfaces.IMap;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

public class Zombie extends NPC {

    private static final double SPEED = Config.getInt("zombieSpeed");
    private static final double ROTATION_SPEED = 0.12;
    private static final EnemySize SIZE = EnemySize.LARGE;
    private static final EnemyType ENEMY_TYPE = EnemyType.ZOMBIE;
    private static final int ANIMATION_COUNT = 8;

    public Zombie(Rectangle2D.Double pos, IMap map) {
        super(pos, map, Config.getInt("zombieHP"));
        setSpeed(SPEED);
        setRotationSpeed(ROTATION_SPEED);
        setSize(SIZE);
        setEnemyType(ENEMY_TYPE);
        setAnimationCount(ANIMATION_COUNT);
    }

    @Override
    public void attack(Double target) {
        // do nothing as zombies do not attack
    }
}
