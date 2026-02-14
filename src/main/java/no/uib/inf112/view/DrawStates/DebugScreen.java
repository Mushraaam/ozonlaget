package no.uib.inf112.view.DrawStates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.interfaces.IGrid;
import no.uib.inf112.interfaces.IMap;

public class DebugScreen implements IDrawer {

    private IMap map;
    private IGrid grid;

    public DebugScreen(IMap map) {
        this.map = map;
        this.grid = this.map.getGrid();
    }

    @Override
    public void draw(Graphics2D graphic) {

        // Draw grid cells
        debugCellsInView(graphic, this.grid);

        // Draw player hitbox
        graphic.setColor(Color.BLUE);
        Rectangle2D.Double hitbox = map.getPlayer().getHitbox();
        graphic.fill(hitbox);

        // Draw enemy hitbox
        for (IEnemy enemy : this.map.getEnemies()) {
            hitbox = enemy.getHitbox();
            if (isVisible(graphic, hitbox)) {
                graphic.fill(hitbox);

            }
        }

        // Draw enemy paths
        graphic.setColor(Color.RED);

        for (IEnemy enemy : this.map.getEnemies()) {
            List<ICell> path = enemy.getCurrentPath();
            if (path == null || path.isEmpty())
                continue;

            for (ICell step : path) {
                Rectangle2D.Double r = step.getBounds();
                if (isVisible(graphic, r)) {

                    double cx = r.getCenterX();
                    double cy = r.getCenterY();

                    double radius = Math.min(r.getWidth(), r.getHeight()) * 0.25;
                    double d = radius * 2;

                    graphic.fill(new Ellipse2D.Double(
                            cx - radius,
                            cy - radius,
                            d,
                            d));
                }
            }
        }

    }

}
