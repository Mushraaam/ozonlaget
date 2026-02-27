package no.uib.inf112.interfaces;

import java.awt.geom.Rectangle2D;

import no.uib.inf112.enums.GunType;

public interface IPlayer {

    public Rectangle2D.Double getHitbox();

    public GunType gun();

    public void setGun(GunType gun);

}
