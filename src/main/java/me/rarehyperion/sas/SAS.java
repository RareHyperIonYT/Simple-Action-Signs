package me.rarehyperion.sas;

import me.rarehyperion.sas.commands.SASCommand;
import me.rarehyperion.sas.listeners.SignListener;
import me.rarehyperion.sas.managers.ConfigManager;
import me.rarehyperion.sas.managers.EconomyManager;
import me.rarehyperion.sas.managers.SignManager;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class SAS extends JavaPlugin implements Listener {

    public static String VERSION = "UNKNOWN";
    private SignManager signManager;

    @Override
    public void onLoad() {
        VERSION = this.getDescription().getVersion();
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        final ConfigManager configManager = new ConfigManager(this);
        final EconomyManager economyManager = new EconomyManager(this, configManager);

        this.signManager = new SignManager(configManager, economyManager, this.getDataFolder());
        this.signManager.loadSigns();

        this.getServer().getPluginManager().registerEvents(new SignListener(this, this.signManager, configManager), this);

        final SASCommand executor = new SASCommand(configManager, this.signManager);

        this.getCommand("sas").setExecutor(executor);
        this.getCommand("sas").setTabCompleter(executor);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll((Plugin) this);

        if(this.signManager == null) {
            return;
        }

        this.signManager.saveSigns();
        this.signManager = null;
    }

}
