package me.gleeming.skywars.listeners;

import me.gleeming.skywars.Skywars;
import me.gleeming.skywars.chest.ChestManager;
import me.gleeming.skywars.game.GameManager;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.UUID;

public class ChestListeners implements Listener {
    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if(ChestManager.getInstance().getRegChestMode().contains(e.getPlayer())) {
            e.setCancelled(true);
            Skywars.getInstance().getLocationsConfig().setLocation(e.getBlock().getLocation(), GameManager.getInstance().getMap() + ".CHESTS.REG." + UUID.randomUUID().toString());
            e.getPlayer().sendMessage(ChatColor.GREEN + "Added a Tier 1 Chest");
        }
        if(ChestManager.getInstance().getOpChestMode().contains(e.getPlayer())) {
            e.setCancelled(true);
            Skywars.getInstance().getLocationsConfig().setLocation(e.getBlock().getLocation(), GameManager.getInstance().getMap() + ".CHESTS.OP." + UUID.randomUUID().toString());
            e.getPlayer().sendMessage(ChatColor.GREEN + "Added a Tier 2 Chest");
        }
    }
}
