package xyz.realkonts.mlgrush.events;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.realkonts.mlgrush.Main;
import xyz.realkonts.mlgrush.arena.Arena;
import xyz.realkonts.mlgrush.arena.ArenaList;
import xyz.realkonts.mlgrush.utils.ColorUtil;

public class MoveEvent implements Listener {

    @EventHandler
    public void move(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        boolean xd = ArenaList.getInArena().containsKey(player.getUniqueId());
        if(xd) {
            Arena arena = ArenaList.getInArena().get(player.getUniqueId());
            YamlConfiguration db = Main.getInstance().getMLGDatabase().getConfig();
            String arenaName = arena.getArenaName();

            double y1 = db.getDouble("arenas." + arenaName + ".pos1.y");
            double y2 = db.getDouble("arenas." + arenaName + ".pos2.y");

            double minY = Math.min(y1, y2);

            if (player.getLocation().getY() < minY) {
                arena.handleFall(player);
            }
        }
    }


}
