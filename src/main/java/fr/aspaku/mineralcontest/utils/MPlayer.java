package fr.aspaku.mineralcontest.utils;

import fr.aspaku.mineralcontest.MineralContest;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MPlayer {


    private MineralContest plugin;

    public MPlayer(Player player, MineralContest plugin) {
        this.plugin = plugin;
        this.player = player;
        this.team = null;
    }

    private Player player;
    public Team team;
    public Classes classe = null;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team, boolean silent) {
        if (plugin.game.state.equals(State.Launched) && getTeam() != null)
            plugin.teamManager.updateTeams(getTeam());
        if (team == null) {
            if (this.team != null) this.team.removeMPlayers(this);

            this.team = null;
            if (plugin.game.state.equals(State.Open)) {
                getPlayer().getInventory().setItem(0, new ItemCreator(Material.BANNER, 0).setName("§cSélection des Teams").getItem());
                player.getInventory().setHelmet(null);
            }
            if (!silent) player.sendMessage("§6Mineral Contest | §cVous avez quitté votre équipe");
        } else {
            Team actualTeam = getTeam();
            if (actualTeam != null) actualTeam.removeMPlayers(this);

            if (plugin.game.state.equals(State.Open)) {
                getPlayer().getInventory().setItem(0, team.getItemCreator().setName("§cSélection des Teams").clearLores().getItem());
                player.getInventory().setHelmet(team.getItemCreator().getItem());
            }

            this.team = team;
            this.team.addMPlayer(this);

            if (!silent) player.sendMessage("§6Mineral Contest | §aVous avez rejoint l'équipe " + team.getName());
        }

        if (plugin.game.state.equals(State.Launched) && getTeam() != null)
            plugin.teamManager.updateTeams(getTeam());
    }

    public Classes getClasse() {
        return classe;
    }

    public void setClasse(Classes classe) {
        this.classe = classe;
    }
}
