package me.rarehyperion.sas.utils;

import org.bukkit.entity.Player;

public class PlaceholderUtil {

    public static String replacePlaceholders(final Player player, final String message) {
        if(player == null || message == null)
            return message;

        return message
                .replace("<player>", player.getName())
                .replace("<uuid>", player.getUniqueId().toString())
                .replace("<world>", player.getWorld().getName())
                .replace("<x>", String.valueOf(player.getLocation().getBlockX()))
                .replace("<y>", String.valueOf(player.getLocation().getBlockY()))
                .replace("<z>", String.valueOf(player.getLocation().getBlockZ()));
    }

    public static boolean hasPlaceholders(final String message) {
        return message != null && (
                message.contains("<player>") ||
                        message.contains("<uuid>") ||
                        message.contains("<world>") ||
                        message.contains("<x>") ||
                        message.contains("<y>") ||
                        message.contains("<z>")
        );
    }

}
