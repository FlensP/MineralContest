package fr.aspaku.mineralcontest.utils.schematics.objects;


import fr.aspaku.mineralcontest.utils.schematics.api.Vector;

public class SchematicBlock implements fr.aspaku.mineralcontest.utils.schematics.objects.api.SchematicBlock {

    private final Vector location;

    private final int id;

    private final byte data;

    public SchematicBlock(Vector location, int id, byte data) {
        this.location = location;
        this.id = id;
        this.data = data;
    }

    @Override
    public Vector getLocation() {
        return location;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public byte getData() {
        return data;
    }
}
