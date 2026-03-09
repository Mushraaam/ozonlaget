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

    /**
     * @return fire rate of gun - returns ms between each shot
     */
    public int fireRate();

    /**
     * The angle of the accuracy-cone of th gun
     * @return angle of cone - lower is more accurate
     */
    public double accuracy();

    /**
     * @return how far this gun shoots
     */
    public int range();

    /**
     * @return the ammount of damage this gun deals
     */
    public int damage();
    //TODO: shoot, reload, firerate?
}
