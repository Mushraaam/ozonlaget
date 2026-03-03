package no.uib.inf112.map;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.FloorType;
import no.uib.inf112.enums.PathType;
import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.interfaces.IStaticObject;

public class Grid implements IGrid {

    private ArrayList<ArrayList<ICell>> cellGrid;

    private Rectangle2D.Double bounds;
    private IMap map;

    // Represent pixel width
    private final static int CELLWIDTH = Config.getInt("cellWidth");
    private final static int CELLHEIGHT = Config.getInt("cellHeight");

    private int colCount;
    private int rowCount;

    private final HashMap<ICell, HashMap<Integer, List<ICell>>> neighbourMap = new HashMap<>();

    public Grid(IMap map) {
        this.map = map;
        this.bounds =this.map.getBounds();

        if (this.bounds.getX() != 0 || this.bounds.getY() != 0) {
            throw new IllegalArgumentException("Bounds for Map must start with x, y = 0");
        }

        double height = this.bounds.getHeight();
        double width = this.bounds.getWidth();

        // Not sure if i shuld bother with ceil/floor
        this.colCount = (int) Math.floor(width / CELLWIDTH);
        this.rowCount = (int) Math.floor(height / CELLHEIGHT);

        this.cellGrid = makeGrid(this.rowCount, this.colCount, CELLWIDTH, CELLHEIGHT, FloorType.NONE);
        fillGrid(this.cellGrid, map.getStaticObjects());

        int[] levelsToCompute = {1, 2, 3, 4, 5, 6};
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                ICell cell = getCell(r, c);
                HashMap<Integer, List<ICell>> levels = new HashMap<>();
                for (int level : levelsToCompute) {
                    levels.put(level, neighbourLevelBFS(cell, level));
                }
                neighbourMap.put(cell, levels);
            }
        }

    }

    @Override
    public List<ICell> getNeighboursAtDepth(ICell cell, int depth) {
        if (cell == null || !neighbourMap.containsKey(cell)) {
            return Collections.emptyList();
        }
        return neighbourMap.get(cell).getOrDefault(depth, Collections.emptyList());
    }

    private List<ICell> neighbourLevelBFS(ICell start, int maxDepth) {
        List<ICell> result = new ArrayList<>();
        HashSet<ICell> visited = new HashSet<>();
        List<ICell> currentLevel = new ArrayList<>();

        currentLevel.add(start);
        visited.add(start);

        for (int i = 0; i < maxDepth; i++) {
            List<ICell> nextLevel = new ArrayList<>();
            for (ICell cell : currentLevel) {
                for (ICell n : getNeighbours(cell)) {
                    if (visited.add(n)) {
                        nextLevel.add(n);
                        result.add(n);
                    }
                }
            }
            currentLevel = nextLevel;
        }
        return result;
    }

    /**
     * Should only be run once in constructor else your pc will break
     * @param grid
     * @param blockers
     */
    private void fillGrid(ArrayList<ArrayList<ICell>> grid, ArrayList<IStaticObject> blockers) {

        //Sets illegal cells for all enemies
        for (ArrayList<ICell> row : grid) {
            for (ICell cell : row) {
                for (IStaticObject blocker : blockers) {
                    if (cell.getBounds().intersects(blocker.getBounds())) {
                        cell.setPathType(PathType.BLOCKED);
                    }
                }
            }
        }

        //Sets illegal cells for medium and large enemies
        for (ArrayList<ICell> row : grid) {
            for (ICell cell : row) {
                if (cell.pathType() == PathType.BLOCKED) {
                    for (ICell neighbour : getNeighbours(cell)) {
                        if (neighbour.pathType() != PathType.BLOCKED) {
                            neighbour.setPathType(PathType.BLOCKED_FOR_MEDIUM);
                        }
                    }
                }
            }
        }

        //Sets illegal cells for large enemies
        for (ArrayList<ICell> row : grid) {
            for (ICell cell : row) {
                if (cell.pathType() == PathType.BLOCKED_FOR_MEDIUM) {
                    for (ICell neighbour : getNeighbours(cell)) {
                        PathType type = neighbour.pathType();
                        if (type != PathType.BLOCKED && type != PathType.BLOCKED_FOR_MEDIUM) {
                            neighbour.setPathType(PathType.BLOCKED_FOR_LARGE);
                        }
                    }
                }
            }
        }
    }

    public ICell getCell(int row, int col) {
        if (row < 0 || row >= rowCount || col < 0 || col >= colCount) {
            return null;
        }
        return cellGrid.get(row).get(col);
    }

    public ArrayList<ICell> getNeighbours(ICell cell) {

        if (cell == null) {
            throw new NullPointerException("Cell cannot be null");
        }

        int row = cell.row();
        int col = cell.col();

        if (row < 0 || col < 0 || row >= this.rowCount || col >= this.colCount) {
            throw new IllegalArgumentException(
                    String.format("Cell: Row %s, Col %s is out of bounds", row, col));
        }

        ArrayList<ICell> neighbours = new ArrayList<>();

        ICell over = null;
        ICell under = null;
        ICell left = null;
        ICell right = null;

        if (row > 0) {
            over = getCell(row - 1, col);
            neighbours.add(over);
        }
        if (row < this.rowCount - 1) {
            under = getCell(row + 1, col);
            neighbours.add(under);
        }
        if (col > 0) {
            left = getCell(row, col - 1);
            neighbours.add(left);
        }
        if (col < this.colCount - 1) {
            right = getCell(row, col + 1);
            neighbours.add(right);
        }

        if (over != null && left != null && !over.isBlocked() && !left.isBlocked()) {
            neighbours.add(getCell(row - 1, col - 1)); // up-left
        }
        if (over != null && right != null && !over.isBlocked() && !right.isBlocked()) {
            neighbours.add(getCell(row - 1, col + 1)); // up-right
        }
        if (under != null && left != null && !under.isBlocked() && !left.isBlocked()) {
            neighbours.add(getCell(row + 1, col - 1)); // down-left
        }
        if (under != null && right != null && !under.isBlocked() && !right.isBlocked()) {
            neighbours.add(getCell(row + 1, col + 1)); // down-right
        }

        return neighbours;
    }


    @Override
    public Iterator<ICell> iterator() {
        ArrayList<ICell> flattenedList = new ArrayList<>(rowCount * colCount);
        for (ArrayList<ICell> row : this.cellGrid) {
            flattenedList.addAll(row);
        }
        return flattenedList.iterator();
    }

    @Override
    public double distance(ICell from, ICell to) {
        int dx = from.col() - to.col();
        int dy = from.row() - to.row();
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public ICell getCellFromPos(Rectangle2D.Double pos) {
        double centerX = pos.getCenterX();
        double centerY = pos.getCenterY();
        return getCellFromXY(centerX, centerY);
    }

    public ICell getCellFromXY(double x, double y) {
        int col = (int) Math.floor(x / CELLWIDTH);
        int row = (int) Math.floor(y / CELLHEIGHT);
        return getCell(row, col);
    }

    public ArrayList<ICell> getCellsInView(Graphics2D graphics) {
        // double x1 = Math.min(this.bounds.getWidth(), Math.max(0,
        // graphics.getClipBounds().getMinX()));
        // double y1 = Math.min(this.bounds.getHeight(), Math.max(0,
        // graphics.getClipBounds().getMinY()));
        // double x2 = Math.min(this.bounds.getWidth(), Math.max(0,
        // graphics.getClipBounds().getMaxX()));
        // double y2 = Math.min(this.bounds.getHeight(), Math.max(0,
        // graphics.getClipBounds().getMaxY()));

        // ICell topLeft = getCellFromXY(x1, y1);
        // ICell botRight = getCellFromXY(x2, y2);

        // int startRow = topLeft.row();
        // int endRow = botRight.row();
        // int startCol = topLeft.col();
        // int endCol = botRight.col();

        // optimalisert versjon av kommenter kode over
        Rectangle2D clip = graphics.getClipBounds();
        int startCol = (int) Math.floor(clip.getMinX() / CELLWIDTH);
        int endCol = (int) Math.floor((clip.getMaxX() - 1) / CELLWIDTH);
        int startRow = (int) Math.floor(clip.getMinY() / CELLHEIGHT);
        int endRow = (int) Math.floor((clip.getMaxY() - 1) / CELLHEIGHT);

        startCol = Math.max(0, Math.min(startCol, colCount - 1));
        endCol = Math.max(0, Math.min(endCol, colCount - 1));
        startRow = Math.max(0, Math.min(startRow, rowCount - 1));
        endRow = Math.max(0, Math.min(endRow, rowCount - 1));

        ArrayList<ICell> inView = new ArrayList<>();
        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                inView.add(getCell(row, col));
            }
        }

        return inView;
    }

    @Override
    public int getCellWidth() {
        return CELLWIDTH;
    }

    @Override
    public int getCellHeight() {
        return CELLHEIGHT;
    }

    @Override
    public int getRowCount() {
        return this.rowCount;
    }

    @Override
    public int getColCount() {
        return this.colCount;
    }

}
