package xyz.realkonts.mlgrush.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.realkonts.mlgrush.builders.ItemBuilder;

public class ItemUtil {

    public static void giveItems(Player player) {
        ItemStack stick = new ItemBuilder(Material.STICK).amount(1).enchant(Enchantment.KNOCKBACK, 1).unbreakable(true).build();
        ItemStack sandstone = new ItemBuilder(Material.SANDSTONE).amount(64).build();
        ItemStack diaPickaxe = new ItemBuilder(Material.DIAMOND_PICKAXE).amount(1).enchant(Enchantment.DIG_SPEED, 3).unbreakable(true).build();
        player.getInventory().setItem(0, stick);
        player.getInventory().setItem(1, sandstone);
        player.getInventory().setItem(2, diaPickaxe);

    }
}
