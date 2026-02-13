package no.uib.inf112.map.npcs;

import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.map.npcs.pathfinding.Pathfinder;
import no.uib.inf112.utility.ImageReader;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Thug implements IEnemy {
    private Rectangle2D.Double pos;
    private BufferedImage tempImg;
    private List<ICell> currentPath = new ArrayList<>();
    private int pathIndex = 0;
    public Thug(Rectangle2D.Double pos){

        this.pos = pos;
        this.tempImg = ImageReader.fetchImage("/no/uib/inf112/map/npcs/thug.png");
    }

    @Override
    public void requestPath(IGrid grid, Pathfinder pathfinder, Rectangle2D.Double targetBounds
    ) {
        ICell start = grid.getCellFromPos(getHitbox());
        ICell goal  = grid.getCellFromPos(targetBounds);

        currentPath = pathfinder.findPath(start, goal);
        pathIndex = 0;
    }

    public List<ICell> getCurrentPath(){
        return  currentPath;
    }



    @Override
    public Rectangle2D.Double getHitbox() {
        return this.pos;
    }


    @Override
    public EnemyType getEnemyType() {
        return EnemyType.THUG;
    }

    @Override
    public int getAnimationIndex() {
        return 0;
    }

    public BufferedImage getImg(){
        return tempImg;
    }

    @Override
    public void move(double deltaTime, IGrid grid) {

    }
}
