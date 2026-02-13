package no.uib.inf112.view.DrawStates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

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
        graphic.setColor(Color.BLACK);
        
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
        Rectangle2D.Double hitbox = map.getPlayer().getHitbox();
        graphic.setColor(new Color(255, 0, 0, 120));
        graphic.fill(hitbox);

        //Draw enemy hitbox
        for (IEnemy enemy : this.map.getEnemies()){
            hitbox = enemy.getHitbox();
            graphic.setColor(new Color(255, 0, 0, 120));
            graphic.fill(hitbox);
        }


        
    }

}
