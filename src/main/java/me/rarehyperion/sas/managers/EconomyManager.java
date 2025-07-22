package me.rarehyperion.sas.managers;

import me.rarehyperion.sas.economy.EconomyProvider;
import me.rarehyperion.sas.economy.providers.VaultEconomyProvider;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class EconomyManager {

    private final JavaPlugin plugin;
    private final List<EconomyProvider> providers;
    private final ConfigManager configManager;

    private EconomyProvider currentProvider;
    private boolean enabled;

    public EconomyManager(final JavaPlugin plugin, final ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.providers = new ArrayList<>();
        this.setup();
    }

    public boolean isEnabled() {
        return this.enabled && this.configManager.isEconomyEnabled();
    }

    public String getProviderName() {
        return this.currentProvider != null ? this.currentProvider.getName() : "None";
    }

    public boolean canAfford(final Player player, final double amount) {
        if(!this.enabled || amount < 0 || this.currentProvider == null)
            return true;

        return this.currentProvider.canAfford(player, amount);
    }

    public double getBalance(final Player player) {
        if(!this.enabled || this.currentProvider == null)
            return 0.0D;

        return this.currentProvider.getBalance(player);
    }

    public boolean withdraw(final Player player, final double amount) {
        if(!this.enabled || this.currentProvider == null || amount < 0)
            return false;

        if(!this.canAfford(player, amount))
            return false;

        return this.currentProvider.withdraw(player, amount);
    }

    private void setup() {
        this.providers.add(new VaultEconomyProvider(this.plugin));

        for(final EconomyProvider provider : this.providers) {
            if(provider.isAvailable()) {
                this.currentProvider = provider;
                this.enabled = true;
                this.plugin.getLogger().info(String.format("Economy support enabled with '%s'.", provider.getName()));
                return;
            }
        }

        this.plugin.getLogger().info("No economy plugin found, continuing without economy support.");
    }

}
