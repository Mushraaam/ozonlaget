package no.uib.inf112.interfaces;

import java.awt.geom.Rectangle2D;

import no.uib.inf112.enums.GunType;

public interface IPlayer {

    public Rectangle2D.Double getHitbox();

    public GunType gunType();

    /**
     * @param gun type of currently equipped gun
     */
    public void setGunType(GunType gun);

    /**
     * @return current ammunition of equipped weapon
     */
    public int currentAmmunition();

    /**
     * @return max ammunition of equipped weapon
     */
    public int maxAmmunition();

    /**
     * @return player max HP
     */
    public int getMaxHP();

    /**
     * @return player current HP
     */
    public int getCurrentHP();
}
