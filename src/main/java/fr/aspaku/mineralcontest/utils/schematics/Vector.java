package fr.aspaku.mineralcontest.utils.schematics;

import org.bukkit.Location;
import org.bukkit.World;

public class Vector implements fr.aspaku.mineralcontest.utils.schematics.api.Vector {

    private double x, y, z;
    private float yaw, pitch;

    public Vector(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Vector(Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public Vector(String string) {
        String[] data = string.split(",");
        this.x = Double.parseDouble(data[0]);
        this.y = Double.parseDouble(data[1]);
        this.z = Double.parseDouble(data[2]);
        this.yaw = Float.parseFloat(data[3]);
        this.pitch = Float.parseFloat(data[4]);
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }

    private int round(double d) {
        return (int) Math.round(d);
    }

    @Override
    public int getBlockX() {
        return round(x);
    }

    @Override
    public int getBlockY() {
        return round(y);
    }

    @Override
    public int getBlockZ() {
        return round(z);
    }

    @Override
    public float getYaw() {
        return yaw;
    }

    @Override
    public float getPitch() {
        return pitch;
    }

    @Override
    public fr.aspaku.mineralcontest.utils.schematics.api.Vector add(fr.aspaku.mineralcontest.utils.schematics.api.Vector vector) {
        this.x += vector.getX();
        this.y += vector.getY();
        this.z += vector.getZ();
        return this;
    }

    @Override
    public fr.aspaku.mineralcontest.utils.schematics.api.Vector subtract(fr.aspaku.mineralcontest.utils.schematics.api.Vector vector) {
        this.x -= vector.getX();
        this.y -= vector.getY();
        this.z -= vector.getZ();
        return this;
    }

    @Override
    public fr.aspaku.mineralcontest.utils.schematics.api.Vector clone() {
        return new Vector(x, y, z, yaw, pitch);
    }

    @Override
    public String toString() {
        return x + "," + y + "," + z + "," + yaw + "," + pitch;
    }

    @Override
    public Location toLocation(World world) {
        return new Location(world, x, y, z, yaw, pitch);
    }
}
