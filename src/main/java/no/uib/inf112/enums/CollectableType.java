package no.uib.inf112.enums;

public enum CollectableType {
    AMMO(0, BuffType.AMMO),
    HEALTH(0, BuffType.HEALTH),
    ARMOR(10, BuffType.ARMOR),
    POWERUP_SPEED(12, BuffType.SPEED),
    POWERUP_DAMAGE(10, BuffType.DAMAGE)
    ;

    final int quantity; //standard duration. Can be changed in the class itself if needed.
    final BuffType buffType;

    CollectableType(int duration, BuffType buffType) {
        this.quantity = duration;
        this.buffType = buffType;
    }

    public int getQuantity(){
        return this.quantity;
    }
    public BuffType buffType(){
        return this.buffType;
    }
}
