package fr.aspaku.mineralcontest.guis;

import fr.aspaku.mineralcontest.MineralContest;
import fr.aspaku.mineralcontest.utils.ItemCreator;
import fr.aspaku.mineralcontest.utils.Team;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class TeamPicker implements InventoryProvider {

    private MineralContest plugin;

    public TeamPicker() {
        this.plugin = (MineralContest) Bukkit.getServer().getPluginManager().getPlugin("MineralContest");
    }

    public static final SmartInventory TEAMPICKER = SmartInventory.builder()
            .id("teamPickerGUI")
            .provider(new TeamPicker())
            .size(3, 9)
            .title("§cMineral Contest | Équipes")
            .manager(JavaPlugin.getPlugin(MineralContest.class).getInvManager())
            .build();

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        ItemStack blackGlass = new ItemCreator(Material.STAINED_GLASS_PANE, 15).setName(" ").getItem();
        inventoryContents.fill(ClickableItem.empty(new ItemStack(blackGlass)));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

        int index = 1;
        for (Team team : plugin.teamManager.getTeams()) {
            ItemStack item = new ItemStack(team.getItemCreator()
                    .setLores(Arrays.asList("§7", "§7Joueurs: " + team.getPlayerInString() + "§7 (" + team.getMPlayers().size() + "/" + plugin.game.teamSize + ")")).getItem());

            ClickableItem clickableItem = ClickableItem.of(item, e -> {
                if (team.getMPlayers().size() < plugin.game.teamSize) {
                    plugin.game.getMplayer((Player) e.getWhoClicked()).setTeam(team, false);
                }
            });

            inventoryContents.set(1, index, clickableItem);
            index++;
        }

    }
}
