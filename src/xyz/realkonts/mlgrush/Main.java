package xyz.realkonts.mlgrush;

import fr.mrmicky.fastboard.FastBoard;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.realkonts.mlgrush.arena.game.ArenaManager;
import xyz.realkonts.mlgrush.commands.CMDMLGRush;
import xyz.realkonts.mlgrush.config.Config;
import xyz.realkonts.mlgrush.config.ConfigUtil;
import xyz.realkonts.mlgrush.database.DatabaseUtil;
import xyz.realkonts.mlgrush.database.PlayerDatabaseUtil;
import xyz.realkonts.mlgrush.events.*;
import xyz.realkonts.mlgrush.updater.UpdateChecker;
import xyz.realkonts.mlgrush.utils.ColorUtil;
import xyz.realkonts.mlgrush.utils.ScoreboardUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    private DatabaseUtil database;

    @Getter
    private PlayerDatabaseUtil playerDatabase;

    private ConfigUtil config;

    @Getter
    private final Map<UUID, FastBoard> boards = new HashMap<>();

    @Getter
    private boolean placeholderAPI;

    @Getter
    private ArenaManager arenaManager;


    @Override
    public void onEnable() {
        instance = this;
        database = new DatabaseUtil(this);
        playerDatabase = new PlayerDatabaseUtil(this);
        config = new ConfigUtil(this);
        arenaManager = new ArenaManager();
        arenaManager.loadArenas(this.database.getConfig().getConfigurationSection("arenas"));
        this.log("Toplam " + arenaManager.getAllArenas().size() + " arena yuklendi.");
        Config.load(config);
        this.setPlaceholderAPI();
        this.registerEvents();
        this.registerCommands();
        this.startScoreboardTask();
        UpdateChecker.checkUpdate();
    }

    private void setPlaceholderAPI() {
        placeholderAPI = (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null);
        if(placeholderAPI) {
            this.log("PlaceholderAPI destegi baslatildi.");
        }
    }

    private void startScoreboardTask() {
        getServer().getScheduler().runTaskTimer(this, () -> {
            for (FastBoard board : this.boards.values()) {
                ScoreboardUtil.updateBoard(board);
            }
        }, 0, 20);
    }

    public void log(String x) {
        Bukkit.getConsoleSender().sendMessage(ColorUtil.fixColor("[" + this.getDescription().getName() + "] " + x));
    }

    private void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        this.getServer().getPluginManager().registerEvents(new QuitEvent(), this);
        this.getServer().getPluginManager().registerEvents(new DeathEvent(), this);
        this.getServer().getPluginManager().registerEvents(new BreakEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlaceEvent(), this);
        this.getServer().getPluginManager().registerEvents(new MoveEvent(), this);
        this.getServer().getPluginManager().registerEvents(new DeathEvent(), this);
        this.getServer().getPluginManager().registerEvents(new DamageEvent(), this);
        this.getServer().getPluginManager().registerEvents(new FoodLevelEvent(), this);
        this.getServer().getPluginManager().registerEvents(new ItemDropEvent(), this);
    }

    private void registerCommands() {
        this.getCommand("mlgrush").setExecutor(new CMDMLGRush());
    }

    public DatabaseUtil getMLGDatabase() {
        return database;
    }

    public ConfigUtil getMLGConfig() {
        return config;
    }
}
