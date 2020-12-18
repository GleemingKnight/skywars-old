package me.gleeming.skywars.leaderboard;

import com.mongodb.client.FindIterable;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Leaderboard {
    @Getter private String name;
    @Getter private List<String> leaderboard = new ArrayList<>();
    @Getter private FindIterable<Document> collection;
    public Leaderboard(String name, FindIterable<Document> collection) {
        this.name = name;
        this.collection = collection;
        load();
        LeaderboardManager.getInstance().getLeaderboards().put(name, this);
        LeaderboardManager.getInstance().getLeaderboardList().add(this);
    }
    public void load() {
        List<LeaderboardEntry> entries = new ArrayList<>();
        List<String> leaderboardStringList = new ArrayList<>();
        Map<Integer, LeaderboardEntry> sortedMap = new HashMap<>();

        leaderboardStringList.add(ChatColor.translateAlternateColorCodes('&', "&8&m---------------------"));

        try {
            for(Document doc : collection) {
                String pName = doc.getString("NAME");
                int value = doc.getInteger(name);
                entries.add(new LeaderboardEntry(value, pName));
            }
        } catch(Exception e) {
            leaderboardStringList.add(ChatColor.translateAlternateColorCodes('&', "&cThere was an error loading this leaderboard.."));
        }

        for(LeaderboardEntry leaderboardEntry : entries) {
            String name = leaderboardEntry.getPlayer();
            int value = leaderboardEntry.getAmount();
            for(int i = 1; i < 11; i++) {
                if(sortedMap.get(i) == null) {
                    sortedMap.put(i, leaderboardEntry);
                    break;
                } else {
                    LeaderboardEntry entry = sortedMap.get(i);
                    if(leaderboardEntry.getAmount() > entry.getAmount()) {
                        sortedMap.remove(i);
                        sortedMap.put(i, leaderboardEntry);
                        break;
                    }
                }
            }
        }

        for(int i = 1; i < 11; i++) {
            try {
                LeaderboardEntry entry = sortedMap.get(i);
                leaderboardStringList.add(ChatColor.translateAlternateColorCodes('&', "&8[&c" + i + "&8] &7" + entry.getPlayer() + " &8- &f" + entry.getAmount()));
            } catch(Exception e) {

            }
        }

        leaderboardStringList.add(ChatColor.translateAlternateColorCodes('&', "&8&m---------------------"));
        leaderboard = leaderboardStringList;

    }
}
