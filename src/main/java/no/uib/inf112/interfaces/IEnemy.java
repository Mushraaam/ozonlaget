package no.uib.inf112.interfaces;

import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.map.npcs.pathfinding.Pathfinder;

import java.util.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public interface IEnemy extends IMovingDrawableObject {

    void requestPath(IGrid grid, Pathfinder pathfinder, Rectangle2D.Double targetBounds
    );

    Rectangle2D.Double getHitbox();

    public EnemyType getEnemyType();

    public int getAnimationIndex();

    BufferedImage getImg();

    List<ICell> getCurrentPath();
}
