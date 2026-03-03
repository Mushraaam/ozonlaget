package no.uib.inf112.interfaces;

import no.uib.inf112.enums.GunType;

public interface IGun {
    
    /**
     * @return max ammunition of gun
     */
    public int maxAmmunition();

    /**
     * @return current ammunition of gun
     */
    public int currentAmmunition();

    /**
     * @return type of this gun
     */
    public GunType type();

    //TODO: shoot, reload, firerate?
}
