package no.uib.inf112.map;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.List;

import no.uib.inf112.core.Spawner;
import no.uib.inf112.enums.GameState;
import no.uib.inf112.interfaces.*;
import no.uib.inf112.map.items.factory.ItemFactory;
import no.uib.inf112.map.npcs.NPC;
import no.uib.inf112.map.npcs.factory.Factory;
import no.uib.inf112.map.npcs.factory.SpawnPoint;
import no.uib.inf112.map.npcs.pathfinding.Pathfinder;
import no.uib.inf112.utility.Camera;
import no.uib.inf112.utility.SoundHandler;

public class TestMap implements IMap {

    private Rectangle2D.Double bounds;

    public TestMap(Rectangle2D.Double bounds) {
        this.bounds = bounds;
    }

    @Override
    public Double getBounds() {
        return this.bounds;
    }

    @Override
    public ArrayList<IStaticObject> getStaticObjects() {
        return new ArrayList<IStaticObject>();
    }

    @Override
    public Pathfinder getPathfinder() {
        return new Pathfinder(this);
    }

    @Override
    public ArrayList<IMovingDrawableObject> getMovingObjects() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMovingObjects'");
    }

    @Override
    public IPlayer getPlayer() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPlayer'");
    }

    @Override
    public GameState getGameState() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGameState'");
    }

    @Override
    public void setGameState(GameState state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setGameState'");
    }

    @Override
    public IGrid getGrid() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGrid'");
    }

    @Override
    public boolean debugMode() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'debugMode'");
    }

    @Override
    public void debugOn() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'debugOn'");
    }

    @Override
    public void debugOff() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'debugOff'");
    }

    @Override
    public void addEnemy(IEnemy enemy) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addEnemy'");
    }

    @Override
    public ArrayList<IEnemy> getEnemies() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEnemies'");
    }

    @Override
    public IGrid getTiles() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTiles'");
    }

    @Override
    public int getEnemyCount() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEnemyCount'");
    }

    @Override
    public void gatherOccupiedCells() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'gatherOccupiedCells'");
    }

    @Override
    public ArrayList<IFloor> getFloors() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFloors'");
    }

    @Override
    public void addFloor(IFloor floor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addFloor'");
    }

    @Override
    public int level() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'level'");
    }

    @Override
    public void resetOccupied() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resetOccupied'");
    }

    @Override
    public void removeShot(IGunShot shot) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeShot'");
    }

    @Override
    public Iterable<IGunShot> gunShots() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'gunShots'");
    }

    @Override
    public void addShot(IGunShot shot) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addShot'");
    }

    @Override
    public Camera getCamera() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCamera'");
    }

    @Override
    public void removeEnemy(NPC npc) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeEnemy'");
    }

    @Override
    public ArrayList<IPuddle> getAOEPuddles() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAOEPuddles'");
    }

    @Override
    public void removeAOEPuddle(IPuddle puddle) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeAOEPuddle'");
    }

    @Override
    public void addAOEPuddle(IPuddle puddle) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAOEPuddle'");
    }

    @Override
    public ArrayList<IProjectile> getProjectiles() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProjectiles'");
    }

    @Override
    public void removeProjectile(IProjectile projectile) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeProjectile'");
    }

    @Override
    public void addProjectile(IProjectile projectile) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addProjectile'");
    }

    @Override
    public ArrayList<SpawnPoint> getSpawnPoints() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSpawnPoints'");
    }

    @Override
    public void addSpawnPoint(SpawnPoint point) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addSpawnPoint'");
    }

    @Override
    public Factory getFactory() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFactory'");
    }

    @Override
    public SoundHandler getSoundHandler() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSoundHandler'");
    }

    @Override
    public ArrayList<ICollectable> getActiveItems() {
        return null;
    }

    @Override
    public void addToActiveItems(ICollectable item) {

    }

    @Override
    public void removeActiveItem(ICollectable item) {

    }

    @Override
    public void setItemSpawnPoints(ArrayList<Double> itemSpawnPoints) {

    }

    @Override
    public List<Double> getItemSpawnPoints() {
        return List.of();
    }

    @Override
    public ItemFactory getItemFactory() {
        return null;
    }

}
