package xyz.realkonts.mlgrush.config;

import java.util.Arrays;
import java.util.List;

public class Config {

    public static String prefix;
    public static String victory;
    public static String deathMessage;
    public static String ownBedBreak;
    public static String enemyBedDestroyed;
    public static String ownBedDestroyed;
    public static String joinQueue;
    public static String alreadyInQueue;
    public static String matchFound;
    public static String noArena;
    public static boolean scoreboardEnabled;
    public static String scoreBoardTitle;
    public static List<String> scoreBoardLobbyLines;
    public static List<String> scoreBoardGameLines;

    public static void load(ConfigUtil configUtil) {
        prefix = getOrSet(configUtil, "settings.prefix", "&6MLGRush &8>");
        victory = getOrSet(configUtil, "messages.victory", "&6%player% &7isimli oyuncu oyunu kazandı.");
        deathMessage = getOrSet(configUtil, "messages.death-message", "&6%player% &7öldü.");
        ownBedBreak = getOrSet(configUtil, "messages.own-bed-break", "&cKendi yatağını kıramazsın!");
        enemyBedDestroyed = getOrSet(configUtil, "messages.enemy-bed-destroyed", "&6%player% &7isimli oyuncunun yatağını kırdın.");
        ownBedDestroyed = getOrSet(configUtil, "messages.own-bed-destroyed", "&6%player% &7isimli oyuncu yatağını kırdı.");
        joinQueue = getOrSet(configUtil, "messages.join-queue", "&aSıraya katıldın.");
        alreadyInQueue = getOrSet(configUtil, "messages.already-in-queue", "&cZaten sıradasın.");
        matchFound = getOrSet(configUtil, "messages.match-found", "&aRakibin bulundu, arenaya aktarılıyorsun.");
        noArena = getOrSet(configUtil, "messages.no-available-arena", "&cMüsait arena bulunamadı, lütfen daha sonra tekrar dene.");
        scoreboardEnabled = getOrSet(configUtil, "scoreboard.enabled", true);
        scoreBoardTitle = getOrSet(configUtil, "scoreboard.title", "&6&lMLGRush");
        scoreBoardLobbyLines = getOrSet(configUtil, "scoreboard.lobby.lines", Arrays.asList(
                "",
                "&fKazanmalar: &a%win%",
                "",
                "&ediscord.gg/52Ce6ryrZW"
        ));
        scoreBoardGameLines = getOrSet(configUtil, "scoreboard.game.lines", Arrays.asList(
                "",
                "&fHarita:",
                "&a%map%",
                "",
                "&fSkor:",
                "%blue_name%: &a%blue_score%",
                "%red_name%: &a%red_score%",
                "",
                "&ediscord.gg/52Ce6ryrZW"
        ));
    }

    private static String getOrSet(ConfigUtil config, String key, String defaultValue) {
        if (!config.getConfig().isSet(key)) {
            config.getConfig().set(key, defaultValue);
            config.saveConfigFile();
        }
        return config.getConfig().getString(key);
    }

    private static boolean getOrSet(ConfigUtil config, String key, boolean defaultValue) {
        if (!config.getConfig().isSet(key)) {
            config.getConfig().set(key, defaultValue);
            config.saveConfigFile();
        }
        return config.getConfig().getBoolean(key);
    }

    private static List<String> getOrSet(ConfigUtil config, String key, List<String> defaultValue) {
        if (!config.getConfig().isSet(key)) {
            config.getConfig().set(key, defaultValue);
            config.saveConfigFile();
        }
        return config.getConfig().getStringList(key);
    }
}
