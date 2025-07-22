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

    public String getCurrencyPrefix() {
        return this.config.getString("currency-prefix", "$");
    }

    public String getCostCommand() {
        return this.config.getString("cost-command", "eco take <player> <cost>");
    }

    public String getInvalidActionMessage(final String actionName) {
        return this.getMessage("action-not-found").replace("<name>", actionName);
    }

    public String getActionCreateMessage() {
        return this.getMessage("action-create");
    }

    public String getActionDeleteMessage() {
        return this.getMessage("action-delete");
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

    public String buildCostCommand(final String playerName, final int cost) {
        return this.getCostCommand()
                .replace("<player>", playerName)
                .replace("<cost>", String.valueOf(cost));
    }

}
