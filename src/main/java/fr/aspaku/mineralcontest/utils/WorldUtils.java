package fr.aspaku.mineralcontest.utils;

import fr.aspaku.mineralcontest.MineralContest;
import fr.aspaku.mineralcontest.utils.schematics.objects.Schematic;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class WorldUtils {

    private static boolean pasting;

    @SuppressWarnings("all")
    public static void pasteSchematic(World world, Location location, MineralContest plugin, String name) throws IOException {
        System.out.println("Pasting spawn");
        pasting = true;
        File file = new File(world.getWorldFolder().getParentFile(), name);
        Schematic schematic;
        try {
            schematic = new Schematic(file, plugin);
        } catch (IOException e) {
            PasteBarrier(world, location);
            pasting = false;
            System.out.println("Spawn was pasted without schematic");
            DefaultLocation.lobby.setY(151);
            return;
        }
        schematic = new Schematic(file, plugin);
        System.out.println("Loc : " + location.getBlockX() + " " + location.getBlockZ());
        System.out.println(255 - schematic.getRegion().getHeight());
        Location clone = location.clone();
        clone.add(-schematic.getRegion().getLength() / 2, 0, -schematic.getRegion().getWidth() / 2);
        System.out.println("New location : " + clone.getBlockX() + " " + clone.getBlockZ());
        schematic.paste(clone, 10000, aLong -> {
            System.out.println("Schematic was pasted in " + (aLong / 1000F) + " seconds");
            pasting = false;
        });
        DefaultLocation.lobby.setY(200);
    }

    public static boolean isPasting() {
        return pasting;
    }

    public static void generateWorld() throws IOException {
        Files.deleteIfExists(new File(Bukkit.getWorldContainer(), "mineral").toPath());

        Path path = Paths.get(Bukkit.getWorld("lobby").getWorldFolder().getParentFile().toPath().toAbsolutePath().toString().replace(".", ""));
        try (FileInputStream is = new FileInputStream(new File(Bukkit.getWorld("lobby").getWorldFolder().getParentFile(), "mineral.zip"))) {
            unzip(is, path);
        }

    }

    public static void unzip(InputStream is, Path targetDir) throws IOException {
        targetDir = targetDir.toAbsolutePath();
        System.out.printf(String.valueOf(targetDir));
        try (ZipInputStream zipIn = new ZipInputStream(is)) {
            for (ZipEntry ze; (ze = zipIn.getNextEntry()) != null; ) {
                Path resolvedPath = targetDir.resolve(ze.getName()).normalize();
                System.out.printf(String.valueOf(resolvedPath));
                if (!resolvedPath.startsWith(targetDir)) {
                    // see: https://snyk.io/research/zip-slip-vulnerability
                    throw new RuntimeException("Entry with an illegal path: "
                            + ze.getName());
                }
                if (ze.isDirectory()) {
                    Files.createDirectories(resolvedPath);
                } else {
                    Files.createDirectories(resolvedPath.getParent());
                    Files.copy(zipIn, resolvedPath);
                }
            }
        }
    }

    public static void PasteBarrier(World world, Location location) throws IOException {
        int x = location.getBlockX();
        int z = location.getBlockZ();
        world.setSpawnLocation(x, 151, z);

        for (int i = -16; i <= 16; i++) {

            for (int j = -16; j <= 16; j++) {

                new Location(world, i + x, 150, j + z).getBlock().setType(Material.BARRIER);
                new Location(world, i + x, 154, j + z).getBlock().setType(Material.BARRIER);
            }
            for (int j = 151; j < 154; j++) {
                new Location(world, i + x, j, z - 16).getBlock().setType(Material.BARRIER);
                new Location(world, i + x, j, z + 16).getBlock().setType(Material.BARRIER);
                new Location(world, x - 16, j, i + z).getBlock().setType(Material.BARRIER);
                new Location(world, x + 16, j, i + z).getBlock().setType(Material.BARRIER);
            }
        }
    }
}

