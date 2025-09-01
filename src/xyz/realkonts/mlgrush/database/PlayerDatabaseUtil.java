package xyz.realkonts.mlgrush.database;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.realkonts.mlgrush.Main;
import xyz.realkonts.mlgrush.config.ConfigUtil;

import java.io.File;
import java.io.IOException;

public class PlayerDatabaseUtil {

    private final Main plugin;
    private File configFile;
    private YamlConfiguration config;

    public PlayerDatabaseUtil(Main plugin) {
        this.plugin = plugin;
        createConfig();
    }

    private void createConfig() {
        File databaseFolder = new File(plugin.getDataFolder(), "database");
        if (!databaseFolder.exists()) {
            databaseFolder.mkdirs();
        }
        configFile = new File(databaseFolder, "player_database.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }


    public void saveConfigFile() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPlayerWinCount(Player player, long count) {
        this.getConfig().set("wincount." + player.getName(), count);
        this.saveConfigFile();
    }

    public long getPlayerWinCount(Player player) {
        return getOrSet(this, "wincount." + player.getName(), 0L);
    }


    public String getOrSet(PlayerDatabaseUtil config, String key, String defaultValue) {
        if (!config.getConfig().isSet(key)) {
            config.getConfig().set(key, defaultValue);
            config.saveConfigFile();
        }
        return config.getConfig().getString(key);
    }

    public boolean getOrSet(PlayerDatabaseUtil config, String key, boolean defaultValue) {
        if (!config.getConfig().isSet(key)) {
            config.getConfig().set(key, defaultValue);
            config.saveConfigFile();
        }
        return config.getConfig().getBoolean(key);
    }

    public int getOrSet(PlayerDatabaseUtil config, String key, int defaultValue) {
        if (!config.getConfig().isSet(key)) {
            config.getConfig().set(key, defaultValue);
            config.saveConfigFile();
        }
        return config.getConfig().getInt(key);
    }

    public long getOrSet(PlayerDatabaseUtil config, String key, long defaultValue) {
        if (!config.getConfig().isSet(key)) {
            config.getConfig().set(key, defaultValue);
            config.saveConfigFile();
        }
        return config.getConfig().getLong(key);
    }


    public YamlConfiguration getConfig() {
        return this.config;
    }

    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }
}

