package fr.aspaku.mineralcontest.utils.schematics.api;

import org.bukkit.World;

public interface Region {
    Vector origin();

    World getWorld();

    Vector getMinLocation();

    Vector getMaxLocation();

    Vector getOffset();

    int getWidth();

    int getHeight();

    int getLength();

    int getSize();

    boolean isInside(Vector location);

    Region getWithoutAir();
}
