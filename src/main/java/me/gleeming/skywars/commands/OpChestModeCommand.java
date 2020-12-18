package me.gleeming.skywars.commands;

import me.gleeming.api.Command.BaseCommand;
import me.gleeming.api.Command.Command;
import me.gleeming.api.Command.CommandArgs;
import me.gleeming.skywars.chest.ChestManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class OpChestModeCommand extends BaseCommand {
    @Command(name = "opchestmode", permission = "command.opchestmode")
    public void onCommand(CommandArgs commandArgs) {
        Player p = commandArgs.getPlayer();
        if(ChestManager.getInstance().getOpChestMode().contains(p)) {
            ChestManager.getInstance().getOpChestMode().remove(p);
            p.sendMessage(ChatColor.RED + "You are no longer in OP chest mode!");
        } else {
            ChestManager.getInstance().getOpChestMode().add(p);
            p.sendMessage(ChatColor.GREEN + "You are now in OP chest mode!");
        }
    }
}
