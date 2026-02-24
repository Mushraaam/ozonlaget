package no.uib.inf112.map;

import java.util.ArrayList;
import java.util.List;
import no.uib.inf112.enums.EnemySize;

public class NavigationLane {
    // A* Search Data
    public double costFromStart;    // gScore
    public double estimatedTotal;  // fScore
    public NavigationLane parent;  // cameFrom

    // Position Data (World Coordinates)
    private final int worldCenterX;
    private final int worldCenterY;

    // Grid Coordinates (Macro Index)
    private final int laneRow;
    private final int laneCol;

    private final List<NavigationLane> neighbors = new ArrayList<>();
    private boolean isWalkable;
    private final EnemySize laneSize;

    // Dynamic Crowd Control
    private int unitOccupancy = 0;

    public NavigationLane(int x, int y, int row, int col, EnemySize size) {
        this.worldCenterX = x;
        this.worldCenterY = y;
        this.laneRow = row;
        this.laneCol = col;
        this.laneSize = size;
    }

    public void addNeighbor(NavigationLane neighbor) {
        if (neighbor != null) neighbors.add(neighbor);
    }

    // Getters and Setters
    public int getCenterX() { return worldCenterX; }
    public int getCenterY() { return worldCenterY; }
    public List<NavigationLane> getNeighbors() { return neighbors; }
    public boolean isWalkable() { return isWalkable; }
    public void setWalkable(boolean walkable) { isWalkable = walkable; }
    public int getOccupancy() { return unitOccupancy; }
    public void addOccupant() { unitOccupancy++; }
    public void clearOccupancy() { unitOccupancy = 0; }
}