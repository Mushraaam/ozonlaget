package no.uib.inf112.interfaces;

import java.awt.geom.Rectangle2D;

public interface ICell {
    

    public Rectangle2D.Double getBounds();

    public boolean isBlocked();

    public void block();

    public void unblock();

    public int row();

    public int col();
}
