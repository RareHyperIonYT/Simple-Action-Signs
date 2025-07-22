package me.rarehyperion.sas.actions.impl;

import me.rarehyperion.sas.actions.Action;
import org.bukkit.entity.Player;

public class SayAction extends Action {

    public SayAction() {
        super("say");
    }

    @Override
    public void execute(final Player player, final String[] args) {
        player.chat(String.join(" ", args));
    }

}
