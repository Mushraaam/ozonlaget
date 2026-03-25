package no.uib.inf112.map.items.buffs;

import no.uib.inf112.config.Config;
import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.player.Player;
import no.uib.inf112.utility.SoundHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.geom.Rectangle2D;

import static org.mockito.Mockito.*;

public class SpeedBuffTest {

    private IMap mockMap;
    private Player mockPlayer;
    private SoundHandler mockSoundHandler;
    private SpeedBuff speedBuff;

    @BeforeEach
    public void setup() {
        mockMap = mock(IMap.class);
        mockPlayer = mock(Player.class);
        mockSoundHandler = mock(SoundHandler.class);

        when(mockMap.getPlayer()).thenReturn(mockPlayer);
        when(mockMap.getSoundHandler()).thenReturn(mockSoundHandler);

        speedBuff = new SpeedBuff(new Rectangle2D.Double(), CollectableType.POWERUP_SPEED, mockMap);
    }

    @Test
    public void testSpeedBuffAppliesCorrectEffects() {
        speedBuff.affectPlayer();

        // make sure speed was correctly set
        verify(mockPlayer, times(1)).setPlayerSpeed(Config.getInt("playerMoveSpeed") * 2);

        // check if the buff was applied to the counter
        verify(mockPlayer, times(1)).setBuffCounter(speedBuff);

        // verify the dank beats started playing
        verify(mockSoundHandler, times(1)).playBuffMusic(CollectableType.POWERUP_SPEED.buffType());
    }
}