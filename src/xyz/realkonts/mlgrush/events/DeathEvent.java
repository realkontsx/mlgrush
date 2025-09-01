package xyz.realkonts.mlgrush.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import xyz.realkonts.mlgrush.Main;
import xyz.realkonts.mlgrush.arena.Arena;
import xyz.realkonts.mlgrush.arena.ArenaList;
import xyz.realkonts.mlgrush.database.DatabaseUtil;

public class DeathEvent implements Listener {

    @EventHandler
    public void death(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if(ArenaList.getInArena().containsKey(p.getUniqueId())) {
            Arena arena = ArenaList.getInArena().get(p.getUniqueId());
            arena.handleDeath(p);
        }
    }

}
