package xyz.realkonts.mlgrush.arena.game;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class ArenaManager {

    private final Map<String, GameArena> arenas = new HashMap<>();

    public void loadArenas(ConfigurationSection section) {
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            ConfigurationSection arenaSec = section.getConfigurationSection(key);
            if (arenaSec == null) continue;

            try {
                String name = arenaSec.getString("name");
                String worldName = arenaSec.getString("worldName");

                Location blueSpawn = getLocation(arenaSec.getConfigurationSection("blueSpawn"), worldName, true);
                Location blueBed = getLocation(arenaSec.getConfigurationSection("blueBed"), worldName, false);
                Location redSpawn = getLocation(arenaSec.getConfigurationSection("redSpawn"), worldName, true);
                Location redBed = getLocation(arenaSec.getConfigurationSection("redBed"), worldName, false);
                Location pos1 = getLocation(arenaSec.getConfigurationSection("pos1"), worldName, false);
                Location pos2 = getLocation(arenaSec.getConfigurationSection("pos2"), worldName, false);

                if (name == null || worldName == null ||
                        blueSpawn == null || blueBed == null ||
                        redSpawn == null || redBed == null ||
                        pos1 == null || pos2 == null) {
                    Bukkit.getLogger().warning("Arena '" + key + "'de eksik veriler var. Tamamladıktan sonra tekrar sunucuyu başlat.");
                    continue;
                }

                GameArena arena = new GameArena(name, worldName, blueSpawn, blueBed, redSpawn, redBed, pos1, pos2);
                arenas.put(name.toLowerCase(), arena);

            } catch (Exception e) {
                Bukkit.getLogger().warning("Arena yüklenirken hata oluştu: " + key);
                e.printStackTrace();
            }
        }
    }

    private Location getLocation(ConfigurationSection sec, String world, boolean useYawPitch) {
        if (sec == null) return null;
        double x = sec.getDouble("x");
        double y = sec.getDouble("y");
        double z = sec.getDouble("z");
        if (useYawPitch) {
            float yaw = (float) sec.getDouble("yaw");
            float pitch = (float) sec.getDouble("pitch");
            return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        } else {
            return new Location(Bukkit.getWorld(world), x, y, z);
        }
    }

    public GameArena getArena(String name) {
        return arenas.get(name.toLowerCase());
    }

    public Collection<GameArena> getAllArenas() {
        return arenas.values();
    }

    public GameArena findAvailableArena() {
        for (GameArena arena : arenas.values()) {
            if (arena.isAvailable()) {
                return arena;
            }
        }
        return null;
    }
}
