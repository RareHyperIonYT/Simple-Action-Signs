package me.rarehyperion.sas.utils;

import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtil {

    public static String buildKey(final Location location) {
        if (location == null)
            throw new IllegalArgumentException("Location cannot be null");

        final World world = location.getWorld();
        final String worldName = world != null ? world.getName() : "world";

        return worldName + ":" + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
    }

}
