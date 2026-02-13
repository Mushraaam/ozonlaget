package no.uib.inf112.view.DrawStates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.interfaces.IMap;

public class DebugScreen implements IDrawer {

    private IMap map;

    public DebugScreen(IMap map) {
        this.map = map;
    }

    @Override
    public void draw(Graphics2D graphic) {
        graphic.setColor(new Color(0, 0, 0, 120));
        
        for (ICell cell : this.map.getGrid()) {
            if (isVisible(graphic, cell.getBounds())) {
                if (cell.isBlocked()) {
                    graphic.fill(cell.getBounds());
                } else {
                    graphic.draw(cell.getBounds());
                }
            }
        }


        //Draw player hitbox
        graphic.setColor(new Color(255, 0, 0, 120));
        Rectangle2D.Double hitbox = map.getPlayer().getHitbox();
        graphic.fill(hitbox);

        //Draw enemy hitbox
        for (IEnemy enemy : this.map.getEnemies()){
            hitbox = enemy.getHitbox();
            graphic.fill(hitbox);
        }

        // Draw enemy paths
        graphic.setColor(new Color(255, 0, 0, 160));

        for (IEnemy enemy : this.map.getEnemies()) {
            List<ICell> path = enemy.getCurrentPath();
            if (path == null || path.isEmpty()) continue;

            for (ICell step : path) {
                Rectangle2D r = step.getBounds();
                double cx = r.getCenterX();
                double cy = r.getCenterY();

                double radius = Math.min(r.getWidth(), r.getHeight()) * 0.25;
                double d = radius * 2;

                graphic.fill(new Ellipse2D.Double(
                        cx - radius,
                        cy - radius,
                        d,
                        d
                ));
            }
        }



    }

}
