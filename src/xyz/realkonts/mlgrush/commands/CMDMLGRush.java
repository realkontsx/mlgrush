package xyz.realkonts.mlgrush.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.realkonts.mlgrush.Main;
import xyz.realkonts.mlgrush.arena.Arena;
import xyz.realkonts.mlgrush.arena.ArenaList;
import xyz.realkonts.mlgrush.arena.game.ArenaManager;
import xyz.realkonts.mlgrush.arena.game.GameArena;
import xyz.realkonts.mlgrush.config.Config;
import xyz.realkonts.mlgrush.utils.ColorUtil;

import java.util.*;

public class CMDMLGRush implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Bu komudu kullanabilmek için oyuncu olmalısın.");
            return true;
        }
        Player player = (Player) sender;
        if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            return this.handleReload(player);
        }
        if(args.length == 1 && args[0].equalsIgnoreCase("reloadarenas")) {
            return this.handleReloadArenas(player);
        }
        if(args.length == 1 && args[0].equalsIgnoreCase("setlobby")) {
            return this.handleSetLobby(player);
        }
        if(args.length == 2 && args[0].equalsIgnoreCase("createarena")) {
            return this.handleCreateArena(player, args[1]);
        }
        if(args.length == 2 && args[0].equalsIgnoreCase("setarenaworld")) {
            return this.handleSetArenaWorld(player, args[1]);
        }
        if(args.length == 2 && args[0].equalsIgnoreCase("setbluespawn")) {
            return this.handleSetBlueSpawn(player, args[1]);
        }
        if(args.length == 2 && args[0].equalsIgnoreCase("setredspawn")) {
            return this.handleSetRedSpawn(player, args[1]);
        }
        if(args.length == 2 && args[0].equalsIgnoreCase("setbluebed")) {
            return this.handleSetBlueBed(player, args[1]);
        }
        if(args.length == 2 && args[0].equalsIgnoreCase("setredbed")) {
            return this.handleSetRedBed(player, args[1]);
        }
        if(args.length == 1 && args[0].equalsIgnoreCase("join")) {
            return this.handleJoin(player);
        }
        if(args.length == 2 && args[0].equalsIgnoreCase("setpos1")) {
            return this.handleSetPos1(player, args[1]);
        }
        if(args.length == 2 && args[0].equalsIgnoreCase("setpos2")) {
            return this.handleSetPos2(player, args[1]);
        }
        this.sendHelpMessage(player);

        return true;
    }

    private void sendHelpMessage(Player player) {
        player.sendMessage("§8§m--------------------§r §b§lMLGRush §8§m--------------------");
        player.sendMessage("§eAdmin Komutları:");
        player.sendMessage(" §7/mlgrush setlobby §f- Lobi konumunu ayarlar.");
        player.sendMessage(" §7/mlgrush createarena <isim> §f- Yeni arena oluşturur.");
        player.sendMessage(" §7/mlgrush setarenaworld <arena> §f- Arenanın dünyasını ayarlar.");
        player.sendMessage(" §7/mlgrush setbluespawn <arena> §f- Mavi takım spawn noktasını ayarlar.");
        player.sendMessage(" §7/mlgrush setredspawn <arena> §f- Kırmızı takım spawn noktasını ayarlar.");
        player.sendMessage(" §7/mlgrush setbluebed <arena> §f- Mavi takım yatağını ayarlar.");
        player.sendMessage(" §7/mlgrush setredbed <arena> §f- Kırmızı takım yatağını ayarlar.");
        player.sendMessage(" §7/mlgrush setpos1 <arena> §f- Arena bölgesi için ilk köşe.");
        player.sendMessage(" §7/mlgrush setpos2 <arena> §f- Arena bölgesi için ikinci köşe.");
        player.sendMessage(" §7/mlgrush reload §f- Config dosyasını yeniden yükler.");
        player.sendMessage(" §7/mlgrush reloadarenas §f- Arenaları yeniden yükler. (Eğer yeni arena oluşturup tamamladıysan kullan.)");
        player.sendMessage("");
        player.sendMessage("§eOyuncu Komutları:");
        player.sendMessage(" §7/mlgrush join §f- Sıraya katılır.");
        player.sendMessage("");
        player.sendMessage("§8§m----------------------------------------------------");
    }

    private boolean handleReload(Player player) {
        Main.getInstance().getMLGConfig().reloadConfig();
        Config.load(Main.getInstance().getMLGConfig());
        player.sendMessage(ColorUtil.fixColor(Config.prefix + " " + "&aConfig dosyası yeniden yüklendi."));
       return true;
    }

    private boolean handleReloadArenas(Player player) {
        ArenaManager arenaManager = Main.getInstance().getArenaManager();
        arenaManager.loadArenas(Main.getInstance().getMLGDatabase().getConfig().getConfigurationSection("arenas"));
        player.sendMessage(ColorUtil.fixColor(Config.prefix + " &7Toplam &6" + arenaManager.getAllArenas().size() + " &7arena yüklendi."));
        Main.getInstance().log("Toplam " + arenaManager.getAllArenas().size() + " arena yüklendi.");
        return true;
    }

    private boolean handleJoin(Player player) {
        UUID playerId = player.getUniqueId();
        List<UUID> queue = ArenaList.getInQueue();

        if (queue.contains(playerId)) {
            player.sendMessage(ColorUtil.fixColor(Config.prefix + " " + Config.alreadyInQueue));
            return true;
        }

        GameArena arena = Main.getInstance().getArenaManager().findAvailableArena();
        if (arena == null) {
            player.sendMessage(ColorUtil.fixColor(Config.prefix + " " + Config.noArena));
            return true;
        }

        queue.add(playerId);
        player.sendMessage(ColorUtil.fixColor(Config.prefix + " " + Config.joinQueue));

        if (queue.size() >= 2) {
            Player p1 = Bukkit.getPlayer(queue.get(0));
            Player p2 = Bukkit.getPlayer(queue.get(1));

            if (p1 != null && p2 != null) {
                p1.sendMessage(ColorUtil.fixColor(Config.prefix + " " + Config.matchFound));
                p2.sendMessage(ColorUtil.fixColor(Config.prefix + " " + Config.matchFound));

                queue.remove(p1.getUniqueId());
                queue.remove(p2.getUniqueId());

                Arena match = new Arena(p1, p2, arena, Main.getInstance().getMLGDatabase());
                ArenaList.getInArena().put(p1.getUniqueId(), match);
                ArenaList.getInArena().put(p2.getUniqueId(), match);

                arena.setAvailable(false);
                match.start();
            }
        }

        return true;
    }




    private boolean handleSetLobby(Player player) {
        if(!player.hasPermission("mlgrush.commands.setlobby")) {
            player.sendMessage(ColorUtil.fixColor(Config.prefix + " &cYetkin yok."));
            return true;
        }
        YamlConfiguration database = Main.getInstance().getMLGDatabase().getConfig();
        database.set("lobby.mapName", player.getLocation().getWorld().getName());
        database.set("lobby.loc.x", player.getLocation().getX());
        database.set("lobby.loc.y", player.getLocation().getY());
        database.set("lobby.loc.z", player.getLocation().getZ());
        database.set("lobby.loc.yaw", player.getLocation().getYaw());
        database.set("lobby.loc.pitch", player.getLocation().getPitch());
        Main.getInstance().getMLGDatabase().saveConfigFile();
        player.sendMessage(ColorUtil.fixColor(Config.prefix + " &aLobi başarıyla ayarlandı."));
        return true;
    }

    private boolean handleCreateArena(Player player, String arenaName) {
        if(!player.hasPermission("mlgrush.commands.arena")) {
            player.sendMessage(ColorUtil.fixColor(Config.prefix + " &cYetkin yok."));
            return true;
        }
        YamlConfiguration database = Main.getInstance().getMLGDatabase().getConfig();
        database.set("arenas." + arenaName + ".name", arenaName);
        Main.getInstance().getMLGDatabase().saveConfigFile();
        player.sendMessage(ColorUtil.fixColor(Config.prefix + " &aArena başarıyla oluşturuldu. Arena ismi: &6" + arenaName));
        return true;
    }

    private boolean handleSetArenaWorld(Player player, String arenaName) {
        String worldName = player.getLocation().getWorld().getName();
        if(!player.hasPermission("mlgrush.commands.arena")) {
            player.sendMessage(ColorUtil.fixColor(Config.prefix + " &cYetkin yok."));
            return true;
        }
        YamlConfiguration database = Main.getInstance().getMLGDatabase().getConfig();
        if(!isValidArena(database, arenaName)) {
            player.sendMessage(ColorUtil.fixColor(Config.prefix + " &cBu isimde arena bulunamadı."));
            return true;
        }
        database.set("arenas." + arenaName + ".worldName", worldName);
        Main.getInstance().getMLGDatabase().saveConfigFile();
        player.sendMessage(ColorUtil.fixColor(Config.prefix + " &aWorld başarıyla ayarlandı. World ismi: &6" + worldName));
        return true;
    }

    private boolean handleSetBlueSpawn(Player player, String arenaName) {
        if(!player.hasPermission("mlgrush.commands.arena")) {
            player.sendMessage(ColorUtil.fixColor(Config.prefix + " &cYetkin yok."));
            return true;
        }
        YamlConfiguration database = Main.getInstance().getMLGDatabase().getConfig();
        if(!isValidArena(database, arenaName)) {
            player.sendMessage(ColorUtil.fixColor(Config.prefix + " &cBu isimde arena bulunamadı."));
            return true;
        }
        database.set("arenas." + arenaName + ".blueSpawn.x", player.getLocation().getX());
        database.set("arenas." + arenaName + ".blueSpawn.y", player.getLocation().getY());
        database.set("arenas." + arenaName + ".blueSpawn.z", player.getLocation().getZ());
        database.set("arenas." + arenaName + ".blueSpawn.yaw", player.getLocation().getYaw());
        database.set("arenas." + arenaName + ".blueSpawn.pitch", player.getLocation().getPitch());
        Main.getInstance().getMLGDatabase().saveConfigFile();
        player.sendMessage(ColorUtil.fixColor(Config.prefix + " &aBlue spawn başarıyla ayarlandı."));
        return true;
    }

    private boolean handleSetRedSpawn(Player player, String arenaName) {
        if(!player.hasPermission("mlgrush.commands.arena")) {
            player.sendMessage(ColorUtil.fixColor(Config.prefix + " &cYetkin yok."));
            return true;
        }
        YamlConfiguration database = Main.getInstance().getMLGDatabase().getConfig();
        if(!isValidArena(database, arenaName)) {
            player.sendMessage(ColorUtil.fixColor(Config.prefix + " &cBu isimde arena bulunamadı."));
            return true;
        }
        database.set("arenas." + arenaName + ".redSpawn.x", player.getLocation().getX());
        database.set("arenas." + arenaName + ".redSpawn.y", player.getLocation().getY());
        database.set("arenas." + arenaName + ".redSpawn.z", player.getLocation().getZ());
        database.set("arenas." + arenaName + ".redSpawn.yaw", player.getLocation().getYaw());
        database.set("arenas." + arenaName + ".redSpawn.pitch", player.getLocation().getPitch());
        Main.getInstance().getMLGDatabase().saveConfigFile();
        player.sendMessage(ColorUtil.fixColor(Config.prefix + " &aRed spawn başarıyla ayarlandı."));
        return true;
    }

    private boolean handleSetBlueBed(Player player, String arenaName) {
        if (!player.hasPermission("mlgrush.commands.arena")) {
            player.sendMessage(ColorUtil.fixColor(Config.prefix + " &cYetkin yok."));
            return true;
        }

        YamlConfiguration database = Main.getInstance().getMLGDatabase().getConfig();
        if (!isValidArena(database, arenaName)) {
            player.sendMessage(ColorUtil.fixColor(Config.prefix + " &cBu isimde arena bulunamadı."));
            return true;
        }

        Block targetBlock = player.getTargetBlock((Set<Material>) null, 5);

        if (targetBlock == null || targetBlock.getType() != Material.BED_BLOCK) {
            player.sendMessage(ColorUtil.fixColor(Config.prefix + " &cBir yatağa bakmalısın."));
            return true;
        }


        BlockState state = targetBlock.getState();

        if (!(state.getData() instanceof org.bukkit.material.Bed)) {
            player.sendMessage(ColorUtil.fixColor(Config.prefix + " &cBu bir yatak bloğu değil."));
            return true;
        }

        org.bukkit.material.Bed bed = (org.bukkit.material.Bed) state.getData();
        if (!bed.isHeadOfBed()) {
            targetBlock = targetBlock.getRelative(bed.getFacing());
        }

        Location bedLoc = targetBlock.getLocation();

        database.set("arenas." + arenaName + ".blueBed.x", bedLoc.getBlockX());
        database.set("arenas." + arenaName + ".blueBed.y", bedLoc.getBlockY());
        database.set("arenas." + arenaName + ".blueBed.z", bedLoc.getBlockZ());

        Main.getInstance().getMLGDatabase().saveConfigFile();
        player.sendMessage(ColorUtil.fixColor(Config.prefix + " &aBlue yatağı başarıyla ayarlandı."));
        return true;
    }

    private boolean handleSetRedBed(Player player, String arenaName) {
        if (!player.hasPermission("mlgrush.commands.arena")) {
            player.sendMessage(ColorUtil.fixColor(Config.prefix + " &cYetkin yok."));
            return true;
        }

        YamlConfiguration database = Main.getInstance().getMLGDatabase().getConfig();
        if (!isValidArena(database, arenaName)) {
            player.sendMessage(ColorUtil.fixColor(Config.prefix + " &cBu isimde arena bulunamadı."));
            return true;
        }

        Block targetBlock = player.getTargetBlock((Set<Material>) null, 5);

        if (targetBlock == null || targetBlock.getType() != Material.BED_BLOCK) {
            player.sendMessage(ColorUtil.fixColor(Config.prefix + " &cBir yatağa bakmalısın."));
            return true;
        }


        BlockState state = targetBlock.getState();

        if (!(state.getData() instanceof org.bukkit.material.Bed)) {
            player.sendMessage(ColorUtil.fixColor(Config.prefix + " &cBu bir yatak bloğu değil."));
            return true;
        }

        org.bukkit.material.Bed bed = (org.bukkit.material.Bed) state.getData();
        if (!bed.isHeadOfBed()) {
            targetBlock = targetBlock.getRelative(bed.getFacing());
        }

        Location bedLoc = targetBlock.getLocation();

        database.set("arenas." + arenaName + ".redBed.x", bedLoc.getBlockX());
        database.set("arenas." + arenaName + ".redBed.y", bedLoc.getBlockY());
        database.set("arenas." + arenaName + ".redBed.z", bedLoc.getBlockZ());

        Main.getInstance().getMLGDatabase().saveConfigFile();
        player.sendMessage(ColorUtil.fixColor(Config.prefix + " &aRed yatağı başarıyla ayarlandı."));
        return true;
    }

    private boolean handleSetPos1(Player player, String arenaName) {
        if (!player.hasPermission("mlgrush.commands.arena")) {
            player.sendMessage(ColorUtil.fixColor(Config.prefix + " &cYetkin yok."));
            return true;
        }

        YamlConfiguration database = Main.getInstance().getMLGDatabase().getConfig();
        if (!isValidArena(database, arenaName)) {
            player.sendMessage(ColorUtil.fixColor(Config.prefix + " &cBu isimde arena bulunamadı."));
            return true;
        }

        Location loc = player.getLocation();
        database.set("arenas." + arenaName + ".pos1.x", loc.getX());
        database.set("arenas." + arenaName + ".pos1.y", loc.getY());
        database.set("arenas." + arenaName + ".pos1.z", loc.getZ());
        Main.getInstance().getMLGDatabase().saveConfigFile();
        player.sendMessage(ColorUtil.fixColor(Config.prefix + " &aPos1 başarıyla ayarlandı."));
        return true;
    }

    private boolean handleSetPos2(Player player, String arenaName) {
        if (!player.hasPermission("mlgrush.commands.arena")) {
            player.sendMessage(ColorUtil.fixColor(Config.prefix + " &cYetkin yok."));
            return true;
        }

        YamlConfiguration database = Main.getInstance().getMLGDatabase().getConfig();
        if (!isValidArena(database, arenaName)) {
            player.sendMessage(ColorUtil.fixColor(Config.prefix + " &cBu isimde arena bulunamadı."));
            return true;
        }

        Location loc = player.getLocation();
        database.set("arenas." + arenaName + ".pos2.x", loc.getX());
        database.set("arenas." + arenaName + ".pos2.y", loc.getY());
        database.set("arenas." + arenaName + ".pos2.z", loc.getZ());
        Main.getInstance().getMLGDatabase().saveConfigFile();
        player.sendMessage(ColorUtil.fixColor(Config.prefix + " &aPos2 başarıyla ayarlandı."));
        return true;
    }



    private boolean isValidArena(YamlConfiguration database, String arenaName) {
        return database.get("arenas." + arenaName + ".name") != null;
    }
}
