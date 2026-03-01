package no.uib.inf112.interfaces;

import java.awt.geom.Rectangle2D;
import java.util.LinkedHashSet;
import java.util.List;

import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.FloorType;
import no.uib.inf112.enums.PathType;
import no.uib.inf112.map.NavigationLane;

public interface ICell {


    List<ICell> getNeighbours();

    public Rectangle2D.Double getBounds();

    public boolean isBlocked();

    public void block();

    public void unblock();

    public int row();

    public int col();

    public FloorType floorType();

    public void setFloorType(FloorType type);

    public PathType pathType();

    public void setPathType(PathType type);

    void setOccupied(boolean b);

    boolean isOccupied();

    LinkedHashSet<IEnemy> getEnemies();

    void removeEnemy(IEnemy enemy);

    void addEnemy(IEnemy enemy);
}
