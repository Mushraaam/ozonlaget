package no.uib.inf112.model;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.List;

import no.uib.inf112.enums.GameState;
import no.uib.inf112.interfaces.*;
import no.uib.inf112.model.items.factory.ItemFactory;
import no.uib.inf112.model.npcs.NPC;
import no.uib.inf112.model.npcs.factory.Factory;
import no.uib.inf112.model.npcs.factory.SpawnPoint;
import no.uib.inf112.model.npcs.pathfinding.Pathfinder;
import no.uib.inf112.utility.Camera;
import no.uib.inf112.utility.SoundHandler;

public class TestMap implements IModel {

    private Rectangle2D.Double bounds;
    private ArrayList<IEnemy> enemies;
    private IGrid grid;

    public TestMap(Rectangle2D.Double bounds) {
        this.bounds = bounds;
        this.enemies = new ArrayList<IEnemy>();
        this.grid = new Grid(this);
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
    public int getTotalDroppedLoot() {
        return 0;
    }

    @Override
    public void increaseDroppedLoot() {

    }

    @Override
    public void decreaseDroppedLoot() {

    }

    @Override
    public Pathfinder getPathfinder() {
        return new Pathfinder(this);
    }

    @Override
    public IPlayer getPlayer() {
        return null;
    }

    @Override
    public GameState getGameState() {
                return null;

    }

    @Override
    public void setGameState(GameState state) {

    }

    @Override
    public IGrid getGrid() {
        return this.grid;
    }

    @Override
    public boolean debugMode() {
        throw new UnsupportedOperationException("Unimplemented method 'debugMode'");
    }

    @Override
    public void debugOn() {
        throw new UnsupportedOperationException("Unimplemented method 'debugOn'");
    }

    @Override
    public void debugOff() {
        throw new UnsupportedOperationException("Unimplemented method 'debugOff'");
    }

    @Override
    public void addEnemy(IEnemy enemy) {
        this.enemies.add(enemy);
    }

    @Override
    public ArrayList<IEnemy> getEnemies() {
        return this.enemies;
    }

    @Override
    public IVehicle getHelicopter() {
        return null;
    }

    @Override
    public IGrid getTiles() {
        throw new UnsupportedOperationException("Unimplemented method 'getTiles'");
    }

    @Override
    public int getEnemyCount() {
        throw new UnsupportedOperationException("Unimplemented method 'getEnemyCount'");
    }

    @Override
    public void gatherOccupiedCells() {
        throw new UnsupportedOperationException("Unimplemented method 'gatherOccupiedCells'");
    }

    @Override
    public ArrayList<IFloor> getFloors() {
        throw new UnsupportedOperationException("Unimplemented method 'getFloors'");
    }

    @Override
    public void addFloor(IFloor floor) {
        throw new UnsupportedOperationException("Unimplemented method 'addFloor'");
    }

    @Override
    public int level() {
        throw new UnsupportedOperationException("Unimplemented method 'level'");
    }

    @Override
    public void resetOccupied() {
        throw new UnsupportedOperationException("Unimplemented method 'resetOccupied'");
    }

    @Override
    public void removeShot(IGunShot shot) {
        throw new UnsupportedOperationException("Unimplemented method 'removeShot'");
    }

    @Override
    public Iterable<IGunShot> gunShots() {
        throw new UnsupportedOperationException("Unimplemented method 'gunShots'");
    }

    @Override
    public void addShot(IGunShot shot) {
        throw new UnsupportedOperationException("Unimplemented method 'addShot'");
    }

    @Override
    public Camera getCamera() {
        throw new UnsupportedOperationException("Unimplemented method 'getCamera'");
    }

    @Override
    public void removeEnemy(NPC npc) {
        this.enemies.remove(npc);
    }

    @Override
    public ArrayList<IPuddle> getAOEPuddles() {
        throw new UnsupportedOperationException("Unimplemented method 'getAOEPuddles'");
    }

    @Override
    public void removeAOEPuddle(IPuddle puddle) {
        throw new UnsupportedOperationException("Unimplemented method 'removeAOEPuddle'");
    }

    @Override
    public void addAOEPuddle(IPuddle puddle) {
        throw new UnsupportedOperationException("Unimplemented method 'addAOEPuddle'");
    }

    @Override
    public ArrayList<IProjectile> getProjectiles() {
        throw new UnsupportedOperationException("Unimplemented method 'getProjectiles'");
    }

    @Override
    public void removeProjectile(IProjectile projectile) {
        throw new UnsupportedOperationException("Unimplemented method 'removeProjectile'");
    }

    @Override
    public void addProjectile(IProjectile projectile) {
        throw new UnsupportedOperationException("Unimplemented method 'addProjectile'");
    }

    @Override
    public ArrayList<SpawnPoint> getSpawnPoints() {
        throw new UnsupportedOperationException("Unimplemented method 'getSpawnPoints'");
    }

    @Override
    public void addSpawnPoint(SpawnPoint point) {
        throw new UnsupportedOperationException("Unimplemented method 'addSpawnPoint'");
    }

    @Override
    public Factory getFactory() {
        throw new UnsupportedOperationException("Unimplemented method 'getFactory'");
    }

    @Override
    public SoundHandler getSoundHandler() {
        throw new UnsupportedOperationException("Unimplemented method 'getSoundHandler'");
    }

    @Override
    public ArrayList<ICollectable> getActiveItems() {
        return null;
    }

    @Override
    public void addToActiveItems(ICollectable item) {
        /*  */
    }

    @Override
    public void removeActiveItem(ICollectable item) {
        /*  */
    }

    @Override
    public void setItemSpawnPoints(ArrayList<Double> buffItemSpawnPoints, ArrayList<Double> itemSpawnPoints) {

    }


    @Override
    public List<Double> getBuffItemSpawnpoint() {
        return List.of();
    }

    @Override
    public List<Double> getInventoryItemSpawnpoint() {
        return List.of();
    }

    @Override
    public ItemFactory getItemFactory() {
        return null;
    }

    @Override
    public void setPlayer(IPlayer player) {

    }

    @Override
    public void resetMap() {
        throw new UnsupportedOperationException("Unimplemented method 'resetMap'");
    }

    @Override
    public void setLevel(int level) {
        throw new UnsupportedOperationException("Unimplemented method 'setLevel'");
    }

    @Override
    public ArrayList<IVehicle> getVehicles() {
        return new ArrayList<>();
    }

    @Override
    public int getDifficulty() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDifficulty'");
    }

    @Override
    public void incrementDifficulty() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'incrementDifficulty'");
    }

}
