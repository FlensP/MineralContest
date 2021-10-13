package fr.aspaku.mineralcontest.commands;

import fr.aspaku.mineralcontest.guis.ConfigInv;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConfigCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player && sender.hasPermission("mineral.config")) {
            ConfigInv.CONFIGINV.open((Player) sender);
        }
        return false;
    }
}
