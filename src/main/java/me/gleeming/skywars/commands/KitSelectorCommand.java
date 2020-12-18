package me.gleeming.skywars.commands;

import me.gleeming.api.Command.BaseCommand;
import me.gleeming.api.Command.Command;
import me.gleeming.api.Command.CommandArgs;
import me.gleeming.skywars.game.GameStatus;
import me.gleeming.skywars.kit.KitManager;
import me.gleeming.skywars.Skywars;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KitSelectorCommand extends BaseCommand {
    @Command(name = "kitselector", permission = "command.kitselector")
    public void onCommand(CommandArgs commandArgs) {
        Player p = commandArgs.getPlayer();
        if(Skywars.getInstance().getGameStatus() == GameStatus.STARTING || Skywars.getInstance().getGameStatus() == GameStatus.WAITING) {
            KitManager.getInstance().openGUI(p);
        } else {
            p.sendMessage(ChatColor.RED + "You cannot use this command right now!");
        }
    }
}
