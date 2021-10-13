package fr.aspaku.mineralcontest.commands;

import fr.aspaku.mineralcontest.MineralContest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    public MineralContest plugin;

    public StartCommand(MineralContest plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player && sender.hasPermission("mineral.config")) {
            plugin.game.launch();
        }
        return false;
    }
}
