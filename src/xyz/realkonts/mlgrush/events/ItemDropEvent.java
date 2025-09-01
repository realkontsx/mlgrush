package xyz.realkonts.mlgrush.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDropEvent implements Listener {

    @EventHandler
    public void dropevent(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }
}
