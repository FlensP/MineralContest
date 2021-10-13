package fr.aspaku.mineralcontest.utils.schematics.api;

import org.bukkit.Location;
import org.bukkit.World;

public interface Vector {
    double getX();

    double getY();

    double getZ();

    int getBlockX();

    int getBlockY();

    int getBlockZ();

    float getYaw();

    float getPitch();

    Vector add(Vector vector);

    Vector subtract(Vector vector);

    Vector clone();

    @Override
    String toString();

    Location toLocation(World world);
}
