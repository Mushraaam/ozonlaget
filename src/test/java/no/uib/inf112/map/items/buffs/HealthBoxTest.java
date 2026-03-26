package no.uib.inf112.map.items.buffs;

import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.player.Player;
import no.uib.inf112.utility.SoundHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.geom.Rectangle2D;
import static org.mockito.Mockito.*;

public class HealthBoxTest {

    private IMap mockMap;
    private Player mockPlayer;
    private HealthBox healthBox;
    private SoundHandler mockSoundHandler;

    @BeforeEach
    public void setup() {
        mockMap = mock(IMap.class);
        mockPlayer = mock(Player.class);
        when(mockMap.getPlayer()).thenReturn(mockPlayer);
        mockSoundHandler = mock(SoundHandler.class);
        healthBox = new HealthBox(new Rectangle2D.Double(), CollectableType.HEALTH, mockMap);
    }

    @Test
    public void testAffectPlayerHealsDamagedPlayer() {
        // Assume player has 50/100 HP, and the health box gives 25 HP
        when(mockPlayer.getCurrentHP()).thenReturn(50);
        when(mockPlayer.getMaxHP()).thenReturn(100);

        when(mockMap.getSoundHandler()).thenReturn(mockSoundHandler);
        healthBox.affectPlayer();

        // make sure the player was healed by the correct amount
        verify(mockPlayer, times(1)).healHP(CollectableType.HEALTH.getQuantity());
    }

    @Test
    public void testCannotPickUpWhenHealthIsFull() {
        // Assume player is at full health
        when(mockPlayer.getCurrentHP()).thenReturn(100);
        when(mockPlayer.getMaxHP()).thenReturn(100);

        healthBox.pickUp();

        // make sure that the healthbox never asked the map to remove it
        verify(mockMap, never()).removeActiveItem(healthBox);
        // player should never be healed
        verify(mockPlayer, never()).healHP(anyInt());
    }
}