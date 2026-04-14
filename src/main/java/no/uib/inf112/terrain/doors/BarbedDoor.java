package no.uib.inf112.terrain.doors;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.List;

import no.uib.inf112.enums.PathType;
import no.uib.inf112.enums.StaticObjectType;
import no.uib.inf112.enums.WallDirection;
import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IDoor;
import no.uib.inf112.interfaces.IModel;

public class BarbedDoor implements IDoor {

    private Rectangle2D.Double bounds;
    private WallDirection dir;
    private StaticObjectType type;
    private boolean isOpen;
    private IModel model;

    public BarbedDoor(Rectangle2D.Double bounds, StaticObjectType type, IModel model) {
        this.bounds = bounds;
        this.dir = calculateDirection(bounds);
        this.type = type;
        this.isOpen = false;
        this.model = model;
    }

    @Override
    public Double getBounds() {
        return this.bounds;
    }

    @Override
    public StaticObjectType getType() {
        return this.type;
    }

    @Override
    public boolean isWall() {
        // technically wall when closed.
        return !isOpen; 
    }

    @Override
    public void openDoor() {
        this.isOpen = true;
        openPathing();
    }

    private void openPathing() {
        ICell middleCell = this.model.getGrid().getCellFromPos(this.bounds);
        List<ICell> cells = this.model.getGrid().getNearbyCells(middleCell.getBounds(), this.bounds.width / 2);

        for (ICell cell : cells){
            if (cell.pathType() == PathType.BLOCKED || 
                cell.pathType() == PathType.BLOCKED_FOR_LARGE || 
                cell.pathType() == PathType.BLOCKED_FOR_MEDIUM){

                cell.setPathType(PathType.UNBLOCKED);

            }
        }
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    @Override
    public WallDirection getWallDirection() {
        return this.dir;
    }

}
