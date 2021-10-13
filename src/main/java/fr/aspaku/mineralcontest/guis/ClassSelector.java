package fr.aspaku.mineralcontest.guis;

import fr.aspaku.mineralcontest.MineralContest;
import fr.aspaku.mineralcontest.utils.Classes;
import fr.aspaku.mineralcontest.utils.ItemCreator;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

public class ClassSelector implements InventoryProvider {


    private MineralContest plugin;

    public ClassSelector() {
        this.plugin = (MineralContest) Bukkit.getServer().getPluginManager().getPlugin("MineralContest");
    }

    public static final SmartInventory CLASSSELECTOR = SmartInventory.builder()
            .id("classSelectorGUI")
            .provider(new ClassSelector())
            .size(1, 9)
            .title("Choisissez votre kit")
            .manager(JavaPlugin.getPlugin(MineralContest.class).getInvManager())
            .build();


    @Override
    public void init(Player player, InventoryContents contents) {
        ClickableItem item;
        int index = 0;
        for (Classes classes : Classes.values()) {
            item = ClickableItem.of(new ItemCreator(classes.getMat(), 0).setName(classes.name()).setLores(Collections.singletonList(classes.description)).getItem(), e -> {
                Player player1 = (Player) e.getWhoClicked();
                plugin.game.getMplayer(player1).setClasse(classes);
                CLASSSELECTOR.close(player1);
            });
            contents.set(0, index, item);
            index++;
        }
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
