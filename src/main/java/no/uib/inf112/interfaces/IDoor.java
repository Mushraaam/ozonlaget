package no.uib.inf112.interfaces;

public interface IDoor extends IWall{

    /**
     * Removes the door from the map, also allows pathing for NPC's where the door used to block it
     */
    public void openDoor();
    
}
