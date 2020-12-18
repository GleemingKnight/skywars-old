package me.gleeming.skywars.area;

import lombok.Getter;
import me.gleeming.skywars.game.GameManager;
import me.gleeming.skywars.Skywars;
import net.minecraft.util.org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import java.io.File;
import java.util.List;
import java.util.Random;

public class AreaManager {
    @Getter private static AreaManager instance;
    public AreaManager() {
        instance = this;
        load();
    }
    public String getRandomMap() {
        Random rand = new Random();
        List<String> maps = Skywars.getInstance().getLocationsConfig().getConfig().getStringList("MAPS");

        int i = rand.nextInt(maps.size()) + 0;
        return maps.get(i);
    }
    public void load() {
        if(Skywars.getInstance().getLocationsConfig().getConfig().getBoolean("RESET")) {
            try {
                String map = getRandomMap();
                GameManager.getInstance().setMap(map.toUpperCase());
                GameManager.getInstance().setDisplayMap(map);
                map = map.toUpperCase();
                Bukkit.unloadWorld("GAMEWORLD", false);
                FileUtils.deleteDirectory(new File("GAMEWORLD"));
                FileUtils.copyDirectory(new File("MAPS/" + map), new File("GAMEWORLD"));
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv import GAMEWORLD normal");
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            Bukkit.createWorld(new WorldCreator("GAMEWORLD").environment(World.Environment.NORMAL).type(WorldType.FLAT));
        }
    }
}
