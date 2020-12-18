package me.gleeming.skywars.commands;

import me.gleeming.api.Command.BaseCommand;
import me.gleeming.api.Command.Command;
import me.gleeming.api.Command.CommandArgs;
import me.gleeming.skywars.game.GameManager;
import me.gleeming.skywars.game.GameStatus;
import me.gleeming.skywars.Skywars;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class StartGameCommand extends BaseCommand {
    @Command(name = "startgame", permission = "command.forcestart", aliases = {"fs", "forcestart"})
    public void onCommand(CommandArgs commandArgs) {
        Player p = commandArgs.getPlayer();
        if(Skywars.getInstance().getGameStatus().equals(GameStatus.WAITING)) {
            GameManager.getInstance().startCountdown();
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&5&lZany &8\u00BB &d" + p.getName() + " &7has force started the game!"));
        } else {
            p.sendMessage(ChatColor.RED + "You cannot use this command right now.");
        }
    }
}
