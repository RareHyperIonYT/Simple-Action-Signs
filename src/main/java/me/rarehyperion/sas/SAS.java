package me.rarehyperion.sas;

import me.rarehyperion.sas.listeners.SignListener;
import me.rarehyperion.sas.managers.ConfigManager;
import me.rarehyperion.sas.managers.SignManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SAS extends JavaPlugin implements Listener {

    private SignManager signManager;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        final ConfigManager configManager = new ConfigManager(this);
        this.signManager = new SignManager(configManager, this.getDataFolder());
        this.signManager.loadSigns();

        this.getServer().getPluginManager().registerEvents(new SignListener(this.signManager, configManager), this);

        Objects.requireNonNull(this.getCommand("sas")).setExecutor((sender, command, label, args) -> {
            if(!sender.hasPermission("sas.reload"))
                return true;

            configManager.reload();
            sender.sendMessage(configManager.getReloadMessage());
            return true;
        });
    }

    @Override
    public void onDisable() {
        if(this.signManager == null)
            return;

        this.signManager.saveSigns();
        this.signManager = null;
    }

}
