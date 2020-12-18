package me.gleeming.skywars.leaderboard;

import lombok.Getter;
import org.bukkit.ChatColor;

public class LeaderboardEntry {
    @Getter private int amount;
    @Getter
    private String player;
    public LeaderboardEntry(int amount, String player) {
        this.amount = amount;
        this.player = player;
    }
    public String getMessage() {
        return ChatColor.translateAlternateColorCodes('&', "&a" + player + " &8- &f" + amount);
    }
}
