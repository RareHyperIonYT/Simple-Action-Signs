package me.rarehyperion.sas.managers;

import me.rarehyperion.sas.actions.BuiltInAction;
import me.rarehyperion.sas.actions.impl.SayAction;
import me.rarehyperion.sas.models.SignAction;
import me.rarehyperion.sas.utils.LocationUtil;
import me.rarehyperion.sas.utils.MaterialUtil;
import me.rarehyperion.sas.utils.PlaceholderUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class SignManager {

    private final Map<String, SignAction> validSigns = new HashMap<>();
    private final Pattern bracketPattern = Pattern.compile("\\[(.+?)]");
    private final List<BuiltInAction> actions = List.of(new SayAction());

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

    public void executeAction(final Player player, final SignAction signAction) {
        String command = PlaceholderUtil.replacePlaceholders(player, signAction.getCommand());
        final int cost = signAction.getCost();

        if(!command.isBlank()) {
            final String lower = command.toLowerCase();
            boolean ignore = false;
            CommandSender sender = player;

            if(lower.startsWith("c:")) {
                command = command.substring(2);
                sender = Bukkit.getConsoleSender();
                ignore = true;
            } else if(lower.startsWith("p:")) {
                command = command.substring(2);
                ignore = true;
            }

            if(ignore || command.startsWith("/")) {
                if(command.startsWith("/")) command = command.substring(1);
                Bukkit.getServer().dispatchCommand(sender, command);
            } else {
                final String[] split = command.split(" ");
                final String name = split[0].toLowerCase();
                boolean found = false;

                for(final BuiltInAction action : this.actions) {
                    if(action.name.equals(name)) {
                        final String[] args = Arrays.copyOfRange(split, 1, split.length);
                        action.execute(player, args);
                        found = true;
                        break;
                    }
                }

                if(!found) {
                    player.sendMessage(this.configManager.getInvalidActionMessage(split[0]));
                }
            }
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
