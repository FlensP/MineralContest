package fr.aspaku.mineralcontest.utils;

import org.bukkit.Location;

import java.util.ArrayList;

public class Team {
    private final String prefix;                          //Prefix dans le tab
    private final String name;                    //Nom affiché en jeu
    private final ItemCreator item;                //Couleur de la banière
    private final ArrayList<MPlayer> mPlayers;    //Les joueurs dans la team
    private Location spawn;
    private Location tp;
    private int points;

    public Team(String prefix, String name, ItemCreator item, Location spawn, Location tp) {
        //Initialisation
        this.prefix = prefix;
        this.name = name;
        this.item = item.setName(name);
        mPlayers = new ArrayList<>();
        points = 0;
        this.spawn = spawn;
        this.tp = tp;
    }

    public void spawn(MPlayer mplayer) {
        mplayer.getPlayer().teleport(spawn);
    }


    //Get le nom pour affichage
    public String getName() {
        return name;
    }

    //Get les joueurs de la team
    public ArrayList<MPlayer> getMPlayers() {
        return mPlayers;
    }

    //Ajouter un joueur
    public void addMPlayer(MPlayer mPlayer) {
        if (!mPlayers.contains(mPlayer)) mPlayers.add(mPlayer);
    }

    //Supprimer un joueur
    public void removeMPlayers(MPlayer sPlayer) {
        mPlayers.remove(sPlayer);
    }

    public ItemCreator getItemCreator() {
        return item;
    }

    //Renvoie la liste des joueurs sous forme de message texte
    public String getPlayerInString() {
        if (mPlayers.size() > 0) {
            StringBuilder txt = new StringBuilder("§e");

            for (MPlayer mPlayer : mPlayers) {
                txt.append(mPlayer.getPlayer().getDisplayName()).append(", ");
            }

            return txt.substring(0, txt.length() - 2);
        } else {
            return "§7Aucun ...";
        }
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public Location getTp() {
        return tp;
    }

    public void setTp(Location tp) {
        this.tp = tp;
    }
}
