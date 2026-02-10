package no.uib.inf112.view.DrawStates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import no.uib.inf112.interfaces.ICell;
import no.uib.inf112.interfaces.IDrawer;
import no.uib.inf112.interfaces.IMap;

public class DebugScreen implements IDrawer {

    private IMap map;

    public DebugScreen(IMap map) {
        this.map = map;
    }

    @Override
    public void draw(Graphics2D graphic) {
        graphic.setColor(Color.BLACK);
        // åpenbart ordne i denne
        ArrayList<ArrayList<ICell>> grid = this.map.getGrid().getGrid();

        for (ArrayList<ICell> row : grid) {
            for (ICell cell : row) {
                if (isVisible(graphic, cell.getBounds())) {
                    if (cell.isBlocked()) {
                        graphic.fill(cell.getBounds());
                    } else {
                        graphic.draw(cell.getBounds());
                    }
                }
            }
        }
    }

}
