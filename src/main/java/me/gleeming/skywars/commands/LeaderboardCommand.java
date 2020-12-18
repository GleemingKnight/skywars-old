package me.gleeming.skywars.commands;

import me.gleeming.api.Command.BaseCommand;
import me.gleeming.api.Command.Command;
import me.gleeming.api.Command.CommandArgs;
import me.gleeming.skywars.leaderboard.LeaderboardManager;
import org.bukkit.entity.Player;

public class LeaderboardCommand extends BaseCommand {
    @Command(name = "leaderboard")

    public void onCommand(CommandArgs commandArgs) {
        Player p = commandArgs.getPlayer();
        LeaderboardManager.getInstance().openGUI(p);
    }
}
