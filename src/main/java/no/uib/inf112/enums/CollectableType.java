package no.uib.inf112.enums;

public enum CollectableType {
    AMMO(0),
    HEALTH(0),
    ARMOR(0),
    POWERUP_SPEED(12),
    POWERUP_DAMAGE(10)
    ;

    final int duration; //standard duration. Can be changed in the class itself if needed.

    CollectableType(int duration) {
        this.duration = duration;
    }

    public int duration(){
        return this.duration;
    }
}
