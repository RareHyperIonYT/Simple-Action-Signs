package me.rarehyperion.sas.actions.impl;

import me.rarehyperion.sas.actions.BuiltInAction;
import org.bukkit.entity.Player;

public class MessageAction extends BuiltInAction {

    public MessageAction() {
        super("msg");
    }

    @Override
    public void execute(final Player player, final String[] args) {
        player.sendMessage(String.join(" ", args));
    }

}
