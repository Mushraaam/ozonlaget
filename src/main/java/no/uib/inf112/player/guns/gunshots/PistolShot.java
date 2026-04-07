package no.uib.inf112.player.guns.gunshots;

import java.awt.geom.Line2D;

import no.uib.inf112.enums.GunType;
import no.uib.inf112.interfaces.IGunShot;
import no.uib.inf112.interfaces.IModel;

public class PistolShot implements IGunShot {

    private int lifeTime;
    private Line2D bounds;
    private GunType type;
    private IModel map;

    public PistolShot(double x1, double y1, double x2, double y2, IModel map) {
        this.bounds = new Line2D.Double(x1, y1, x2, y2);
        this.lifeTime = 4;
        this.type = GunType.DEAGLE;
        this.map = map;
    }

    @Override
    public void reduceLifeTime() {
        this.lifeTime--;
        if (this.lifeTime <= 0) {
            this.map.removeShot(this);
        }
    }

    @Override
    public Line2D bounds() {
        return this.bounds;
    }

    @Override
    public GunType gunType() {
        return this.type;
    }
}
