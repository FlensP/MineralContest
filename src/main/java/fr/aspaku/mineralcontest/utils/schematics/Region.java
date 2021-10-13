package fr.aspaku.mineralcontest.utils.schematics;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

public class Region implements fr.aspaku.mineralcontest.utils.schematics.api.Region {

    private World world;

    private final fr.aspaku.mineralcontest.utils.schematics.api.Vector minLocation, maxLocation;

    private final fr.aspaku.mineralcontest.utils.schematics.api.Vector offset;
    private final fr.aspaku.mineralcontest.utils.schematics.api.Vector origin;

    public Region(World world, fr.aspaku.mineralcontest.utils.schematics.api.Vector origin, fr.aspaku.mineralcontest.utils.schematics.api.Vector position1, fr.aspaku.mineralcontest.utils.schematics.api.Vector position2) {
        this.world = world;

        double minX = Math.min(position1.getX(), position2.getX());
        double minY = Math.min(position1.getY(), position2.getY());
        double minZ = Math.min(position1.getZ(), position2.getZ());
        minLocation = new Vector(minX, minY, minZ);

        double maxX = Math.max(position1.getX(), position2.getX());
        double maxY = Math.max(position1.getY(), position2.getY());
        double maxZ = Math.max(position1.getZ(), position2.getZ());
        maxLocation = new Vector(maxX, maxY, maxZ);

        offset = minLocation.clone().subtract(origin);
        this.origin = origin;
    }

    public Region(fr.aspaku.mineralcontest.utils.schematics.api.Vector minLocation, fr.aspaku.mineralcontest.utils.schematics.api.Vector offset, int width, int height, int length) {
        this.minLocation = minLocation;

        double maxX = width + minLocation.getX() - 1;
        double maxY = height + minLocation.getY() - 1;
        double maxZ = length + minLocation.getZ() - 1;
        this.maxLocation = new Vector(maxX, maxY, maxZ);

        this.offset = offset;
        origin = minLocation.clone().subtract(offset);
    }

    @Override
    public fr.aspaku.mineralcontest.utils.schematics.api.Vector origin() {
        return origin;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public fr.aspaku.mineralcontest.utils.schematics.api.Vector getMinLocation() {
        return minLocation;
    }

    @Override
    public fr.aspaku.mineralcontest.utils.schematics.api.Vector getMaxLocation() {
        return maxLocation;
    }

    @Override
    public fr.aspaku.mineralcontest.utils.schematics.api.Vector getOffset() {
        return offset;
    }

    @Override
    public int getWidth() {
        return maxLocation.getBlockX() - minLocation.getBlockX() + 1;
    }

    @Override
    public int getHeight() {
        return maxLocation.getBlockY() - minLocation.getBlockY() + 1;
    }

    @Override
    public int getLength() {
        return maxLocation.getBlockZ() - minLocation.getBlockZ() + 1;
    }

    @Override
    public int getSize() {
        return getWidth() * getHeight() * getLength();
    }

    @Override
    public boolean isInside(fr.aspaku.mineralcontest.utils.schematics.api.Vector location) {
        return location.getX() >= minLocation.getX() && location.getX() <= maxLocation.getX()
                && location.getY() >= minLocation.getY() && location.getY() <= maxLocation.getY()
                && location.getZ() >= minLocation.getZ() && location.getZ() <= maxLocation.getZ();
    }

    @Override
    public fr.aspaku.mineralcontest.utils.schematics.api.Region getWithoutAir() {
        int lowestX = Integer.MAX_VALUE;
        int lowestY = Integer.MAX_VALUE;
        int lowestZ = Integer.MAX_VALUE;

        int highestX = Integer.MIN_VALUE;
        int highestY = Integer.MIN_VALUE;
        int highestZ = Integer.MIN_VALUE;

        for (int x = minLocation.getBlockX(); x < maxLocation.getBlockX(); x++) {
            for (int y = minLocation.getBlockY(); y < maxLocation.getBlockY(); y++) {
                for (int z = minLocation.getBlockZ(); z < maxLocation.getBlockZ(); z++) {

                    if (world.getBlockAt(x, y, z).getType() == Material.AIR) {
                        continue;
                    }

                    if (x < lowestX) {
                        lowestX = x;
                    }
                    if (y < lowestY) {
                        lowestY = y;
                    }
                    if (z < lowestZ) {
                        lowestZ = z;
                    }

                    if (x > highestX) {
                        highestX = x;
                    }
                    if (y > highestY) {
                        highestY = y;
                    }
                    if (z > highestZ) {
                        highestZ = z;
                    }
                }
            }
        }

        for (Entity entities : world.getEntities()) {

            if (entities instanceof Item || entities instanceof Player) {
                continue;
            }

            fr.aspaku.mineralcontest.utils.schematics.api.Vector location = new Vector(entities.getLocation());
            if (!isInside(location)) {
                continue;
            }
            int x = location.getBlockX();
            int y = location.getBlockY();
            int z = location.getBlockZ();

            if (x < lowestX) {
                lowestX = x;
            }
            if (y < lowestY) {
                lowestY = y;
            }
            if (z < lowestZ) {
                lowestZ = z;
            }

            if (x > highestX) {
                highestX = x;
            }
            if (y > highestY) {
                highestY = y;
            }
            if (z > highestZ) {
                highestZ = z;
            }
        }

        fr.aspaku.mineralcontest.utils.schematics.api.Vector lowest = new Vector(lowestX, lowestY, lowestZ);
        fr.aspaku.mineralcontest.utils.schematics.api.Vector highest = new Vector(highestX, highestY, highestZ);

        return new Region(world, origin, lowest, highest);
    }
}
