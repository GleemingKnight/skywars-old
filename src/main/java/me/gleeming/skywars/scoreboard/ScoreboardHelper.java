package me.gleeming.skywars.scoreboard;

import com.bizarrealex.aether.scoreboard.Board;
import com.bizarrealex.aether.scoreboard.BoardAdapter;
import com.bizarrealex.aether.scoreboard.cooldown.BoardCooldown;
import me.gleeming.api.Time.Time;
import me.gleeming.skywars.database.MongoDB;
import me.gleeming.skywars.game.GameManager;
import me.gleeming.skywars.game.GameStatus;
import me.gleeming.skywars.kit.KitManager;
import me.gleeming.skywars.Skywars;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ScoreboardHelper implements BoardAdapter {
    public String getTitle(Player p) {
        return ChatColor.translateAlternateColorCodes('&', "&5&lZany &7(Skywars)");
    }

    @Override
    public List<String> getScoreboard(Player player, Board board, Set<BoardCooldown> cooldowns) {
        List<String> list = new ArrayList<>();
        list.add("&8&m-------------------");
        if(Skywars.getInstance().getGameStatus().equals(GameStatus.WAITING) || Skywars.getInstance().getGameStatus().equals(GameStatus.STARTING)) {
            list.add("&dPlayers: &f" + Bukkit.getOnlinePlayers().size() + "&8/&712");
            list.add(" ");
            list.add("&dMap: &f" + GameManager.getInstance().getDisplayMap());
            list.add(" ");
            try {
                list.add("&dKit: &f" + KitManager.getInstance().getByName(MongoDB.getInstance().getKit(player.getUniqueId())).getDisplayName());
            } catch(Exception e) {
                list.add("&dKit: &cNone");
            }
            list.add(" ");
            if(Skywars.getInstance().getGameStatus() == GameStatus.WAITING) {
                list.add("&dwww.Zany.rip");
            } else {
                list.add("&7Starting in &d" + GameManager.getInstance().getStartingCountdown() + " &7seconds");
            }
        } else if(Skywars.getInstance().getGameStatus().equals(GameStatus.STARTED)) {
            list.add("&dGame Time: &f" + Time.getFormat(GameManager.getInstance().getGameTimeInSeconds()));
            list.add(" ");
            list.add("&dPlayers Alive: &f" + GameManager.getInstance().getAlivePlayers().size());
            list.add(" ");
            list.add("&dKills: &f" + GameManager.getInstance().getKills().get(player.getUniqueId()));
            list.add("");
            list.add("&dwww.Zany.rip");
        } else if(Skywars.getInstance().getGameStatus().equals(GameStatus.ENDED)) {
            list.add("&dWinners: ");
            for(Player p : GameManager.getInstance().getWinners()) {
                list.add("&8- &7" + p.getName());
            }
            list.add(" ");
            list.add("&7Restarting in &d" + GameManager.getInstance().getRestartTime());
        }
        list.add("&8&m-------------------");
        return list;
    }
}
