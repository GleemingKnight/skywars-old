package me.gleeming.skywars.commands;

import me.gleeming.api.Command.BaseCommand;
import me.gleeming.api.Command.Command;
import me.gleeming.api.Command.CommandArgs;
import me.gleeming.skywars.chest.ChestManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AddChestItemCommand extends BaseCommand {
    @Command(name = "addchestinv", permission = "command.addchestinv")
    public void onCommand(CommandArgs commandArgs) {
        Player p = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();
        if(args[0].equalsIgnoreCase("OP")) {
            ChestManager.getInstance().addInv(p.getInventory(), true);
            ChestManager.getInstance().reloadItems();
            p.sendMessage(ChatColor.GREEN + "Successfully added inventory to the OP list.");
        } else {
            ChestManager.getInstance().addInv(p.getInventory(), true);
            ChestManager.getInstance().reloadItems();
            p.sendMessage(ChatColor.GREEN + "Successfully added inventory to the normal list.");
        }
    }
}
