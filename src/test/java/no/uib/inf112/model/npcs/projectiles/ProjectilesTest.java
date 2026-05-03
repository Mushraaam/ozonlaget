package no.uib.inf112.model.npcs.projectiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import no.uib.inf112.model.Model;
import no.uib.inf112.model.npcs.projectiles.AcidPuddleProjectile;
import no.uib.inf112.model.npcs.projectiles.ExplosionProjectile;
import no.uib.inf112.model.npcs.projectiles.MinionProjectile;
import no.uib.inf112.model.npcs.projectiles.puddles.AcidPuddle;
import no.uib.inf112.model.npcs.projectiles.puddles.ExplosionPuddle;
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

    @Test
    void projectileMoveTest() {
        Rectangle2D.Double start = new Rectangle2D.Double(0, 0, 20, 20);
        Rectangle2D.Double target = new Rectangle2D.Double(100, 0, 20, 20);

        IProjectile projectile = new AcidPuddleProjectile(start, target, model);
        model.addProjectile(projectile);

        projectile.move();

        assertEquals(1, projectile.getAnimationTick());
        assertEquals(1, model.getProjectiles().size());

        Rectangle2D.Double bounds = projectile.getBounds();

        assertEquals(-20, bounds.getX());
        assertEquals(2, bounds.getY());
        assertEquals(80, bounds.getWidth());
        assertEquals(16, bounds.getHeight());
        assertEquals(0, projectile.angle());
    }

    @Test
    void acidpuddleTest() {
        Rectangle2D.Double start = new Rectangle2D.Double(0, 0, 20, 20);
        Rectangle2D.Double target = new Rectangle2D.Double(5, 0, 20, 20);

        IProjectile projectile = new AcidPuddleProjectile(start, target, model);
        model.addProjectile(projectile);

        assertEquals(1, model.getProjectiles().size());
        assertTrue(model.getAOEPuddles().isEmpty());

        projectile.move();

        assertTrue(model.getProjectiles().isEmpty());
        assertEquals(1, model.getAOEPuddles().size());

        IPuddle puddle = model.getAOEPuddles().get(0);

        assertTrue(puddle instanceof AcidPuddle);
        assertEquals(PuddleType.ACID, puddle.getType());

        Rectangle2D.Double puddleBounds = puddle.getBounds();
        assertEquals(-25, puddleBounds.getX());
        assertEquals(-30, puddleBounds.getY());
        assertEquals(80, puddleBounds.getWidth());
        assertEquals(80, puddleBounds.getHeight());
    }

    @Test
    void explosionPuddleTest() {
        Rectangle2D.Double start = new Rectangle2D.Double(0, 0, 20, 20);
        Rectangle2D.Double target = new Rectangle2D.Double(5, 0, 20, 20);

        IProjectile projectile = new ExplosionProjectile(start, target, model);
        model.addProjectile(projectile);

        assertEquals(1, model.getProjectiles().size());
        assertTrue(model.getAOEPuddles().isEmpty());

        projectile.move();

        assertTrue(model.getProjectiles().isEmpty());
        assertEquals(1, model.getAOEPuddles().size());

        IPuddle puddle = model.getAOEPuddles().get(0);

        assertTrue(puddle instanceof ExplosionPuddle);
        assertEquals(PuddleType.EXPLOSION, puddle.getType());
        assertEquals(40, puddle.lifeTime());

        Rectangle2D.Double puddleBounds = puddle.getBounds();
        assertEquals(-55, puddleBounds.getX());
        assertEquals(-60, puddleBounds.getY());
        assertEquals(140, puddleBounds.getWidth());
        assertEquals(140, puddleBounds.getHeight());
    }

    @Test
    void minionProjectileSpawnsEnemyTest() {
        Rectangle2D.Double start = new Rectangle2D.Double(0, 0, 20, 20);
        Rectangle2D.Double target = new Rectangle2D.Double(5, 0, 20, 20);

        IProjectile projectile = new MinionProjectile(start, target, model);
        model.addProjectile(projectile);

        assertEquals(0, model.getEnemyCount());
        assertEquals(1, model.getProjectiles().size());

        projectile.move();

        assertTrue(model.getProjectiles().isEmpty());
        assertEquals(1, model.getEnemyCount());
        assertFalse(model.getEnemies().isEmpty());
    }

    @Test
    void projectileCanMoveMultipleTimesTest() {
        Rectangle2D.Double start = new Rectangle2D.Double(0, 0, 20, 20);
        Rectangle2D.Double target = new Rectangle2D.Double(100, 0, 20, 20);

        IProjectile projectile = new ExplosionProjectile(start, target, model);
        model.addProjectile(projectile);

        projectile.move();
        projectile.move();
        projectile.move();

        assertEquals(3, projectile.getAnimationTick());
        assertEquals(1, model.getProjectiles().size());
        assertTrue(model.getAOEPuddles().isEmpty());
        assertEquals(0, model.getEnemyCount());
    }

    @Test
    void projectileHitTest() {
        Rectangle2D.Double start = new Rectangle2D.Double(0, 0, 20, 20);
        Rectangle2D.Double target = new Rectangle2D.Double(100, 0, 20, 20);

        IProjectile projectile = new ExplosionProjectile(start, target, model);
        model.addProjectile(projectile);

        for (int i = 0; i < 20; i++) {
            if (model.getProjectiles().contains(projectile)) {
                projectile.move();
            }
        }

        assertFalse(model.getProjectiles().contains(projectile));
        assertEquals(1, model.getAOEPuddles().size());
    }
}
