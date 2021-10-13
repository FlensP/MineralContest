package fr.aspaku.mineralcontest.utils.schematics.api;

public interface Positions {
    Vector getPosition1();

    Vector getPosition2();

    void setPosition1(Vector position1);

    void setPosition2(Vector position2);

    boolean isIncomplete();
}
