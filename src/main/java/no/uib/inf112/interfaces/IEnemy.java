package no.uib.inf112.interfaces;

import no.uib.inf112.enums.EnemyType;

public interface IEnemy {
    
    public EnemyType getEnemyType();

    public int getAnimationIndex();
}
