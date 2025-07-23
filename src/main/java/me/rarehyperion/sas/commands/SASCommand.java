package me.rarehyperion.sas.commands;

import me.rarehyperion.sas.SAS;
import me.rarehyperion.sas.managers.ConfigManager;
import me.rarehyperion.sas.managers.SignManager;
import me.rarehyperion.sas.models.SignAction;
import me.rarehyperion.sas.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;
import java.util.stream.Stream;

@SuppressWarnings("NullableProblems")
public class SASCommand implements CommandExecutor, TabCompleter {

    private static final int ITEMS_PER_PAGE = 3;

    private final ConfigManager configManager;
    private final SignManager signManager;

    public SASCommand(final ConfigManager configManager, final SignManager signManager) {
        this.configManager = configManager;
        this.signManager = signManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if(!cmd.getName().equalsIgnoreCase("sas"))
            return true;

        if(args.length < 1) {
            sender.sendMessage(MessageUtil.format(
                String.format("&b&lSimple Action Signs &7(%s)", SAS.VERSION)
            ));

            sender.sendMessage(MessageUtil.format(
                "&8❘ &7A lightweight plugin that turns signs into custom command executors."
            ));

            return true;
        }

        final String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "reload": {
                if(!sender.hasPermission("sas.admin.reload")) {
                    return true;
                }

                this.configManager.reload();
                sender.sendMessage(this.configManager.getReloadMessage());
                break;
            }

            case "list": {
                if(!sender.hasPermission("sas.admin")) {
                    return true;
                }

                final Map<String, SignAction> signs = this.signManager.getSigns();

                if(signs.isEmpty()) {
                    sender.sendMessage(this.configManager.getNoSignsMessage());
                    break;
                }

                int page = 1;
                if (args.length > 1) {
                    try {
                        page = Integer.parseInt(args[1]);
                        if (page < 1) page = 1;
                    } catch (final NumberFormatException ignored) {}
                }

                final int totalSigns = signs.size();
                final int totalPages = (int) Math.ceil((double) totalSigns / ITEMS_PER_PAGE);
                final int startIndex = (page - 1) * ITEMS_PER_PAGE;
                final int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, totalSigns);

                if (page > totalPages) {
                    sender.sendMessage(this.configManager.getInvalidPageMessage()
                            .replace("<page>", String.valueOf(page))
                            .replace("<totalPages>", String.valueOf(totalPages)));
                    break;
                }

                // Header
                final List<String> title = this.configManager.getListTitle();

                for(int i = 0; i < title.size(); i++) {
                    final String text = title.get(i);
                    title.set(i, text
                            .replace("<page>", String.valueOf(page))
                            .replace("<totalPages>", String.valueOf(totalPages))
                            .replace("<totalSigns>", String.valueOf(totalSigns))
                    );
                }

                final List<String> message = new ArrayList<>(title);
                message.add("");

                final List<Map.Entry<String, SignAction>> signList = new ArrayList<>(signs.entrySet());

                for (int i = startIndex; i < endIndex; i++) {
                    message.addAll(this.getElementInfo(signList, i));
                    message.add("");
                }

                if (totalPages > 1) {
                    message.addAll(this.configManager.getListFooter());
                }

                for (final String line : message) {
                    sender.sendMessage(line);
                }

                break;
            }
        }

        return true;
    }

    private List<String> getElementInfo(final List<Map.Entry<String, SignAction>> signList, final int i) {
        final Map.Entry<String, SignAction> entry = signList.get(i);
        final String key = entry.getKey();
        final SignAction action = entry.getValue();

        final List<String> elementInfo = new ArrayList<>(this.configManager.getListFormat());

        elementInfo.replaceAll(line -> line
                .replace("<name>", Objects.requireNonNull(Bukkit.getOfflinePlayer(UUID.fromString(action.getOwner())).getName()))
                .replace("<index>", String.valueOf((i + 1)))
                .replace("<key>", key)
                .replace("<command>", action.getCommand())
                .replace("<description>", action.getDescription())
                .replace("<cost>", MessageUtil.format(this.configManager.getCurrencyPrefix() + action.getCost())));

        return elementInfo;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length == 1) {
            return Stream.of("reload", "list")
                    .filter(option -> option.startsWith(args[0].toLowerCase()))
                    .toList();
        } else if(args.length == 2 && args[0].equalsIgnoreCase("list") && args[1].isEmpty()) {
            return List.of("<page>");
        }

        return List.of();
    }

}
