package me.gleeming.skywars.listeners;

import me.gleeming.skywars.database.MongoDB;
import me.gleeming.skywars.kit.Kit;
import me.gleeming.skywars.kit.KitManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class KitListeners implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        try {
            for(Kit kit : KitManager.getInstance().getKits()) {
                if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', kit.getDisplayName()))) {
                    Player p = (Player) e.getWhoClicked();
                    p.closeInventory();
                    if(!MongoDB.getInstance().hasKit(p.getUniqueId(), kit.getName())) {
                        p.sendMessage(ChatColor.RED + "You do not have access to this kit!");
                    } else {
                        MongoDB.getInstance().setKit(p.getUniqueId(), kit.getName());
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have selected the kit named " + kit.getDisplayName() + "&7!"));
                    }
                }
            }
        } catch(Exception ex) {

        }
    }
}
