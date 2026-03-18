package no.uib.inf112.interfaces;

import no.uib.inf112.enums.BuffType;
import no.uib.inf112.enums.CollectableType;

import java.awt.geom.Rectangle2D;

public interface ICollectable {


    Rectangle2D.Double getHitbox();

    CollectableType getType();

    int getAmount();

    void setNewAmount(int amount);

    void pickUp();


    BuffType getBuffType();

    void affectPlayer();
}

