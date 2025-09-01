package xyz.realkonts.mlgrush.database;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.realkonts.mlgrush.Main;

import java.io.File;
import java.io.IOException;
import java.util.NavigableMap;
import java.util.TreeMap;

public class DatabaseUtil {

    private final Main plugin;
    private File configFile;
    private YamlConfiguration config;

    public DatabaseUtil(Main plugin) {
        this.plugin = plugin;
        createConfig();
    }

    private void createConfig() {
        File databaseFolder = new File(plugin.getDataFolder(), "database");
        if (!databaseFolder.exists()) {
            databaseFolder.mkdirs();
        }
        configFile = new File(databaseFolder, "database.yml");
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


    public YamlConfiguration getConfig() {
        return this.config;
    }

    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }
}

