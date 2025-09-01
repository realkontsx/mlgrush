package xyz.realkonts.mlgrush.config;

import org.bukkit.configuration.file.YamlConfiguration;
import xyz.realkonts.mlgrush.Main;

import java.io.File;
import java.io.IOException;

public class ConfigUtil {

    private final Main plugin;
    private File configFile;
    private YamlConfiguration config;

    public ConfigUtil(Main plugin) {
        this.plugin = plugin;
        createConfig();
    }

    private void createConfig() {
        configFile = new File(plugin.getDataFolder(), "config.yml");
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

