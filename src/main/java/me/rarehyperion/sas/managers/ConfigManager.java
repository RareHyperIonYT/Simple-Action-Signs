package me.rarehyperion.sas.managers;

import me.rarehyperion.sas.utils.MessageUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

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

    public List<String> getListTitle() {
        return this.getList("language.list-signs.title");
    }

    public List<String> getListFormat() {
        return this.getList("language.list-signs.element-format");
    }

    public List<String> getListFooter() {
        return this.getList("language.list-signs.footer");
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
        return this.getMessage("sign-created");
    }

    public String getSignEditMessage() {
        return this.getMessage("sign-edited");
    }

    public String getSignDeleteMessage() {
        return this.getMessage("sign-deleted");
    }

    public String getSneakMessage() {
        return this.getMessage("sneak-required");
    }

    public String getReloadMessage() {
        return this.getMessage("reload");
    }

    public String getNoSignsMessage() {
        return this.getMessage("list-signs.none-found");
    }

    public String getPermissionMessage() {
        return this.getMessage("permission-use");
    }

    public String formatCost(final int cost) {
        if (cost <= 0)
            return "";

        return this.getCurrencyPrefix() + cost;
    }

    public List<String> getList(final String key) {
        final List<String> strings = new ArrayList<>();

        for(final String element : this.config.getStringList(key)) {
            strings.add(MessageUtil.format(element));
        }

        return strings;
    }

    public String getMessage(final String key) {
        final String message = this.config.getString("language." + key);
        return message != null ? MessageUtil.format(message) : null;
    }

    public String getInvalidPageMessage() {
        return this.getMessage("list-signs.invalid-page");
    }
}
