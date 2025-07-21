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

    public static boolean isSame(final Location location1, final Location location2) {
        if(location1 == null || location2 == null)
            return false;

        return location1.getBlockX() == location2.getBlockX() &&
               location1.getBlockY() == location2.getBlockY() &&
               location1.getBlockZ() == location2.getBlockZ() &&
               location1.getWorld()  == location2.getWorld();
    }

}
