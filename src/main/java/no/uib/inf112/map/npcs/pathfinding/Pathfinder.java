package no.uib.inf112.map.npcs.pathfinding;

import java.util.*;

import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.enums.PathType;

public class Pathfinder {

    private final IGrid grid;

    public Pathfinder(IGrid grid) { // Uses A* algorithm.
        this.grid = grid;

    }

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
                return smoothPath(reconstructPath(cameFrom, current), size);
            }

            closed.add(current);

            for (ICell neighbor : grid.getNeighbours(current)) {
                if (neighbor == null || !canEnter(neighbor, size)) {
                    continue;
                }
                if (closed.contains(neighbor)) {
                    continue;
                }
                double tentativeG = score.get(current) + stepCost(current, neighbor) + neighbor.getWeight();

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

    private double heuristic(ICell a, ICell b) {
        return grid.distance(a, b);
    }

    private double stepCost(ICell from, ICell to) {
        int dx = Math.abs(from.col() - to.col());
        int dy = Math.abs(from.row() - to.row());
        return (dx == 1 && dy == 1) ? Math.sqrt(2) : 1.0; // forsøk på diagonal
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




    private List<ICell> smoothPath(List<ICell> path, EnemySize size) {
        if (path == null || path.size() <= 2) return path;

        ArrayList<ICell> out = new ArrayList<>();
        int i = 0;
        out.add(path.get(0));

        while (i < path.size() - 1) {
            int best = i + 1;

            for (int j = i + 1; j < path.size(); j++) {
                if (hasLineOfSight(path.get(i), path.get(j), size)) {
                    best = j;
                } else {
                    break;
                }
            }

            out.add(path.get(best));
            i = best;
        }

        return out;
    }

    private boolean hasLineOfSight(ICell a, ICell b, EnemySize size) {
        int x = a.col(), y = a.row();
        int x1 = b.col(), y1 = b.row();

        int dx = Math.abs(x1 - x), dy = -Math.abs(y1 - y);
        int sx = x < x1 ? 1 : -1,  sy = y < y1 ? 1 : -1;
        int err = dx + dy;

        while (true) {
            ICell cell = grid.getCell(y, x);
            if (cell == null || !canEnter(cell, size) || cell.getWeight() > 3) return false;
            if (x == x1 && y == y1) return true;
            int e2 = 2 * err;
            int nextX = x, nextY = y;
            if (e2 >= dy) { err += dy; nextX += sx; }
            if (e2 <= dx) { err += dx; nextY += sy; }
            if (nextX != x && nextY != y) {
                if (!canEnter(grid.getCell(y, nextX), size) ||
                        !canEnter(grid.getCell(nextY, x), size)) return false;
            }

            x = nextX;
            y = nextY;
        }
    }

}
