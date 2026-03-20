package no.uib.inf112.enums;

public enum Direction {
    EAST(1, 0),
    NORTH_EAST(1, -1),
    NORTH(0, -1),
    NORTH_WEST(-1, -1),
    WEST(-1, 0),
    SOUTH_WEST(-1, 1),
    SOUTH(0, 1),
    SOUTH_EAST(1, 1);

    final int dx;
    final int dy;
    public final double radians;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
        this.radians = Math.atan2(dy, dx);
    }
}
