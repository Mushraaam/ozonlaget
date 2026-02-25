package no.uib.inf112.map.npcs.pathfinding;

import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.PathType;
import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.map.NavigationLane;

import java.util.*;

public class Pathfinder {
    private final IMap map;
    private final IGrid grid;

    private Map<NavigationLane, Double> gScore = new HashMap<>();
    private Map<NavigationLane, NavigationLane> cameFrom = new HashMap<>();


    private static final int OCCUPY_WEIGHT = 15;

    public Pathfinder(IMap map) {
        this.map = map;
        this.grid = map.getGrid();
    }

    public List<NavigationLane> findPath(NavigationLane start, NavigationLane goal, EnemySize size) {
        if (start == null || goal == null || !goal.isWalkable()) return List.of();
        if (start.equals(goal)) return List.of(start);

        PriorityQueue<NavigationLane> open = new PriorityQueue<>(Comparator.comparingDouble(
                lane -> gScore.getOrDefault(lane, Double.POSITIVE_INFINITY) + heuristic(lane, goal)));

        gScore.clear();
        cameFrom.clear();

        gScore.put(start, 0.0);
        open.add(start);

        while (!open.isEmpty()) {
            NavigationLane current = open.poll();

            if (current.equals(goal)) {
                return reconstructPath(current);
            }

            for (NavigationLane neighbor : current.getNeighbors()) {
                double tentativeG = gScore.get(current) + stepCost(current, neighbor);

                if (tentativeG < gScore.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeG);
                    if (!open.contains(neighbor)) open.add(neighbor);
                }
            }
        }
        return List.of();
    }

    private double stepCost(NavigationLane from, NavigationLane neighbor) {
        double dist = Math.hypot(from.getCenterX() - neighbor.getCenterX(),
                from.getCenterY() - neighbor.getCenterY());

        return dist + (neighbor.getOccupyCount() * OCCUPY_WEIGHT);
    }

    private double heuristic(NavigationLane a, NavigationLane b) {
        return Math.hypot(a.getCenterX() - b.getCenterX(), a.getCenterY() - b.getCenterY());
    }

    private List<NavigationLane> reconstructPath(NavigationLane goal) {
        LinkedList<NavigationLane> path = new LinkedList<>();
        NavigationLane curr = goal;
        while (curr != null) {
            path.addFirst(curr);
            curr = cameFrom.get(curr);
        }
        return path;
    }

    public boolean canEnter(ICell cell, EnemySize size) {
        if (cell == null) return false;

        PathType type = cell.pathType();
        return switch (size) {
            case SMALL -> type != PathType.BLOCKED;
            case MEDIUM -> type != PathType.BLOCKED && type != PathType.BLOCKED_FOR_MEDIUM;
            case LARGE -> type == PathType.UNBLOCKED;
        };
    }
}