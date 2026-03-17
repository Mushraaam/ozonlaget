package no.uib.inf112.player.guns;

import no.uib.inf112.enums.GunType;

public class MP5 extends Gun {

    private static final int MAX_AMMO = 300;
    private static final GunType GUNTYPE = GunType.MP5;

    public MP5() {
        super(3);
        setMaxAmmo(MAX_AMMO);
        setCurrentAmmo(MAX_AMMO);
        setGunType(GUNTYPE);
        setAccuracy(0.12);
        setRange(500);
        setDamage(20);
    }

}
