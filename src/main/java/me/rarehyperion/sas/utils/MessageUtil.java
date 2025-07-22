package me.rarehyperion.sas.utils;

import org.bukkit.ChatColor;

public class MessageUtil {

    public static String format(final String message) {
        if(message == null)
            return null;

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String formatLegacy(final String message) {
        if(message == null)
            return null;

        final String formatted = ChatColor.translateAlternateColorCodes('&', message);
        return ChatColor.translateAlternateColorCodes('§', formatted);
    }

}
