package me.gleeming.skywars.chest;

import lombok.Getter;
import me.gleeming.skywars.game.GameManager;
import me.gleeming.skywars.Skywars;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ChestManager {
    @Getter private static ChestManager instance;
    @Getter private List<ItemStack[]> regItems = new ArrayList<>();
    @Getter private List<ItemStack[]> opItems = new ArrayList<>();
    @Getter private List<Chest> regChests = new ArrayList<>();
    @Getter private List<Chest> opChests = new ArrayList<>();
    @Getter private List<Player> regChestMode = new ArrayList<>();
    @Getter private List<Player> opChestMode = new ArrayList<>();
    public ChestManager() {
        instance = this;
        try {
            reloadItems();
        } catch(Exception e) {

        }
    }
    public void reloadItems() {
        try {
            regItems.clear();
            for(String s : Skywars.getInstance().getNormalChestConfig().getConfig().getKeys(false)) {
                if(!s.contains(".")) {
                    regItems.add(Skywars.getInstance().getNormalChestConfig().getItemArray(s));
                }
            }

            opItems.clear();
            for(String s : Skywars.getInstance().getOPChestConfig().getConfig().getKeys(false)) {
                if(!s.contains(".")) {
                    opItems.add(Skywars.getInstance().getOPChestConfig().getItemArray(s));
                }
            }
        } catch(Exception e) {

        }

        regChests.clear();
        opChests.clear();
        for(String s : Skywars.getInstance().getLocationsConfig().getConfig().getConfigurationSection(GameManager.getInstance().getMap() + ".CHESTS.REG").getKeys(true)) {
            try {
                if(!s.contains(".")) {
                    Location loc = Skywars.getInstance().getLocationsConfig().getLocation(GameManager.getInstance().getMap() + ".CHESTS.REG." + s);
                    Block block = loc.getWorld().getBlockAt(loc);
                    if (block.getState() instanceof Chest) {
                        Chest chest = (Chest) block.getState();
                        regChests.add(chest);
                    }
                }
            } catch(Exception e) {

            }
        }

        for(String s : Skywars.getInstance().getLocationsConfig().getConfig().getConfigurationSection(GameManager.getInstance().getMap() + ".CHESTS.OP").getKeys(true)) {
            try {
                if(!s.contains(".")) {
                    Location loc = Skywars.getInstance().getLocationsConfig().getLocation(GameManager.getInstance().getMap() + ".CHESTS.OP." + s);
                    Block block = loc.getWorld().getBlockAt(loc);
                    if (block.getState() instanceof Chest) {
                        Chest chest = (Chest) block.getState();
                        opChests.add(chest);
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void load() {
        for(Chest chest : regChests) {
            fillChest(chest, false);
        }
        for(Chest chest : opChests) {
            fillChest(chest, true);
        }
    }

    public void fillChest(Chest chest, boolean op) {
        Random rand = new Random();
        if(!op) {
            int invNumber = rand.nextInt(regItems.size()) + 0;
            ItemStack[] items = regItems.get(invNumber);
            for(ItemStack itemStack : items) {
                if(itemStack != null) {
                    chest.getInventory().setItem(rand.nextInt(26) + 0, itemStack);
                }
            }
        } else {
            int invNumber = rand.nextInt(opItems.size()) + 0;
            ItemStack[] items = opItems.get(invNumber);
            for(ItemStack itemStack : items) {
                if(itemStack != null) {
                    chest.getInventory().setItem(rand.nextInt(26) + 0, itemStack);
                }
            }
        }
    }
    public void addInv(Inventory inv, boolean op) {
        if(!op) {
            Skywars.getInstance().getNormalChestConfig().getConfig().set(UUID.randomUUID().toString(), inv.getContents());
            Skywars.getInstance().getNormalChestConfig().save();
            reloadItems();
        } else {
            Skywars.getInstance().getOPChestConfig().getConfig().set(UUID.randomUUID().toString(), inv.getContents());
            Skywars.getInstance().getOPChestConfig().save();
            reloadItems();
        }
    }
}
