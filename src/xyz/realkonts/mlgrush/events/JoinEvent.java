package xyz.realkonts.mlgrush.events;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.realkonts.mlgrush.Main;
import xyz.realkonts.mlgrush.config.Config;
import xyz.realkonts.mlgrush.updater.UpdateChecker;
import xyz.realkonts.mlgrush.utils.ColorUtil;
import xyz.realkonts.mlgrush.utils.LobbyUtil;

public class JoinEvent implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(player.isOp() && UpdateChecker.isAvailableUpdate()) {
            player.sendMessage(ColorUtil.fixColor("&6MLGRush &eeklentisi için yeni bir sürüm bulundu.\n&7Yeni versiyon: &f" + UpdateChecker.getNewVersion() + "\n&7Güncelleme notu: &f" + UpdateChecker.getUpdateMessage()));
        }
        FastBoard board = new FastBoard(player);

        board.updateTitle(ColorUtil.fixColor(Config.scoreBoardTitle));

        Main.getInstance().getBoards().put(player.getUniqueId(), board);
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            player.getInventory().clear();
            LobbyUtil.teleportToLobby(player);
            player.setFoodLevel(20);
            player.setSaturation(20);
        }, 1L);
    }
}
