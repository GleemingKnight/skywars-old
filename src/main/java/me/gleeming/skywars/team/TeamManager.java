package me.gleeming.skywars.team;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TeamManager {
    @Getter private static TeamManager instance;
    @Getter private HashMap<UUID, Team> teams = new HashMap<>();
    public TeamManager() {
        instance = this;
    }
    public void createTeam(Player p) {
        teams.put(p.getUniqueId(), new Team(p));
    }
}
