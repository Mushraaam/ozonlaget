package no.uib.inf112.interfaces;

import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import no.uib.inf112.enums.BuffType;
import no.uib.inf112.enums.GunType;
import no.uib.inf112.utility.SoundHandler;

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

    /**
     * Increase player hp by heal amount
     * @param heal
     */
    public void healHP(int heal);


    /**
     * deals damage to player
     * @param damage
     */
    public void takeDamage(int damage);

    /**
     * Shoots towards mouseClick
     * @param e
     * @return true if successful shot
     */
    public boolean shoot(MouseEvent e);

    /**
     * @return firerate of current gun - ms between shots
     */
    public int fireRate();

    /**
     * @return type of current buff - BuffType.NONE if no buff active
     */
    public BuffType buffType();

    /**
     * Applies buff of corresponding type.
     * @param type
     * @param soundHandler 
     */
    public void setBuff(BuffType type, SoundHandler soundHandler);

    /**
     * @return remaining time of buff
     */
    public int buffCountDown();

    /**
     * Decrements time remaining on buffcounter
     */
    public void decrementBuff(SoundHandler soundHandler);

    public int getArmor();
}

