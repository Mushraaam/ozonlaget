package no.uib.inf112.player.guns;

import no.uib.inf112.enums.GunType;
import no.uib.inf112.interfaces.IGun;

public class DEagle implements IGun {

    public static final GunType GUNTYPE = GunType.DEAGLE;
    private static final int MAX_AMMO = 100;
    private int currentAmmunition;

    public DEagle(){
        this.currentAmmunition = MAX_AMMO;
    }

    @Override
    public int maxAmmunition() {
        return MAX_AMMO;
    }

    @Override
    public int currentAmmunition() {
        return this.currentAmmunition;
    }

    @Override
    public GunType type() {
        return GUNTYPE;
    }
    
}
