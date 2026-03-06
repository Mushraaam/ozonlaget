package no.uib.inf112.map.npcs.pathfinding;

import java.util.*;

import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.enums.PathType;
import no.uib.inf112.interfaces.IMap;

public class Pathfinder {
    private final IMap map;
    private final IGrid grid;
    private final int width;
    private final int height;

    private final double[] gScore;
    private final int[] cameFromIdx;
    private final int[] lastVisitedId;
    private int currentSearchId = 0;

    private static final int OCCUPIED_WEIGHT = 5;

    public Pathfinder(IMap map) {
        this.map = map;
        this.grid = map.getGrid();
        this.width = grid.getColCount();
        this.height = grid.getRowCount();

        int totalCells = width * height;
        this.gScore = new double[totalCells];
        this.cameFromIdx = new int[totalCells];
        this.lastVisitedId = new int[totalCells];
    }

    public List<ICell> findPath(ICell start, ICell goal, EnemySize size) {
        if (start == null || goal == null || !canEnter(goal, size))
            return List.of();
        if (start.equals(goal))
            return List.of(start);

        currentSearchId++;

        int startIdx = getIdx(start);
        int goalIdx = getIdx(goal);

        // Reset start point
        gScore[startIdx] = 0.0;
        lastVisitedId[startIdx] = currentSearchId;
        cameFromIdx[startIdx] = -1;

        PriorityQueue<ICell> open = new PriorityQueue<>(Comparator.comparingDouble(
                c -> getGScore(c) + heuristic(c, goal)));

        open.add(start);

        while (!open.isEmpty()) {
            ICell current = open.poll();
            int currentIdx = getIdx(current);

            if (currentIdx == goalIdx) {
                return reconstructPath(current, startIdx);
            }

            for (ICell neighbor : grid.getNeighbours(current)) {
                if (neighbor == null || !canEnter(neighbor, size))
                    continue;

                int nIdx = getIdx(neighbor);
                double tentativeG = gScore[currentIdx] + stepCost(current, neighbor);

                // If neighbor hasn't been seen this search, or we found a better way
                if (lastVisitedId[nIdx] != currentSearchId || tentativeG < gScore[nIdx]) {
                    lastVisitedId[nIdx] = currentSearchId;
                    gScore[nIdx] = tentativeG;
                    cameFromIdx[nIdx] = currentIdx;
                    open.add(neighbor);
                }
            }
        }
        return List.of();
    }

    private int getIdx(ICell cell) {
        return cell.row() * width + cell.col();
    }

    private double getGScore(ICell cell) {
        int idx = getIdx(cell);
        return (lastVisitedId[idx] == currentSearchId) ? gScore[idx] : Double.POSITIVE_INFINITY;
    }

    private List<ICell> reconstructPath(ICell goalCell, int startIdx) {
        LinkedList<ICell> path = new LinkedList<>();
        int curr = getIdx(goalCell);
        while (curr != -1) {
            path.addFirst(grid.getCell(curr / width, curr % width));
            if (curr == startIdx)
                break;
            curr = cameFromIdx[curr];
        }
        return path;
    }

    /**
     * Calculates the estimated cost from cell a to cell b.
     */
    private double heuristic(ICell a, ICell b) {
        return grid.distance(a, b);
    }

    /**
     * Calculates the movement cost between two adjacent cells.
     * Account for diagonal movement (sqrt(2)) vs orthogonal movement (1.0).
     */
    private double stepCost(ICell from, ICell to) {
        int dx = from.col() - to.col();
        int dy = from.row() - to.row();
        double cost = (dx != 0 && dy != 0) ? 1.4142 : 1.0;
        int enemyCount = to.getEnemies().size();

        double trafficPenalty = enemyCount * OCCUPIED_WEIGHT;

        return cost + trafficPenalty;
    }

    /**
     * Checks if a specific enemy size is allowed to enter a cell based on its
     * PathType.
     */
    public boolean canEnter(ICell cell, EnemySize size) {

        PathType type = cell.pathType(); // Need to do somthing about this one, Probably only 2 layers, or bigger
                                         // layers?
        switch (size) {
            case SMALL -> {
                return type != PathType.BLOCKED && type != PathType.BLOCKED_FOR_MEDIUM;
            }
            case MEDIUM -> {
                return type != PathType.BLOCKED && type != PathType.BLOCKED_FOR_MEDIUM;
            }
            case LARGE -> {
                return type == PathType.UNBLOCKED;
            }
            default -> throw new IllegalStateException("No known case for size");
        }
    }

}
