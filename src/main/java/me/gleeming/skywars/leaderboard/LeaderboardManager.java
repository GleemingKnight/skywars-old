package me.gleeming.skywars.leaderboard;

import lombok.Getter;
import me.gleeming.api.Item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LeaderboardManager {
    @Getter private static LeaderboardManager instance;
    @Getter private HashMap<String, Leaderboard> leaderboards = new HashMap<>();
    @Getter private List<Leaderboard> leaderboardList = new ArrayList<>();
    public LeaderboardManager() {
        instance = this;
    }
    public void openGUI(Player p) {
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', "&c&lLeaderboards"));

        inv.setItem(3, new ItemBuilder(Material.DIAMOND_SWORD, 1, "&bTotal Kills").setLore(leaderboards.get("KILLS").getLeaderboard()).toItemStack());
        inv.setItem(5, new ItemBuilder(Material.DIAMOND, 1, "&bTotal Wins").setLore(leaderboards.get("WINS").getLeaderboard()).toItemStack());

        p.openInventory(inv);
    }
}
