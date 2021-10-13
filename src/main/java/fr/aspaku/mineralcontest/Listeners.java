package fr.aspaku.mineralcontest;

import fr.aspaku.mineralcontest.guis.ConfigInv;
import fr.aspaku.mineralcontest.guis.TeamPicker;
import fr.aspaku.mineralcontest.utils.*;
import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Objects;

public class Listeners implements Listener {

    public MineralContest plugin;

    public Listeners(MineralContest plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPortalTravel(PlayerPortalEvent event) {
        event.setCancelled(true);
        event.getPlayer().sendMessage("§4Le Nether et l'End sont désactivés");
    }


    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (!plugin.game.state.equals(State.Launched)) {
            event.setCancelled(true);
        } else {
            if (isNear(event.getBlock().getLocation())) {
                event.setCancelled(true);
            } else {
                if (plugin.game.getMplayer(event.getPlayer()).getClasse().equals(Classes.Mineur)) {
                    Block block = event.getBlock();
                    Location loc = new Location(block.getWorld(), block.getLocation().getBlockX() + 0.5, block.getLocation().getBlockY() + 0.5, block.getLocation().getBlockZ() + 0.5);


                    Material currentItemType = event.getPlayer().getItemInHand().getType();

                    switch (block.getType()) {

                        case IRON_ORE:

                            if (!currentItemType.equals(Material.DIAMOND_PICKAXE) && !currentItemType.equals(Material.IRON_PICKAXE) && !currentItemType.equals(Material.STONE_PICKAXE)) {
                                return;
                            }
                            block.getWorld().spawn(loc, ExperienceOrb.class).setExperience(1);
                            block.setType(Material.AIR);
                            block.getWorld().dropItem(loc, new ItemStack(Material.IRON_INGOT, 1));
                            break;

                        case GOLD_ORE:
                            if (!currentItemType.equals(Material.DIAMOND_PICKAXE) && !currentItemType.equals(Material.IRON_PICKAXE)) {
                                return;
                            }
                            block.getWorld().spawn(loc, ExperienceOrb.class).setExperience(1);
                            block.setType(Material.AIR);
                            block.getWorld().dropItem(loc, new ItemStack(Material.GOLD_INGOT, 1));
                            break;

                        default:
                            break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (!plugin.game.state.equals(State.Launched)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (!plugin.game.state.equals(State.Launched)) {
                event.setCancelled(true);
            } else {
                if (plugin.game.getMplayer(player).getClasse() == Classes.Agile) {
                    if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                        event.setCancelled(true);
                    }
                    /*if(player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL){
                            event.setCancelled(true);
                    }*/
                }
            }
        }
    }

    @EventHandler
    public void onDamagebyEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) return;
        if (plugin.game.state.equals(State.Open)) {
            event.setCancelled(true);
            return;
        }
        Entity entity_damager;
        if (!(event.getDamager() instanceof Player)) {
            if (event.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getDamager();
                if (arrow.getShooter() instanceof Player) {
                    return;
                } else {
                    entity_damager = (Entity) arrow.getShooter();
                }
            } else return;
        } else {
            entity_damager = event.getDamager();
        }
        if (!(entity_damager instanceof Player)) return;

        if (plugin.game.getMplayer((Player) entity_damager) == null) return;
        Classes classes = plugin.game.getMplayer((Player) entity_damager).getClasse();
        if (classes.equals(Classes.Guerrier)) {
            event.setDamage(event.getDamage() * 1.25);
        } else if (classes.equals(Classes.Robuste)) {
            event.setDamage(event.getDamage() * 0.85);
        }
        Player player = ((Player) entity).getPlayer();
        Player damager = ((Player) entity_damager);

        if (player.getLocation().getY() == DefaultLocation.tp_blue.getY() && isNear(player.getLocation())) {
            event.setCancelled(true);
            damager.sendMessage("Vous ne pouvez pas faire de dégats à quelqu'un qui est dans son tp");
            return;
        }
        if (damager.getLocation().getY() == DefaultLocation.tp_blue.getY() && isNear(damager.getLocation())) {
            event.setCancelled(true);
            damager.sendMessage("Vous ne pouvez pas faire de dégats à quelqu'un si vous êtes dans votre tp");
            return;
        }
        if (isInABase(player.getLocation())) {
            event.setCancelled(true);
            damager.sendMessage("Vous ne pouvez pas faire de dégats à quelqu'un qui est dans sa base");
            return;
        }
        if (isInABase(damager.getLocation())) {
            event.setCancelled(true);
            damager.sendMessage("Vous ne pouvez pas faire de dégats à quelqu'un si vous êtes dans votre base");
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.getPlayer() != null) {
            if (plugin.game.state.equals(State.Open)) {
                event.setCancelled(true);
            } else if (isNear(event.getBlock().getLocation())) {
                event.setCancelled(true);
            } else if (event.getBlock().getType().equals(Material.CHEST)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        if (event.getPlayer() != null) {
            if (plugin.game.state.equals(State.Open)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onCloseInv(InventoryCloseEvent event) {
        if (!plugin.game.state.equals(State.Launched)) return;
        if (plugin.game.getMplayer((Player) event.getPlayer()) == null) return;
        if (event.getInventory().getHolder() instanceof Chest) {
            if (isInABase(((Chest) event.getInventory().getHolder()).getLocation())) {
                for (ItemStack item : event.getInventory().getContents()) {
                    if (item != null) {
                        if (item.getType() == Material.DIAMOND) {
                            plugin.game.getMplayer((Player) event.getPlayer()).getTeam().setPoints(plugin.game.getMplayer((Player) event.getPlayer()).getTeam().getPoints() + 150);
                        } else if (item.getType() == Material.GOLD_INGOT) {
                            plugin.game.getMplayer((Player) event.getPlayer()).getTeam().setPoints(plugin.game.getMplayer((Player) event.getPlayer()).getTeam().getPoints() + 50);
                        } else if (item.getType() == Material.EMERALD) {
                            plugin.game.getMplayer((Player) event.getPlayer()).getTeam().setPoints(plugin.game.getMplayer((Player) event.getPlayer()).getTeam().getPoints() + 300);
                        } else if (item.getType() == Material.IRON_INGOT) {
                            plugin.game.getMplayer((Player) event.getPlayer()).getTeam().setPoints(plugin.game.getMplayer((Player) event.getPlayer()).getTeam().getPoints() + 10);
                        }
                    }
                }
                BPlayerBoard board = Netherboard.instance().getBoard(((Player) event.getPlayer()));
                for (MPlayer mPlayer : plugin.game.getMplayer(((Player) event.getPlayer())).getTeam().getMPlayers())
                    board.set("§c>> Points: " + mPlayer.getTeam().getPoints(), 1);
                event.getInventory().clear();
            } else {
                event.getPlayer().sendMessage("Vous n'êtes pas dans une base");
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();

        if (plugin.game.state.equals(State.Open)) {
            if (WorldUtils.isPasting()) {
                player.kickPlayer("Le spawn est en train d'appaître, attends quelques instants");
            }
            plugin.game.makePlayerJoin(player);
            plugin.game.mplayers.add(new MPlayer(player, plugin));
        } else {
            boolean remove = false;
            for (MPlayer mPlayer : plugin.game.decoPlayers) {

                if (mPlayer.getPlayer().getName().equals(player.getName())) {
                    mPlayer.setPlayer(player);
                    plugin.game.decoPlayers.remove(mPlayer);
                    mPlayer.getPlayer().setDisplayName(mPlayer.getTeam().getPrefix() + mPlayer.getPlayer().getName());
                    mPlayer.getPlayer().setPlayerListName(mPlayer.getTeam().getPrefix() + mPlayer.getPlayer().getName());
                    mPlayer.getPlayer().setPlayerListName(player.getDisplayName());
                    plugin.game.resetEffects(mPlayer);

                    remove = true;
                    break;
                }
            }

            if (remove) {
                plugin.game.broadcast(player.getName() + " est revenu dans la partie");
            } else {
                plugin.game.setSpectatorMode(player, "La partie est lancé, gamemode spectator activé");
            }

            if (plugin.game.state.equals(State.Launched)) {
                BPlayerBoard board = Netherboard.instance().createBoard(player.getPlayer(), "§6§lBingo");
                String teamName;
                int points = 0;
                if (plugin.game.getMplayer(player) == null) {
                    teamName = "§7Spectateur";
                } else {
                    teamName = plugin.game.getMplayer(player).getTeam().getName();
                    points = plugin.game.getMplayer(player).getTeam().getPoints();
                }
                board.setAll(
                        "§7§m+-------------------+",
                        "§c>> Équipe: " + teamName,
                        "§c>> Points: §f" + points,
                        "§c>> Joueurs: §f" + Bukkit.getOnlinePlayers().size(),
                        "§c>> Temps restant : ",
                        "§f§7§m+-------------------+"
                );
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();
        if (player.getGameMode().equals(GameMode.SPECTATOR)) return;
        if (plugin.game.state.equals(State.Open)) {
            plugin.game.makePlayerLeave(player);
        }
        if (plugin.game.state.equals(State.Launched)) {
            plugin.game.broadcast(player.getName() + " n'a pas payé sa facture d'électricité !");
            plugin.game.decoPlayers.add(plugin.game.getMplayer(player));
        }
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (plugin.game.state.equals(State.Open)) {
            event.setCancelled(true);
        } else if (plugin.game.getMplayer((Player) event.getWhoClicked()).getClasse() != null && plugin.game.getMplayer((Player) event.getWhoClicked()).getClasse().equals(Classes.Mineur)) {
            if (event.getCurrentItem().getType().equals(Material.BARRIER)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (plugin.game.state.equals(State.Launched)) {
            for (ItemStack item : event.getEntity().getInventory().getContents()) {
                if (item != null) {
                    if (Objects.requireNonNull(item).getType() == Material.DIAMOND || Objects.requireNonNull(item).getType() == Material.IRON_INGOT || Objects.requireNonNull(item).getType() == Material.GOLD_INGOT || Objects.requireNonNull(item).getType() == Material.EMERALD) {
                        event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), item);
                    }
                }
            }
            Player player = event.getEntity();
            MPlayer mplayer = plugin.game.getMplayer(event.getEntity());
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
            player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
            player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
            player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
            player.getInventory().setItem(0, new ItemStack(Material.IRON_SWORD));
            player.getInventory().setItem(1, new ItemStack(Material.BOW));
            player.getInventory().setItem(2, new ItemStack(Material.ARROW, 32));
            mplayer.getTeam().spawn(mplayer);
        }
    }


    public boolean isNear(Location loc) {
        double distance = Math.sqrt(Math.pow(loc.getX() - DefaultLocation.centre.getX(), 2) + Math.pow(loc.getZ() - DefaultLocation.centre.getZ(), 2));
        return distance < 78;
    }

    public boolean isInABase(Location loc) {
        if (isNear(loc)) {
            if (-6 < loc.getX() && loc.getX() < -2) {
                if (loc.getY() < -36 && loc.getY() > -43) {
                    return true; //base verte
                } else if (loc.getY() < 56 && loc.getY() > 50) {
                    return true; //base bleue
                }
            } else if (loc.getY() < 10 && loc.getY() > 2) {
                if (loc.getX() < 43 && loc.getX() > 36) {
                    return true; //base jaune
                } else if (loc.getX() > -56 && loc.getX() < -50) {
                    return true; //base rouge
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null) return;

        if (item.getType() == Material.COMMAND && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase("§cConfig")) {
            ConfigInv.CONFIGINV.open(player);
        }

        if (item.getType() == Material.BANNER && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase("§cSélection des Teams")) {
            TeamPicker.TEAMPICKER.open(player);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!plugin.game.state.equals(State.Launched)) return;
        if (plugin.game.getMplayer(event.getPlayer()) == null) return;

        if (isInABase(event.getTo())) {
            Location loc = event.getTo();
            Location spawn = plugin.game.getMplayer(event.getPlayer()).getTeam().getSpawn();
            double distance = Math.sqrt(Math.pow(loc.getX() - spawn.getX(), 2) + Math.pow(loc.getZ() - spawn.getZ(), 2));
            if (distance > 10) {
                Vector vector = new Vector(event.getFrom().getBlockX() - event.getTo().getBlockX(), 0, event.getFrom().getBlockZ() - event.getTo().getBlockZ()).normalize().multiply(5);
                event.getPlayer().setVelocity(vector);
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){
        if (!plugin.game.state.equals(State.Launched)) return;
        if (plugin.game.getMplayer(event.getPlayer()) == null) return;

        event.setRespawnLocation(plugin.game.getMplayer(event.getPlayer()).getTeam().getSpawn());
        plugin.game.resetEffects(plugin.game.getMplayer(event.getPlayer()));
    }
}
