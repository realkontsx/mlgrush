package xyz.realkonts.mlgrush.events;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.realkonts.mlgrush.Main;
import xyz.realkonts.mlgrush.arena.Arena;
import xyz.realkonts.mlgrush.arena.ArenaList;

public class QuitEvent implements Listener {

    @EventHandler
    public void quit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        FastBoard board = Main.getInstance().getBoards().remove(player.getUniqueId());

        if (board != null) {
            board.delete();
        }

        if(ArenaList.getInArena().containsKey(player.getUniqueId())) {
            Arena arena = ArenaList.getInArena().get(player.getUniqueId());
            arena.handleQuit(player);
        }
    }
}
