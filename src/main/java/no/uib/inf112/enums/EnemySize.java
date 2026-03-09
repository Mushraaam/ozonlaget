package no.uib.inf112.enums;

import java.awt.Color;

public enum EnemySize {
    SMALL(4, Color.lightGray),
    MEDIUM(6, Color.yellow),
    LARGE(8, Color.blue);

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