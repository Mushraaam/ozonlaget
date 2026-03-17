package no.uib.inf112.player.guns;

import no.uib.inf112.enums.GunType;

public class DEagle extends Gun {

    public static final GunType GUNTYPE = GunType.DEAGLE;
    private static final int MAX_AMMO = 100;

    public DEagle() {
        super(20);
        setMaxAmmo(MAX_AMMO);
        setCurrentAmmo(MAX_AMMO);
        setGunType(GUNTYPE);
        setAccuracy(0.1);
        setRange(550);
        setDamage(50);
    }
}
