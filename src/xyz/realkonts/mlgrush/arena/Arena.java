package xyz.realkonts.mlgrush.arena;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.Bed;
import org.bukkit.scheduler.BukkitTask;
import xyz.realkonts.mlgrush.Main;
import xyz.realkonts.mlgrush.arena.game.GameArena;
import xyz.realkonts.mlgrush.config.Config;
import xyz.realkonts.mlgrush.database.DatabaseUtil;
import xyz.realkonts.mlgrush.utils.ColorUtil;
import xyz.realkonts.mlgrush.utils.ItemUtil;
import xyz.realkonts.mlgrush.utils.LobbyUtil;

import java.util.HashMap;

public class Arena {
    @Getter
    private String arenaName;

    @Getter
    private Player blue;

    @Getter
    private Player red;

    @Getter
    @Setter
    private int blueScore;

    @Getter
    @Setter
    private int redScore;

    @Getter
    private GameArena gameArena;

    @Getter
    private DatabaseUtil database;

    @Getter
    private HashMap<Block, Location> placedBlocks = new HashMap<>();

    @Getter
    private Location blueSpawnLocation;

    @Getter
    private Location redSpawnLocation;

    @Getter
    private Location pos1;

    @Getter
    private Location pos2;

    public Arena(Player player1, Player player2, GameArena gameArena, DatabaseUtil database) {
        this.blue = player1;
        this.red = player2;
        this.gameArena = gameArena;
        this.arenaName = gameArena.getName();
        this.database = database;
    }


    public void start() {
        World world = Bukkit.getWorld(this.database.getConfig().getString("arenas." + arenaName + ".worldName"));
        this.blueSpawnLocation = new Location(world, this.database.getConfig().getDouble("arenas." + arenaName + ".blueSpawn.x"), this.database.getConfig().getDouble("arenas." + arenaName + ".blueSpawn.y"), this.database.getConfig().getDouble("arenas." + arenaName + ".blueSpawn.z"), (float) this.database.getConfig().getDouble("arenas." + arenaName + ".blueSpawn.yaw"), (float) this.database.getConfig().getDouble("arenas." + arenaName + ".blueSpawn.pitch"));
        this.redSpawnLocation = new Location(world, this.database.getConfig().getDouble("arenas." + arenaName + ".redSpawn.x"), this.database.getConfig().getDouble("arenas." + arenaName + ".redSpawn.y"), this.database.getConfig().getDouble("arenas." + arenaName + ".redSpawn.z"), (float) this.database.getConfig().getDouble("arenas." + arenaName + ".redSpawn.yaw"), (float) this.database.getConfig().getDouble("arenas." + arenaName + ".redSpawn.pitch"));
        this.pos1 = new Location(world, this.database.getConfig().getDouble("arenas." + arenaName + ".pos1.x"), this.database.getConfig().getDouble("arenas." + arenaName + ".pos1.y"), this.database.getConfig().getDouble("arenas." + arenaName + ".pos1.z"));
        this.pos2 = new Location(world, this.database.getConfig().getDouble("arenas." + arenaName + ".pos2.x"), this.database.getConfig().getDouble("arenas." + arenaName + ".pos2.y"), this.database.getConfig().getDouble("arenas." + arenaName + ".pos2.z"));
        this.teleportPlayersToSpawn();
        this.blue.getInventory().clear();
        this.red.getInventory().clear();
        ItemUtil.giveItems(this.blue);
        ItemUtil.giveItems(this.red);
    }

    public void teleportPlayersToSpawn() {
        this.blue.teleport(this.blueSpawnLocation);
        this.red.teleport(this.redSpawnLocation);
    }

    public void sendMessageToPlayers(String msg) {
        this.blue.sendMessage(msg);
        this.red.sendMessage(msg);
    }

