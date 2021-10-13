package fr.aspaku.mineralcontest.utils;

import org.bukkit.Material;

public enum Classes {

    Agile(Material.FEATHER, "Vous vous déplacez avec 20% de vitesse en plus et vous ne prenez pas de dégâts de chute."),
    Mineur(Material.DIAMOND_PICKAXE, "Lorsque vous minez, les minerais sont déjà fondus mais vous avez une ligne d'inventaire en moins"),
    Robuste(Material.DIAMOND_CHESTPLATE, "Vous avez 15 ❤ mais vous faites 15% de dégats en moins et aller 15% moins vite"),
    Travailleur(Material.GOLD_INGOT, "Vous augmentez les points gagnés par votre équipe de 20% mais vous n'aurez que 5 ❤"),
    Guerrier(Material.IRON_SWORD, "Vous infligez 25% de dégats supplémentaires mais vous n'aurez que 7 ❤");

    public Material mat;
    public String description;

    Classes(Material mat, String description) {
        this.mat = mat;
        this.description = description;
    }

    public Material getMat() {
        return mat;
    }
}
