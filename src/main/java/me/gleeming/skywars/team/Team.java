package me.gleeming.skywars.team;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Team {
    @Getter private List<Player> teamPlayers;
    @Getter private int teamNumber;
    public Team(Player p) {
        this.teamPlayers = new ArrayList<>();
        teamPlayers.add(p);
        teamNumber = TeamManager.getInstance().getTeams().values().size() + 1;
    }
}
