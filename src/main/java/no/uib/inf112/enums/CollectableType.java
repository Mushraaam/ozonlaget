package no.uib.inf112.enums;

public enum CollectableType {
    AMMO_PISTOL(25, BuffType.AMMO),
    AMMO_RIFLE(30, BuffType.AMMO),
    AMMO_SHOTGUN(10, BuffType.AMMO),
    HEALTH(35, BuffType.HEALTH),
    ARMOR(10, BuffType.ARMOR),
    POWERUP_SPEED(12, BuffType.SPEED),
    POWERUP_DAMAGE(10, BuffType.DAMAGE),
    POWERUP_RAINBOW(40, BuffType.RAINBOW),
    NONE(0, BuffType.NONE );

    final int quantity; //standard duration. Can be changed in the class itself if needed.
    final BuffType buffType;

    CollectableType(int amount, BuffType buffType) {
        this.quantity = amount;
        this.buffType = buffType;
    }


    public int getQuantity(){
        return this.quantity;
    }
    public BuffType buffType(){
        return this.buffType;
    }
}
