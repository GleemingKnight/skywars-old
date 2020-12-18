package me.gleeming.skywars.cage;

import lombok.Getter;
import me.gleeming.skywars.database.MongoDB;
import me.gleeming.skywars.game.GameManager;
import me.gleeming.skywars.Skywars;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CageManager {
    @Getter
    private static CageManager instance;

    public CageManager() {
        instance = this;
    }

    public void load(Player p) {
        Location loc = p.getLocation();
        p.teleport(new Location(loc.getWorld(), loc.getBlockX() + 0.5, loc.getBlockY(), loc.getBlockZ() + 0.5));
        Material material = getCageMaterial(p);

        loc.getWorld().getBlockAt(loc.subtract(0, 1, 0)).setType(material);
        loc.add(0, 1, 0);

        loc.getWorld().getBlockAt(loc.add(0, 2, 0)).setType(material);
        loc.subtract(0, 2, 0);

        loc.getWorld().getBlockAt(loc.add(1, 0, 0)).setType(material);
        loc.subtract(1, 0, 0);

        loc.getWorld().getBlockAt(loc.add(0, 0, 1)).setType(material);
        loc.subtract(0, 0, 1);

        loc.getWorld().getBlockAt(loc.add(1, 1, 0)).setType(material);
        loc.subtract(1, 1, 0);

        loc.getWorld().getBlockAt(loc.add(0, 1, 1)).setType(material);
        loc.subtract(0, 1, 1);

        loc.getWorld().getBlockAt(loc.subtract(1, 0, 0)).setType(material);
        loc.add(1, 0, 0);

        loc.getWorld().getBlockAt(loc.subtract(0, 0, 1)).setType(material);
        loc.add(0, 0, 1);

        loc.getWorld().getBlockAt(loc.subtract(1, 0, 0).add(0, 1, 0)).setType(material);
        loc.add(1, 0, 0);
        loc.subtract(0, 1, 0);

        loc.getWorld().getBlockAt(loc.subtract(0, 0, 1).add(0, 1, 0)).setType(material);
        loc.add(0, 0, 1);
        loc.subtract(0, 1, 0);
    }

    public void removeCages() {
        for (int i = 1; i < 13; i++) {
            Location loc = Skywars.getInstance().getLocationsConfig().getLocation(GameManager.getInstance().getMap().toUpperCase() + "." + i);
            loc.getWorld().getBlockAt(loc.subtract(0, 1, 0)).setType(Material.AIR);
            loc.add(0, 1, 0);

            loc.getWorld().getBlockAt(loc.add(0, 2, 0)).setType(Material.AIR);
            loc.subtract(0, 2, 0);

            loc.getWorld().getBlockAt(loc.add(1, 0, 0)).setType(Material.AIR);
            loc.subtract(1, 0, 0);

            loc.getWorld().getBlockAt(loc.add(0, 0, 1)).setType(Material.AIR);
            loc.subtract(0, 0, 1);

            loc.getWorld().getBlockAt(loc.add(1, 1, 0)).setType(Material.AIR);
            loc.subtract(1, 1, 0);

            loc.getWorld().getBlockAt(loc.add(0, 1, 1)).setType(Material.AIR);
            loc.subtract(0, 1, 1);

            loc.getWorld().getBlockAt(loc.subtract(1, 0, 0)).setType(Material.AIR);
            loc.add(1, 0, 0);

            loc.getWorld().getBlockAt(loc.subtract(0, 0, 1)).setType(Material.AIR);
            loc.add(0, 0, 1);

            loc.getWorld().getBlockAt(loc.subtract(1, 0, 0).add(0, 1, 0)).setType(Material.AIR);
            loc.add(1, 0, 0);
            loc.subtract(0, 1, 0);

            loc.getWorld().getBlockAt(loc.subtract(0, 0, 1).add(0, 1, 0)).setType(Material.AIR);
            loc.add(0, 0, 1);
            loc.subtract(0, 1, 0);
        }
    }
    public Material getCageMaterial(Player p) {
        String cage = MongoDB.getInstance().getCage(p.getUniqueId());
        if(cage.equals("DEFAULT")) {
            return Material.GLASS;
        } else if(cage.equals("CLOUD")) {
            return Material.STAINED_GLASS;
        }

        return Material.GLASS;
    }
}
