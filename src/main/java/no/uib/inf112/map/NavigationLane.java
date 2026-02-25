package no.uib.inf112.map;

import java.awt.geom.Rectangle2D;
import java.util.*;

import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IEnemy;

public class NavigationLane {
    private final List<ICell> cells;
    private final Rectangle2D.Double bounds;

    private final int worldCenterX;
    private final int worldCenterY;

    private final List<NavigationLane> neighbors = new ArrayList<>();
    private boolean isWalkable;
    private final Set<IEnemy> enemiesInLane = new LinkedHashSet<>();


    public NavigationLane(int centerX, int centerY, List<ICell> cells) {
        this.worldCenterX = centerX;
        this.worldCenterY = centerY;

        this.cells = cells;

        this.bounds = calculateBounds(cells);
    }

    public void addNeighbor(NavigationLane neighbor) {
        if (neighbor != null) neighbors.add(neighbor);
    }

    public int getOccupyCount(){
        return this.enemiesInLane.size();
    }

    public void addOccupant(IEnemy enemy) {
        enemiesInLane.add(enemy);
    }

    public void removeOccupant(IEnemy enemy) {
        enemiesInLane.remove(enemy);
    }

    public Collection<IEnemy> getEnemies() {
        return enemiesInLane;
    }

    private Rectangle2D.Double calculateBounds(List<ICell> cells) {
        if (cells.isEmpty()) return new Rectangle2D.Double();

        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        for (ICell cell : cells) {
            Rectangle2D.Double cb = cell.getBounds();
            minX = Math.min(minX, cb.x);
            minY = Math.min(minY, cb.y);
            maxX = Math.max(maxX, cb.x + cb.width);
            maxY = Math.max(maxY, cb.y + cb.height);
        }

        return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
    }

    public Rectangle2D.Double getBounds() {
        return bounds;
    }

    // Getters and Setters
    public int getCenterX() { return worldCenterX; }
    public int getCenterY() { return worldCenterY; }
    public List<NavigationLane> getNeighbors() { return neighbors; }
    public boolean isWalkable() { return isWalkable; }
    public void setWalkable(boolean walkable) { isWalkable = walkable; }

}