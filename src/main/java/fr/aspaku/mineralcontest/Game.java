package fr.aspaku.mineralcontest;

import fr.aspaku.mineralcontest.guis.ClassSelector;
import fr.aspaku.mineralcontest.utils.*;
import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Game {

    public MineralContest plugin;
    public State state;
    public int teamSize;
    public int tick;
    public ArrayList<MPlayer> mplayers;
    public ArrayList<MPlayer> decoPlayers = new ArrayList();
    public int duration = 60 * 60;
    public boolean friendly_fire = false;
    public BPlayerBoard board;
    public boolean tp_available = false;

    public Game(MineralContest plugin) {
        this.plugin = plugin;
    }

    public void create() {
        this.state = State.Open;
        teamSize = 2;
        mplayers = new ArrayList<>();

        try {
            WorldUtils.pasteSchematic(DefaultLocation.lobby.getWorld(), DefaultLocation.lobby, plugin, "spawn.schematic");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tick() {
        if (tick == duration * 20 && state.equals(State.Launched)) {
            end();
        }
        if (tick == 0 && state.equals(State.Launched)) {
            broadcast("La partie peut commencer, bonne chance");
            initEffect();
        }
        if (tick % 20 == 0 && state.equals(State.Launched)) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                board = Netherboard.instance().getBoard(player);
                board.set("§c>> Joueurs: §f" + mplayers.size(), 2);
                if (tick < 0) {
                    board.set("§c>> La partie commence dans :" + getTime(-tick / 20), 3);
                } else {
                    board.set("§c>> Temps restant : §f" + getTime(duration - tick / 20), 3);
                }
            }
        }
        if (tick % (600 * 20) == (590 * 20) && tick != 0 && state.equals(State.Launched)) { //10 sec before X0min for fill the chest
            broadcast("Un coffre va être disponible dans 10 secondes");
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                fillChest();
                setTp_available(true);
                for (MPlayer mPlayer : mplayers) {
                    net.md_5.bungee.api.chat.TextComponent msg = new net.md_5.bungee.api.chat.TextComponent("Le coffre de l'arène a été rempli, cliquez ici pour téléporter votre équipe");
                    msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tparene"));
                    mPlayer.getPlayer().spigot().sendMessage(msg);
                }
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    setTp_available(false);
                }, 200);
            }, 200);

        }
        if (state.equals(State.Open)) {
            for (MPlayer mPlayer : mplayers) {
                Player player = mPlayer.getPlayer();
                if (player.getLocation().getY() < 150) player.teleport(DefaultLocation.lobby);
            }
        }

        tick++;
    }

    public void launch() {
        /*
        for (MPlayer mPlayer : mplayers){
            if (mPlayer.getTeam()==null){
                broadcast("§cAu moins un joueur n'est pas dans une Team, il est donc impossible de lancer la partie !");
                return;
            }
        }*/
        if (plugin.teamManager.getNotEmptyTeams().size() != 4) {
            broadcast("§cUne des quatres équipes n'a pas de joueurs, lancement annulé !");
            return;
        }
        tick = -1200;
        for (MPlayer mplayer : mplayers) {
            mplayer.getPlayer().setDisplayName(mplayer.getTeam().getPrefix() + mplayer.getPlayer().getName());
            mplayer.getPlayer().setPlayerListName(mplayer.getTeam().getPrefix() + mplayer.getPlayer().getName());
            broadcast(mplayer.getTeam().getName());
            broadcast(mplayer.getPlayer().getName());
            mplayer.getTeam().spawn(mplayer);
            broadcast("Téléportation de §7" + mplayer.getPlayer().getName());
            mplayer.getPlayer().getInventory().clear();
            mplayer.getPlayer().getInventory().setArmorContents(null);
            mplayer.getPlayer().getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
            mplayer.getPlayer().getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
            mplayer.getPlayer().getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
            mplayer.getPlayer().getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
            mplayer.getPlayer().getInventory().setItem(0, new ItemStack(Material.IRON_SWORD));
            mplayer.getPlayer().getInventory().setItem(1, new ItemStack(Material.BOW));
            mplayer.getPlayer().getInventory().setItem(2, new ItemStack(Material.ARROW, 32));
            mplayer.getPlayer().setGameMode(GameMode.SURVIVAL);
            mplayer.getPlayer().setMaxHealth(20);
            mplayer.getPlayer().setHealth(20);
            mplayer.getPlayer().setFoodLevel(40);
            mplayer.getPlayer().setExp(0);
            mplayer.getPlayer().setLevel(0);
            mplayer.getPlayer().getActivePotionEffects().clear();
            mplayer.getPlayer().getLocation().getWorld().setTime(0);
            mplayer.getPlayer().getLocation().getWorld().setStorm(false);
            mplayer.getPlayer().getLocation().getWorld().setThundering(false);
            mplayer.getPlayer().getLocation().getWorld().setDifficulty(Difficulty.PEACEFUL);
            mplayer.getPlayer().getLocation().getWorld().setDifficulty(Difficulty.NORMAL);
            mplayer.getPlayer().setPlayerListName(mplayer.getPlayer().getDisplayName());
            board = Netherboard.instance().createBoard(mplayer.getPlayer(), "§6§lMineral Contest");
            board.setAll(
                    "§7§m+-------------------+",
                    "§c>> Équipe: " + mplayer.getTeam().getName(),
                    "§c>> Points: §f" + mplayer.getTeam().getPoints(),
                    "§c>> Joueurs: §f" + Bukkit.getOnlinePlayers().size(),
                    "§c>> Temps restant : ",
                    "§f§7§m+-------------------+"
            );
            mplayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 10, false, false));
            mplayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 200, false, false));
            ClassSelector.CLASSSELECTOR.open(mplayer.getPlayer());
        }


        this.state = State.Launched;
    }

    public void end() {
        this.state = State.End;
        addBonus();
        Team team_win = plugin.teamManager.getTeams().get(0);
        Team team_2 = null;
        Team team_3 = null;
        Team team_4 = null;
        for (Team team : plugin.teamManager.getTeams()) {
            if (team.getPoints() > team_win.getPoints()) team_win = team;
        }
        for (Team team : plugin.teamManager.getTeams()) {
            if (!team.equals(team_win)) {
                if (team_2 == null) team_2 = team;
                else {
                    if (team.getPoints() > team_2.getPoints()) team_2 = team;
                }
            }
        }
        for (Team team : plugin.teamManager.getTeams()) {
            if (!(team.equals(team_win) || team.equals(team_2))) {
                if (team_3 == null) team_3 = team;
                else {
                    if (team.getPoints() > team_3.getPoints()) team_3 = team;
                }
            }
        }
        for (Team team : plugin.teamManager.getTeams()) {
            if (!(team.equals(team_win) || team.equals(team_2) || team.equals(team_3))) team_4 = team;
        }

        broadcast("§bL'équipe" + team_win.getName() + "§bremporte la partie avec " + team_win.getPoints() + " points !");

        for (MPlayer mplayer : mplayers) {
            mplayer.getPlayer().setGameMode(GameMode.SPECTATOR);
            mplayer.getPlayer().getLocation().getWorld().setDifficulty(Difficulty.PEACEFUL);
            board = Netherboard.instance().getBoard(mplayer.getPlayer());
            board.setAll(
                    "§7§m+-------------------+",
                    "§c>> 1er : §f" + team_win.getName() + " : " + team_win.getPoints() + " points",
                    "§c>> 2ème : §f" + team_2.getName() + " : " + team_2.getPoints() + " points",
                    "§c>> 3ème : §f" + team_3.getName() + " : " + team_3.getPoints() + " points",
                    "§c>> 4ème : §f" + team_4.getName() + " : " + team_4.getPoints() + " points",
                    "§f§7§m+-------------------+"
            );
        }
    }


    public MPlayer getMplayer(Player player) {
        for (MPlayer mPlayer : mplayers) {
            if (mPlayer.getPlayer() == player) {
                return mPlayer;
            }
        }
        return null;
    }

    public void addBonus() {
        for (Team team : plugin.teamManager.getTeams()) {
            for (MPlayer mPlayer : team.getMPlayers()) {
                if (mPlayer.getClasse().equals(Classes.Travailleur)) {
                    team.setPoints((int) Math.floor(1.25 * team.getPoints()));
                }
            }
        }
    }

    public void makePlayerJoin(Player player) {
        player.teleport(DefaultLocation.lobby);
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(40);
        broadcast(player.getDisplayName() + "§e a rejoint la partie §7(" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ")");
        ItemStack ItemTeams = new ItemCreator(Material.BANNER, 0).setName("§cSélection des Teams").getItem();
        player.getInventory().setItem(0, ItemTeams);
        player.updateInventory();

        if (player.hasPermission("mineral.config")) {
            ItemStack ItemConfig = new ItemCreator(Material.COMMAND, 0).setName("§cConfig").getItem();
            player.getInventory().setItem(8, ItemConfig);
            player.updateInventory();
        }
    }

    public void makePlayerLeave(Player player) {
        broadcast(player.getDisplayName() + "§e a quitté la partie §7(" + (Bukkit.getOnlinePlayers().size() - 1) + "/" + Bukkit.getMaxPlayers() + ")");
        if (getMplayer(player).getTeam() == null) return;
        getMplayer(player).team.removeMPlayers(getMplayer(player));
        mplayers.remove(getMplayer(player));
    }

    public void setSpectatorMode(Player player, String message) {
        player.sendMessage("§6Mineral Contest | §b" + message);
        player.setGameMode(GameMode.SPECTATOR);
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
    }

    public void setDuration(int duration) {
        this.duration = Math.max(duration, 1);
    }

    public String getBoolean(boolean bool) {
        if (bool) return "activé";
        return "désactivé";
    }

    public void setTeamSize(int nbr) {
        teamSize = Math.max(nbr, 1);
    }

    public void broadcast(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("§6Mineral Contest | §b" + message);
        }
    }

    public void initEffect() {
        for (MPlayer mPlayer : mplayers) {
            resetEffects(mPlayer);
        }
    }

    public void resetEffects(MPlayer mPlayer) {

        for (PotionEffect effect : mPlayer.getPlayer().getActivePotionEffects()) {
            mPlayer.getPlayer().removePotionEffect(effect.getType());
        }
        if (mPlayer.getClasse() == null) {
            mPlayer.setClasse(Classes.Agile);
        }
        mPlayer.getPlayer().setMaxHealth(20);
        if (mPlayer.getClasse().equals(Classes.Travailleur)) {
            mPlayer.getPlayer().setMaxHealth(10);
        } else if (mPlayer.getClasse().equals(Classes.Robuste)) {
            mPlayer.getPlayer().setMaxHealth(30);
            mPlayer.getPlayer().setWalkSpeed((float) (0.2f * 0.85));
        } else if (mPlayer.getClasse().equals(Classes.Guerrier)) {
            mPlayer.getPlayer().setMaxHealth(14);
        } else if (mPlayer.getClasse().equals(Classes.Agile)) {
            mPlayer.getPlayer().setWalkSpeed((float) (0.2f * 1.20));
        } else if (mPlayer.getClasse().equals(Classes.Mineur)) {
            PlayerInventory inv = mPlayer.getPlayer().getInventory();
            inv.setItem(9, new ItemStack(Material.BARRIER));
            inv.setItem(10, new ItemStack(Material.BARRIER));
            inv.setItem(11, new ItemStack(Material.BARRIER));
            inv.setItem(12, new ItemStack(Material.BARRIER));
            inv.setItem(13, new ItemStack(Material.BARRIER));
            inv.setItem(14, new ItemStack(Material.BARRIER));
            inv.setItem(15, new ItemStack(Material.BARRIER));
            inv.setItem(16, new ItemStack(Material.BARRIER));
            inv.setItem(17, new ItemStack(Material.BARRIER));
        }

    }

    public void fillChest() {
        BlockState state = DefaultLocation.centre.getBlock().getState();
        if (!(state instanceof Chest)) {
            broadcast("PAS UN COFFRE");
            return;
        }
        Chest chest = (Chest) state;
        Random r = new Random();
        for (int i = 0; i < 26; i++) {
            int random = r.nextInt(100);
            if (random < 60) {
                chest.getInventory().setItem(i, new ItemStack(Material.IRON_INGOT));
            } else if (random < 90) {
                chest.getInventory().setItem(i, new ItemStack(Material.GOLD_INGOT));
            } else {
                chest.getInventory().setItem(i, new ItemStack(Material.DIAMOND));
            }
        }
    }

    public void setTp_available(boolean tp_available) {
        this.tp_available = tp_available;
    }

    public String getTime(int s) {
        int m = 0;
        int h = 0;

        while (s >= 60) {
            m++;
            s -= 60;
        }
        while (m >= 60) {
            h++;
            m -= 60;
        }

        String msg = "";
        if (h > 0) {
            if (h < 10) {
                msg += "0" + h + ":";
            } else {
                msg += h + ";";
            }
        }
        if (m < 10) {
            msg += "0" + m + ":";
        } else {
            msg += m + ":";
        }
        if (s < 10) {
            msg += "0" + s;
        } else {
            msg += s;
        }

        return msg;
    }


}
