package me.gleeming.skywars;

import com.bizarrealex.aether.Aether;
import lombok.Getter;
import lombok.Setter;
import me.gleeming.api.Configuration.Config;
import me.gleeming.skywars.area.AreaManager;
import me.gleeming.skywars.cage.CageManager;
import me.gleeming.skywars.chest.ChestManager;
import me.gleeming.skywars.commands.*;
import me.gleeming.skywars.database.MongoDB;
import me.gleeming.skywars.game.GameManager;
import me.gleeming.skywars.game.GameStatus;
import me.gleeming.skywars.game.GameType;
import me.gleeming.skywars.kit.KitManager;
import me.gleeming.skywars.leaderboard.LeaderboardManager;
import me.gleeming.skywars.listeners.ChestListeners;
import me.gleeming.skywars.listeners.KitListeners;
import me.gleeming.skywars.listeners.UtilListeners;
import me.gleeming.skywars.scoreboard.ScoreboardHelper;
import me.gleeming.skywars.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Skywars extends JavaPlugin {
    @Getter private static Skywars instance;

    @Getter private Config databaseConfig;
    @Getter private Config locationsConfig;
    @Getter private Config normalChestConfig;
    @Getter private Config oPChestConfig;
    @Getter private GameType gameType;

    @Getter @Setter private GameStatus gameStatus;
    public void onEnable() {
        instance = this;

        gameStatus = GameStatus.WAITING;
        gameType = GameType.SOLO;

        // Configurations
        databaseConfig = new Config("database.yml", Skywars.getInstance());
        locationsConfig = new Config("locations.yml", Skywars.getInstance());
        normalChestConfig = new Config("normalchests.yml", Skywars.getInstance());
        oPChestConfig = new Config("opchests.yml", Skywars.getInstance());

        // Other Instantiations
        new Aether(Skywars.getInstance(), new ScoreboardHelper());
        new MongoDB();

        // Commands
        new SetSpawnCommand();
        new StartGameCommand();
        new LeaderboardCommand();
        new AddChestItemCommand();
        new GiveKitCommand();
        new KitSelectorCommand();
        new RegChestModeCommand();
        new OpChestModeCommand();

        // Managers
        new GameManager();
        new AreaManager();
        new ChestManager();
        new TeamManager();
        new CageManager();
        new LeaderboardManager();
        new KitManager();

        // Listeners
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new UtilListeners(), Skywars.getInstance());
        manager.registerEvents(new KitListeners(), Skywars.getInstance());
        manager.registerEvents(new ChestListeners(), Skywars.getInstance());
    }
}
