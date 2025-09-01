package xyz.realkonts.mlgrush.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.realkonts.mlgrush.Main;
import xyz.realkonts.mlgrush.arena.Arena;
import xyz.realkonts.mlgrush.arena.ArenaList;
import xyz.realkonts.mlgrush.config.Config;

public class PlaceEvent implements Listener {

    @EventHandler
    public void place(BlockPlaceEvent event) {
        if(ArenaList.getInArena().containsKey(event.getPlayer().getUniqueId())) {
            Arena arena = ArenaList.getInArena().get(event.getPlayer().getUniqueId());
            arena.getPlacedBlocks().put(event.getBlock(), event.getBlock().getLocation());
            Player player = event.getPlayer();
            Location pos1 = arena.getPos1();
            Location pos2 = arena.getPos2();
            Location placed = event.getBlockPlaced().getLocation();
            if (!isInside(placed, pos1, pos2)) {
                event.setCancelled(true);
                player.sendMessage(Config.prefix + " &cSınıra ulaştın!");
            }
        } else {
            String worldName = Main.getInstance().getMLGDatabase().getConfig().getString("lobby.mapName");
            if(worldName == null) return;
            if(event.getPlayer().getLocation().getWorld().getName().equals(worldName)) {
                event.setCancelled(true);
            }
        }
    }

    private boolean isInside(Location loc, Location pos1, Location pos2) {
        int x1 = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int y1 = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int z1 = Math.min(pos1.getBlockZ(), pos2.getBlockZ());

        int x2 = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int y2 = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int z2 = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        return x >= x1 && x <= x2 &&
                y >= y1 && y <= y2 &&
                z >= z1 && z <= z2;
    }

}
