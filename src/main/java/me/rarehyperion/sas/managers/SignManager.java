package me.rarehyperion.sas.managers;

import me.rarehyperion.sas.models.SignAction;
import me.rarehyperion.sas.utils.LocationUtil;
import me.rarehyperion.sas.utils.MaterialUtil;
import me.rarehyperion.sas.utils.PlaceholderUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class SignManager {

    private final Map<String, SignAction> validSigns = new HashMap<>();
    private final Pattern bracketPattern = Pattern.compile("\\[(.+?)]");

    private final ConfigManager configManager;
    private final File signsFile;

    public SignManager(final ConfigManager configManager, final File dataFolder) {
        this.configManager = configManager;
        this.signsFile = new File(dataFolder, "signs.bin");
    }

    public void register(final Location location, final String command, final int cost) {
        final String key = LocationUtil.buildKey(location);
        this.validSigns.put(key, new SignAction(command, cost));
        this.saveSigns();
    }

    public void remove(final Location location) {
        final String key = LocationUtil.buildKey(location);
        this.validSigns.remove(key);
        this.saveSigns();
    }

    public SignAction getAction(final Location location) {
        final String key = LocationUtil.buildKey(location);
        return this.validSigns.get(key);
    }

    public boolean hasAction(final Location location) {
        final String key = LocationUtil.buildKey(location);
        return this.validSigns.containsKey(key);
    }

    public boolean isValidFormat(final String line) {
        return line != null && this.bracketPattern.matcher(line).find();
    }

    public void executeAction(final Player player, final SignAction action) {
        final String command = PlaceholderUtil.replacePlaceholders(player, action.getCommand());
        final int cost = action.getCost();

        if(!command.isBlank()) {
            Bukkit.getServer().dispatchCommand(player,
                    command.startsWith("/") ? command.substring(1) : command);
        }

        if(cost > 0) {
            final String costCommand = this.configManager.buildCostCommand(player.getName(), cost);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), costCommand);
        }
    }

    public int parseCost(final String costLine) {
        if(costLine == null) return 0;

        try {
            return Integer.parseInt(costLine.trim());
        } catch (final NumberFormatException exception) {
            return 0;
        }
    }

    public String formatCostDisplay(final int cost) {
        return this.configManager.formatCost(cost);
    }

    public boolean isSign(final Material material) {
        return MaterialUtil.isSign(material);
    }

    public void saveSigns() {
        try(final ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(this.signsFile))) {
            final HashMap<String, SignAction> signsToSave = new HashMap<>(this.validSigns); // Creating a copy to ensure serializability, this isn't redundant.
            outputStream.writeObject(signsToSave);
        } catch (final IOException exception) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to save signs to file!", exception);
        }
    }

    public void loadSigns() {
        if(!this.signsFile.exists())
            return;

        try(final ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(this.signsFile))) {
            @SuppressWarnings("unchecked")
            final Map<String, SignAction> loadedSigns = (Map<String, SignAction>) inputStream.readObject();

            this.validSigns.clear();
            this.validSigns.putAll(loadedSigns);
        } catch (final IOException | ClassNotFoundException exception) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to load signs from file!", exception);
        }
    }

}
