package no.uib.inf112.enums;

public enum CollectableType {
    //BUFFS
    AMMO_PISTOL(25, BuffType.AMMO),
    AMMO_RIFLE(30, BuffType.AMMO),
    AMMO_SHOTGUN(10, BuffType.AMMO),
    HEALTH(35, BuffType.HEALTH),
    ARMOR(10, BuffType.ARMOR),
    POWERUP_SPEED(30, BuffType.SPEED),
    POWERUP_DAMAGE(30, BuffType.DAMAGE),
    POWERUP_RAINBOW(40, BuffType.RAINBOW),


    //INVENTORY ITEMS
    GATEKEY(InvItemType.GATEKEY),
    CHOPPERKEY(InvItemType.CHOPPERKEY),
    GASCAN(InvItemType.GASCAN),

    //nuffin
    NONE(0, BuffType.NONE );

    final int quantity; //standard duration. Can be changed in the class itself if needed.
    final BuffType buffType;
    final InvItemType itemType;

    CollectableType(int amount, BuffType buffType) {
        this.quantity = amount;
        this.buffType = buffType;
        this.itemType = InvItemType.NONE;
    }

    CollectableType(InvItemType itemType) {
        this.quantity = 0;
        this.buffType = BuffType.NONE;
        this.itemType = itemType;
    }


    public int getQuantity(){
        return this.quantity;
    }
    public BuffType buffType(){
        return this.buffType;
    }
    public InvItemType inventoryItemType(){
        return this.itemType;
    }
}
