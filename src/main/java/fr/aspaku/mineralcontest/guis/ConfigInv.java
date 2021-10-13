package fr.aspaku.mineralcontest.guis;

import fr.aspaku.mineralcontest.MineralContest;
import fr.aspaku.mineralcontest.utils.ItemCreator;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

public class ConfigInv implements InventoryProvider {

    private MineralContest plugin;

    public ConfigInv() {
        this.plugin = (MineralContest) Bukkit.getServer().getPluginManager().getPlugin("MineralContest");
    }

    public static final SmartInventory CONFIGINV = SmartInventory.builder()
            .id("configGUI")
            .provider(new ConfigInv())
            .size(4, 9)
            .title("Configuration")
            .manager(JavaPlugin.getPlugin(MineralContest.class).getInvManager())
            .build();


    @Override
    public void init(Player player, InventoryContents contents) {
        ItemStack blackGlass = new ItemCreator(Material.STAINED_GLASS_PANE, 15).setName(" ").getItem();
        contents.fill(ClickableItem.empty(new ItemStack(blackGlass)));

        contents.set(0, 8, ClickableItem.of(new ItemCreator(Material.BARRIER, 0).setName("§4Fermer l'inventaire").getItem(), e -> {
            CONFIGINV.close(player);
        }));

        ItemStack pvpTimeAdd10s = new ItemCreator(Material.BANNER, 0).setName("§a+10 secondes").setBasecolor(DyeColor.WHITE).addBannerPreset(10, DyeColor.BLACK).getItem();

        ItemStack pvpTimeRemove10s = new ItemCreator(Material.BANNER, 0).setName("§c-10 secondes").setBasecolor(DyeColor.WHITE).addBannerPreset(9, DyeColor.BLACK).getItem();

        ItemStack pvpTimeAdd1m = new ItemCreator(Material.BANNER, 0).setName("§a+1 minute").setBasecolor(DyeColor.WHITE).addBannerPreset(10, DyeColor.BLACK).getItem();

        ItemStack pvpTimeRemove1m = new ItemCreator(Material.BANNER, 0).setName("§c-1 minute").setBasecolor(DyeColor.WHITE).addBannerPreset(9, DyeColor.BLACK).getItem();

        ItemStack pvpTimeAdd10m = new ItemCreator(Material.BANNER, 0).setName("§a+10 minutes").setBasecolor(DyeColor.WHITE).addBannerPreset(10, DyeColor.BLACK).getItem();

        ItemStack pvpTimeRemove10m = new ItemCreator(Material.BANNER, 0).setName("§c-10 minutes").setBasecolor(DyeColor.WHITE).addBannerPreset(9, DyeColor.BLACK).getItem();


        contents.set(1, 1, ClickableItem.of(new ItemStack(pvpTimeRemove10m),
                e -> plugin.game.setDuration(plugin.game.duration - 600)));

        contents.set(1, 2, ClickableItem.of(new ItemStack(pvpTimeRemove1m),
                e -> plugin.game.setDuration(plugin.game.duration - 60)));

        contents.set(1, 3, ClickableItem.of(new ItemStack(pvpTimeRemove10s),
                e -> plugin.game.setDuration(plugin.game.duration - 10)));

        contents.set(1, 5, ClickableItem.of(new ItemStack(pvpTimeAdd10s),
                e -> plugin.game.setDuration(plugin.game.duration + 10)));

        contents.set(1, 6, ClickableItem.of(new ItemStack(pvpTimeAdd1m),
                e -> plugin.game.setDuration(plugin.game.duration + 60)));

        contents.set(1, 7, ClickableItem.of(new ItemStack(pvpTimeAdd10m),
                e -> plugin.game.setDuration(plugin.game.duration + 600)));

    }

    @Override
    public void update(Player player, InventoryContents contents) {
        ClickableItem item = ClickableItem.empty(new ItemCreator(Material.WATCH, 0).setName("Durée de la partie").setLores(Collections.singletonList(plugin.game.getTime(plugin.game.duration))).getItem());
        contents.set(1, 4, item);
        item = ClickableItem.of(new ItemCreator(Material.IRON_SWORD, 0).setName("Friendly Fire :" + plugin.game.getBoolean(plugin.game.friendly_fire)).getItem(), e -> {
            plugin.game.friendly_fire = !plugin.game.friendly_fire;
        });
        contents.set(2, 4, item);
        item = ClickableItem.of(new ItemCreator(Material.BANNER, 0).setBasecolor(DyeColor.BLACK).setName("Taille des équipes : " + plugin.game.teamSize).getItem(), e -> {
            if (e.getClick().isRightClick()) {
                plugin.game.setTeamSize(plugin.game.teamSize++);
            } else {
                plugin.game.setTeamSize(plugin.game.teamSize--);
            }
        });
    }
}
