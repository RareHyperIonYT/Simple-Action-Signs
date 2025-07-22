package me.rarehyperion.sas.economy;

import org.bukkit.entity.Player;

public interface EconomyProvider {

    String getName();

    boolean isAvailable();

    double getBalance(final Player player);

    boolean canAfford(final Player player, final double amount);

    boolean withdraw(final Player player, final double amount);

}
