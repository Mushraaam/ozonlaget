package no.uib.inf112.interfaces;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import no.uib.inf112.enums.EnemySize;
import no.uib.inf112.enums.PathType;
import no.uib.inf112.map.NavigationLane;
import no.uib.inf112.utility.ImageHandler;

public interface IDrawer {

    /**
     * Draws an image
     *
     * @param graphic
     * @param image   - Image to be drawn
     * @param bounds  - Bounds for the image
     */
    default void drawImage(Graphics2D graphic, BufferedImage image, Rectangle2D.Double bounds) {
        if (isVisible(graphic, bounds)) {
            graphic.drawImage(image,
                    (int) bounds.getX(),
                    (int) bounds.getY(),
                    (int) bounds.getWidth(),
                    (int) bounds.getHeight(),
                    null);
        }
    }

    /**
     * @param graphic
     * @param objectBounds
     * @return true if object is withing view bounds
     * Use to decide if something should be drawn or not
     */
    default boolean isVisible(Graphics2D graphic, Rectangle2D.Double objectBounds) {
        Rectangle2D clip = graphic.getClipBounds();
        return objectBounds.intersects(clip);
    }

    default void drawCellsInView(Graphics2D graphics, IGrid grid, ImageHandler handler) {

        int cellWidth = grid.getCellWidth();
        int cellHeight = grid.getCellHeight();
        int colCount = grid.getColCount();
        int rowCount = grid.getRowCount();

        Rectangle2D clip = graphics.getClipBounds();
        int startCol = (int) Math.floor(clip.getMinX() / cellWidth);
        int endCol = (int) Math.floor((clip.getMaxX() - 1) / cellWidth);
        int startRow = (int) Math.floor(clip.getMinY() / cellHeight);
        int endRow = (int) Math.floor((clip.getMaxY() - 1) / cellHeight);

        startCol = Math.max(0, Math.min(startCol, colCount - 1));
        endCol = Math.max(0, Math.min(endCol, colCount - 1));
        startRow = Math.max(0, Math.min(startRow, rowCount - 1));
        endRow = Math.max(0, Math.min(endRow, rowCount - 1));

        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                ICell cell = grid.getCell(row, col);
                drawImage(graphics, handler.getFloor(cell.floorType()), cell.getBounds());
            }
        }
    }

    default void debugCellsInView(Graphics2D graphics, IGrid grid, EnemySize laneSize) {
        int cellWidth = grid.getCellWidth();
        int cellHeight = grid.getCellHeight();
        int colCount = grid.getColCount();
        int rowCount = grid.getRowCount();

        Rectangle2D clip = graphics.getClipBounds();
        int startCol = (int) Math.floor(clip.getMinX() / cellWidth);
        int endCol = (int) Math.floor((clip.getMaxX() - 1) / cellWidth);
        int startRow = (int) Math.floor(clip.getMinY() / cellHeight);
        int endRow = (int) Math.floor((clip.getMaxY() - 1) / cellHeight);

        startCol = Math.max(0, Math.min(startCol, colCount - 1));
        endCol = Math.max(0, Math.min(endCol, colCount - 1));
        startRow = Math.max(0, Math.min(startRow, rowCount - 1));
        endRow = Math.max(0, Math.min(endRow, rowCount - 1));
        int step = laneSize.footprint();
        int count = 0;
        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                ICell cell = grid.getCell(row, col);
                PathType type = cell.pathType();
                graphics.setColor(Color.DARK_GRAY);
                if (type == PathType.UNBLOCKED) {
                    graphics.draw(cell.getBounds());
                } else if (type == PathType.BLOCKED) {
                    graphics.setColor(Color.BLACK);
                    graphics.fill(cell.getBounds());
                } else if (type == PathType.BLOCKED_FOR_MEDIUM) {
                    graphics.setColor(Color.DARK_GRAY);
                    graphics.fill(cell.getBounds());
                } else if (type == PathType.BLOCKED_FOR_LARGE) {
                    graphics.setColor(Color.LIGHT_GRAY);
                    graphics.fill(cell.getBounds());
                }
                if (count % step == 0 ) {
                    NavigationLane lane = (cell.getNavigationLane(laneSize));
                    if (lane != null) {
                        graphics.setColor(laneSize.getDebugColor());
                        graphics.draw(lane.getBounds());
                    }
                }
                count++;

            }
        }
    }

    /**
     * Draws the screen for the corresponding class
     *
     * @param graphic
     */
    public void draw(Graphics2D graphic);

}
