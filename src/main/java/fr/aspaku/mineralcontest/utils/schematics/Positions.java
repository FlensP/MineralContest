package fr.aspaku.mineralcontest.utils.schematics;

import fr.aspaku.mineralcontest.utils.schematics.api.Vector;

public class Positions implements fr.aspaku.mineralcontest.utils.schematics.api.Positions {

    private Vector position1, position2;

    @Override
    public Vector getPosition1() {
        return position1;
    }

    @Override
    public Vector getPosition2() {
        return position2;
    }

    @Override
    public void setPosition1(Vector position1) {
        this.position1 = position1;
    }

    @Override
    public void setPosition2(Vector position2) {
        this.position2 = position2;
    }

    @Override
    public boolean isIncomplete() {
        return position1 == null || position2 == null;
    }

}
