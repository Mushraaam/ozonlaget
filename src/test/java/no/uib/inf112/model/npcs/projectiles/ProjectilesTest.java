package no.uib.inf112.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;

import java.awt.geom.Rectangle2D;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import no.uib.inf112.enums.PuddleType;
import no.uib.inf112.interfaces.IProjectile;
import no.uib.inf112.interfaces.IPuddle;
import no.uib.inf112.model.npcs.projectiles.AcidPuddleProjectile;
import no.uib.inf112.model.npcs.projectiles.ExplosionProjectile;
import no.uib.inf112.model.npcs.projectiles.MinionProjectile;
import no.uib.inf112.utility.SoundHandler;
import no.uib.inf112.view.LoadStatus;

public class ProjectilesTest {
    private MockedConstruction<SoundHandler> mockedSoundHandler;
    private Model model;

    @BeforeEach
    void createModel() {
        mockedSoundHandler = mockConstruction(SoundHandler.class);
        model = new Model(mock(LoadStatus.class));
    }

    @AfterEach
    void closeMockedSoundHandler() {
        mockedSoundHandler.close();
    }

    @Test
    void acidProjectileInitialStateTest() {
        Rectangle2D.Double start = new Rectangle2D.Double(100, 100, 20, 20);
        Rectangle2D.Double target = new Rectangle2D.Double(200, 100, 20, 20);

        IProjectile projectile = new AcidPuddleProjectile(start, target, model);

        assertEquals(PuddleType.ACID, projectile.getType());
        assertEquals(0, projectile.getAnimationTick());

        Rectangle2D.Double bounds = projectile.getBounds();
        assertEquals(70, bounds.getX());
        assertEquals(102, bounds.getY());
        assertEquals(80, bounds.getWidth());
        assertEquals(16, bounds.getHeight());

        assertEquals(0, projectile.angle());
    }

    @Test
    void explosionProjectileInitialStateTest() {
        Rectangle2D.Double start = new Rectangle2D.Double(100, 100, 20, 20);
        Rectangle2D.Double target = new Rectangle2D.Double(200, 100, 20, 20);

        IProjectile projectile = new ExplosionProjectile(start, target, model);

        assertEquals(PuddleType.BOSS_FIREBALL, projectile.getType());
        assertEquals(0, projectile.getAnimationTick());

        Rectangle2D.Double bounds = projectile.getBounds();
        assertEquals(78, bounds.getX());
        assertEquals(92, bounds.getY());
        assertEquals(64, bounds.getWidth());
        assertEquals(36, bounds.getHeight());
    }

    @Test
    void minionProjectileInitialStateTest() {
        Rectangle2D.Double start = new Rectangle2D.Double(100, 100, 20, 20);
        Rectangle2D.Double target = new Rectangle2D.Double(200, 100, 20, 20);

        IProjectile projectile = new MinionProjectile(start, target, model);

        assertEquals(PuddleType.BUGPROJECTILE, projectile.getType());
        assertEquals(0, projectile.getAnimationTick());

        Rectangle2D.Double bounds = projectile.getBounds();
        assertEquals(78, bounds.getX());
        assertEquals(92, bounds.getY());
        assertEquals(64, bounds.getWidth());
        assertEquals(36, bounds.getHeight());
    }

}

