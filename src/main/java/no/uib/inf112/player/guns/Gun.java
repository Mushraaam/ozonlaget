package no.uib.inf112.player.guns;

import no.uib.inf112.enums.GunType;
import no.uib.inf112.interfaces.IGun;

public abstract class Gun implements IGun {
    private GunType gunType;
    private int maxAmmo;
    private int currentAmmunition;

    public Gun() {
    }

    @Override
    public int maxAmmunition() {
        return maxAmmo;
    }

    @Override
    public int currentAmmunition() {
        return this.currentAmmunition;
    }

    @Override
    public GunType type() {
        return gunType;
    }

    // Setters for constructors

    protected void setCurrentAmmo(int ammo) {
        this.currentAmmunition = ammo;
    }

    protected void setGunType(GunType type) {
        this.gunType = type;
    }

    protected void setMaxAmmo(int maxAmmo) {
        this.maxAmmo = maxAmmo;
    }
    
}
