package fr.aspaku.mineralcontest.utils.schematics.objects.api;

import fr.aspaku.mineralcontest.utils.schematics.api.Vector;

public interface SchematicLocation {
    String getKey();

    Vector getLocation();

    @Override
    String toString();
}
