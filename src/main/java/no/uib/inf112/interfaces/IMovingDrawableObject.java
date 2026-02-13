package no.uib.inf112.interfaces;

import no.uib.inf112.map.Grid;

public interface IMovingDrawableObject {

    void move(double deltaTime, IGrid grid);
}
