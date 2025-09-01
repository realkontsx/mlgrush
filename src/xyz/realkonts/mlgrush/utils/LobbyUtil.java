package xyz.realkonts.mlgrush.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.realkonts.mlgrush.Main;
import xyz.realkonts.mlgrush.config.Config;

public class LobbyUtil {

    public static void teleportToLobby(Player player) {
        YamlConfiguration database = Main.getInstance().getMLGDatabase().getConfig();
        String worldName = database.getString("lobby.mapName");
        if(worldName == null) {
            player.sendMessage(ColorUtil.fixColor(Config.prefix + " &cDatabase'de lobi world ismi bulunamadı."));
        }
        double x = database.getDouble("lobby.loc.x");
        double y = database.getDouble("lobby.loc.y");
        double z = database.getDouble("lobby.loc.z");
        float yaw = (float) database.getDouble("lobby.loc.yaw");
        float pitch = (float) database.getDouble("lobby.loc.pitch");
        World world = Bukkit.getWorld(worldName);
        if(world == null) {
            player.sendMessage(ColorUtil.fixColor(Config.prefix + " &cDatabase'de lobi world ismi &6" + worldName + " &colarak belirtilmiş ama world bulunamadı."));
            return;
        }
        Location location = new Location(world, x, y, z, yaw, pitch);
        player.teleport(location);
    }
}
