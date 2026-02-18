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

    /**
     * Responsible for calculating the shortest valid path between two cells
     * on the game grid using the A* search algorithm.
     */
    public Pathfinder(IMap map) { // Uses A* algorithm.
        this.map = map;
        this.grid = map.getGrid();
    }

    /**
     * Finds a path from a start cell to a goal cell, taking into account
     * the size of the enemy and cell weights.
     *
     * @param start The starting cell.
     * @param goal  The destination cell.
     * @param size  The size of the enemy (determines which cells are available to use).
     * @return A list of cells representing the smoothed path, or an empty list if no path exists.
     */
    public List<ICell> findPath(ICell start, ICell goal, EnemySize size) {

        if (start == null || goal == null)
            return List.of();
        if (!canEnter(goal, size))
            return List.of();
        if (start.equals(goal))
            return List.of(start);
        Map<ICell, Double> score = new HashMap<>();
        score.put(start, 0.0);
        Map<ICell, ICell> cameFrom = new HashMap<>();

        PriorityQueue<ICell> open = new PriorityQueue<>(Comparator.comparingDouble(
                c -> score.getOrDefault(c, Double.POSITIVE_INFINITY) + heuristic(c, goal)));

        Set<ICell> openSet = new HashSet<>();
        Set<ICell> closed = new HashSet<>();

        open.add(start);
        openSet.add(start);

        while (!open.isEmpty()) {
            ICell current = open.poll();
            openSet.remove(current);

            if (current.equals(goal)) {
                return (reconstructPath(cameFrom, current));
            }

            closed.add(current);

            for (ICell neighbor : grid.getNeighbours(current)) {
                if (neighbor == null || !canEnter(neighbor, size)) {
                    continue;
                }
                if (closed.contains(neighbor)) {
                    continue;
                }
                double tentativeG = score.get(current) + stepCost(current, neighbor);

                if (tentativeG < score.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                    cameFrom.put(neighbor, current);
                    score.put(neighbor, tentativeG);

                    if (!openSet.contains(neighbor)) {
                        open.add(neighbor);
                        openSet.add(neighbor);
                    } else {
                        open.remove(neighbor);
                        open.add(neighbor);
                    }
                }
            }
        }

        return List.of(); // no path // should this throw an exception?
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
        int baseCost = (map.inOccupiedCells(to)) ? 12 : 1;
        int dx = Math.abs(from.col() - to.col());
        int dy = Math.abs(from.row() - to.row());
        double move = (dx == 1 && dy == 1) ? Math.sqrt(2) : 1.0;
        return move+baseCost;// forsøk på diagonal
    }

    /**
     * Traces back from the goal to the start using the 'cameFrom' map to build the final path.
     */
    private List<ICell> reconstructPath(Map<ICell, ICell> cameFrom, ICell current) {
        LinkedList<ICell> path = new LinkedList<>();
        path.addFirst(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.addFirst(current);
        }
        return path;
    }



    /**
     * Checks if a specific enemy size is allowed to enter a cell based on its PathType.
     */
    private boolean canEnter(ICell cell, EnemySize size) {

        PathType type = cell.pathType();
        switch (size) {
            case SMALL -> {
                return type != PathType.BLOCKED;
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
