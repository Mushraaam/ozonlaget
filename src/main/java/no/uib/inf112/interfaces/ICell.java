package no.uib.inf112.interfaces;

import java.awt.geom.Rectangle2D;
import java.util.LinkedHashSet;
import java.util.List;

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

    boolean isOccupied(EnemySize currentEnemy);

    boolean occupiedBy(IEnemy enemy);

    void setOccupant(IEnemy enemy, EnemySize size);

    int occupiedCount(EnemySize size);

    void clearOccupants();

    
}
