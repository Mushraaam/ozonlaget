package no.uib.inf112.interfaces;

import no.uib.inf112.enums.CollectableType;

import java.awt.geom.Rectangle2D;

public interface ICollectable {


    Rectangle2D.Double getHitbox();

    CollectableType getType();

    int getDuration();

    void setNewDuration(int seconds);

    void pickUp();


    String getImagePath();

    void affectPlayer();
}

