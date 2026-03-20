package no.uib.inf112.player.guns;

import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.enums.GunType;

public class ShotGun extends Gun{

    private static final int MAX_AMMO = 20;
    private static final GunType GUNTYPE = GunType.SHOTGUN;

    public ShotGun() {
        super(35);
        setMaxAmmo(MAX_AMMO);
        setCurrentAmmo(MAX_AMMO);
        setGunType(GUNTYPE);
        setAccuracy(0.5);
        setRange(200);
        setDamage(30);
        setAmmoType(CollectableType.AMMO_SHOTGUN);
    
}}
