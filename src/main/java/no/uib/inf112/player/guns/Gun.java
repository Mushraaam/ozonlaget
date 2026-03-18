package no.uib.inf112.player.guns;

import no.uib.inf112.enums.BuffType;
import no.uib.inf112.enums.GunType;
import no.uib.inf112.interfaces.IGun;

public abstract class Gun implements IGun {
    private GunType gunType;
    private int maxAmmo;
    private int currentAmmunition;
    private int fireRate;
    private double accuracy;
    private int range;
    private int damage;
    private int reloadDelay;

    public Gun(int fireRate) {
        this.fireRate = fireRate;
        this.reloadDelay = 0;
    }

    @Override
    public boolean shoot(BuffType type) {
        if (this.reloadDelay > 0) {
            return true;
        }
        if (this.currentAmmunition > 0) {
            this.currentAmmunition--;
        }
        if (type == BuffType.RAINBOW) {
            this.currentAmmunition = this.maxAmmo;
        }
        this.reloadDelay = this.fireRate;
        return this.currentAmmunition <= 0;
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

    @Override
    public int damage(BuffType type) {

        if (type == BuffType.RAINBOW) {
            return this.damage * 2;
        }

        return this.damage;
    }

    @Override
    public int range() {
        return this.range;
    }

    @Override
    public int fireRate() {
        return this.fireRate;
    }

    @Override
    public double accuracy() {
        return this.accuracy;
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

    protected void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    protected void setRange(int range) {
        this.range = range;
    }

    protected void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public void reload() {
        if (this.reloadDelay > 0) {
            this.reloadDelay--;
        }
    }

}
