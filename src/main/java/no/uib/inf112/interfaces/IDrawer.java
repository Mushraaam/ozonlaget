package no.uib.inf112.interfaces;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import no.uib.inf112.enums.PathType;
import no.uib.inf112.utility.ImageHandler;
import java.awt.Color;

public interface IDrawer {

    /**
     * Draws an image
     * Visibility should be checked before calling this function
     * @param graphic
     * @param image   - Image to be drawn
     * @param bounds  - Bounds for the image
     */
    default void drawImage(Graphics2D graphic, BufferedImage image, Rectangle2D.Double bounds) {
        graphic.drawImage(image,
                (int) bounds.getX(),
                (int) bounds.getY(),
                (int) bounds.getWidth(),
                (int) bounds.getHeight(),
                null);

    }

    /**
     * Checks if an object is in view
     * @param graphic
     * @param objectBounds
     * @return true if object is withing view bounds.
     *         Use to decide if something should be drawn or not
     */
    default boolean isVisible(Graphics2D graphic, Rectangle2D.Double objectBounds) {
        Rectangle2D clip = graphic.getClipBounds();
        return objectBounds.intersects(clip);
    }

    /**
     * Draws cells in view
     * Uses some math to calculate only the cells that should be drawn before drawing (is in view)
     * @param graphics
     * @param grid
     * @param handler
     * @param debug - if this is run from debug mode or not, changes behavior
     */
    default void drawCellsInView(Graphics2D graphics, IGrid grid, ImageHandler handler, boolean debug) {

        int cellWidth = grid.getCellWidth();
        int cellHeight = grid.getCellHeight();
        int colCount = grid.getColCount();
        int rowCount = grid.getRowCount();

        Rectangle2D clip = graphics.getClipBounds();
        int startCol = (int) Math.floor(clip.getMinX() / cellWidth);
        int endCol = (int) Math.floor((clip.getMaxX() - 1) / cellWidth);
        int startRow = (int) Math.floor(clip.getMinY() / cellHeight);
        int endRow = (int) Math.floor((clip.getMaxY() - 1) / cellHeight);

        startCol = Math.clamp(startCol, 0, colCount - 1);
        endCol = Math.clamp(endCol, 0, colCount - 1);
        startRow = Math.clamp(startRow, 0, rowCount - 1);
        endRow = Math.clamp(endRow, 0, rowCount - 1);

        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                if (!debug) {
                    ICell cell = grid.getCell(row, col);
                    drawImage(graphics, handler.getFloor(cell.floorType()), cell.getBounds());
                } else {
                    ICell cell = grid.getCell(row, col);
                    PathType type = cell.pathType();
                    graphics.setColor(Color.DARK_GRAY);
                    if (type == PathType.UNBLOCKED) {
                        graphics.draw(cell.getBounds());
                    } else if (type == PathType.BLOCKED) {
                        graphics.fill(cell.getBounds());
                    } else if (type == PathType.BLOCKED_FOR_MEDIUM) {
                        graphics.setColor(Color.DARK_GRAY);
                        graphics.fill(cell.getBounds());
                    } else if (type == PathType.BLOCKED_FOR_LARGE) {
                        graphics.setColor(Color.LIGHT_GRAY);
                        graphics.fill(cell.getBounds());
                    }
                }
            }
        }
    }

    default void debugCellsInView(Graphics2D graphics, IGrid grid) {
        drawCellsInView(graphics, grid, null, true);
    }

    /**
     * Draws the screen for the corresponding class
     * 
     * @param graphic
     */
    public void draw(Graphics2D graphic);

}
