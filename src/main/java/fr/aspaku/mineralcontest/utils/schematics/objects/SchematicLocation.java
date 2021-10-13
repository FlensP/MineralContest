package fr.aspaku.mineralcontest.utils.schematics.objects;


import fr.aspaku.mineralcontest.utils.schematics.Vector;

public class SchematicLocation implements fr.aspaku.mineralcontest.utils.schematics.objects.api.SchematicLocation {

    private final String key;

    private final Vector location;

    public SchematicLocation(String key, Vector location) {
        this.key = key;
        this.location = location;
    }

    public SchematicLocation(String string) {
        String[] data = string.split(";");
        this.key = data[0];
        this.location = new Vector(data[1]);
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Vector getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return key + ";" + location.toString();
    }
}
