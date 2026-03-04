package no.uib.inf112.player.guns;

import no.uib.inf112.enums.GunType;
import no.uib.inf112.interfaces.IGun;

public class MP5 implements IGun{

    private static final int MAX_AMMO = 300;
    private static final GunType GUN_TYPE = GunType.MP5;
    private int currentAmmo;

    public MP5(){
        this.currentAmmo = MAX_AMMO;
    }

    @Override
    public int maxAmmunition() {
        return MAX_AMMO;
    }

    @Override
    public int currentAmmunition() {
        return this.currentAmmo;
    }

    @Override
    public GunType type() {
        return GUN_TYPE;
    }
    
}
