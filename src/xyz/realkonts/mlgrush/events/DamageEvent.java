package xyz.realkonts.mlgrush.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import xyz.realkonts.mlgrush.Main;
import xyz.realkonts.mlgrush.arena.Arena;
import xyz.realkonts.mlgrush.arena.ArenaList;

public class DamageEvent implements Listener {

    @EventHandler
    public void damage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        if(ArenaList.getInArena().containsKey(player.getUniqueId())) {
            Arena arena = ArenaList.getInArena().get(player.getUniqueId());
            arena.handleDamage(player);
            if(event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setCancelled(true);
            }
        } else {
            event.setCancelled(true);
        }
    }


}
