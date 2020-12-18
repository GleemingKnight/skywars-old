package me.gleeming.skywars.commands;

import me.gleeming.api.Command.BaseCommand;
import me.gleeming.api.Command.Command;
import me.gleeming.api.Command.CommandArgs;
import me.gleeming.skywars.database.MongoDB;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class GiveKitCommand extends BaseCommand {
    @Command(name = "givekit", permission = "command.givekit")
    public void onCommand(CommandArgs commandArgs) {
        Player p = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();
        if(args.length != 2) {
            p.sendMessage(ChatColor.RED + "Usage: /givekit <Player> <Kit>");
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            if(offlinePlayer == null) {
                p.sendMessage(ChatColor.RED + "Invalid Player");
            } else {
                MongoDB.getInstance().addKit(offlinePlayer.getUniqueId(), args[1].toUpperCase());
                p.sendMessage(ChatColor.GREEN + "Added kit");
            }
        }
    }
}
