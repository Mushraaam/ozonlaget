package no.uib.inf112.enums;

import no.uib.inf112.config.Config;
import java.awt.Color;

public enum EnemySize {
    SMALL((int)Config.getInt("smallEnemy")/10, Color.lightGray),
    MEDIUM((int)Config.getInt("mediumEnemy")/10, Color.CYAN),
    LARGE((int)Config.getInt("largeEnemy")/10, Color.GREEN);

    private final int footprintValue;
    private final Color debugColor;

    EnemySize(int footprintValue, Color color) {

        this.footprintValue = footprintValue;
        this.debugColor = color;
    }

    /**
     * @return The length of one side of the square footprint in grid cells.
     */
    public int footprint() {
        return this.footprintValue;
    }

    public Color getDebugColor() {
        return debugColor;
    }
}