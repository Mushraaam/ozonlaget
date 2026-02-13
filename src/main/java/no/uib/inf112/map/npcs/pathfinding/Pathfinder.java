package no.uib.inf112.map.npcs.pathfinding;

import java.util.*;
import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IGrid;

public class Pathfinder {

    private final IGrid grid;

    public Pathfinder(IGrid grid) { //Uses A* algorithm.
        this.grid = grid;
    }

    public List<ICell> findPath(ICell start, ICell goal) {
        if (start == null || goal == null) return List.of();
        if (start.isBlocked() || goal.isBlocked()) return List.of();
        if (start.equals(goal)) return List.of(start);
        Map<ICell, Double> score = new HashMap<>();
        score.put(start, 0.0);
        Map<ICell, ICell> cameFrom = new HashMap<>();

        PriorityQueue<ICell> open = new PriorityQueue<>(Comparator.comparingDouble(
                c -> score.getOrDefault(c, Double.POSITIVE_INFINITY) + heuristic(c, goal)
        ));

        Set<ICell> openSet = new HashSet<>();
        Set<ICell> closed = new HashSet<>();

        open.add(start);
        openSet.add(start);

        while (!open.isEmpty()) {
            ICell current = open.poll();
            openSet.remove(current);

            if (current.equals(goal)) {
                return reconstructPath(cameFrom, current);
            }

            closed.add(current);

            for (ICell neighbor : grid.getNeighbours(current)) {
                if (neighbor == null || neighbor.isBlocked()) continue;
                if (closed.contains(neighbor)) continue;

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

        return List.of(); // no path
    }

    private double heuristic(ICell a, ICell b) {
        return grid.distance(a, b);
    }

    private double stepCost(ICell from, ICell to) {
        int dx = Math.abs(from.col() - to.col());
        int dy = Math.abs(from.row() - to.row());
        return (dx == 1 && dy == 1) ? Math.sqrt(2) : 1.0; //forsøk på diagonal
    }

    private List<ICell> reconstructPath(Map<ICell, ICell> cameFrom, ICell current) {
        LinkedList<ICell> path = new LinkedList<>();
        path.addFirst(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.addFirst(current);
        }
        return path;
    }
}
