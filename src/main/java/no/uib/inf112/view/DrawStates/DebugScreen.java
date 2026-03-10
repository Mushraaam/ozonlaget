package no.uib.inf112.view.DrawStates;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Map;
import no.uib.inf112.utility.PerfTracker;
import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.interfaces.IMap;

public class DebugScreen implements IDrawer {

    private IMap map;
    private IGrid grid;
    private long lastFrameNs = System.nanoTime();
    private double frameMs = 0.0;
    private double fps = 0.0;
    private double smoothedMs = 0;

    public DebugScreen(IMap map) {
        this.map = map;
        this.grid = this.map.getGrid();
    }

    @Override
    public void draw(Graphics2D graphic) {
        
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

        // Draw enemy paths
        graphic.setColor(Color.RED);
        graphic.setStroke(new java.awt.BasicStroke(2f));


        for (IEnemy enemy : this.map.getEnemies()) {
            List<ICell> path = enemy.getCurrentPath();
            if (path == null || path.size() < 2) return;

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

                    graphic.fillOval((int)(cx - dotRadius), (int)(cy - dotRadius), dotRadius * 2, dotRadius * 2);

                    if (i < path.size() - 1) {
                        ICell next = path.get(i + 1);
                        Rectangle2D.Double nextBounds = next.getBounds();

                        graphic.drawLine(
                                (int)cx, (int)cy,
                                (int)nextBounds.getCenterX(), (int)nextBounds.getCenterY()
                        );
                    }
                }
            }

        //DRAW STATS
        PerfTracker.tick(true); // Increment FPS
        AffineTransform old = graphic.getTransform();
        graphic.setTransform(new AffineTransform());
        graphic.setFont(new Font("Monospaced", Font.BOLD, 14));
        int x = 20;
        int y = 30;
        graphic.setColor(Color.WHITE);
        graphic.drawString(String.format("FPS: %3.0f | UPS: %3.0f", PerfTracker.fps, PerfTracker.ups), x, y);
        y += 20;
        graphic.drawString(String.format("npc count: %d", map.getEnemyCount()), x, y);
        y += 20;
        graphic.drawString("--- TASK BREAKDOWN ---", x, y);
        for (Map.Entry<String, Double> entry : PerfTracker.taskMs.entrySet()) {
            y += 20;
            double time = entry.getValue();
            graphic.drawString(String.format("%-15s: %6.2f ms", entry.getKey(), time), x, y);
        }
        graphic.setTransform(old);

    }
}

}