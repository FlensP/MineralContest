package fr.aspaku.mineralcontest.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class DefaultLocation {
    public static Location lobby;
    public static Location world;
    public static Location centre;
    public static Location spawn_red;
    public static Location spawn_yellow;
    public static Location spawn_blue;
    public static Location spawn_green;
    public static Location tp_red;
    public static Location tp_yellow;
    public static Location tp_blue;
    public static Location tp_green;

    public static void create() {
        lobby = new Location(Bukkit.getWorld("lobby"), 11, 97, 0);
        world = new Location(Bukkit.getWorld("mineral"), -22, 74, 296);
        centre = new Location(Bukkit.getWorld("mineral"), -22, 65, 296);

        spawn_red = new Location(Bukkit.getWorld("mineral"), -68, 74, 296);
        spawn_blue = new Location(Bukkit.getWorld("mineral"), -22, 74, 342);
        spawn_green = new Location(Bukkit.getWorld("mineral"), -22, 74, 250);
        spawn_yellow = new Location(Bukkit.getWorld("mineral"), 23, 74, 296);

        tp_red = new Location(Bukkit.getWorld("mineral"), -39, 68, 296);
        tp_blue = new Location(Bukkit.getWorld("mineral"), -22, 68, 313);
        tp_green = new Location(Bukkit.getWorld("mineral"), -22, 68, 279);
        tp_yellow = new Location(Bukkit.getWorld("mineral"), -5, 68, 296);
    }
}
