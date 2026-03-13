package no.uib.inf112.enums;

public enum EnemyAction {


    WALK(0),
    ATTACK(1),
    RANGED_ATTACK(2),
    DEAD(3);

    private final int index;

    private EnemyAction(int index){
        this.index = index;
    }
    public int index(){
        return this.index;
    }
}