    public void handleDeath(Player player) {
        this.sendMessageToPlayers(ColorUtil.fixColor(Config.prefix + " " + Config.deathMessage.replaceAll("%player%", player.getName())));
        player.getInventory().clear();
        ItemUtil.giveItems(player);
        Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
            player.spigot().respawn();
            if (player.getUniqueId().equals(this.blue.getUniqueId())) {
                this.blue.teleport(this.blueSpawnLocation);
            } else if (player.getUniqueId().equals(this.red.getUniqueId())) {
                this.red.teleport(this.redSpawnLocation);
            }
        }, 1L);
    }

    public void handleFall(Player player) {
        this.sendMessageToPlayers(ColorUtil.fixColor(Config.prefix + " " + Config.deathMessage.replaceAll("%player%", player.getName())));
        player.getInventory().clear();
        ItemUtil.giveItems(player);
        if (player.getUniqueId().equals(this.blue.getUniqueId())) {
            this.blue.teleport(this.blueSpawnLocation);
        } else if (player.getUniqueId().equals(this.red.getUniqueId())) {
            this.red.teleport(this.redSpawnLocation);
        }
    }

    public void handleDamage(Player player) {
        Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.setSaturation(20f);
        }, 1L);
    }

    public void handleBedBreak(Player player, Block block) {
        if (block.getType() != Material.BED_BLOCK) return;

        Bed bedData = (Bed) block.getState().getData();
        Block head = bedData.isHeadOfBed() ? block : block.getRelative(bedData.getFacing());
        Block foot = bedData.isHeadOfBed() ? block.getRelative(bedData.getFacing().getOppositeFace()) : block;

        String teamBed = isRedBed(head) || isRedBed(foot) ? "red" :
                isBlueBed(head) || isBlueBed(foot) ? "blue" : "";

        if (teamBed.isEmpty()) return;

        boolean isBluePlayer = player.getUniqueId().equals(blue.getUniqueId());
        boolean isRedPlayer = !isBluePlayer;
        Player opponent = isBluePlayer ? red : blue;

        if ((isBluePlayer && teamBed.equals("blue")) || (isRedPlayer && teamBed.equals("red"))) {
            player.sendMessage(ColorUtil.fixColor(Config.prefix + " " + Config.ownBedBreak));
            return;
        }

        player.sendMessage(ColorUtil.fixColor(Config.prefix + " " + Config.enemyBedDestroyed.replaceAll("%player%", (isBluePlayer ? red.getName() : blue.getName()))));
        opponent.sendMessage(ColorUtil.fixColor(Config.prefix + " " + Config.ownBedDestroyed.replaceAll("%player%", (isBluePlayer ? blue.getName() : red.getName()))));

        this.handleAddScore(player);
    }

    public void handleAddScore(Player player) {
        boolean isBluePlayer = player.getUniqueId().equals(blue.getUniqueId());
        boolean isRedPlayer = !isBluePlayer;

        if (isBluePlayer && ++blueScore > 4) {
            finish(blue);
        } else {
            this.teleportPlayersToSpawn();
            this.clearPlacedBlocks();
        }
        if (isRedPlayer && ++redScore > 4) {
            finish(red);
        } else {
            this.teleportPlayersToSpawn();
            this.clearPlacedBlocks();
        }
    }


    private boolean isRedBed(Block block) {
        Location loc = block.getLocation();
        Location redBedLoc = getRedBedLocationFromConfig();
        return loc.getBlockX() == redBedLoc.getBlockX()
                && loc.getBlockY() == redBedLoc.getBlockY()
                && loc.getBlockZ() == redBedLoc.getBlockZ();
    }

    private boolean isBlueBed(Block block) {
        Location loc = block.getLocation();
        Location blueBedLoc = getBlueBedLocationFromConfig();
        return loc.getBlockX() == blueBedLoc.getBlockX()
                && loc.getBlockY() == blueBedLoc.getBlockY()
                && loc.getBlockZ() == blueBedLoc.getBlockZ();
    }

    private Location getRedBedLocationFromConfig() {
        int x = database.getConfig().getInt("arenas." + arenaName + ".redBed.x");
        int y = database.getConfig().getInt("arenas." + arenaName + ".redBed.y");
        int z = database.getConfig().getInt("arenas." + arenaName + ".redBed.z");
        String world = database.getConfig().getString("arenas." + arenaName + ".worldName");
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    private Location getBlueBedLocationFromConfig() {
        int x = database.getConfig().getInt("arenas." + arenaName + ".blueBed.x");
        int y = database.getConfig().getInt("arenas." + arenaName + ".blueBed.y");
        int z = database.getConfig().getInt("arenas." + arenaName + ".blueBed.z");
        String world = database.getConfig().getString("arenas." + arenaName + ".worldName");
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public void clearPlacedBlocks() {
        for (Block block : getPlacedBlocks().keySet()) {
            block.setType(Material.AIR);
        }
        getPlacedBlocks().clear();
    }

    public void handleQuit(Player player) {
        if(player.getUniqueId().equals(this.blue.getUniqueId())) {
            this.finishSafely(this.red, this.blue);
        } else if(player.getUniqueId().equals(this.red.getUniqueId())) {
            this.finishSafely(this.blue, this.red);
        }
    }

    public void finish(Player winner) {
        this.clearPlacedBlocks();
        this.sendMessageToPlayers(ColorUtil.fixColor(Config.prefix + " " + Config.victory.replaceAll("%player%", winner.getName())));
        Main.getInstance().getPlayerDatabase().setPlayerWinCount(winner, Main.getInstance().getPlayerDatabase().getPlayerWinCount(winner) + 1);
        this.blue.getInventory().clear();
        this.red.getInventory().clear();
        ArenaList.getInArena().remove(this.blue.getUniqueId());
        ArenaList.getInArena().remove(this.red.getUniqueId());
        if (this.gameArena != null) {
            this.gameArena.setAvailable(true);
        }

        Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
            LobbyUtil.teleportToLobby(this.blue);
            LobbyUtil.teleportToLobby(this.red);
        }, 1L);
    }

    public void finishSafely(Player winner, Player leaver) {
        this.clearPlacedBlocks();
        if (winner != null && winner.isOnline()) {
            this.sendMessageToPlayers(ColorUtil.fixColor(
                    Config.prefix + " " + Config.victory.replaceAll("%player%", winner.getName())
            ));
            long currentWins = Main.getInstance().getPlayerDatabase().getPlayerWinCount(winner);
            Main.getInstance().getPlayerDatabase().setPlayerWinCount(winner, currentWins + 1);
        }
        if (this.blue != null && this.blue.isOnline()) this.blue.getInventory().clear();
        if (this.red != null && this.red.isOnline()) this.red.getInventory().clear();
        ArenaList.getInArena().remove(this.blue.getUniqueId());
        ArenaList.getInArena().remove(this.red.getUniqueId());
        if (this.gameArena != null) {
            this.gameArena.setAvailable(true);
        }
        Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
            if (this.blue != null && this.blue.isOnline()) LobbyUtil.teleportToLobby(this.blue);
            if (this.red != null && this.red.isOnline()) LobbyUtil.teleportToLobby(this.red);
        }, 1L);
    }

}
