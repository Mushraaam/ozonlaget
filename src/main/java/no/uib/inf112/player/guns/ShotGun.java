package no.uib.inf112.player.guns;

import no.uib.inf112.enums.GunType;

public class ShotGun extends Gun{

    private static final int MAX_AMMO = 20;
    private static final GunType GUNTYPE = GunType.SHOTGUN;

    public ShotGun() {
        super(35);
        setMaxAmmo(MAX_AMMO);
        setCurrentAmmo(MAX_AMMO);
        setGunType(GUNTYPE);
        setAccuracy(0.6);
        setRange(150);
        setDamage(30);
    }
    
}
