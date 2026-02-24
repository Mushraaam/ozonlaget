package no.uib.inf112.enums;

public enum EnemySize {
    SMALL(4),
    MEDIUM(6),
    LARGE(8);

    private final int footprintValue;

    EnemySize(int footprintValue) {
        this.footprintValue = footprintValue;
    }

    /**
     * @return The length of one side of the square footprint in grid cells.
     */
    public int footprint() {
        return this.footprintValue;
    }

}