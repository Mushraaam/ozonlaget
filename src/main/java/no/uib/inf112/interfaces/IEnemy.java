package no.uib.inf112.interfaces;

import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.map.npcs.pathfinding.Pathfinder;

import java.util.*;
import java.awt.geom.Rectangle2D;

public interface IEnemy extends IMovingDrawableObject {

    public void requestPath(IGrid grid, Pathfinder pathfinder, Rectangle2D.Double targetBounds);

    public Rectangle2D.Double getHitbox();

    public EnemyType getEnemyType();

    public int getAnimationIndex();

    public List<ICell> getCurrentPath();

    public void incrementAnimationIndex();

    public EnemySize size();

    public double getFacingAngle();

}
