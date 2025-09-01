package xyz.realkonts.mlgrush.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import xyz.realkonts.mlgrush.arena.Arena;
import xyz.realkonts.mlgrush.arena.ArenaList;

public class FoodLevelEvent implements Listener {

    @EventHandler
    public void foodlevelchange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        if (ArenaList.getInArena().containsKey(player.getUniqueId())) {
            player.setFoodLevel(20);
            player.setSaturation(20f);
            event.setCancelled(true);
        }
    }

}
