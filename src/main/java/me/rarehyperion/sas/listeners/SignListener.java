package me.rarehyperion.sas.listeners;

import me.rarehyperion.sas.managers.ConfigManager;
import me.rarehyperion.sas.managers.SignManager;
import me.rarehyperion.sas.models.SignAction;
import me.rarehyperion.sas.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

public class SignListener implements Listener {

    private final JavaPlugin plugin;
    private final SignManager signManager;
    private final ConfigManager configManager;

    public SignListener(final JavaPlugin plugin, final SignManager signManager, final ConfigManager configManager) {
        this.plugin = plugin;
        this.signManager = signManager;
        this.configManager = configManager;
    }

    @EventHandler
    public void onSignCreate(final SignChangeEvent event) {
        final Player player = event.getPlayer();

        if(!player.hasPermission("sas.admin"))
            return;

        final String typeLine = event.getLine(0);
        final String commandLine = event.getLine(1);
        final String descriptionLine = event.getLine(2);
        final String costLine = event.getLine(3);

        if(typeLine == null || typeLine.isBlank() || commandLine == null || commandLine.isBlank())
            return;

        if(!this.signManager.isValidFormat(typeLine))
            return;

        final Location location = event.getBlock().getLocation();
        final int cost = this.signManager.parseCost(costLine);

        String createMessage = this.configManager.getSignCreateMessage();
        if(this.signManager.hasAction(location)) createMessage = this.configManager.getSignEditMessage();

        this.signManager.register(player, location, commandLine, descriptionLine, cost);
        this.updateDisplay(event, typeLine, descriptionLine, cost);

        if(createMessage == null) return;
        player.sendMessage(createMessage);
    }

    @EventHandler
    public void onSignClick(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getClickedBlock();

        if(!this.isValidInteraction(event.getAction(), event.getHand(), block) || block == null)
            return;

        if(!this.signManager.isSign(block.getType()) || !this.signManager.hasAction(block.getLocation()))
            return;

        final SignAction signAction = this.signManager.getAction(block.getLocation());

        if(player.isSneaking() && player.hasPermission("sas.admin")) {
            // Whoever decided 2 classes with the same exact name was a good idea should be fired.
            final org.bukkit.block.Sign sign = (org.bukkit.block.Sign) block.getState();
            final SignSide front = sign.getSide(Side.FRONT);

            front.setLine(1, signAction.getCommand());
            front.setLine(2, signAction.getDescription());

            if(signAction.getCost() > 0)
                front.setLine(3, String.valueOf(signAction.getCost()));

            sign.update(true);

            // You'd think you can do this without a delay after updating, but clearly I was wrong.
            // Might switch to processing packets in the future so it's seamless for the user experience.
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> player.openSign(sign, Side.FRONT), 2L);

            event.setCancelled(true);
            return;
        }

        if(!player.hasPermission("sas.use")) {
            final String permissionMessage = this.configManager.getPermissionMessage();
            if(permissionMessage != null) player.sendMessage(permissionMessage);
            return;
        }

        this.signManager.executeAction(player, signAction);

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onSignBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getBlock();

        if(this.hasAttachedActionSign(block)) {
            event.setCancelled(true);
            return;
        }

        if(!this.signManager.isSign(block.getType()) || !this.signManager.hasAction(block.getLocation()))
            return;

        if(player.hasPermission("sas.admin")) {
            if(player.isSneaking()) {
                player.sendMessage(this.configManager.getSignDeleteMessage());
                this.signManager.remove(block.getLocation());
                return;
            }

            player.sendMessage(this.configManager.getSneakMessage());
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockPhysics(final BlockPhysicsEvent event) {
        final Block block = event.getBlock();

        if(this.signManager.isSign(event.getChangedType()))
            return;

        if(this.signManager.isSign(block.getType()) && this.signManager.hasAction(block.getLocation())) {
            // Cancelling doesn't stop the sign from being destroyed, so just unregister it instead.
            // It's a very unlikely situation anyway, as it's supporting block can't be destroyed directly.
            this.signManager.remove(block.getLocation());
        }
    }

    private boolean hasAttachedActionSign(final Block block) {
        for(final BlockFace face : BlockFace.values()) {
            final Block adjacent = block.getRelative(face);

            if(!this.signManager.isSign(adjacent.getType()) || !this.signManager.hasAction(adjacent.getLocation()))
                continue;

            final BlockData data = adjacent.getBlockData();

            if(data instanceof WallSign sign) {
                final BlockFace support = sign.getFacing();

                if(support == face) {
                    return true;
                }
            } else if(data instanceof Sign) {
                return face == BlockFace.UP;
            }
        }

        return false;
    }

    private void updateDisplay(final SignChangeEvent event, final String typeLine, final String descriptionLine, final int cost) {
        if(typeLine != null) {
            event.setLine(0, MessageUtil.formatLegacy(typeLine));
        }

        if (descriptionLine != null) {
            event.setLine(1, MessageUtil.formatLegacy(descriptionLine));
            event.setLine(2, "");
        }

        if (cost > 0) {
            final String costDisplay = this.signManager.formatCostDisplay(cost);
            event.setLine(2, MessageUtil.formatLegacy(costDisplay));
            event.setLine(3, "");
        }
    }

    private boolean isValidInteraction(final Action action, final EquipmentSlot hand, final Block block) {
        return action == Action.RIGHT_CLICK_BLOCK
                && hand == EquipmentSlot.HAND
                && block != null;
    }

}
