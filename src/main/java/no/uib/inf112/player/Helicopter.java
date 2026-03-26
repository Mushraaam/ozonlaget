package no.uib.inf112.player;

import java.awt.geom.Rectangle2D;

import no.uib.inf112.interfaces.IVehicle;

public class Helicopter implements IVehicle {

    private int GAS_NEEDED = 6;
    private int gasDeposited = 0;
    private static final int ANIMATION_COUNT = 7;
    private static final int EXPAND_LIMIT = 60;
    private static final double SPEED = 6;
    private Rectangle2D.Double bounds;

    private int index;
    private int expansion;

    public Helicopter(Rectangle2D.Double bounds) {
        this.bounds = bounds;
        this.index = 0;
    }

    @Override
    public boolean isFuelFull() {
        return this.gasDeposited >= GAS_NEEDED;
    }

    @Override
    public void depositGas() {
        gasDeposited++;
    }

    public int getIndex() {
        return this.index;
    }

    public Rectangle2D.Double getBounds() {
        return this.bounds;
    }

    @Override
    public void increment() {
        this.index = (this.index + 1) % ANIMATION_COUNT;
        expandBounds();
    }

    private void expandBounds() {
        if (this.expansion < EXPAND_LIMIT) {
            this.expansion++;
            this.bounds = new Rectangle2D.Double(
                    this.bounds.getX() - 0.05 * expansion,
                    this.bounds.getY() - 0.05 * expansion,
                    this.bounds.width + 0.1 * expansion,
                    this.bounds.height + 0.1 * expansion);
        }
        if (this.expansion == EXPAND_LIMIT) {
            move();
        }

    }

    private void move() {

        this.bounds = new Rectangle2D.Double(
                this.bounds.getX() + SPEED,
                this.bounds.getY(),
                this.bounds.getWidth(),
                this.bounds.getHeight());

    }

}
