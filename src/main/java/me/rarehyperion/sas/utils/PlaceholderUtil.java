package me.rarehyperion.sas.utils;

import org.bukkit.entity.Player;

public class PlaceholderUtil {

    public static String replacePlaceholders(final Player player, final String message) {
        if(player == null || message == null)
            return message;

        return message
                .replace("@p", player.getName())
                .replace("@u", player.getUniqueId().toString())
                .replace("@w", player.getWorld().getName())
                .replace("@x", String.valueOf(player.getLocation().getBlockX()))
                .replace("@y", String.valueOf(player.getLocation().getBlockY()))
                .replace("@z", String.valueOf(player.getLocation().getBlockZ()));
    }

}
