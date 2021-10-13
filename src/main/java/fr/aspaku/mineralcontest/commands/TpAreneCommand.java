package fr.aspaku.mineralcontest.commands;

import fr.aspaku.mineralcontest.MineralContest;
import fr.aspaku.mineralcontest.utils.MPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpAreneCommand implements CommandExecutor {

    public MineralContest plugin;

    public TpAreneCommand(MineralContest plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player && plugin.game.getMplayer((Player) sender) != null) {
            if (plugin.game.tp_available) {
                for (MPlayer mPlayer : plugin.game.getMplayer((Player) sender).getTeam().getMPlayers()) {
                    mPlayer.getPlayer().teleport(mPlayer.getTeam().getTp());
                }
            }
        }
        return false;
    }
}
