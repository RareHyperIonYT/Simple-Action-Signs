package me.rarehyperion.sas.utils;

import org.bukkit.Material;

public class MaterialUtil {

    public static boolean isSign(final Material material) {
        if (material == null)
            return false;

        final String name = material.name();
        return name.contains("SIGN") && !name.contains("HANGING");
    }

}
