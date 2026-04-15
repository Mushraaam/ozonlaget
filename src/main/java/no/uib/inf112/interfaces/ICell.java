package no.uib.inf112.interfaces;

import java.awt.geom.Rectangle2D;
import java.util.Set;

import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.FloorType;
import no.uib.inf112.enums.PathType;

public interface ICell {


    public Rectangle2D.Double getBounds();

    public int row();

    public int col();

    public FloorType floorType();

    public void setFloorType(FloorType type);

    public PathType pathType();

    public void setPathType(PathType type);

    public boolean isOccupied(EnemySize currentEnemy);

    public boolean occupiedBy(IEnemy enemy);

    public void setOccupant(IEnemy enemy, EnemySize size);

    public int occupiedCount(EnemySize size);

    public void clearOccupants();


    Set<IEnemy> getEnemies(); //not used?
}
