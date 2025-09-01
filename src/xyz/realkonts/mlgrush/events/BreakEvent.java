package xyz.realkonts.mlgrush.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import xyz.realkonts.mlgrush.Main;
import xyz.realkonts.mlgrush.arena.Arena;
import xyz.realkonts.mlgrush.arena.ArenaList;
import xyz.realkonts.mlgrush.config.Config;
import xyz.realkonts.mlgrush.utils.ColorUtil;

public class BreakEvent implements Listener {

    @EventHandler
    public void bbreak(BlockBreakEvent event) {
        YamlConfiguration database = Main.getInstance().getMLGDatabase().getConfig();
        if(ArenaList.getInArena().containsKey(event.getPlayer().getUniqueId())) {
            Arena arena = ArenaList.getInArena().get(event.getPlayer().getUniqueId());
            if(event.getBlock().getType() == Material.BED_BLOCK) {
                arena.handleBedBreak(event.getPlayer(), event.getBlock());
            }
            if (!arena.getPlacedBlocks().containsValue(event.getBlock().getLocation())) {
                event.setCancelled(true);
            }
        } else {
            String worldName = database.getString("lobby.mapName");
            if(worldName == null) return;
            if(event.getPlayer().getLocation().getWorld().getName().equals(worldName)) {
                event.setCancelled(true);
            }
        }
    }
}
