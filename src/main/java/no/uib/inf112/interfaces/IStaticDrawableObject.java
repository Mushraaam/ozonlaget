package no.uib.inf112.interfaces;

import no.uib.inf112.enums.StaticObjectType;

public interface IStaticDrawableObject extends IStaticObject {

    /**
     * @return type of this object
     */
    public StaticObjectType getType();

}
