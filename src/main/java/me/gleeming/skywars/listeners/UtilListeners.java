package me.gleeming.skywars.listeners;

import me.gleeming.api.Item.ItemBuilder;
import me.gleeming.skywars.database.MongoDB;
import me.gleeming.skywars.game.GameManager;
import me.gleeming.skywars.game.GameStatus;
import me.gleeming.skywars.Skywars;
import me.gleeming.skywars.team.TeamManager;
import net.minecraft.server.v1_7_R4.EnumClientCommand;
import net.minecraft.server.v1_7_R4.PacketPlayInClientCommand;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.UUID;

public class UtilListeners implements Listener {
    private HashMap<Player, Location> deathLocations = new HashMap<>();
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(Skywars.getInstance().getGameStatus().equals(GameStatus.WAITING)) {
            e.setJoinMessage("");
            TeamManager.getInstance().createTeam(p);
            e.getPlayer().teleport(Skywars.getInstance().getLocationsConfig().getLocation("LOBBY"));
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);
            p.setHealth(20);
            p.setFireTicks(0);
            p.setAllowFlight(false);
            p.setGameMode(GameMode.SURVIVAL);
            for(PotionEffect potionEffect : p.getActivePotionEffects()) {
                p.removePotionEffect(potionEffect.getType());
            }
            p.getInventory().setItem(0, new ItemBuilder(Material.BOW, 1, "&8\u00BB &dKit Selector &8\u00AB").setLore(ChatColor.translateAlternateColorCodes('&', "&8\u00BB &dClick &7to open the kit selector &8\u00AB")).toItemStack());
            p.getInventory().setItem(4, new ItemBuilder(Material.DIAMOND, 1, "&8\u00BB &dLeaderboards &8\u00AB").setLore(ChatColor.translateAlternateColorCodes('&', "&8\u00BB &dClick &7to view the leaderboards")).toItemStack());
            p.getInventory().setItem(8, new ItemBuilder(Material.BED, 1, "&8\u00BB &cBack to Lobby &8\u00AB").setLore(ChatColor.translateAlternateColorCodes('&', "&8\u00BB &dClick &7to be sent back to the lobby")).toItemStack());
        } else {
            GameManager.getInstance().addSpectating(e.getPlayer());
        }

        if(!MongoDB.getInstance().hasProfile(p.getUniqueId())) {
            MongoDB.getInstance().createProfile(p.getUniqueId());
        }
    }
    @EventHandler
    public void onSpawn(EntitySpawnEvent e) {
        e.setCancelled(true);
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        try {
            if(Skywars.getInstance().getGameStatus() == GameStatus.WAITING || Skywars.getInstance().getGameStatus() == GameStatus.STARTING) {
                if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("Leaderboard")) {
                    e.getPlayer().chat("/leaderboard");
                } else if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("Lobby")) {
                    e.getPlayer().kickPlayer("Back to Lobby");
                } else if(e.getPlayer().getItemInHand().getType().equals(Material.BOW)) {
                    e.getPlayer().chat("/kitselector");
                }
            }
            if(GameManager.getInstance().getPlayersSpectating().contains(e.getPlayer().getUniqueId())) {
                if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("Lobby")) {
                    e.getPlayer().kickPlayer("Back to Lobby");
                } else if(e.getPlayer().getItemInHand().getType().equals(Material.COMPASS)) {
                    Inventory inv = Bukkit.createInventory(null, 17, ChatColor.translateAlternateColorCodes('&', "&aAlive Players"));

                    int i = 0;
                    for(UUID uuid : GameManager.getInstance().getAlivePlayers()) {
                        Player p = Bukkit.getPlayer(uuid);
                        inv.setItem(i, new ItemBuilder(Material.SKULL, 1, "&a" + p.getName()).setLore(ChatColor.translateAlternateColorCodes('&', "&8\u00BB &dClick &7to teleport to this player &8\u00AB")).toItemStack());
                        i++;
                    }

                    e.getPlayer().openInventory(inv);
                }
            }
        } catch(Exception ex) {

        }
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if(Skywars.getInstance().getGameStatus() != GameStatus.STARTED && !p.getGameMode().equals(GameMode.CREATIVE) && !GameManager.getInstance().getPlayersSpectating().contains(p.getUniqueId())) {
            e.setCancelled(true);
        }
        if(GameManager.getInstance().getPlayersSpectating().contains(p.getUniqueId())) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if(Skywars.getInstance().getGameStatus() != GameStatus.STARTED && !p.getGameMode().equals(GameMode.CREATIVE) && !GameManager.getInstance().getPlayersSpectating().contains(p.getUniqueId())) {
            e.setCancelled(true);
        }
        if(GameManager.getInstance().getPlayersSpectating().contains(p.getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        if(Skywars.getInstance().getGameStatus() != GameStatus.WAITING && !e.getPlayer().hasPermission("rank.vip")) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&', "&7This game has already &dStarted&7! &7Buy a rank to spectate after the game has started!"));
        }
    }
    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if(Skywars.getInstance().getGameStatus() != GameStatus.STARTED && !p.getGameMode().equals(GameMode.CREATIVE) && !GameManager.getInstance().getPlayersSpectating().contains(p.getUniqueId())) {
            e.setCancelled(true);
        }
        if(GameManager.getInstance().getPlayersSpectating().contains(p.getUniqueId())) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if(e.getCause() == EntityDamageEvent.DamageCause.VOID) {
            if(e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                p.setHealth(0);
            }
        }
        if(!Skywars.getInstance().getGameStatus().equals(GameStatus.STARTED)) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onEnttity(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            if(GameManager.getInstance().getPlayersSpectating().contains(p.getUniqueId())) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if(Skywars.getInstance().getGameStatus().equals(GameStatus.WAITING)) {
            TeamManager.getInstance().getTeams().remove(e.getPlayer().getUniqueId());
        } else {
            if(GameManager.getInstance().getAlivePlayers().contains(e.getPlayer().getUniqueId())) {
                GameManager.getInstance().getAlivePlayers().remove(e.getPlayer().getUniqueId());
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&d" + e.getPlayer().getName() + " &7has quit and been disqualified."));

                GameManager.getInstance().executeDeath(e.getPlayer());
            }
        }
        e.setQuitMessage("");
    }
    @EventHandler
    public void onFood(FoodLevelChangeEvent e) {
        e.setFoodLevel(20);
    }
    @EventHandler
    public void onWeather(WeatherChangeEvent e) {
        if(e.toWeatherState()) {
            e.setCancelled(true);
            for(World world : Bukkit.getServer().getWorlds()) {
                world.setWeatherDuration(0);
            }
        } else {
            for(World world : Bukkit.getServer().getWorlds()) {
                world.setWeatherDuration(0);
            }
        }
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();

        e.setDeathMessage("");
        GameManager.getInstance().executeDeath(p);
        deathLocations.put(p, p.getLocation());
        try {
            int i = GameManager.getInstance().getKills().get(e.getEntity().getKiller().getUniqueId());
            GameManager.getInstance().getKills().remove(e.getEntity().getKiller().getUniqueId());
            GameManager.getInstance().getKills().put(e.getEntity().getKiller().getUniqueId(), i + 1);
            MongoDB.getInstance().addKill(e.getEntity().getKiller().getUniqueId());
            MongoDB.getInstance().addDeath(e.getEntity().getUniqueId());

            e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&d" + e.getEntity().getName() + " &7was slain by &d" + e.getEntity().getKiller().getName()));
        } catch(Exception ex) {
            e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&d" + e.getEntity().getName() + " &7has died."));
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Skywars.getInstance(), (Runnable) new Runnable() {
            @Override
            public void run() {
                p.setCanPickupItems(false);
                final PacketPlayInClientCommand packet = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);
                ((CraftPlayer) p).getHandle().playerConnection.a(packet);
            }
        }, (long) 1);
    }
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        e.getPlayer().setCanPickupItems(true);
        e.getPlayer().teleport(deathLocations.get(e.getPlayer()));
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        try {
            Player p = (Player) e.getWhoClicked();
            if(Skywars.getInstance().getGameStatus() != GameStatus.STARTED && !p.getGameMode().equals(GameMode.CREATIVE) && !GameManager.getInstance().getPlayersSpectating().contains(p.getUniqueId())) {
                e.setCancelled(true);
            }
            if(GameManager.getInstance().getPlayersSpectating().contains(p.getUniqueId())) {
                e.setCancelled(true);
                for(Player player : Bukkit.getOnlinePlayers()) {
                    if(e.getCurrentItem().getItemMeta().getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', "&a" + player.getName()))) {
                        p.teleport(player.getLocation());
                        p.sendMessage(ChatColor.GREEN + "You have been teleported to " + player.getName() + "!");
                    }
                }
            }
        } catch(Exception ex) {

        }
    }
}
