package fr.aspaku.mineralcontest;

import fr.aspaku.mineralcontest.commands.ConfigCommand;
import fr.aspaku.mineralcontest.commands.StartCommand;
import fr.aspaku.mineralcontest.commands.TpAreneCommand;
import fr.aspaku.mineralcontest.utils.DefaultLocation;
import fr.aspaku.mineralcontest.utils.TeamManager;
import fr.aspaku.mineralcontest.utils.WorldUtils;
import fr.minuskube.inv.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;

public class MineralContest extends JavaPlugin {

    public Game game;
    public PluginManager manager;
    public TeamManager teamManager;
    private final InventoryManager invManager = new InventoryManager(this);
    private MineralContest instance;

    @Override
    public void onEnable() {

        System.out.println("Mineral Contest enabling...");


        instance = this;
        manager = getServer().getPluginManager();
        manager.registerEvents(new Listeners(this), this);
        game = new Game(this);


        //Création du jeu
        WorldCreator wc = new WorldCreator("lobby");
        wc.environment(World.Environment.NORMAL);
        wc.createWorld();

        World lobby = Bukkit.getWorld("lobby");
        lobby.setGameRuleValue("doMobSpawning", "false");
        lobby.setGameRuleValue("doDaylightCycle", "false");

        try {
            WorldUtils.generateWorld();
        } catch (IOException e) {
            e.printStackTrace();
        }

        WorldCreator wc2 = new WorldCreator("mineral");
        wc2.environment(World.Environment.NORMAL);
        wc2.createWorld();

        Bukkit.getWorld("mineral").setGameRuleValue("keepInventory", "true");
        Bukkit.getWorld("mineral").setGameRuleValue("doDaylightCycle", "false");

        DefaultLocation.create();
        teamManager = new TeamManager(this);

        game.create();
        invManager.init();
        System.out.println("Mineral Contest enabled");
        getCommand("config").setExecutor(new ConfigCommand());
        getCommand("start").setExecutor(new StartCommand(this));
        getCommand("tparene").setExecutor(new TpAreneCommand(this));
        Bukkit.getScheduler().runTaskTimer(this, () -> game.tick(), 1, 1);

    }

    public InventoryManager getInvManager() {
        return invManager;
    }

    public Game getGame() {
        return game;
    }

    public MineralContest getInstance() {
        return instance;
    }

    public ArrayList<Integer> slot(int nbr) {
        int l = 0;
        int c = 0;
        c = nbr % 9;
        l = (nbr - c) / 9;
        ArrayList<Integer> cl = new ArrayList<>();
        cl.add((Integer) l);
        cl.add((Integer) c);
        return cl;
    }


}
