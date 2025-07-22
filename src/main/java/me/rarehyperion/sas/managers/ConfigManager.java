package me.rarehyperion.sas.managers;

import me.rarehyperion.sas.utils.MessageUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration config;

    public ConfigManager(final JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public void reload() {
        this.plugin.reloadConfig();
        this.config = this.plugin.getConfig();
    }

    public boolean isEconomyEnabled() {
        return this.config.getBoolean("economy.enabled", true);
    }

    public String getCurrencyPrefix() {
        return this.config.getString("economy.prefix", "$");
    }

    public String getAffordMessage() {
        return this.getMessage("not-enough-money");
    }

    public String getInvalidActionMessage(final String actionName) {
        return this.getMessage("action-not-found").replace("<name>", actionName);
    }

    public String getSignCreateMessage() {
        return this.getMessage("sign-create");
    }

    public String getSignEditMessage() {
        return this.getMessage("sign-edit");
    }

    public String getSignDeleteMessage() {
        return this.getMessage("sign-delete");
    }

    public String getSneakMessage() {
        return this.getMessage("sneak-required");
    }

    public String getReloadMessage() {
        return this.getMessage("reload-success");
    }

    public String getPermissionMessage() {
        return this.getMessage("permission-use");
    }

    public String formatCost(final int cost) {
        if (cost <= 0)
            return "";

        return this.getCurrencyPrefix() + cost;
    }

    public String getMessage(final String key) {
        final String message = this.config.getString("messages." + key);
        return message != null ? MessageUtil.format(message) : null;
    }

}
