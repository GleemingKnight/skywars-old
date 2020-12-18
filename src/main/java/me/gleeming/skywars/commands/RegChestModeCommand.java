package me.gleeming.skywars.commands;

import me.gleeming.api.Command.BaseCommand;
import me.gleeming.api.Command.Command;
import me.gleeming.api.Command.CommandArgs;
import me.gleeming.skywars.chest.ChestManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RegChestModeCommand extends BaseCommand {
    @Command(name = "regchestmode", permission = "command.regchestmode")
    public void onCommand(CommandArgs commandArgs) {
        Player p = commandArgs.getPlayer();
        if(ChestManager.getInstance().getRegChestMode().contains(p)) {
            ChestManager.getInstance().getRegChestMode().remove(p);
            p.sendMessage(ChatColor.RED + "You are no longer in regular chest mode!");
        } else {
            ChestManager.getInstance().getRegChestMode().add(p);
            p.sendMessage(ChatColor.GREEN + "You are now in regular chest mode!");
        }
    }
}
