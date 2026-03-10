package no.uib.inf112.interfaces;

public interface IMovingDrawableObject {

    /**
     * @param grid Current grid.
     *             Moves the object.
     * @param dt
     */
    void move(IGrid grid, double dt);
}
