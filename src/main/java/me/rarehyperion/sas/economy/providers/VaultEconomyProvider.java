package me.rarehyperion.sas.economy.providers;

import me.rarehyperion.sas.economy.EconomyProvider;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

public class VaultEconomyProvider implements EconomyProvider {

    private final JavaPlugin plugin;
    private boolean available;

    private Economy economy;

    public VaultEconomyProvider(final JavaPlugin plugin) {
        this.plugin = plugin;
        this.setup();
    }

    @Override
    public String getName() {
        return "Vault";
    }

    @Override
    public boolean isAvailable() {
        return this.available;
    }

    @Override
    public double getBalance(final Player player) {
        if(!this.available) return 0.0D;
        return this.economy.getBalance(player);
    }

    @Override
    public boolean canAfford(final Player player, final double amount) {
        if(!this.available) return true;
        return this.economy.has(player, amount);
    }

    @Override
    public boolean withdraw(final Player player, final double amount) {
        if(!this.available) return false;

        final EconomyResponse response = this.economy.withdrawPlayer(player, amount);
        return response.transactionSuccess();
    }

    private void setup() {
        final Server server = this.plugin.getServer();
        final PluginManager pluginManager = server.getPluginManager();
        final ServicesManager servicesManager = server.getServicesManager();

        if(!pluginManager.isPluginEnabled("Vault")) {
            this.available = false;
            return;
        }

        final RegisteredServiceProvider<Economy> rsp = servicesManager.getRegistration(Economy.class);

        if(rsp == null) {
            this.available = false;
            return;
        }

        this.economy = rsp.getProvider();
        this.available = true;
    }

}
