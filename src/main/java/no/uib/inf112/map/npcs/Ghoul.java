package no.uib.inf112.map.npcs;

import java.awt.geom.Rectangle2D.Double;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.interfaces.IMap;

public class Ghoul extends NPC {

    private static final double SPEED = Config.getInt("ghoulSpeed");
    private static final double ROTATION_SPEED = 0.12;
    private static final EnemySize SIZE = EnemySize.MEDIUM;
    private static final EnemyType ENEMY_TYPE = EnemyType.GHOUL;
    private static final int ANIMATION_COUNT = 8;

    public Ghoul(Double pos, IMap map) {
        super(pos, map);
        setSpeed(SPEED);
        setRotationSpeed(ROTATION_SPEED);
        setSize(SIZE);
        setEnemyType(ENEMY_TYPE);
        setAnimationCount(ANIMATION_COUNT);
    }

    @Override
    public void attack(Double target) {
        //TODO: implement
    }
    
}
