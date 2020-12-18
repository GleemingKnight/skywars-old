package me.gleeming.skywars.kit;

import lombok.Getter;
import me.gleeming.api.Item.ItemBuilder;
import me.gleeming.skywars.database.MongoDB;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KitManager {
    @Getter private static KitManager instance;
    @Getter private List<Kit> kits = new ArrayList<>();
    public KitManager() {
        instance = this;
        load();
    }
    public void load() {
        kits.add(new Kit("NONE", new ItemStack[] {

        }, "&cNone", Material.STAINED_GLASS_PANE));
        kits.add(new Kit("BOWER", new ItemStack[] {
                new ItemBuilder(Material.BOW, 1, "&dBower's Bow").addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 2).toItemStack(),
                new ItemBuilder(Material.ARROW, 16, "&dBower's Arrows").toItemStack()
        }, "&dBower", Material.BOW));
    }
    public void openGUI(Player p) {
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', "&cKit Selector"));

        int i = 0;
        for(Kit kit : kits) {
            inv.setItem(i, kit.getItem(p));
            i++;
        }

        p.openInventory(inv);
    }
    public void giveKit(Player p) {
        Kit kit = getByName(MongoDB.getInstance().getKit(p.getUniqueId()));
        if(kit == null || kit.getName().equals("NONE")) {
            p.sendMessage(ChatColor.RED + "You currently do not have a kit selected so you were not given any items.");
        } else {
            for (ItemStack item : kit.getItems()) {
                p.getInventory().addItem(item);
            }
        }
    }
    public Kit getByName(String name) {
        for(Kit kit : kits) {
            if(kit.getName().equalsIgnoreCase(name)) {
                return kit;
            }
        }
        return null;
    }

}
