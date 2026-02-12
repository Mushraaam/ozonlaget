package no.uib.inf112.interfaces;

import no.uib.inf112.enums.EnemyType;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public interface IEnemy extends IMovingDrawableObject {

    Rectangle2D.Double getHitbox();

    public EnemyType getEnemyType();

    public int getAnimationIndex();

    BufferedImage getImg();
}
