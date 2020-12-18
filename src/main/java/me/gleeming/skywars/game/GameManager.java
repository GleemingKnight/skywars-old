package me.gleeming.skywars.game;

import lombok.Getter;
import lombok.Setter;
import me.gleeming.api.Item.ItemBuilder;
import me.gleeming.skywars.cage.CageManager;
import me.gleeming.skywars.chest.ChestManager;
import me.gleeming.skywars.database.MongoDB;
import me.gleeming.skywars.kit.KitManager;
import me.gleeming.skywars.Skywars;
import me.gleeming.skywars.Skywars;
import me.gleeming.skywars.team.Team;
import me.gleeming.skywars.team.TeamManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GameManager {
    @Getter private static GameManager instance;
    @Getter @Setter private String map = "";
    @Getter @Setter private String displayMap = "";
    @Getter private int startingCountdown = 15;
    @Getter private List<UUID> alivePlayers = new ArrayList<>();
    @Getter private List<UUID> playersSpectating = new ArrayList<>();
    @Getter private HashMap<UUID, Integer> kills = new HashMap<>();
    @Getter private int restartTime = 21;
    @Getter private List<Player> winners = new ArrayList<>();
    @Getter private int gameTimeInSeconds;
    public GameManager() {
        instance = this;
    }
    public void startCountdown() {
        Skywars.getInstance().setGameStatus(GameStatus.STARTING);
        Skywars.getInstance().getHubAPI().setStatus(2);
        List<Integer> spawns = new ArrayList<>();
        for(int i = 1; i <= 12; i++) {
            spawns.add(i);
        }
        for(Team team : TeamManager.getInstance().getTeams().values()) {
            int i = new Random().nextInt(spawns.size() - 1) + 0;
            spawns.remove(i);
            for(Player p : team.getTeamPlayers()) {
                p.teleport(Skywars.getInstance().getLocationsConfig().getLocation(map.toUpperCase() + "." + i));
                CageManager.getInstance().load(p);
            }
        }
        new BukkitRunnable() {
            public void run() {
                startingCountdown--;
                if(startingCountdown == 10 || startingCountdown <= 5) {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&5&lZany &8\u00BB &7The game will start in &d " + startingCountdown + " second(s)"));
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                    }
                }
                if(startingCountdown == 0) {
                    startGame();
                    cancel();
                }
            }
        }.runTaskTimer(Skywars.getInstance(), 20, 20);
    }
    public void executeDeath(Player p) {
        alivePlayers.remove(p.getUniqueId());
        addSpectating(p);
        if(alivePlayers.size() == 1) {
            Player player = Bukkit.getPlayer(alivePlayers.get(0));
            winGame(Arrays.asList(player));
        }
    }
    public void winGame(List<Player> p) {
        Skywars.getInstance().setGameStatus(GameStatus.ENDED);
        Skywars.getInstance().getHubAPI().setStatus(4);
        winners = p;
        StringBuilder s = new StringBuilder();
        Iterator<Player> playerIterator = p.iterator();
        while(playerIterator.hasNext()) {
            Player player = playerIterator.next();
            if(playerIterator.hasNext()) {
                s.append(player.getName() + ", ");
            } else {
                s.append(player.getName());
            }
        }
        for(Player all : p) {
            MongoDB.getInstance().addWin(all.getUniqueId());
        }
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&5&lZany &8\u00BB &7Congratulations to &d" + s.toString() + " &7for winning the game!"));

        new BukkitRunnable() {
            public void run() {
                restartTime--;
                if(restartTime == 15 || restartTime == 10 || restartTime == 5) {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&5&lZany &8\u00BB &7The server is restarting in &d" + restartTime + " second(s)"));
                }
                if(restartTime == 5) {
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        p.kickPlayer("to lobby");
                    }
                }
                if(restartTime == 0) {
                    Bukkit.shutdown();
                }
            }
        }.runTaskTimer(Skywars.getInstance(), 0, 20);
    }
    public void addSpectating(Player p) {
        playersSpectating.add(p.getUniqueId());
        for(Player all : Bukkit.getOnlinePlayers()) {
            all.hidePlayer(p);
        }
        p.setCanPickupItems(false);
        p.spigot().setCollidesWithEntities(false);
        p.sendMessage(ChatColor.GREEN + "You are now in spectating mode!");
        p.getInventory().clear();
        p.setHealth(20);
        p.setFoodLevel(20);
        p.setFireTicks(0);
        p.updateInventory();
        p.setAllowFlight(true);
        new BukkitRunnable() {
            public void run() {
                p.getInventory().setItem(0, new ItemBuilder(Material.COMPASS, 1, "&bView Players Left").toItemStack());
                p.getInventory().setItem(8, new ItemBuilder(Material.BED, 1, "&cBack to Lobby").toItemStack());
                cancel();
            }
        }.runTaskTimer(Skywars.getInstance(), 30, 0);
    }
    public void startGame() {
        Skywars.getInstance().getHubAPI().setStatus(3);
        ChestManager.getInstance().load();
        Skywars.getInstance().setGameStatus(GameStatus.STARTED);
        CageManager.getInstance().removeCages();

        for(Player p : Bukkit.getOnlinePlayers()) {
            p.getInventory().clear();
            alivePlayers.add(p.getUniqueId());
            kills.put(p.getUniqueId(), 0);
            p.closeInventory();
            KitManager.getInstance().giveKit(p);
        }

        new BukkitRunnable() {
            public void run() {
                gameTimeInSeconds++;
            }
        }.runTaskTimer(Skywars.getInstance(), 0, 20);
    }
}
