package no.uib.inf112.map.npcs;

import no.uib.inf112.enums.EnemyType;
import no.uib.inf112.interfaces.IEnemy;
import no.uib.inf112.utility.ImageReader;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Thug implements IEnemy {
    private Rectangle2D.Double pos;
    private BufferedImage tempImg;

    public Thug(Rectangle2D.Double pos){

        this.pos = pos;
        this.tempImg = ImageReader.fetcImage("src\\main\\java\\no\\resources\\thug.png");
    }

    @Override
    public Rectangle2D.Double getBounds() {
        return this.pos;
    }


    @Override
    public EnemyType getEnemyType() {
        return EnemyType.THUG;
    }

    @Override
    public int getAnimationIndex() {
        return 0;
    }

    public BufferedImage getImg(){
        return tempImg;
    }


}
