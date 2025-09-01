package xyz.realkonts.mlgrush.utils;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class ColorUtil {

    public static String fixColor(String x) {
        return ChatColor.translateAlternateColorCodes('&', x);
    }

    public static List<String> fixColor(List<String> lines) {
        return lines.stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.toList());
    }
}
