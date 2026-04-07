package no.uib.inf112.view.drawstates;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;
import no.uib.inf112.interfaces.*;
import no.uib.inf112.model.npcs.factory.SpawnPoint;

public class DebugScreen implements IDrawer {

    private IModel map;
    private IGrid grid;

    public DebugScreen(IModel map) {
        this.map = map;
        this.grid = this.map.getGrid();
    }

    @Override
    public void draw(Graphics2D graphic) {

        // Draw spawn zones
        drawSpawnZones(graphic);

        // Draw grid cells
        graphic.setStroke(new BasicStroke(1));
        debugCellsInView(graphic, this.grid);

        // Draw player hitbox
        graphic.setColor(Color.BLUE);
        Rectangle2D.Double hitbox = map.getPlayer().getHitbox();
        graphic.fill(hitbox);

        // Draw enemy hitbox
        for (IEnemy enemy : this.map.getEnemies()) {
            hitbox = enemy.getHitbox();
            if (isVisible(graphic, hitbox)) {
                graphic.setColor(enemy.size().getDebugColor());
                graphic.fill(hitbox);
            }
        }

        // Draw item hitboxes
        graphic.setColor(Color.PINK);
        for (ICollectable item : this.map.getActiveItems()) {
            if (isVisible(graphic, item.getHitbox())) {
                graphic.fill(item.getHitbox());
            }
        }

        // Draw player location
        Rectangle2D.Double playerLoc = map.getPlayer().getHitbox();
        Font font = graphic.getFont();
        graphic.setFont(new Font(font.getName(), font.getStyle(), 10));

        String coordsText = "x: " + playerLoc.getX() + "   y: " + playerLoc.getY();
        int textX = (int) playerLoc.getX();
        int textY = (int) playerLoc.getY() - 20;
        graphic.setColor(Color.BLACK);
        graphic.fillRect(textX - 2, textY - 10, 150, 14);
        graphic.setColor(Color.WHITE);
        graphic.drawString(coordsText, textX, textY);

        // Draw enemy paths
        graphic.setColor(Color.RED);
        graphic.setStroke(new java.awt.BasicStroke(2f));

        for (IEnemy enemy : this.map.getEnemies()) {
            List<ICell> path = enemy.getCurrentPath();
            if (path == null || path.size() < 2)
                return;

            Rectangle viewBounds = graphic.getClipBounds();

            graphic.setColor(new Color(255, 0, 0, 150));
            graphic.setStroke(new BasicStroke(1.5f));

            for (int i = 0; i < path.size(); i++) {
                ICell current = path.get(i);
                Rectangle2D.Double currentBounds = current.getBounds();

                if (viewBounds.intersects(currentBounds)) {
                    double cx = currentBounds.getCenterX();
                    double cy = currentBounds.getCenterY();
                    int dotRadius = 3;

                    graphic.fillOval((int) (cx - dotRadius), (int) (cy - dotRadius), dotRadius * 2, dotRadius * 2);

                    if (i < path.size() - 1) {
                        ICell next = path.get(i + 1);
                        Rectangle2D.Double nextBounds = next.getBounds();

                        graphic.drawLine(
                                (int) cx, (int) cy,
                                (int) nextBounds.getCenterX(), (int) nextBounds.getCenterY());
                    }
                }
            }
        }
    }

    private void drawSpawnZones(Graphics2D graphic) {
        for (SpawnPoint point : this.map.getSpawnPoints()) {
            graphic.setColor(Color.GREEN);
            graphic.fill(point.bounds());
        }
    }

}