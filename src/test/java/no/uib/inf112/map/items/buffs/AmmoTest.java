package no.uib.inf112.map.items.buffs;

import no.uib.inf112.enums.BuffType;
import no.uib.inf112.enums.CollectableType;
import no.uib.inf112.enums.GunType;
import no.uib.inf112.interfaces.IGun;
import no.uib.inf112.interfaces.IMap;
import no.uib.inf112.player.Player;
import no.uib.inf112.utility.SoundHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;

import static org.mockito.Mockito.*;

public class AmmoTest {

    private IMap mockMap;
    private Player mockPlayer;
    private SoundHandler mockSoundHandler;
    private HashMap<GunType, IGun> fakeOwnedGuns;
    private Ammo ammoBox;

    @BeforeEach
    public void setup() {
        mockMap = mock(IMap.class);
        mockPlayer = mock(Player.class);
        mockSoundHandler = mock(SoundHandler.class);

        when(mockMap.getPlayer()).thenReturn(mockPlayer);
        when(mockMap.getSoundHandler()).thenReturn(mockSoundHandler);

        fakeOwnedGuns = new HashMap<>();
        when(mockPlayer.getOwnedGuns()).thenReturn(fakeOwnedGuns);
    }

    @Test
    public void testAffectPlayerAddsAmmoToMatchingGun() {
        IGun mockPistol = mock(IGun.class);
        when(mockPistol.getAmmoType()).thenReturn(CollectableType.AMMO_PISTOL);

        fakeOwnedGuns.put(GunType.DEAGLE, mockPistol);

        ammoBox = new Ammo(new Rectangle2D.Double(), CollectableType.AMMO_PISTOL, mockMap);

        ammoBox.affectPlayer();

        verify(mockPistol, times(1)).increaseAmmo(CollectableType.AMMO_PISTOL.getQuantity());

        verify(mockSoundHandler, times(1)).playBuffSound(CollectableType.AMMO_PISTOL.buffType());
    }

    @Test
    public void testAffectPlayerIgnoresGunsThatDoNotMatch() {
        IGun mockShotgun = mock(IGun.class);
        when(mockShotgun.getAmmoType()).thenReturn(CollectableType.AMMO_SHOTGUN);
        fakeOwnedGuns.put(GunType.SHOTGUN, mockShotgun);

        //pistol ammo pickup
        ammoBox = new Ammo(new Rectangle2D.Double(), CollectableType.AMMO_PISTOL, mockMap);

        ammoBox.affectPlayer();
        //shouldnt increasy shotgun ammo
        verify(mockShotgun, never()).increaseAmmo(anyInt());

    }
}