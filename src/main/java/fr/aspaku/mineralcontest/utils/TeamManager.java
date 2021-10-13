package fr.aspaku.mineralcontest.utils;

import fr.aspaku.mineralcontest.MineralContest;
import org.bukkit.DyeColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class TeamManager {

    private MineralContest plugin;
    private ArrayList<Team> teams;

    public TeamManager(MineralContest plugin) {
        this.plugin = plugin;
        teams = new ArrayList<>();

        createTeams();
    }


    public void createTeams() {
        Team team = new Team("§4", "§4Rouge", new ItemCreator(Material.BANNER, 0).setBasecolor(DyeColor.RED), DefaultLocation.spawn_red, DefaultLocation.tp_red);
        teams.add(team);
        team = new Team("§b", "§bBleue", new ItemCreator(Material.BANNER, 0).setBasecolor(DyeColor.BLUE), DefaultLocation.spawn_blue, DefaultLocation.tp_blue);
        teams.add(team);
        team = new Team("§a", "§aVerte", new ItemCreator(Material.BANNER, 0).setBasecolor(DyeColor.GREEN), DefaultLocation.spawn_green, DefaultLocation.tp_green);
        teams.add(team);
        team = new Team("§e", "§eJaune", new ItemCreator(Material.BANNER, 0).setBasecolor(DyeColor.YELLOW), DefaultLocation.spawn_yellow, DefaultLocation.tp_yellow);
        teams.add(team);
    }

    public List<Team> getAllTeams() {
        ArrayList<Team> allTeams = new ArrayList<>();
        allTeams.addAll(teams);
        return allTeams;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public Team getTeam(String name) {
        for (Team team : teams) {
            if (team.getName().equals(name)) return team;
        }
        return null;
    }


    public List<Team> getNotFullTeams() {

        //Pour envoyer les team les plus vides d'abord
        //évite de commencer avec les deux joueurs dans la même team
        int lowest = 100;
        for (Team team : getTeams()) {
            if (team.getMPlayers().size() < lowest) lowest = team.getMPlayers().size();
        }

        ArrayList<Team> teams = new ArrayList<>();
        for (Team team : getTeams()) {
            if (team.getMPlayers().size() == lowest) teams.add(team);
        }
        return teams;
    }


    public List<Team> getNotEmptyTeams() {
        ArrayList<Team> teams = new ArrayList<>();
        for (Team team : getTeams()) {
            if (team.getMPlayers().size() > 0) teams.add(team);
        }
        return teams;
    }


    public void checkEveryTeams() {
        List<Team> activatedTeams = getTeams();
        for (Team team : getAllTeams()) {
            //Check team inactivées
            if (!activatedTeams.contains(team)) {
                ArrayList<MPlayer> mPlayers = new ArrayList<>(team.getMPlayers());
                for (MPlayer mPlayer : mPlayers) {
                    mPlayer.setTeam(null, true);
                }
            }

            //Check taille des jouerus dans la team
            if (team.getMPlayers().size() > plugin.game.teamSize) {
                team.getMPlayers().get(plugin.game.teamSize - 1).setTeam(null, true);
            }
        }
    }

    public void killTeam(Team team) {
        teams.remove(team);
    }

    public void resTeam(Team team) {
        teams.add(team);
    }

    public void updateTeams(Team team) {
        if (team.getMPlayers().size() == 0) {
            if (teams.contains(team))
                teams.remove(team);
        }
    }

    public void deleteTeam(Team team) {
        teams.remove(team);
        for (MPlayer mPlayer : new ArrayList<>(team.getMPlayers())) {
            mPlayer.setTeam(null, true);
        }
    }

    public void deleteLastTeam() {
        Team team = teams.get(teams.size() - 1);
        deleteTeam(team);
    }
}
