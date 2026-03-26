package no.uib.inf112.map.items.buffs;

import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.player.Player;
import no.uib.inf112.utility.SoundHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.geom.Rectangle2D;

import static org.mockito.Mockito.*;

public class DamageBuffTest {

    private IMap mockMap;
    private Player mockPlayer;
    private SoundHandler mockSoundHandler;
    private DamageBuff damageBuff;

    @BeforeEach
    public void setup() {
        mockMap = mock(IMap.class);
        mockPlayer = mock(Player.class);
        mockSoundHandler = mock(SoundHandler.class);

        when(mockMap.getPlayer()).thenReturn(mockPlayer);
        when(mockMap.getSoundHandler()).thenReturn(mockSoundHandler);

        damageBuff = new DamageBuff(new Rectangle2D.Double(), CollectableType.POWERUP_DAMAGE, mockMap);
    }

    @Test
    public void testDamageBuffAppliesCorrectEffects() {
        damageBuff.affectPlayer();

        // counter go up, yes?
        verify(mockPlayer, times(1)).setBuffCounter(damageBuff);

        // dank music? Must be.
        verify(mockSoundHandler, times(1)).playBuffMusic(CollectableType.POWERUP_DAMAGE.buffType());
    }
}