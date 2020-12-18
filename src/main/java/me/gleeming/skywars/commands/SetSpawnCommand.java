package me.gleeming.skywars.commands;

import me.gleeming.api.Command.BaseCommand;
import me.gleeming.api.Command.Command;
import me.gleeming.api.Command.CommandArgs;
import me.gleeming.skywars.game.GameManager;
import me.gleeming.skywars.Skywars;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SetSpawnCommand extends BaseCommand {
    @Command(name = "setspawn", permission = "command.setspawn")
    public void onCommand(CommandArgs commandArgs) {
        Player p = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();
        if(args.length != 1) {
            p.sendMessage(ChatColor.RED + "Usage: /setspawn <Lobby|1-12>");
        } else {
            if(args[0].equalsIgnoreCase("lobby")) {
                Skywars.getInstance().getLocationsConfig().setLocation(p.getLocation(), "LOBBY");
            } else {
                int spawn;
                try {
                    spawn = Integer.parseInt(args[0]);
                } catch(Exception e) {
                    p.chat("/setspawn");
                    return;
                }

                Skywars.getInstance().getLocationsConfig().setLocation(p.getLocation(), GameManager.getInstance().getMap() + "." + spawn);
            }
        }
    }
}
