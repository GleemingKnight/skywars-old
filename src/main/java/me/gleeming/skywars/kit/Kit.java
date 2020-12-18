package me.gleeming.skywars.kit;

import lombok.Getter;
import me.gleeming.api.Item.ItemBuilder;
import me.gleeming.skywars.database.MongoDB;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Kit {
    @Getter private String name;
    @Getter private String displayName;
    @Getter private ItemStack[] items;
    @Getter private Material display;
    public Kit(String name, ItemStack[] items, String displayName, Material display) {
        this.name = name;
        this.displayName = displayName;
        this.items = items;
        this.display = display;
    }
    public ItemStack getItem(Player p) {
        ItemBuilder itemBuilder = new ItemBuilder(display, 1, displayName);

        if(!MongoDB.getInstance().hasKit(p.getUniqueId(), name)) {
            itemBuilder.setLore(ChatColor.translateAlternateColorCodes('&', "&8\u00BB &cYou do not have access to this kit &8\u00AB"));
        } else {
            itemBuilder.setLore(ChatColor.translateAlternateColorCodes('&', "&8\u00BB &aClick to select this kit &8\u00AB"));
        }

        return itemBuilder.toItemStack();
    }
}
