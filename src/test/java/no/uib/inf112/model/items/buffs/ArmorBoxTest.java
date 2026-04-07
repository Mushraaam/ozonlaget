package no.uib.inf112.model.items.buffs;

import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.interfaces.IModel;
import no.uib.inf112.model.items.buffs.ArmorBox;
import no.uib.inf112.player.Player;
import no.uib.inf112.utility.SoundHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Rectangle2D;

import static org.mockito.Mockito.*;

public class ArmorBoxTest {

    private IModel mockMap;
    private Player mockPlayer;
    private SoundHandler mockSoundHandler;
    private ArmorBox armorBox;

    @BeforeEach
    public void setup() {
        mockMap = mock(IModel.class);
        mockPlayer = mock(Player.class);
        mockSoundHandler = mock(SoundHandler.class);

        when(mockMap.getPlayer()).thenReturn(mockPlayer);
        when(mockMap.getSoundHandler()).thenReturn(mockSoundHandler);

        armorBox = new ArmorBox(new Rectangle2D.Double(), CollectableType.ARMOR, mockMap);
    }

    @Test
    public void testArmorBoxAppliesCorrectEffects() {
        // deliver!!!
        armorBox.affectPlayer();

        // did it go up
        verify(mockPlayer, times(1)).increaseArmor(CollectableType.ARMOR.getQuantity());

        // ohhyeah
        verify(mockSoundHandler, times(1)).playBuffSound(CollectableType.ARMOR.buffType());
    }
}