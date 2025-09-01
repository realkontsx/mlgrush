package xyz.realkonts.mlgrush.utils;

import fr.mrmicky.fastboard.FastBoard;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import xyz.realkonts.mlgrush.Main;
import xyz.realkonts.mlgrush.arena.Arena;
import xyz.realkonts.mlgrush.arena.ArenaList;
import xyz.realkonts.mlgrush.config.Config;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardUtil {

    public static void updateBoard(FastBoard board) {
        Player player = board.getPlayer();
        if (ArenaList.getInArena().containsKey(player.getUniqueId())) {
            Arena arena = ArenaList.getInArena().get(player.getUniqueId());
            List<String> gameLines = new ArrayList<>(Config.scoreBoardGameLines);
            gameLines.replaceAll(line -> line
                    .replace("%map%", arena.getArenaName())
                    .replace("%blue_name%", arena.getBlue().getName())
                    .replace("%red_name%", arena.getRed().getName())
                    .replace("%blue_score%", String.valueOf(arena.getBlueScore()))
                    .replace("%red_score%", String.valueOf(arena.getRedScore()))
            );

            if (Main.getInstance().isPlaceholderAPI()) {
                board.updateLines(ColorUtil.fixColor(PlaceholderAPI.setPlaceholders(player, gameLines)));
            } else {
                board.updateLines(ColorUtil.fixColor(gameLines));
            }
        } else {
            List<String> gameLines = new ArrayList<>(Config.scoreBoardLobbyLines);
            gameLines.replaceAll(line -> line
                    .replace("%win%", String.valueOf(Main.getInstance().getPlayerDatabase().getPlayerWinCount(player)))
                    .replace("%kills%", String.valueOf(player.getStatistic(Statistic.PLAYER_KILLS)))
                    .replace("%deaths%", String.valueOf(player.getStatistic(Statistic.DEATHS)))
            );
            if (Main.getInstance().isPlaceholderAPI()) {
                board.updateLines(ColorUtil.fixColor(PlaceholderAPI.setPlaceholders(player, gameLines)));
            } else {
                board.updateLines(ColorUtil.fixColor(gameLines));
            }
        }
    }
}
