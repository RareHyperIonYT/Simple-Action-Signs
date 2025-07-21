package me.rarehyperion.sas.utils;

import org.bukkit.Material;

public class MaterialUtil {

    public static boolean isSign(final Material material) {
        if (material == null)
            return false;

        final String name = material.name();
        return name.contains("SIGN") && !name.contains("HANGING");
    }

    public static boolean isWallSign(final Material material) {
        if (material == null)
            return false;

        final String name = material.name();
        return name.contains("WALL_SIGN");
    }

    public static boolean isStandingSign(final Material material) {
        if (material == null)
            return false;

        final String name = material.name();
        return name.contains("SIGN") && !name.contains("WALL") && !name.contains("HANGING");
    }

}
